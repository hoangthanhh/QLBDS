package com.qlbds.service;

import com.qlbds.constant.RoleTypeEnum;
import com.qlbds.constant.UserStatusEnum;
import com.qlbds.dto.UserDTO;
import com.qlbds.entity.User;
import com.qlbds.repository.UserRepository;
import com.qlbds.util.SecurityUtil;

import java.util.List;

public class UserService {
    private UserRepository userRepository = new UserRepository();

    // ==========================================
    // 1. NHÓM HÀM CHO AUTHENTICATION (CŨ CỦA BẠN)
    // ==========================================
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

        user.setRole(RoleTypeEnum.CUSTOMER);
        user.setStatus(UserStatusEnum.ACTIVE);
        user.setIsVerified(false);

        return userRepository.save(user) ? "SUCCESS" : "Lỗi hệ thống khi lưu dữ liệu, vui lòng thử lại!";
    }

    public User loginUser(String email, String password) {
        User user = userRepository.findByEmail(email);

        if (user != null) {
            String hashedPass = SecurityUtil.hashPassword(password);
            if (user.getPassword().equals(hashedPass) && user.getStatus() == UserStatusEnum.ACTIVE) {
                return user;
            }
        }
        return null;
    }

    // ==========================================
    // 2. NHÓM HÀM CHO ADMIN QUẢN LÝ (BỔ SUNG THÊM)
    // ==========================================

    // Lấy danh sách có phân trang
    public List<User> getUsersWithPagination(int offset, int limit) {
        return userRepository.findAllWithPagination(offset, limit);
    }

    // Đếm tổng số lượng tài khoản (Phục vụ chia trang)
    public long countTotalUsers() {
        return userRepository.countAllUsers();
    }

    // Tìm tài khoản theo ID
    public User getUserById(int id) {
        return userRepository.findById(id);
    }

    // Đổi mật khẩu tài khoản
    public boolean changePassword(int userId, String newPassword) {
        User user = userRepository.findById(userId);
        if (user != null) {
            user.setPassword(SecurityUtil.hashPassword(newPassword));
            return userRepository.update(user);
        }
        return false;
    }

    // Cập nhật thông tin tài khoản
    public boolean updateUser(User user) {
        if (user == null) {
            return false;
        }
        return userRepository.update(user);
    }

    // Xóa tài khoản
    public boolean deleteUser(int id) {
        return userRepository.delete(id);
    }

    // ==========================================
    // 3. NGHIỆP VỤ ADMIN THÊM TÀI KHOẢN (SỬ DỤNG DTO)
    // ==========================================

    // Hàm tạo tài khoản mới từ trang Admin nhận đối tượng UserDTO
    public String createAccountByAdmin(UserDTO userDTO) {
        // 1. Kiểm tra Validate dữ liệu DTO chuẩn nghiệp vụ
        String validateError = userDTO.validateAdminCreate();
        if (validateError != null) {
            return validateError;
        }

        // 2. Kiểm tra xem email đã tồn tại chưa để tránh lỗi trùng lặp dữ liệu
        if (userRepository.findByEmail(userDTO.getEmail()) != null) {
            return "Email này đã tồn tại trong hệ thống!";
        }

        // 3. Mapping dữ liệu từ DTO sang Entity User
        User user = new User();
        user.setFullName(userDTO.getFullName().trim());
        user.setEmail(userDTO.getEmail().trim().toLowerCase());
        user.setPhone(userDTO.getPhone() != null ? userDTO.getPhone().trim() : "");
        // Bắt buộc mã hóa mật khẩu trước khi lưu xuống DB
        user.setPassword(SecurityUtil.hashPassword(userDTO.getPassword()));
        // Ép kiểu chuỗi Role từ DTO về chuẩn Enum (CUSTOMER, STAFF, ADMIN)
        user.setRole(RoleTypeEnum.valueOf(userDTO.getRole()));
        user.setStatus(UserStatusEnum.ACTIVE);
        // Tài khoản do Admin cấp mặc định được coi là đã xác thực an toàn
        user.setIsVerified(true);

        // 4. Lưu xuống Cơ sở dữ liệu qua Repository
        boolean saved = userRepository.save(user);
        return saved ? "SUCCESS" : "Lỗi hệ thống khi lưu tài khoản!";
    }
}