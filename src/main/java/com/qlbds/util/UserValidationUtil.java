package com.qlbds.util;

import com.qlbds.dto.UserDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class UserValidationUtil {

    // Regex kiểm tra ký tự đặc biệt
    private static final Pattern SPECIAL_CHAR_PATTERN = Pattern.compile("[!@#$%^&*(),.?\":{}|<>]");

    // 1. Validate cho Khách hàng Đăng ký (Gom toàn bộ lỗi)
    public static List<String> validateRegister(UserDTO dto) {
        List<String> errors = new ArrayList<>();

        if (dto.getFullName() == null || dto.getFullName().trim().isEmpty()) {
            errors.add("Họ và tên không được để trống!");
        }
        if (dto.getEmail() == null || !dto.getEmail().trim().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            errors.add("Email không hợp lệ!");
        }
        if (dto.getPhone() == null || !dto.getPhone().trim().matches("^\\d{10,11}$")) {
            errors.add("Số điện thoại phải từ 10 đến 11 chữ số!");
        }

        // Kiểm tra lỗi mật khẩu
        List<String> passErrors = checkPassword(dto.getPassword());
        errors.addAll(passErrors);

        // Kiểm tra khớp mật khẩu xác nhận (nếu có trường confirmPassword)
        if (dto.getPassword() != null && dto.getConfirmPassword() != null) {
            if (!dto.getPassword().equals(dto.getConfirmPassword())) {
                errors.add("Mật khẩu xác nhận không khớp!");
            }
        }

        return errors;
    }

    // 2. Validate cho Admin thêm tài khoản (Gom toàn bộ lỗi)
    public static List<String> validateAdminCreate(UserDTO dto) {
        List<String> errors = new ArrayList<>();

        if (dto.getFullName() == null || dto.getFullName().trim().isEmpty()) {
            errors.add("Họ tên không được để trống!");
        }
        if (dto.getEmail() == null || !dto.getEmail().trim().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            errors.add("Email không hợp lệ!");
        }
        if (dto.getPhone() != null && !dto.getPhone().trim().isEmpty()) {
            if (!dto.getPhone().trim().matches("^\\d{10,11}$")) {
                errors.add("Số điện thoại phải từ 10 đến 11 chữ số!");
            }
        }

        List<String> passErrors = checkPassword(dto.getPassword());
        errors.addAll(passErrors);

        return errors;
    }

    // Hàm phụ trợ check chi tiết mật khẩu
    // Hàm phụ trợ check chi tiết mật khẩu
    public static List<String> checkPassword(String password) {
        List<String> passErrors = new ArrayList<>();

        if (password == null || password.trim().isEmpty()) {
            passErrors.add("Mật khẩu không được để trống!");
        } else if (password.length() < 6) {
            passErrors.add("Mật khẩu phải có tối thiểu từ 6 ký tự trở lên!");
        } else if (!SPECIAL_CHAR_PATTERN.matcher(password).find()) {
            passErrors.add("Mật khẩu phải chứa ít nhất một ký tự đặc biệt (ví dụ: @, #, $, !,...)!");
        }

        return passErrors;
    }
}