package com.qlbds.service;

import com.qlbds.dto.admin.DashboardDTO;
import com.qlbds.repository.ReportRepository;
import com.qlbds.util.ValidationUtil; // Thêm import ValidationUtil mới

import java.math.BigDecimal;
import java.time.LocalDate;

public class ReportService {

    private ReportRepository reportRepo = new ReportRepository();

    public DashboardDTO getDashboardData(String startDateStr, String endDateStr) {
        DashboardDTO dto = new DashboardDTO();

        // 1. Thống kê 4 thẻ tổng quan
        dto.setTotalAvailableBDS(reportRepo.countAvailableBDS());
        dto.setTotalDepositAmount(reportRepo.getTotalDepositAmount());
        dto.setTotalSoldBDS(reportRepo.countSoldBDS());
        dto.setTotalRevenue(reportRepo.getTotalRevenue());

        // 2. Thống kê biểu đồ 12 tháng năm hiện tại
        int currentYear = LocalDate.now().getYear(); //
        dto.setMonthlyData(reportRepo.getMonthlyTransactionCounts(currentYear)); //

        // 3. Xử lý logic lọc ngày tháng
        boolean isFirstAccess = (startDateStr == null || startDateStr.trim().isEmpty()) &&
                (endDateStr == null || endDateStr.trim().isEmpty()); //

        // Lần đầu vào trang: Tự động gán mặc định (Đầu tháng -> Hôm nay)
        if (isFirstAccess) {
            LocalDate defaultStart = LocalDate.now().withDayOfMonth(1); //
            LocalDate defaultEnd = LocalDate.now(); //

            dto.setStartDate(defaultStart.toString()); //
            dto.setEndDate(defaultEnd.toString()); //
            dto.setFilteredRevenue(reportRepo.getFilteredRevenue(defaultStart, defaultEnd)); //
            return dto; //
        }

        // Người dùng thực hiện lọc: Gọi sang lớp ValidationUtil tập trung mới để kiểm tra lỗi
        String errorMessage = ValidationUtil.validateFilterRange(startDateStr, endDateStr);
        dto.setStartDate(startDateStr); //
        dto.setEndDate(endDateStr); //

        if (errorMessage != null) {
            dto.setDateError(errorMessage); //
            dto.setFilteredRevenue(BigDecimal.ZERO); //
        } else {
            // Gọi hàm parse ngày từ ValidationUtil mới
            LocalDate start = ValidationUtil.parseDate(startDateStr);
            LocalDate end = ValidationUtil.parseDate(endDateStr);
            dto.setFilteredRevenue(reportRepo.getFilteredRevenue(start, end)); //
        }

        return dto; //
    }
}