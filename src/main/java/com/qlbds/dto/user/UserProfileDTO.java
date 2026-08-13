package com.qlbds.dto.user;

public class UserProfileDTO {
    private String fullName;
    private String phone;

    public UserProfileDTO() {}

    public UserProfileDTO(String fullName, String phone) {
        this.fullName = fullName;
        this.phone = phone;
    }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
}