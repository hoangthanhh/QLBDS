package com.qlbds.service;

import com.qlbds.constant.RoleTypeEnum;
import com.qlbds.constant.UserStatusEnum;
import com.qlbds.dto.acc.LoginDTO;
import com.qlbds.dto.acc.RegisterDTO;
import com.qlbds.dto.admin.AdminUserDTO;
import com.qlbds.dto.user.ChangePasswordDTO;
import com.qlbds.dto.user.UserDTO;
import com.qlbds.dto.user.UserProfileDTO;
import com.qlbds.entity.User;
import com.qlbds.repository.UserRepository;
import com.qlbds.util.SecurityUtil;
import com.qlbds.util.ValidationUtil;

import java.util.ArrayList;
import java.util.List;

public class UserService {
    private final UserRepository repo = new UserRepository();

    // Helper method xác thực mật khẩu
    private boolean verifyPassword(String inputPassword, String storedPassword) {
        if (inputPassword == null || storedPassword == null) return false;
        if (inputPassword.equals(storedPassword)) return true;
        return SecurityUtil.hashPassword(inputPassword).equals(storedPassword);
    }

    // Helper method dùng chung để chuyển User thành UserDTO
    private UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setFullName(user.getFullName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setRole(user.getRole() != null ? user.getRole().name() : "");
        dto.setStatus(user.getStatus() != null ? user.getStatus().name() : "");
        dto.setIsVerified(user.getIsVerified() != null ? user.getIsVerified() : false);
        dto.setCreatedAt(user.getCreatedAt());
        return dto;
    }

    // CUSTOMER (Đăng nhập, Đăng ký, Profile, Đổi MK)
    // 1. ĐĂNG KÝ TÀI KHOẢN KHÁCH HÀNG
    public String registerUser(RegisterDTO dto) {
        if (dto == null) return "Dữ liệu đăng ký không hợp lệ!";

        if (!ValidationUtil.isValidPhone(dto.getPhone())) return "Định dạng SĐT không hợp lệ!";
        if (!ValidationUtil.isValidEmail(dto.getEmail())) return "Định dạng Email không hợp lệ!";
        if (!ValidationUtil.isValidPassword(dto.getPassword()))
            return "Mật khẩu tối thiểu 6 ký tự, không chứa khoảng trắng!";
        if (!dto.getPassword().equals(dto.getConfirmPassword())) return "Xác nhận mật khẩu không khớp!";

        String cleanEmail = dto.getEmail().trim().toLowerCase();
        String cleanPhone = dto.getPhone().trim();

        if (repo.findByEmail(cleanEmail) != null) return "Email này đã được đăng ký!";
        if (repo.findByPhone(cleanPhone) != null) return "Số điện thoại này đã được sử dụng!";

        User user = new User();
        user.setFullName(dto.getFullName().trim());
        user.setPhone(cleanPhone);
        user.setEmail(cleanEmail);
        user.setPassword(SecurityUtil.hashPassword(dto.getPassword()));
        user.setRole(RoleTypeEnum.CUSTOMER);
        user.setStatus(UserStatusEnum.ACTIVE);
        user.setIsVerified(false);

        return repo.insertUser(user) ? "SUCCESS" : "Lỗi hệ thống khi lưu dữ liệu!";
    }

    // 2. ĐĂNG NHẬP HỆ THỐNG
    public UserDTO loginUser(LoginDTO loginDTO) {
        if (ValidationUtil.isEmpty(loginDTO.getEmail()) || ValidationUtil.isEmpty(loginDTO.getPassword()))
            return null;

        User user = repo.findByEmail(loginDTO.getEmail().trim());

        if (user != null && user.getStatus() == UserStatusEnum.ACTIVE
                && verifyPassword(loginDTO.getPassword(), user.getPassword())) {
            return convertToDTO(user);
        }
        return null;
    }

    // 3. CẬP NHẬT THÔNG TIN CÁ NHÂN (PROFILE)
    public String updateProfile(Integer userId, UserProfileDTO profileDTO) {
        if (profileDTO == null) return "Dữ liệu không hợp lệ!";

        // TỐI ƯU 2: Dùng isEmpty() thay cho việc kiểm tra lặp lại
        if (ValidationUtil.isEmpty(profileDTO.getFullName())) return "Họ tên không được để trống!";
        if (!ValidationUtil.isValidPhone(profileDTO.getPhone())) return "Định dạng SĐT không hợp lệ!";

        String cleanPhone = profileDTO.getPhone().trim();
        User userExist = repo.findByPhone(cleanPhone);
        if (userExist != null && !userExist.getId().equals(userId))
            return "Số điện thoại này đã được sử dụng bởi tài khoản khác!";

        User user = repo.findById(userId);
        if (user == null) return "Tài khoản không tồn tại!";

        user.setFullName(profileDTO.getFullName().trim());
        user.setPhone(cleanPhone);

        return repo.updateUser(user) ? "SUCCESS" : "Lỗi hệ thống khi cập nhật!";
    }

    // 4. KHÁCH HÀNG TỰ ĐỔI MẬT KHẨU
    public List<String> changePasswordAsUser(int id, ChangePasswordDTO dto) {
        List<String> errors = new ArrayList<>();
        if (dto == null) {
            errors.add("Dữ liệu không hợp lệ!");
            return errors;
        }

        String oldPass = dto.getOldPassword() != null ? dto.getOldPassword().trim() : "";
        String newPass = dto.getNewPassword() != null ? dto.getNewPassword().trim() : "";
        String confirmPass = dto.getConfirmPassword() != null ? dto.getConfirmPassword().trim() : "";

        if (ValidationUtil.isEmpty(oldPass)) {
            errors.add("Vui lòng nhập mật khẩu hiện tại!");
            return errors;
        }

        User user = repo.findById(id);
        if (user == null) {
            errors.add("Không tìm thấy tài khoản!");
            return errors;
        }

        if (!verifyPassword(oldPass, user.getPassword())) {
            errors.add("Mật khẩu hiện tại không chính xác!");
            return errors;
        }

        return processPasswordUpdate(user, newPass, confirmPass);
    }

    private List<String> processPasswordUpdate(User user, String newPassword, String confirmPassword) {
        List<String> errors = new ArrayList<>(ValidationUtil.checkPassword(newPassword));

        if (ValidationUtil.isEmpty(confirmPassword)) {
            errors.add("Mật khẩu xác nhận không được để trống!");
        } else if (!newPassword.equals(confirmPassword)) {
            errors.add("Mật khẩu xác nhận không khớp!");
        }

        if (!errors.isEmpty()) return errors;

        if (verifyPassword(newPassword, user.getPassword())) {
            errors.add("Mật khẩu mới không được trùng với mật khẩu hiện tại!");
            return errors;
        }

        user.setPassword(SecurityUtil.hashPassword(newPassword));
        if (!repo.updateUser(user)) {
            errors.add("Lỗi hệ thống khi cập nhật mật khẩu!");
        }

        return errors;
    }

    // ADMIN (Danh sách, Tìm kiếm, Thêm/Sửa, Khóa/Mở)
    // 1. LẤY DANH SÁCH USER CÓ HỖ TRỢ TÌM KIẾM THEO TỪ KHÓA VÀ PHÂN TRANG
    public List<UserDTO> getUserList(String keyword, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<User> entities = repo.searchUsers(keyword, offset, pageSize);
        List<UserDTO> dtos = new ArrayList<>();

        for (User entity : entities) {
            dtos.add(convertToDTO(entity)); // Gọi hàm helper để tái sử dụng
        }
        return dtos;
    }

    // 2. TÍNH TỔNG SỐ TRANG CÓ LỌC TỪ KHÓA
    public int getTotalPages(String keyword, int pageSize) {
        if (pageSize <= 0) pageSize = 5;
        int totalRecords = repo.countSearchUsers(keyword);
        return (int) Math.ceil((double) totalRecords / pageSize);
    }

    // 3. ADMIN THÊM TÀI KHOẢN MỚI
    public List<String> addAdminUser(AdminUserDTO.Create dto) {
        List<String> errors = ValidationUtil.validateAdminCreate(dto);

        if (!ValidationUtil.isEmpty(dto.getEmail()) && repo.findByEmail(dto.getEmail().trim()) != null) {
            errors.add("Email " + dto.getEmail() + " đã tồn tại!");
        }

        if (!ValidationUtil.isEmpty(dto.getPhone()) && repo.findByPhone(dto.getPhone().trim()) != null) {
            errors.add("Số điện thoại " + dto.getPhone() + " đã được sử dụng!");
        }

        if (!errors.isEmpty()) return errors;

        User entity = new User();
        entity.setFullName(dto.getFullName().trim());
        entity.setEmail(dto.getEmail().trim());
        entity.setPhone(dto.getPhone().trim()); // Đã an toàn để gọi trim() vì qua bước kiểm tra isEmpty()
        entity.setPassword(SecurityUtil.hashPassword(dto.getPassword()));

        try {
            RoleTypeEnum role = RoleTypeEnum.valueOf(dto.getRole());
            entity.setRole(role == RoleTypeEnum.CUSTOMER ? RoleTypeEnum.STAFF : role);
        } catch (Exception e) {
            entity.setRole(RoleTypeEnum.STAFF);
        }

        entity.setStatus(UserStatusEnum.ACTIVE);
        entity.setIsVerified(true);

        if (!repo.insertUser(entity)) {
            errors.add("Lỗi hệ thống khi lưu vào cơ sở dữ liệu!");
        }
        return errors;
    }

    // 4. ADMIN SỬA THÔNG TIN TÀI KHOẢN
    public List<String> editUser(AdminUserDTO.Update dto) {
        List<String> errors = new ArrayList<>();
        if (ValidationUtil.isEmpty(dto.getFullName())) {
            errors.add("Họ tên không được để trống!");
        }

        User existingUser = repo.findById(dto.getId());
        if (existingUser == null) {
            errors.add("Không tìm thấy tài khoản!");
            return errors;
        }

        if (!ValidationUtil.isValidEmail(dto.getEmail())) {
            errors.add("Định dạng Email không hợp lệ!");
        } else {
            String cleanEmail = dto.getEmail().trim();
            User checkEmail = repo.findByEmail(cleanEmail);
            if (checkEmail != null && !checkEmail.getId().equals(dto.getId()))
                errors.add("Email " + dto.getEmail() + " đã tồn tại!");
        }

        if (!ValidationUtil.isValidPhone(dto.getPhone())) {
            errors.add("Số điện thoại không hợp lệ! Phải bắt đầu bằng số 0.");
        } else {
            String cleanPhone = dto.getPhone().trim();
            User checkPhone = repo.findByPhone(cleanPhone);
            if (checkPhone != null && !checkPhone.getId().equals(dto.getId()))
                errors.add("Số điện thoại " + dto.getPhone() + " đã bị trùng!");
        }

        if (!errors.isEmpty()) return errors;

        existingUser.setFullName(dto.getFullName().trim());
        existingUser.setEmail(dto.getEmail().trim());
        existingUser.setPhone(dto.getPhone().trim());

        if (existingUser.getRole() != RoleTypeEnum.ADMIN) {
            try {
                existingUser.setRole(RoleTypeEnum.valueOf(dto.getRole()));
            } catch (Exception ignored) {
            }
        }
        if (!repo.updateUser(existingUser)) errors.add("Lỗi hệ thống khi cập nhật cơ sở dữ liệu!");
        return errors;
    }

    // 5. ADMIN ĐỔI MẬT KHẨU CHO USER
    public List<String> changePasswordAsAdmin(int id, String newPassword, String confirmPassword) {
        User user = repo.findById(id);
        if (user == null) {
            List<String> errors = new ArrayList<>();
            errors.add("Không tìm thấy tài khoản!");
            return errors;
        }

        return processPasswordUpdate(user, newPassword, confirmPassword);
    }

    // 6. ADMIN THAY ĐỔI VAI TRÒ (ROLE)
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

    // 7. ADMIN KHÓA / MỞ KHÓA TÀI KHOẢN
    public String toggleUserStatus(int id) {
        User user = repo.findById(id);
        if (user == null) return "Tài khoản không tồn tại!";
        if (user.getRole() == RoleTypeEnum.ADMIN) return "Không thể khóa tài khoản Admin bảo vệ hệ thống!";
        repo.toggleStatus(id);
        return "SUCCESS";
    }
}