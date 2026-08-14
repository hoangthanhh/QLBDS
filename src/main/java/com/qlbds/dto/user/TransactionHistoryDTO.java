package com.qlbds.dto.user;

import com.qlbds.constant.TransactionStatusEnum;
import com.qlbds.constant.TransactionTypeEnum;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TransactionHistoryDTO {
    private String transactionCode;
    private Integer propertyId;
    private String propertyTitle;
    private String thumbnail;
    private Long amount;
    private TransactionTypeEnum type;
    private TransactionStatusEnum status;
    private LocalDateTime createdAt;

    // GETTERS & SETTERS (Bạn tự generate nhé)
    public String getTransactionCode() { return transactionCode; }
    public void setTransactionCode(String transactionCode) { this.transactionCode = transactionCode; }
    public Integer getPropertyId() { return propertyId; }
    public void setPropertyId(Integer propertyId) { this.propertyId = propertyId; }
    public String getPropertyTitle() { return propertyTitle; }
    public void setPropertyTitle(String propertyTitle) { this.propertyTitle = propertyTitle; }
    public String getThumbnail() { return thumbnail; }
    public void setThumbnail(String thumbnail) { this.thumbnail = thumbnail; }
    public Long getAmount() { return amount; }
    public void setAmount(Long amount) { this.amount = amount; }
    public TransactionTypeEnum getType() { return type; }
    public void setType(TransactionTypeEnum type) { this.type = type; }
    public TransactionStatusEnum getStatus() { return status; }
    public void setStatus(TransactionStatusEnum status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    // HÀM FORMAT HỖ TRỢ TRỰC TIẾP CHO GIAO DIỆN JSP
    public String getFormattedType() {
        return type == TransactionTypeEnum.DEPOSIT ? "Đặt cọc" : "Mua đứt";
    }

    public String getFormattedDate() {
        if (createdAt == null) return "";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return createdAt.format(formatter);
    }

    public String getStatusBadge() {
        if (status == null) return "";
        switch (status) {
            case PENDING: return "<span class='badge bg-warning text-dark'>Chờ xác nhận</span>";
            case COMPLETED: return "<span class='badge bg-success'>Thành công</span>";
            case CANCELLED: return "<span class='badge bg-secondary'>Đã hủy</span>";
            case REJECTED: return "<span class='badge bg-danger'>Bị từ chối</span>";
            default: return "";
        }
    }
}