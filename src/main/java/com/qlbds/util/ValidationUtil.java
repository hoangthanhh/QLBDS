package com.qlbds.util;

import com.qlbds.dto.admin.AdminUserDTO;
import com.qlbds.dto.property.PropertySaveDTO;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class ValidationUtil {

    // 1. BIÊN DỊCH REGEX 1 LẦN DUY NHẤT ĐỂ TỐI ƯU HIỆU NĂNG
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^0\\d{9,10}$");
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^\\S{6,}$");

    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    public static boolean isValidEmail(String email) {
        if (isEmpty(email)) return false;
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    public static boolean isValidPhone(String phone) {
        if (isEmpty(phone)) return false;
        return PHONE_PATTERN.matcher(phone.trim()).matches();
    }

    public static boolean isValidPassword(String password) {
        if (isEmpty(password)) return false;
        return PASSWORD_PATTERN.matcher(password).matches();
    }

    public static List<String> checkPassword(String password) {
        List<String> passErrors = new ArrayList<>();
        if (isEmpty(password)) {
            passErrors.add("Mật khẩu không được để trống!");
        } else if (!isValidPassword(password)) {
            passErrors.add("Mật khẩu phải có tối thiểu 6 ký tự và không chứa khoảng trắng!");
        }
        return passErrors;
    }

    public static List<String> validateAdminCreate(AdminUserDTO.Create dto) {
        List<String> errors = new ArrayList<>();

        if (isEmpty(dto.getFullName())) {
            errors.add("Họ tên không được để trống!");
        }

        if (!isValidEmail(dto.getEmail())) {
            errors.add("Email không hợp lệ!");
        }

        if (!isEmpty(dto.getPhone()) && !isValidPhone(dto.getPhone())) {
            errors.add("Số điện thoại phải từ 10 đến 11 chữ số và bắt đầu bằng số 0!");
        }

        errors.addAll(checkPassword(dto.getPassword()));
        return errors;
    }

    public static LocalDate parseDate(String dateStr) {
        if (isEmpty(dateStr)) return null;
        try {
            return LocalDate.parse(dateStr.trim());
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    public static String validateFilterRange(String startDateStr, String endDateStr) {
        boolean startEmpty = isEmpty(startDateStr);
        boolean endEmpty = isEmpty(endDateStr);

        if (startEmpty && endEmpty) return "Vui lòng chọn Từ ngày và Đến ngày để lọc dữ liệu!";
        if (startEmpty) return "Vui lòng chọn Từ ngày cụ thể!";
        if (endEmpty) return "Vui lòng chọn Đến ngày cụ thể!";

        LocalDate start = parseDate(startDateStr);
        LocalDate end = parseDate(endDateStr);

        if (start == null || end == null) return "Định dạng ngày tháng chọn không hợp lệ!";
        if (start.isAfter(end)) return "Từ ngày không được lớn hơn Đến ngày!";
        return null;
    }

    public static List<String> validateProperty(PropertySaveDTO dto, boolean isCreate) {
        List<String> errors = new ArrayList<>();

        if (dto == null) {
            errors.add("Dữ liệu không hợp lệ!");
            return errors;
        }

        if (isEmpty(dto.getTitle())) {
            errors.add("Tiêu đề BĐS không được để trống!");
        }

        if (isEmpty(dto.getAddress())) {
            errors.add("Địa chỉ BĐS không được để trống!");
        }

        if (dto.getPrice() == null || dto.getPrice() <= 0) {
            errors.add("Giá BĐS phải lớn hơn 0!");
        }

        if (dto.getArea() == null || dto.getArea() <= 0) {
            errors.add("Diện tích BĐS phải lớn hơn 0!");
        }

        if (isEmpty(dto.getPropertyType())) {
            errors.add("Vui lòng chọn loại BĐS!");
        }

        // Tối ưu lại phần kiểm tra danh sách ảnh
        int imageCount = (dto.getImageParts() != null) ? dto.getImageParts().size() : 0;

        if (isCreate && imageCount == 0) {
            errors.add("Vui lòng tải lên ít nhất một ảnh cho BĐS!");
        }

        if (imageCount > 10) {
            errors.add("Chỉ được phép tải lên tối đa 10 ảnh cho mỗi BĐS!");
        }

        return errors;
    }
}