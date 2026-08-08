package com.qlbds.dto;

public class UserDTO {
    private String fullName;
    private String phone;
    private String email;
    private String password;
    private String confirmPassword;
    private String role; // "CUSTOMER", "STAFF", "ADMIN"

    // Constructor rỗng (Bắt buộc cho Jackson/Gson/Servlet binding)
    public UserDTO() {
    }

    // Constructor đầy đủ tham số
    public UserDTO(String fullName, String phone, String email, String password, String confirmPassword, String role) {
        this.fullName = fullName;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.role = role;
    }

    // Constructor phục vụ cho Admin tạo tài khoản nhanh
    public UserDTO(String fullName, String phone, String email, String password, String role) {
        this.fullName = fullName;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    // ==========================================
    // CÁC HÀM VALIDATE CHUẨN NGHIỆP VỤ thực tế
    // ==========================================

    // Validate cho luồng Đăng ký Khách hàng
    public String validateRegister() {
        if (fullName == null || fullName.trim().isEmpty()) {
            return "Họ và tên không được để trống!";
        }
        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            return "Email không đúng định dạng!";
        }
        if (password == null || password.length() < 6) {
            return "Mật khẩu phải chứa ít nhất 6 ký tự!";
        }
        if (!password.equals(confirmPassword)) {
            return "Mật khẩu xác nhận không khớp!";
        }
        return null;
    }

    // Validate cho luồng Admin tạo tài khoản
    public String validateAdminCreate() {
        if (fullName == null || fullName.trim().isEmpty()) {
            return "Họ và tên không được để trống!";
        }
        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            return "Email không đúng định dạng!";
        }
        if (password == null || password.length() < 6) {
            return "Mật khẩu phải chứa ít nhất 6 ký tự!";
        }
        if (role == null || role.trim().isEmpty()) {
            return "Vui lòng chọn vai trò (phân quyền) cho tài khoản!";
        }
        return null;
    }

    // ==========================================
    // GETTERS & SETTERS
    // ==========================================
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}