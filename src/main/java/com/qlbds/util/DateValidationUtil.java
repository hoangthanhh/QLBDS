package com.qlbds.util;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class DateValidationUtil {

    public static String validateDateRange(String startDateStr, String endDateStr) {
        if (startDateStr == null || startDateStr.trim().isEmpty() ||
                endDateStr == null || endDateStr.trim().isEmpty()) {
            return null; // Bỏ qua nếu người dùng không chọn ngày
        }

        try {
            LocalDate start = LocalDate.parse(startDateStr);
            LocalDate end = LocalDate.parse(endDateStr);

            if (start.isAfter(end)) {
                return "Lỗi: 'Từ ngày' không được lớn hơn 'Đến ngày'!";
            }
        } catch (DateTimeParseException e) {
            return "Định dạng ngày tháng không hợp lệ!";
        }
        return null;
    }
}