package com.qlbds.dto.admin;

import java.math.BigDecimal;
import java.util.List;

public class DashboardDTO {
    private long totalDepositedBDS;
    private long totalSoldBDS;

    public long getTotalDepositedBDS() { return totalDepositedBDS; }
    public void setTotalDepositedBDS(long totalDepositedBDS) { this.totalDepositedBDS = totalDepositedBDS; }

    public long getTotalSoldBDS() { return totalSoldBDS; }
    public void setTotalSoldBDS(long totalSoldBDS) { this.totalSoldBDS = totalSoldBDS; }
    private long totalTransactions;
    private BigDecimal totalRevenue;
    private List<Long> monthlyData; // Mảng 12 phần tử đại diện cho 12 tháng
    private BigDecimal filteredRevenue;
    private String startDate;
    private String endDate;
    private String dateError; // Chứa thông báo lỗi nếu chọn thiếu hoặc ngược ngày

    public DashboardDTO() {
        this.totalRevenue = BigDecimal.ZERO;
        this.filteredRevenue = BigDecimal.ZERO;
    }

    public long getTotalTransactions() { return totalTransactions; }
    public void setTotalTransactions(long totalTransactions) { this.totalTransactions = totalTransactions; }

    public BigDecimal getTotalRevenue() { return totalRevenue; }
    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = (totalRevenue != null) ? totalRevenue : BigDecimal.ZERO;
    }

    public List<Long> getMonthlyData() { return monthlyData; }
    public void setMonthlyData(List<Long> monthlyData) { this.monthlyData = monthlyData; }

    public BigDecimal getFilteredRevenue() { return filteredRevenue; }
    public void setFilteredRevenue(BigDecimal filteredRevenue) {
        this.filteredRevenue = (filteredRevenue != null) ? filteredRevenue : BigDecimal.ZERO;
    }

    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }

    public String getEndDate() { return endDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }

    public String getDateError() { return dateError; }
    public void setDateError(String dateError) { this.dateError = dateError; }
}