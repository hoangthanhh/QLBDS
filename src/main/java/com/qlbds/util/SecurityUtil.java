package com.qlbds.util;

import java.security.MessageDigest;
import java.util.Base64;

public class SecurityUtil {

    /**
     * Hàm băm mật khẩu bằng thuật toán SHA-256
     * @param password Mật khẩu gốc (chuỗi văn bản thường)
     * @return Chuỗi mật khẩu đã được băm mã hóa dưới dạng Base64
     */
    public static String hashPassword(String password) {
        try {
            // Khởi tạo đối tượng MessageDigest với thuật toán SHA-256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // Thực hiện băm chuỗi mật khẩu (chuyển về mảng byte dạng UTF-8)
            byte[] hash = digest.digest(password.getBytes("UTF-8"));

            // Chuyển đổi mảng byte kết quả sang chuỗi mã hóa Base64 để lưu vào Database
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception ex) {
            // Ném ra ngoại lệ nếu có lỗi trong quá trình cấu hình mã hóa
            throw new RuntimeException("Lỗi xảy ra trong quá trình mã hóa mật khẩu", ex);
        }
    }
}