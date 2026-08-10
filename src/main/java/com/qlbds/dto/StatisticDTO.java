package com.qlbds.dto;

import java.util.List;

public class StatisticDTO {
    private int totalAccounts;
    private int totalProperties;
    private int totalTransactions;
    private double totalRevenue;
    private List<Integer> monthlyTransactionData; // Dữ liệu biểu đồ 12 tháng
    private double filteredRevenue; // Doanh thu theo khoảng ngày lọc

    // Getters and Setters
    public int getTotalAccounts() {
        return totalAccounts;
    }

    public void setTotalAccounts(int totalAccounts) {
        this.totalAccounts = totalAccounts;
    }

    public int getTotalProperties() {
        return totalProperties;
    }

    public void setTotalProperties(int totalProperties) {
        this.totalProperties = totalProperties;
    }

    public int getTotalTransactions() {
        return totalTransactions;
    }

    public void setTotalTransactions(int totalTransactions) {
        this.totalTransactions = totalTransactions;
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public List<Integer> getMonthlyTransactionData() {
        return monthlyTransactionData;
    }

    public void setMonthlyTransactionData(List<Integer> monthlyTransactionData) {
        this.monthlyTransactionData = monthlyTransactionData;
    }

    public double getFilteredRevenue() {
        return filteredRevenue;
    }

    public void setFilteredRevenue(double filteredRevenue) {
        this.filteredRevenue = filteredRevenue;
    }
}