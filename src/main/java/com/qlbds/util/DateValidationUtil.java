package com.qlbds.util;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class DateValidationUtil {

    /**
     * Parse chuỗi ngày sang LocalDate, trả về null nếu chuỗi rỗng hoặc sai định dạng
     */
    public static LocalDate parseDate(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return null;
        }
        try {
            return LocalDate.parse(dateStr.trim());
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    /**
     * Bắt lỗi bắt buộc chọn ngày cụ thể và từ ngày không được lớn hơn đến ngày
     */
    public static String validateFilterRange(String startDateStr, String endDateStr) {
        boolean startEmpty = (startDateStr == null || startDateStr.trim().isEmpty());
        boolean endEmpty = (endDateStr == null || endDateStr.trim().isEmpty());

        if (startEmpty && endEmpty) {
            return "Vui lòng chọn Từ ngày và Đến ngày để lọc dữ liệu!";
        }
        if (startEmpty) {
            return "Vui lòng chọn Từ ngày cụ thể!";
        }
        if (endEmpty) {
            return "Vui lòng chọn Đến ngày cụ thể!";
        }

        LocalDate start = parseDate(startDateStr);
        LocalDate end = parseDate(endDateStr);

        if (start == null || end == null) {
            return "Định dạng ngày tháng chọn không hợp lệ!";
        }

        if (start.isAfter(end)) {
            return "Từ ngày không được lớn hơn Đến ngày!";
        }

        return null; // Không có lỗi
    }
}