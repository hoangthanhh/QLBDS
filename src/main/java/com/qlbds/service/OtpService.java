package com.qlbds.service;

import com.qlbds.entity.OtpCode;
import com.qlbds.entity.User;
import com.qlbds.repository.OtpRepository;
import com.qlbds.util.EmailUtil;
import com.qlbds.util.HibernateUtil;
import org.hibernate.Session;

import java.time.LocalDateTime;
import java.util.Random;

public class OtpService {

    private OtpRepository otpRepository = new OtpRepository();

    // NGHIỆP VỤ 1: Kiểm tra giới hạn ngày, khởi tạo OTP mới và kích hoạt gửi email
    public String generateAndSendOtp(User currentUser) {
        if (currentUser == null) {
            return "Phiên làm việc đã hết hạn, vui lòng đăng nhập lại!";
        }

        // BẢO MẬT: Giới hạn tối đa 5 lần tạo mã OTP trong ngày
        long otpCountToday = otpRepository.countOtpGeneratedToday(currentUser.getId());
        if (otpCountToday >= 5) {
            return "Tài khoản đã vượt quá giới hạn yêu cầu nhận mã OTP trong ngày hôm nay (Tối đa 5 lần/ngày). Vui lòng thử lại vào ngày mai!";
        }

        try {
            // Hủy toàn bộ các mã cũ chưa dùng trước khi tạo mã mới
            OtpCode oldOtp = otpRepository.findLatestActiveOtp(currentUser.getId());
            if (oldOtp != null) {
                oldOtp.setIsUsed(true);
                otpRepository.updateOtp(oldOtp);
            }

            // Sinh chuỗi 6 số ngẫu nhiên
            String generatedOtp = String.format("%06d", new Random().nextInt(999999));

            // Lưu thực thể OTP mới
            OtpCode otpEntity = new OtpCode();
            otpEntity.setUser(currentUser);
            otpEntity.setOtpCode(generatedOtp);
            otpEntity.setExpiresAt(LocalDateTime.now().plusMinutes(5)); // Hạn thời gian thực 5 phút
            otpEntity.setFailedAttempts(0);
            otpEntity.setIsUsed(false);

            otpRepository.saveOtp(otpEntity);

            // Gửi email bằng Thread chạy ngầm bất đồng bộ
            new Thread(() -> {
                EmailUtil.sendOtpEmail(currentUser.getEmail(), currentUser.getFullName(), generatedOtp);
            }).start();

            return "SUCCESS";
        } catch (Exception e) {
            e.printStackTrace();
            return "Lỗi hệ thống trong quá trình khởi tạo mã xác thực!";
        }
    }

    // NGHIỆP VỤ 2: Kiểm tra thời hạn mã và so khớp chuỗi ký tự người dùng nhập
    public String verifyUserOtp(User currentUser, String inputOtp) {
        if (currentUser == null) {
            return "Phiên làm việc đã hết hạn, vui lòng đăng nhập lại!";
        }

        OtpCode otp = otpRepository.findLatestActiveOtp(currentUser.getId());
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

        // So sánh chuỗi ký tự nhập vào
        if (!otp.getOtpCode().equals(inputOtp)) {
            otp.setFailedAttempts(otp.getFailedAttempts() + 1);
            otpRepository.updateOtp(otp);
            return "Mã OTP không chính xác! Bạn còn " + (3 - otp.getFailedAttempts()) + " lần thử.";
        }

        // Xác thực thành công -> Hủy trạng thái mã và cập nhật quyền User
        otp.setIsUsed(true);
        otpRepository.updateOtp(otp);

        currentUser.setIsVerified(true);
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            org.hibernate.Transaction tx = session.beginTransaction();
            session.update(currentUser);
            tx.commit();
        }

        return "SUCCESS";
    }
}