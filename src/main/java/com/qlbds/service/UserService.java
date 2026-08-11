package com.qlbds.service;

import com.qlbds.constant.RoleTypeEnum;
import com.qlbds.constant.UserStatusEnum;
import com.qlbds.dto.UserDTO;
import com.qlbds.entity.User;
import com.qlbds.repository.UserRepository;
import com.qlbds.util.SecurityUtil;
import com.qlbds.util.ValidationUtil;

import java.util.ArrayList;
import java.util.List;

public class UserService {
    private UserRepository repo = new UserRepository();

    private boolean verifyPassword(String inputPassword, String storedPassword) {
        if (inputPassword == null || storedPassword == null) return false;
        if (inputPassword.equals(storedPassword)) return true;
        return SecurityUtil.hashPassword(inputPassword).equals(storedPassword);
    }

    public String registerUser(UserDTO userDTO) {
        if (!ValidationUtil.isValidPhone(userDTO.getPhone())) return "Định dạng SĐT không hợp lệ!";
        if (!ValidationUtil.isValidEmail(userDTO.getEmail())) return "Định dạng Email không hợp lệ!";
        if (!ValidationUtil.isValidPassword(userDTO.getPassword())) return "Mật khẩu tối thiểu 6 ký tự, không chứa khoảng trắng!";
        if (!userDTO.getPassword().equals(userDTO.getConfirmPassword())) return "Xác nhận mật khẩu không khớp!";

        if (repo.findByEmail(userDTO.getEmail()) != null) return "Email này đã được đăng ký!";
        if (repo.findByPhone(userDTO.getPhone()) != null) return "Số điện thoại này đã được sử dụng!";

        User user = new User();
        user.setFullName(userDTO.getFullName().trim());
        user.setPhone(userDTO.getPhone().trim());
        user.setEmail(userDTO.getEmail().trim().toLowerCase());
        user.setPassword(SecurityUtil.hashPassword(userDTO.getPassword()));
        user.setRole(RoleTypeEnum.CUSTOMER);
        user.setStatus(UserStatusEnum.ACTIVE);
        user.setIsVerified(false);

        return repo.insertUser(user) ? "SUCCESS" : "Lỗi hệ thống khi lưu dữ liệu!";
    }

    public User loginUser(String email, String password) {
        if (email == null || password == null) return null;
        User user = repo.findByEmail(email.trim());
        if (user != null && user.getStatus() == UserStatusEnum.ACTIVE && verifyPassword(password, user.getPassword())) {
            return user;
        }
        return null;
    }

    public String updateProfile(Integer userId, String fullName, String phone) {
        if (fullName == null || fullName.trim().isEmpty()) return "Họ tên không được để trống!";
        if (!ValidationUtil.isValidPhone(phone)) return "Định dạng SĐT không hợp lệ!";

        User userExist = repo.findByPhone(phone.trim());
        if (userExist != null && !userExist.getId().equals(userId)) return "Số điện thoại này đã được sử dụng bởi tài khoản khác!";

        User user = repo.findById(userId);
        if (user == null) return "Tài khoản không tồn tại!";

        user.setFullName(fullName.trim());
        user.setPhone(phone.trim());

        return repo.updateUser(user) ? "SUCCESS" : "Lỗi hệ thống khi cập nhật!";
    }

    // --- CÁC HÀM CHO ADMIN QUẢN LÝ ---
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

    public List<String> addAdminUser(UserDTO dto) {
        List<String> errors = ValidationUtil.validateAdminCreate(dto);
        if (dto.getEmail() != null && repo.findByEmail(dto.getEmail().trim()) != null) errors.add("Email " + dto.getEmail() + " đã tồn tại!");
        if (dto.getPhone() != null && !dto.getPhone().trim().isEmpty() && repo.findByPhone(dto.getPhone().trim()) != null) errors.add("Số điện thoại " + dto.getPhone() + " đã được sử dụng!");
        if (!errors.isEmpty()) return errors;

        User entity = new User();
        entity.setFullName(dto.getFullName().trim());
        entity.setEmail(dto.getEmail().trim());
        entity.setPhone(dto.getPhone());
        entity.setPassword(SecurityUtil.hashPassword(dto.getPassword()));

        try {
            RoleTypeEnum role = RoleTypeEnum.valueOf(dto.getRole());
            entity.setRole(role == RoleTypeEnum.CUSTOMER ? RoleTypeEnum.STAFF : role);
        } catch (Exception e) { entity.setRole(RoleTypeEnum.STAFF); }

        entity.setStatus(UserStatusEnum.ACTIVE);
        entity.setIsVerified(true);
        if (!repo.insertUser(entity)) errors.add("Lỗi hệ thống khi lưu vào cơ sở dữ liệu!");
        return errors;
    }

    public List<String> editUser(UserDTO dto) {
        List<String> errors = new ArrayList<>();
        if (dto.getFullName() == null || dto.getFullName().trim().isEmpty()) errors.add("Họ tên không được để trống!");

        User existingUser = repo.findById(dto.getId());
        if (existingUser == null) { errors.add("Không tìm thấy tài khoản!"); return errors; }

        if (!ValidationUtil.isValidEmail(dto.getEmail())) {
            errors.add("Định dạng Email không hợp lệ!");
        } else {
            User checkEmail = repo.findByEmail(dto.getEmail().trim());
            if (checkEmail != null && !checkEmail.getId().equals(dto.getId())) errors.add("Email " + dto.getEmail() + " đã tồn tại!");
        }

        if (!ValidationUtil.isValidPhone(dto.getPhone())) {
            errors.add("Số điện thoại không hợp lệ! Phải bắt đầu bằng số 0.");
        } else {
            User checkPhone = repo.findByPhone(dto.getPhone().trim());
            if (checkPhone != null && !checkPhone.getId().equals(dto.getId())) errors.add("Số điện thoại " + dto.getPhone() + " đã bị trùng!");
        }

        if (!errors.isEmpty()) return errors;

        existingUser.setFullName(dto.getFullName().trim());
        existingUser.setEmail(dto.getEmail().trim());
        existingUser.setPhone(dto.getPhone().trim());

        if (existingUser.getRole() != RoleTypeEnum.ADMIN) {
            try { existingUser.setRole(RoleTypeEnum.valueOf(dto.getRole())); } catch (Exception ignored) {}
        }
        if (!repo.updateUser(existingUser)) errors.add("Lỗi hệ thống khi cập nhật cơ sở dữ liệu!");
        return errors;
    }

    // --- ADMIN ĐỔI MẬT KHẨU CHO KHÁCH (Không cần pass cũ) ---
    public List<String> changePasswordAsAdmin(int id, String newPassword, String confirmPassword) {
        List<String> errors = new ArrayList<>(ValidationUtil.checkPassword(newPassword));
        if (confirmPassword == null || confirmPassword.trim().isEmpty()) errors.add("Mật khẩu xác nhận không được để trống!");
        else if (newPassword != null && !newPassword.equals(confirmPassword)) errors.add("Mật khẩu xác nhận không khớp!");
        if (!errors.isEmpty()) return errors;

        User user = repo.findById(id);
        if (user == null) { errors.add("Không tìm thấy tài khoản!"); return errors; }

        user.setPassword(SecurityUtil.hashPassword(newPassword));
        if (!repo.updateUser(user)) errors.add("Lỗi hệ thống khi cập nhật mật khẩu!");
        return errors;
    }

    // --- USER TỰ ĐỔI MẬT KHẨU (Bắt buộc check pass cũ) ---
    public List<String> changePasswordAsUser(int id, String oldPassword, String newPassword, String confirmPassword) {
        List<String> errors = new ArrayList<>();
        if (oldPassword == null || oldPassword.trim().isEmpty()) {
            errors.add("Vui lòng nhập mật khẩu hiện tại!");
            return errors;
        }

        User user = repo.findById(id);
        if (user == null) {
            errors.add("Không tìm thấy tài khoản!");
            return errors;
        }

        if (!verifyPassword(oldPassword, user.getPassword())) {
            errors.add("Mật khẩu hiện tại không chính xác!");
            return errors;
        }

        if (verifyPassword(newPassword, user.getPassword())) {
            errors.add("Mật khẩu mới không được trùng với mật khẩu hiện tại!");
            return errors;
        }

        // Tái sử dụng logic kiểm tra pass mới của Admin
        return changePasswordAsAdmin(id, newPassword, confirmPassword);
    }

    public String changeUserRole(int id, String roleStr) {
        User user = repo.findById(id);
        if (user == null) return "Tài khoản không tồn tại!";
        if (user.getRole() == RoleTypeEnum.ADMIN) return "Không thể hạ quyền Admin bảo vệ hệ thống!";
        try {
            repo.updateRole(id, RoleTypeEnum.valueOf(roleStr));
            return "SUCCESS";
        } catch (Exception ignored) { return "Vai trò không hợp lệ!"; }
    }

    public String toggleUserStatus(int id) {
        User user = repo.findById(id);
        if (user == null) return "Tài khoản không tồn tại!";
        if (user.getRole() == RoleTypeEnum.ADMIN) return "Không thể khóa tài khoản Admin bảo vệ hệ thống!";
        repo.toggleStatus(id);
        return "SUCCESS";
    }
}