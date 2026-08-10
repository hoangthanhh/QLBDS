package com.qlbds.service;

import com.qlbds.dto.StatisticDTO;
import com.qlbds.repository.StatisticRepository;
import com.qlbds.util.DateValidationUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class StatisticService {

    private StatisticRepository repo = new StatisticRepository();

    public StatisticDTO getDashboardData(String startDateStr, String endDateStr, int year) {
        StatisticDTO dto = new StatisticDTO();

        // 1. Gán các số liệu tổng quan
        dto.setTotalAccounts(repo.countTotalAccounts());
        dto.setTotalProperties(repo.countTotalProperties());
        dto.setTotalTransactions(repo.countTotalCompletedTransactions());
        dto.setTotalRevenue(repo.sumTotalRevenue());

        // 2. Xử lý biểu đồ 12 tháng
        List<Integer> monthlyData = new ArrayList<>();
        for (int i = 0; i < 12; i++) monthlyData.add(0); // Khởi tạo mảng 12 phần tử = 0

        List<Object[]> rawMonthlyData = repo.getMonthlyTransactionData(year);
        if (rawMonthlyData != null) {
            for (Object[] row : rawMonthlyData) {
                int month = (Integer) row[0];
                Long count = (Long) row[1];
                if (month >= 1 && month <= 12) {
                    monthlyData.set(month - 1, count.intValue()); // Đưa số lượng vào đúng tháng
                }
            }
        }
        dto.setMonthlyTransactionData(monthlyData);

        // 3. Xử lý tính doanh thu theo ngày lọc
        String dateError = DateValidationUtil.validateDateRange(startDateStr, endDateStr);
        if (dateError == null && startDateStr != null && endDateStr != null
                && !startDateStr.isEmpty() && !endDateStr.isEmpty()) {

            LocalDateTime start = LocalDate.parse(startDateStr).atStartOfDay();
            LocalDateTime end = LocalDate.parse(endDateStr).atTime(LocalTime.MAX);

            dto.setFilteredRevenue(repo.sumRevenueByDateRange(start, end));
        } else {
            dto.setFilteredRevenue(0.0); // Không lọc hoặc lỗi ngày thì doanh thu lọc = 0
        }

        return dto;
    }
}