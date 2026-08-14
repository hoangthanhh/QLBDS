package com.qlbds.service;

import com.qlbds.constant.RoleTypeEnum;
import com.qlbds.constant.UserStatusEnum;
import com.qlbds.dto.user.UserDTO;
import com.qlbds.dto.acc.LoginDTO;
import com.qlbds.dto.acc.RegisterDTO;
import com.qlbds.dto.user.ChangePasswordDTO;
import com.qlbds.dto.user.UserCreateDTO;
import com.qlbds.dto.user.UserProfileDTO;
import com.qlbds.dto.user.UserUpdateDTO;
import com.qlbds.entity.User;
import com.qlbds.repository.UserRepository;
import com.qlbds.util.SecurityUtil;
import com.qlbds.util.ValidationUtil;

import java.util.ArrayList;
import java.util.List;

public class UserService {
    private UserRepository repo = new UserRepository();

    // Helper method xác thực mật khẩu
    private boolean verifyPassword(String inputPassword, String storedPassword) {
        if (inputPassword == null || storedPassword == null) return false;
        if (inputPassword.equals(storedPassword)) return true;
        return SecurityUtil.hashPassword(inputPassword).equals(storedPassword);
    }


    //  CUSTOMER (Đăng nhập, Đăng ký, Profile, Đổi MK)
    // 1. ĐĂNG KÝ TÀI KHOẢN KHÁCH HÀNG
    public String registerUser(RegisterDTO dto) {
        if (dto == null) return "Dữ liệu đăng ký không hợp lệ!";

        if (!ValidationUtil.isValidPhone(dto.getPhone())) return "Định dạng SĐT không hợp lệ!";
        if (!ValidationUtil.isValidEmail(dto.getEmail())) return "Định dạng Email không hợp lệ!";
        if (!ValidationUtil.isValidPassword(dto.getPassword()))
            return "Mật khẩu tối thiểu 6 ký tự, không chứa khoảng trắng!";
        if (!dto.getPassword().equals(dto.getConfirmPassword())) return "Xác nhận mật khẩu không khớp!";

        if (repo.findByEmail(dto.getEmail().trim()) != null) return "Email này đã được đăng ký!";
        if (repo.findByPhone(dto.getPhone().trim()) != null) return "Số điện thoại này đã được sử dụng!";

        User user = new User();
        user.setFullName(dto.getFullName().trim());
        user.setPhone(dto.getPhone().trim());
        user.setEmail(dto.getEmail().trim().toLowerCase());
        user.setPassword(SecurityUtil.hashPassword(dto.getPassword()));
        user.setRole(RoleTypeEnum.CUSTOMER);
        user.setStatus(UserStatusEnum.ACTIVE);
        user.setIsVerified(false);

        return repo.insertUser(user) ? "SUCCESS" : "Lỗi hệ thống khi lưu dữ liệu!";
    }

    // 2. ĐĂNG NHẬP HỆ THỐNG
    public UserDTO loginUser(LoginDTO loginDTO) {
        if (loginDTO.getEmail() == null || loginDTO.getPassword() == null) return null;

        User user = repo.findByEmail(loginDTO.getEmail().trim());

        if (user != null && user.getStatus() == UserStatusEnum.ACTIVE
                && verifyPassword(loginDTO.getPassword(), user.getPassword())) {

            UserDTO userDTO = new UserDTO();
            userDTO.setId(user.getId());
            userDTO.setFullName(user.getFullName());
            userDTO.setEmail(user.getEmail());
            userDTO.setPhone(user.getPhone());
            userDTO.setRole(user.getRole().name());
            userDTO.setStatus(user.getStatus().name());

            userDTO.setIsVerified(user.getIsVerified() != null ? user.getIsVerified() : false);
            userDTO.setCreatedAt(user.getCreatedAt());

            return userDTO;
        }
        return null;
    }

    // 3. CẬP NHẬT THÔNG TIN CÁ NHÂN (PROFILE)
    public String updateProfile(Integer userId, UserProfileDTO profileDTO) {
        if (profileDTO == null) return "Dữ liệu không hợp lệ!";
        if (profileDTO.getFullName() == null || profileDTO.getFullName().trim().isEmpty())
            return "Họ tên không được để trống!";
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

        if (oldPass.isEmpty()) {
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

        if (verifyPassword(newPass, user.getPassword())) {
            errors.add("Mật khẩu mới không được trùng với mật khẩu hiện tại!");
            return errors;
        }

        return changePasswordAsAdmin(id, newPass, confirmPass);
    }


    // ADMIN (Danh sách, Tìm kiếm, Thêm/Sửa, Khóa/Mở)
    // 1. LẤY DANH SÁCH USER CÓ HỖ TRỢ TÌM KIẾM THEO TỪ KHÓA VÀ PHÂN TRANG
    public List<UserDTO> getUserList(String keyword, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<User> entities = repo.searchUsers(keyword, offset, pageSize);
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

    // Overload tương thích hàm cũ (khi không nhập từ khóa)
    public List<UserDTO> getUserList(int page, int pageSize) {
        return getUserList(null, page, pageSize);
    }

    // 2. TÍNH TỔNG SỐ TRANG CÓ LỌC TỪ KHÓA
    public int getTotalPages(String keyword, int pageSize) {
        if (pageSize <= 0) pageSize = 5;
        int totalRecords = repo.countSearchUsers(keyword);
        return (int) Math.ceil((double) totalRecords / pageSize);
    }

    // Overload tương thích hàm cũ
    public int getTotalPages(int pageSize) {
        return getTotalPages(null, pageSize);
    }

    // 3. ADMIN THÊM TÀI KHOẢN MỚI
    public List<String> addAdminUser(UserCreateDTO dto) {
        List<String> errors = ValidationUtil.validateAdminCreate(dto);
        if (dto.getEmail() != null && repo.findByEmail(dto.getEmail().trim()) != null)
            errors.add("Email " + dto.getEmail() + " đã tồn tại!");
        if (dto.getPhone() != null && !dto.getPhone().trim().isEmpty() && repo.findByPhone(dto.getPhone().trim()) != null)
            errors.add("Số điện thoại " + dto.getPhone() + " đã được sử dụng!");
        if (!errors.isEmpty()) return errors;

        User entity = new User();
        entity.setFullName(dto.getFullName().trim());
        entity.setEmail(dto.getEmail().trim());
        entity.setPhone(dto.getPhone());
        entity.setPassword(SecurityUtil.hashPassword(dto.getPassword()));

        try {
            RoleTypeEnum role = RoleTypeEnum.valueOf(dto.getRole());
            entity.setRole(role == RoleTypeEnum.CUSTOMER ? RoleTypeEnum.STAFF : role);
        } catch (Exception e) {
            entity.setRole(RoleTypeEnum.STAFF);
        }

        entity.setStatus(UserStatusEnum.ACTIVE);
        entity.setIsVerified(true);
        if (!repo.insertUser(entity)) errors.add("Lỗi hệ thống khi lưu vào cơ sở dữ liệu!");
        return errors;
    }

    // 4. ADMIN SỬA THÔNG TIN TÀI KHOẢN
    public List<String> editUser(UserUpdateDTO dto) {
        List<String> errors = new ArrayList<>();
        if (dto.getFullName() == null || dto.getFullName().trim().isEmpty()) errors.add("Họ tên không được để trống!");

        User existingUser = repo.findById(dto.getId());
        if (existingUser == null) {
            errors.add("Không tìm thấy tài khoản!");
            return errors;
        }

        if (!ValidationUtil.isValidEmail(dto.getEmail())) {
            errors.add("Định dạng Email không hợp lệ!");
        } else {
            User checkEmail = repo.findByEmail(dto.getEmail().trim());
            if (checkEmail != null && !checkEmail.getId().equals(dto.getId()))
                errors.add("Email " + dto.getEmail() + " đã tồn tại!");
        }

        if (!ValidationUtil.isValidPhone(dto.getPhone())) {
            errors.add("Số điện thoại không hợp lệ! Phải bắt đầu bằng số 0.");
        } else {
            User checkPhone = repo.findByPhone(dto.getPhone().trim());
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

    // 5. ADMIN ĐỔI MẬT KHẨU CHO USER (CÓ BẮT LỖI TRÙNG MẬT KHẨU CŨ)
    public List<String> changePasswordAsAdmin(int id, String newPassword, String confirmPassword) {
        List<String> errors = new ArrayList<>(ValidationUtil.checkPassword(newPassword));
        if (confirmPassword == null || confirmPassword.trim().isEmpty()) {
            errors.add("Mật khẩu xác nhận không được để trống!");
        } else if (newPassword != null && !newPassword.equals(confirmPassword)) {
            errors.add("Mật khẩu xác nhận không khớp!");
        }

        if (!errors.isEmpty()) return errors;

        User user = repo.findById(id);
        if (user == null) {
            errors.add("Không tìm thấy tài khoản!");
            return errors;
        }

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