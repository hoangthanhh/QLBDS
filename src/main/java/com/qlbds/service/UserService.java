package com.qlbds.service;

import com.qlbds.constant.RoleTypeEnum;
import com.qlbds.constant.UserStatusEnum;
import com.qlbds.dto.UserDTO;
import com.qlbds.entity.User;
import com.qlbds.repository.UserRepository;
import com.qlbds.util.SecurityUtil;

public class UserService {
    private UserRepository userRepository = new UserRepository();

    public String registerUser(UserDTO userDTO) {
        if (!userDTO.getPassword().equals(userDTO.getConfirmPassword())) {
            return "Mật khẩu xác nhận không khớp!";
        }

        if (userRepository.findByEmail(userDTO.getEmail()) != null) {
            return "Email này đã được đăng ký trong hệ thống!";
        }

        User user = new User();
        user.setFullName(userDTO.getFullName());
        user.setPhone(userDTO.getPhone());
        user.setEmail(userDTO.getEmail());
        user.setPassword(SecurityUtil.hashPassword(userDTO.getPassword()));

        // Gán các giá trị mặc định theo đúng Entity của bạn
        user.setRole(RoleTypeEnum.CUSTOMER);
        user.setStatus(UserStatusEnum.ACTIVE);
        user.setIsVerified(false); // Chưa xác thực OTP

        return userRepository.save(user) ? "SUCCESS" : "Lỗi hệ thống khi lưu dữ liệu, vui lòng thử lại!";
    }

    public User loginUser(String email, String password) {
        User user = userRepository.findByEmail(email);

        if (user != null) {
            String hashedPass = SecurityUtil.hashPassword(password);

            // Khớp mật khẩu và tài khoản phải ở trạng thái ACTIVE
            if (user.getPassword().equals(hashedPass) && user.getStatus() == UserStatusEnum.ACTIVE) {
                return user;
            }
        }
        return null;
    }
}