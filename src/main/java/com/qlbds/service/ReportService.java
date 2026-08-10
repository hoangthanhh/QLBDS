package com.qlbds.service;

import com.qlbds.dto.DashboardDTO;
import com.qlbds.repository.ReportRepository;
import com.qlbds.util.DateValidationUtil;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ReportService {

    private ReportRepository reportRepo = new ReportRepository();

    public DashboardDTO getDashboardData(String startDateStr, String endDateStr) {
        DashboardDTO dto = new DashboardDTO();

        // 1. Thống kê 4 thẻ tổng quan
        dto.setTotalAccounts(reportRepo.countTotalAccounts());
        dto.setTotalBDS(reportRepo.countAvailableBDS());
        dto.setTotalTransactions(reportRepo.countTotalSuccessfulTransactions());
        dto.setTotalRevenue(reportRepo.getTotalRevenue());

        // 2. Thống kê biểu đồ 12 tháng năm hiện tại
        int currentYear = LocalDate.now().getYear();
        dto.setMonthlyData(reportRepo.getMonthlyTransactionCounts(currentYear));

        // 3. Xử lý logic lọc ngày tháng
        boolean isFirstAccess = (startDateStr == null || startDateStr.trim().isEmpty()) &&
                (endDateStr == null || endDateStr.trim().isEmpty());

        // Lần đầu vào trang: Tự động gán mặc định (Đầu tháng -> Hôm nay)
        if (isFirstAccess) {
            LocalDate defaultStart = LocalDate.now().withDayOfMonth(1);
            LocalDate defaultEnd = LocalDate.now();

            dto.setStartDate(defaultStart.toString());
            dto.setEndDate(defaultEnd.toString());
            dto.setFilteredRevenue(reportRepo.getFilteredRevenue(defaultStart, defaultEnd));
            return dto;
        }

        // Người dùng thực hiện lọc: Validate bắt lỗi chặt chẽ
        String errorMessage = DateValidationUtil.validateFilterRange(startDateStr, endDateStr);
        dto.setStartDate(startDateStr);
        dto.setEndDate(endDateStr);

        if (errorMessage != null) {
            dto.setDateError(errorMessage);
            dto.setFilteredRevenue(BigDecimal.ZERO);
        } else {
            LocalDate start = DateValidationUtil.parseDate(startDateStr);
            LocalDate end = DateValidationUtil.parseDate(endDateStr);
            dto.setFilteredRevenue(reportRepo.getFilteredRevenue(start, end));
        }

        return dto;
    }
}