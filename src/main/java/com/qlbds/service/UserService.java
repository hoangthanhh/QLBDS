package com.qlbds.service;

import com.qlbds.constant.RoleTypeEnum;
import com.qlbds.constant.UserStatusEnum;
import com.qlbds.dto.UserDTO;
import com.qlbds.entity.User;
import com.qlbds.repository.UserRepository;
import com.qlbds.util.SecurityUtil;
import com.qlbds.util.UserValidationUtil;

import java.util.ArrayList;
import java.util.List;

public class UserService {
    private UserRepository repo = new UserRepository();

    // Hàm phụ trợ so sánh mật khẩu nhập vào với DB (Tương thích cả PlainText cũ và Base64 SHA-256 mới)
    private boolean verifyPassword(String inputPassword, String storedPassword) {
        if (inputPassword == null || storedPassword == null) return false;

        // Check 1: So sánh trực tiếp nếu trong DB là mật khẩu cũ chưa mã hóa
        if (inputPassword.equals(storedPassword)) {
            return true;
        }

        // Check 2: Mã hóa SHA-256 bằng SecurityUtil rồi so sánh
        String hashedInput = SecurityUtil.hashPassword(inputPassword);
        return hashedInput.equals(storedPassword);
    }

    // 1. ĐĂNG NHẬP (Sử dụng SecurityUtil)
    public User loginUser(String email, String password) {
        if (email == null || password == null) return null;
        User user = repo.findByEmail(email.trim());
        if (user != null && verifyPassword(password, user.getPassword())) {
            return user;
        }
        return null;
    }

    // 2. ĐĂNG KÝ KHÁCH HÀNG (Dành riêng cho Khách hàng tự đăng ký ở phía Client)
    public String registerUser(UserDTO dto) {
        List<String> errors = UserValidationUtil.validateRegister(dto);

        if (dto.getEmail() != null && repo.findByEmail(dto.getEmail().trim()) != null) {
            errors.add("Email này đã được sử dụng!");
        }
        if (dto.getPhone() != null && repo.findByPhone(dto.getPhone().trim()) != null) {
            errors.add("Số điện thoại này đã được đăng ký!");
        }

        if (!errors.isEmpty()) {
            return errors.get(0);
        }

        User entity = new User();
        entity.setFullName(dto.getFullName().trim());
        entity.setEmail(dto.getEmail().trim());
        entity.setPhone(dto.getPhone().trim());

        // Mã hóa SHA-256 Base64 bằng SecurityUtil
        entity.setPassword(SecurityUtil.hashPassword(dto.getPassword()));

        entity.setRole(RoleTypeEnum.CUSTOMER);
        entity.setStatus(UserStatusEnum.ACTIVE);
        entity.setIsVerified(true);

        return repo.insertUser(entity) ? "SUCCESS" : "Lỗi hệ thống khi lưu CSDL!";
    }

    // 3. LẤY DANH SÁCH & PHÂN TRANG
    public List<UserDTO> getUserList(int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<User> entities = repo.findAllUsers(offset, pageSize);
        List<UserDTO> dtos = new ArrayList<>();

        for (User entity : entities) {
            UserDTO dto = new UserDTO();
            dto.setId(entity.getId());
            dto.setFullName(entity.getFullName());
            dto.setEmail(entity.getEmail());
            dto.setPhone(entity.getPhone());
            dto.setRole(entity.getRole() != null ? entity.getRole().name() : "");
            dto.setStatus(entity.getStatus() != null ? entity.getStatus().name() : "");
            dtos.add(dto);
        }
        return dtos;
    }

    public int getTotalPages(int pageSize) {
        if (pageSize <= 0) pageSize = 5;
        int totalRecords = repo.countTotalUsers();
        return (int) Math.ceil((double) totalRecords / pageSize);
    }

    // 4. ADMIN THÊM TÀI KHOẢN (Chỉ hỗ trợ thêm Staff và Admin)
    public List<String> addAdminUser(UserDTO dto) {
        List<String> errors = UserValidationUtil.validateAdminCreate(dto);

        if (dto.getEmail() != null && repo.findByEmail(dto.getEmail().trim()) != null) {
            errors.add("Email " + dto.getEmail() + " đã tồn tại!");
        }
        if (dto.getPhone() != null && !dto.getPhone().trim().isEmpty() && repo.findByPhone(dto.getPhone().trim()) != null) {
            errors.add("Số điện thoại " + dto.getPhone() + " đã được sử dụng!");
        }

        if (!errors.isEmpty()) {
            return errors;
        }

        User entity = new User();
        entity.setFullName(dto.getFullName().trim());
        entity.setEmail(dto.getEmail().trim());
        entity.setPhone(dto.getPhone());

        // Mã hóa SHA-256 Base64 bằng SecurityUtil
        entity.setPassword(SecurityUtil.hashPassword(dto.getPassword()));

        try {
            RoleTypeEnum role = RoleTypeEnum.valueOf(dto.getRole());
            // Bảo vệ logic: Nếu chọn CUSTOMER thì mặc định ép về STAFF
            entity.setRole(role == RoleTypeEnum.CUSTOMER ? RoleTypeEnum.STAFF : role);
        } catch (Exception e) {
            entity.setRole(RoleTypeEnum.STAFF);
        }

        entity.setStatus(UserStatusEnum.ACTIVE);
        entity.setIsVerified(true);

        boolean inserted = repo.insertUser(entity);
        if (!inserted) {
            errors.add("Lỗi hệ thống khi lưu vào cơ sở dữ liệu!");
        }

        return errors;
    }

    // 5. CẬP NHẬT THÔNG TIN CÁ NHÂN
    public List<String> editUser(UserDTO dto) {
        List<String> errors = new ArrayList<>();

        if (dto.getFullName() == null || dto.getFullName().trim().isEmpty()) {
            errors.add("Họ tên không được để trống!");
        }

        User existingUser = repo.findById(dto.getId());
        if (existingUser == null) {
            errors.add("Không tìm thấy tài khoản!");
            return errors;
        }

        if (dto.getEmail() == null || !dto.getEmail().trim().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            errors.add("Định dạng Email không hợp lệ!");
        } else {
            User checkEmail = repo.findByEmail(dto.getEmail().trim());
            if (checkEmail != null && !checkEmail.getId().equals(dto.getId())) {
                errors.add("Email " + dto.getEmail() + " đã tồn tại!");
            }
        }

        if (dto.getPhone() == null || !dto.getPhone().trim().matches("^\\d{10,11}$")) {
            errors.add("Số điện thoại phải từ 10 - 11 chữ số!");
        } else {
            User checkPhone = repo.findByPhone(dto.getPhone().trim());
            if (checkPhone != null && !checkPhone.getId().equals(dto.getId())) {
                errors.add("Số điện thoại " + dto.getPhone() + " đã bị trùng!");
            }
        }

        if (!errors.isEmpty()) {
            return errors;
        }

        existingUser.setFullName(dto.getFullName().trim());
        existingUser.setEmail(dto.getEmail().trim());
        existingUser.setPhone(dto.getPhone().trim());

        if (existingUser.getRole() != RoleTypeEnum.ADMIN) {
            try {
                existingUser.setRole(RoleTypeEnum.valueOf(dto.getRole()));
            } catch (Exception ignored) {
            }
        }

        boolean updated = repo.updateUser(existingUser);
        if (!updated) {
            errors.add("Lỗi hệ thống khi cập nhật cơ sở dữ liệu!");
        }

        return errors;
    }

    // 6. ĐỔI MẬT KHẨU (Check trùng mật khẩu cũ + Lưu SHA-256 Base64 bằng SecurityUtil)
    public List<String> changePassword(int id, String newPassword, String confirmPassword) {
        List<String> errors = new ArrayList<>();

        List<String> passErrors = UserValidationUtil.checkPassword(newPassword);
        errors.addAll(passErrors);

        if (confirmPassword == null || confirmPassword.trim().isEmpty()) {
            errors.add("Mật khẩu xác nhận không được để trống!");
        } else if (newPassword != null && !newPassword.equals(confirmPassword)) {
            errors.add("Mật khẩu xác nhận không khớp!");
        }

        if (!errors.isEmpty()) {
            return errors;
        }

        User user = repo.findById(id);
        if (user == null) {
            errors.add("Không tìm thấy tài khoản!");
            return errors;
        }

        // Kiểm tra xem mật khẩu mới có bị trùng với mật khẩu hiện tại trong CSDL không
        if (verifyPassword(newPassword, user.getPassword())) {
            errors.add("Mật khẩu mới không được trùng với mật khẩu hiện tại!");
            return errors;
        }

        // Mã hóa SHA-256 Base64 mật khẩu mới trước khi lưu CSDL
        user.setPassword(SecurityUtil.hashPassword(newPassword));
        boolean updated = repo.updateUser(user);
        if (!updated) {
            errors.add("Lỗi hệ thống khi cập nhật mật khẩu!");
        }

        return errors;
    }

    // 7. THAY ĐỔI VAI TRÒ
    public String changeUserRole(int id, String roleStr) {
        User user = repo.findById(id);
        if (user == null) return "Tài khoản không tồn tại!";
        if (user.getRole() == RoleTypeEnum.ADMIN) return "Không thể hạ quyền Admin bảo vệ hệ thống!";

        try {
            repo.updateRole(id, RoleTypeEnum.valueOf(roleStr));
            return "SUCCESS";
        } catch (Exception ignored) {
            return "Vai trò không hợp lệ!";
        }
    }

    // 8. KHÓA / MỞ KHÓA TÀI KHOẢN (XÓA MỀM)
    public String toggleUserStatus(int id) {
        User user = repo.findById(id);
        if (user == null) return "Tài khoản không tồn tại!";
        if (user.getRole() == RoleTypeEnum.ADMIN) return "Không thể khóa tài khoản Admin bảo vệ hệ thống!";

        repo.toggleStatus(id);
        return "SUCCESS";
    }
}