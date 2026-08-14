package com.qlbds.util;

import com.qlbds.dto.user.UserCreateDTO;
import com.qlbds.dto.property.PropertyCreateUpdateDTO;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class    ValidationUtil {

    private static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@(.+)$";
    private static final String PHONE_PATTERN = "^0\\d{9,10}$";
    private static final String PASSWORD_PATTERN = "^\\S{6,}$";

    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) return false;
        return Pattern.compile(EMAIL_PATTERN).matcher(email.trim()).matches();
    }

    public static boolean isValidPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) return false;
        return Pattern.compile(PHONE_PATTERN).matcher(phone.trim()).matches();
    }

    public static boolean isValidPassword(String password) {
        if (password == null || password.trim().isEmpty()) return false;
        return Pattern.compile(PASSWORD_PATTERN).matcher(password).matches();
    }

    public static List<String> checkPassword(String password) {
        List<String> passErrors = new ArrayList<>();
        if (password == null || password.trim().isEmpty()) {
            passErrors.add("Mật khẩu không được để trống!");
        } else if (!isValidPassword(password)) {
            passErrors.add("Mật khẩu phải có tối thiểu 6 ký tự và không chứa khoảng trắng!");
        }
        return passErrors;
    }

    // ĐÃ SỬA: Dùng UserCreateDTO
    public static List<String> validateAdminCreate(UserCreateDTO dto) {
        List<String> errors = new ArrayList<>();
        if (dto.getFullName() == null || dto.getFullName().trim().isEmpty()) errors.add("Họ tên không được để trống!");
        if (!isValidEmail(dto.getEmail())) errors.add("Email không hợp lệ!");
        if (dto.getPhone() != null && !dto.getPhone().trim().isEmpty()) {
            if (!isValidPhone(dto.getPhone())) errors.add("Số điện thoại phải từ 10 đến 11 chữ số và bắt đầu bằng số 0!");
        }
        errors.addAll(checkPassword(dto.getPassword()));
        return errors;
    }

    public static LocalDate parseDate(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) return null;
        try {
            return LocalDate.parse(dateStr.trim());
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    public static String validateFilterRange(String startDateStr, String endDateStr) {
        boolean startEmpty = (startDateStr == null || startDateStr.trim().isEmpty());
        boolean endEmpty = (endDateStr == null || endDateStr.trim().isEmpty());
        if (startEmpty && endEmpty) return "Vui lòng chọn Từ ngày và Đến ngày để lọc dữ liệu!";
        if (startEmpty) return "Vui lòng chọn Từ ngày cụ thể!";
        if (endEmpty) return "Vui lòng chọn Đến ngày cụ thể!";

        LocalDate start = parseDate(startDateStr);
        LocalDate end = parseDate(endDateStr);
        if (start == null || end == null) return "Định dạng ngày tháng chọn không hợp lệ!";
        if (start.isAfter(end)) return "Từ ngày không được lớn hơn Đến ngày!";
        return null;
    }
    public static List<String> validateProperty(PropertyCreateUpdateDTO dto, boolean isCreate) {
        List<String> errors = new ArrayList<>();
        if (dto == null) {
            errors.add("Dữ liệu gửi lên không hợp lệ!");
            return errors;
        }
        if (dto.getTitle() == null || dto.getTitle().trim().isEmpty()) {
            errors.add("Tiêu đề BĐS không được để trống!");
        }
        if (dto.getAddress() == null || dto.getAddress().trim().isEmpty()) {
            errors.add("Địa chỉ BĐS không được để trống!");
        }
        if (dto.getPropertyType() == null || dto.getPropertyType().trim().isEmpty()) {
            errors.add("Vui lòng chọn loại hình BĐS!");
        }
        if (dto.getPrice() == null || dto.getPrice() <= 0) {
            errors.add("Giá BĐS phải lớn hơn 0!");
        }
        if (dto.getArea() == null || dto.getArea() <= 0) {
            errors.add("Diện tích BĐS phải lớn hơn 0!");
        }
        if (isCreate && (dto.getImageParts() == null || dto.getImageParts().isEmpty())) {
            errors.add("Vui lòng chọn ít nhất 1 ảnh minh họa khi tạo mới!");
        }
        return errors;
    }
}