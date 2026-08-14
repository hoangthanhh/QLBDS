package com.qlbds.dto.user;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UserDTO {
    private Integer id;
    private String fullName;
    private String email;
    private String phone;
    private String role;
    private String status;
    private Boolean isVerified; // Dùng Boolean (chữ B viết hoa) để đồng bộ với Entity, tránh NullPointerException
    private LocalDateTime createdAt; // Dùng để hiển thị ngày tham gia trên trang cá nhân

    public UserDTO() {
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    // ĐÃ XÓA GETTER/SETTER CỦA PASSWORD VÀ CONFIRM_PASSWORD ĐỂ BẢO MẬT BỘ NHỚ SESSION

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Boolean getIsVerified() { return isVerified; }
    public void setIsVerified(Boolean isVerified) { this.isVerified = isVerified; }

    // ... các getter setter khác của bạn giữ nguyên ...

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    // THÊM HÀM NÀY ĐỂ RENDER RA GIAO DIỆN
    public String getFormattedCreatedAt() {
        if (this.createdAt == null) return "";
        // Định dạng ra kiểu: 15 Thg 08, 2026
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd 'Thg' MM, yyyy");
        return this.createdAt.format(formatter);
    }
}