package com.qlbds.service;

import com.qlbds.constant.RoleTypeEnum;
import com.qlbds.constant.UserStatusEnum;
import com.qlbds.dto.UserDTO;
import com.qlbds.entity.OtpCode;
import com.qlbds.entity.User;
import com.qlbds.repository.OtpRepository;
import com.qlbds.repository.UserRepository;
import com.qlbds.util.EmailUtil;
import com.qlbds.util.SecurityUtil;
import com.qlbds.util.ValidationUtil;

import java.time.LocalDateTime;
import java.util.Random;

public class UserService {
    private UserRepository userRepository = new UserRepository();

    public String registerUser(UserDTO userDTO) {
        if (!ValidationUtil.isValidPhone(userDTO.getPhone())) return "Định dạng SĐT không hợp lệ!";
        if (!ValidationUtil.isValidEmail(userDTO.getEmail())) return "Định dạng Email không hợp lệ!";
        if (!ValidationUtil.isValidPassword(userDTO.getPassword())) return "Mật khẩu tối thiểu 6 ký tự và chứa ít nhất 1 ký tự đặc biệt!";
        if (!userDTO.getPassword().equals(userDTO.getConfirmPassword())) return "Xác nhận mật khẩu không khớp!";
        if (userRepository.findByEmail(userDTO.getEmail()) != null) return "Email này đã được đăng ký!";
        if (userRepository.findByPhone(userDTO.getPhone()) != null) return "Số điện thoại này đã được sử dụng!";

        User user = new User();
        user.setFullName(userDTO.getFullName().trim());
        user.setPhone(userDTO.getPhone().trim());
        user.setEmail(userDTO.getEmail().trim().toLowerCase());
        user.setPassword(SecurityUtil.hashPassword(userDTO.getPassword()));
        user.setRole(RoleTypeEnum.CUSTOMER);
        user.setStatus(UserStatusEnum.ACTIVE);
        user.setIsVerified(false);

        // CHỈ LƯU USER, KHÔNG GỬI OTP Ở ĐÂY
        return userRepository.save(user) ? "SUCCESS" : "Lỗi hệ thống khi lưu dữ liệu!";
    }

    // NGHIỆP VỤ ĐĂNG NHẬP
    public User loginUser(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user != null && user.getStatus() == UserStatusEnum.ACTIVE) {
            // Giữ nguyên logic băm mật khẩu đồng bộ với phần đổi mật khẩu
            if (user.getPassword().equals(SecurityUtil.hashPassword(password))) {
                return user;
            }
        }
        return null;
    }

    // NGHIỆP VỤ CẬP NHẬT THÔNG TIN
    public String updateProfile(Integer userId, String fullName, String phone) {
        if (fullName == null || fullName.trim().isEmpty()) return "Họ tên không được để trống!";
        if (!ValidationUtil.isValidPhone(phone)) return "Định dạng SĐT không hợp lệ!";

        User userExist = userRepository.findByPhone(phone.trim());
        if (userExist != null && !userExist.getId().equals(userId)) {
            return "Số điện thoại này đã được sử dụng bởi tài khoản khác!";
        }

        User user = userRepository.findById(userId);
        if (user == null) return "Tài khoản không tồn tại!";

        user.setFullName(fullName.trim());
        user.setPhone(phone.trim());

        return userRepository.update(user) ? "SUCCESS" : "Lỗi hệ thống khi cập nhật!";
    }

    // NGHIỆP VỤ ĐỔI MẬT KHẨU
    public String changePassword(Integer userId, String oldPassword, String newPassword, String confirmPassword) {
        if (oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) return "Vui lòng nhập đủ các trường!";

        User user = userRepository.findById(userId);
        if (user == null) return "Tài khoản không tồn tại!";

        if (!user.getPassword().equals(SecurityUtil.hashPassword(oldPassword))) {
            return "Mật khẩu hiện tại không chính xác!";
        }

        if (!ValidationUtil.isValidPassword(newPassword)) return "Mật khẩu mới tối thiểu 6 ký tự!";
        if (!newPassword.equals(confirmPassword)) return "Xác nhận mật khẩu mới không trùng khớp!";

        user.setPassword(SecurityUtil.hashPassword(newPassword));
        return userRepository.update(user) ? "SUCCESS" : "Lỗi hệ thống khi đổi mật khẩu!";
    }
}