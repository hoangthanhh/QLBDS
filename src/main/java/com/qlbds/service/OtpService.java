package com.qlbds.service;

import com.qlbds.dto.user.UserDTO;
import com.qlbds.dto.acc.VerifyOtpDTO;
import com.qlbds.entity.OtpCode;
import com.qlbds.entity.User;
import com.qlbds.repository.OtpRepository;
import com.qlbds.repository.UserRepository;
import com.qlbds.util.EmailUtil;

import java.time.LocalDateTime;
import java.util.Random;

public class OtpService {

    private OtpRepository otpRepository = new OtpRepository();
    private UserRepository userRepository = new UserRepository(); // Khai báo UserRepository để xử lý DB chuẩn tầng

    // NGHIỆP VỤ 1: Nhận UserDTO từ Session, khởi tạo OTP mới và kích hoạt gửi email ngầm
    public String generateAndSendOtp(UserDTO currentUserDTO) {
        if (currentUserDTO == null) {
            return "Phiên làm việc đã hết hạn, vui lòng đăng nhập lại!";
        }

        // BẢO MẬT: Giới hạn tối đa 5 lần tạo mã OTP trong ngày
        long otpCountToday = otpRepository.countOtpGeneratedToday(currentUserDTO.getId());
        if (otpCountToday >= 5) {
            return "Tài khoản đã vượt quá giới hạn yêu cầu nhận mã OTP trong ngày hôm nay (Tối đa 5 lần/ngày). Vui lòng thử lại vào ngày mai!";
        }

        try {
            // Hủy toàn bộ các mã cũ chưa dùng trước khi tạo mã mới
            OtpCode oldOtp = otpRepository.findLatestActiveOtp(currentUserDTO.getId());
            if (oldOtp != null) {
                oldOtp.setIsUsed(true);
                otpRepository.updateOtp(oldOtp);
            }

            // Sinh chuỗi 6 số ngẫu nhiên
            String generatedOtp = String.format("%06d", new Random().nextInt(999999));

            // Cần 1 thực thể User ảo chứa ID để map mối quan hệ Hibernate quan bảng OtpCode
            User userMapping = new User();
            userMapping.setId(currentUserDTO.getId());

            // Lưu thực thể OTP mới
            OtpCode otpEntity = new OtpCode();
            otpEntity.setUser(userMapping);
            otpEntity.setOtpCode(generatedOtp);
            otpEntity.setExpiresAt(LocalDateTime.now().plusMinutes(5)); // Hạn thời gian thực 5 phút
            otpEntity.setFailedAttempts(0);
            otpEntity.setIsUsed(false);

            otpRepository.saveOtp(otpEntity);

            // Gửi email bằng Thread chạy ngầm bất đồng bộ
            new Thread(() -> {
                EmailUtil.sendOtpEmail(currentUserDTO.getEmail(), currentUserDTO.getFullName(), generatedOtp);
            }).start();

            return "SUCCESS";
        } catch (Exception e) {
            e.printStackTrace();
            return "Lỗi hệ thống trong quá trình khởi tạo mã xác thực!";
        }
    }

    // NGHIỆP VỤ 2: Nhận VerifyOtpDTO và so khớp chuỗi ký tự nhập vào
    public String verifyUserOtp(UserDTO currentUserDTO, VerifyOtpDTO verifyOtpDTO) {
        if (currentUserDTO == null || verifyOtpDTO == null) {
            return "Phiên làm việc đã hết hạn hoặc dữ liệu xác thực không hợp lệ!";
        }

        OtpCode otp = otpRepository.findLatestActiveOtp(currentUserDTO.getId());
        if (otp == null) {
            return "Không tìm thấy yêu cầu xác thực hoặc mã OTP đã hết hiệu lực!";
        }

        // BẢO MẬT: Khóa mã nếu nhập sai quá 3 lần
        if (otp.getFailedAttempts() >= 3) {
            otp.setIsUsed(true);
            otpRepository.updateOtp(otp);
            return "Mã OTP này đã bị khóa do nhập sai quá 3 lần. Vui lòng nhấn nút yêu cầu gửi lại mã mới!";
        }

        // BẢO MẬT: Kiểm tra thời gian hết hạn mã (5 phút)
        if (otp.getExpiresAt().isBefore(LocalDateTime.now())) {
            otp.setIsUsed(true);
            otpRepository.updateOtp(otp);
            return "Mã OTP đã hết hạn sử dụng. Vui lòng bấm nút lấy mã mới!";
        }

        // So sánh chuỗi ký tự nhập vào từ DTO
        if (!otp.getOtpCode().equals(verifyOtpDTO.getOtpCode())) {
            otp.setFailedAttempts(otp.getFailedAttempts() + 1);
            otpRepository.updateOtp(otp);
            return "Mã OTP không chính xác! Bạn còn " + (3 - otp.getFailedAttempts()) + " lần thử.";
        }

        // Xác thực thành công -> Hủy trạng thái mã OTP
        otp.setIsUsed(true);
        otpRepository.updateOtp(otp);

        // Chuẩn kiến trúc: Gọi tầng Repo để cập nhật Entity User thật dưới CSDL
        User actualUser = userRepository.findById(currentUserDTO.getId());
        if (actualUser != null) {
            actualUser.setIsVerified(true);
            userRepository.updateUser(actualUser);
            return "SUCCESS";
        }

        return "Không tìm thấy thông tin tài khoản trên hệ thống!";
    }
}