package com.qlbds.util;

import java.util.regex.Pattern;

public class ValidationUtil {

    // Email chuẩn
    private static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@(.+)$";
    // SĐT Việt Nam: 10 số, bắt đầu bằng 0
    private static final String PHONE_PATTERN = "^0\\d{9}$";
    // Mật khẩu: Tối thiểu 6 ký tự k khoang trang
    private static final String PASSWORD_PATTERN = "^\\S{6,}$";

    public static boolean isValidEmail(String email) {
        if (email == null) return false;
        return Pattern.compile(EMAIL_PATTERN).matcher(email).matches();
    }

    public static boolean isValidPhone(String phone) {
        if (phone == null) return false;
        return Pattern.compile(PHONE_PATTERN).matcher(phone).matches();
    }

    public static boolean isValidPassword(String password) {
        if (password == null) return false;
        return Pattern.compile(PASSWORD_PATTERN).matcher(password).matches();
    }
}