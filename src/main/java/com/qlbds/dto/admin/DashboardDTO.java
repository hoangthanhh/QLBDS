package com.qlbds.dto.admin;

import java.math.BigDecimal;
import java.util.List;

public class DashboardDTO {
    private long totalAvailableBDS; // BĐS đang bán
    private BigDecimal totalDepositAmount;
    private long totalSoldBDS;      // BĐS đã bán
    private BigDecimal totalRevenue;
    private List<Long> monthlyData;
    private BigDecimal filteredRevenue;
    private String startDate;
    private String endDate;
    private String dateError;

    public BigDecimal getTotalDepositAmount() { return totalDepositAmount; }
    public void setTotalDepositAmount(BigDecimal totalDepositAmount) {
        this.totalDepositAmount = (totalDepositAmount != null) ? totalDepositAmount : BigDecimal.ZERO;
    }

    public DashboardDTO() {
        this.totalRevenue = BigDecimal.ZERO;
        this.filteredRevenue = BigDecimal.ZERO;
    }

    public long getTotalAvailableBDS() { return totalAvailableBDS; }
    public void setTotalAvailableBDS(long totalAvailableBDS) { this.totalAvailableBDS = totalAvailableBDS; }


    public long getTotalSoldBDS() { return totalSoldBDS; }
    public void setTotalSoldBDS(long totalSoldBDS) { this.totalSoldBDS = totalSoldBDS; }

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