<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ include file="staffHeader.jsp" %>

<div class="container-fluid pt-4 px-2">
    <div class="d-flex align-items-center justify-content-between mb-4">
        <h4 class="mb-0 text-primary fw-bold"><i class="fa-solid fa-briefcase me-2"></i>Bàn làm việc Nhân viên</h4>
        <span class="text-muted">Xin chào, <strong>${sessionScope.currentUser.fullName}</strong>!</span>
    </div>

    <!-- 4 THẺ TỔNG QUAN -->
        <div class="row g-4 mb-4">
            <!-- BĐS Đang bán -->
            <div class="col-sm-6 col-xl-3">
                <div class="bg-light rounded d-flex align-items-center justify-content-between p-4 shadow-sm border-start border-4 border-primary">
                    <div>
                        <p class="mb-2 text-uppercase text-muted fw-bold" style="font-size: 12px;">BĐS Đang bán</p>
                        <h4 class="mb-0 fw-bold text-dark">${totalAvailable}</h4>
                    </div>
                    <!-- Sửa thành icon Tòa nhà -->
                    <i class="fa-solid fa-building fa-2x text-primary opacity-75"></i>
                </div>
            </div>

            <!-- Tiền cọc đã nhận -->
                    <div class="col-sm-6 col-xl-3">
                        <div class="bg-light rounded d-flex align-items-center justify-content-between p-4 shadow-sm border-start border-4 border-success">
                            <div>
                                <p class="mb-2 text-uppercase text-muted fw-bold" style="font-size: 12px;">Tiền cọc đã nhận</p>
                                <h4 class="mb-0 fw-bold text-dark">
                                    <fmt:formatNumber value="${totalDepositAmount}" type="currency" currencySymbol="đ" maxFractionDigits="0"/>
                                </h4>
                            </div>
                            <!-- Sửa thành icon Túi tiền -->
                            <i class="fa-solid fa-sack-dollar fa-2x text-success opacity-75"></i>
                        </div>
                    </div>

            <!-- BĐS Đã bán -->
            <div class="col-sm-6 col-xl-3">
                <div class="bg-light rounded d-flex align-items-center justify-content-between p-4 shadow-sm border-start border-4 border-info">
                    <div>
                        <p class="mb-2 text-uppercase text-muted fw-bold" style="font-size: 12px;">BĐS Đã mua</p>
                        <h4 class="mb-0 fw-bold text-dark">${totalSold}</h4>
                    </div>
                    <!-- Sửa thành icon Ngôi nhà có dấu tích -->
                    <i class="fa-solid fa-house-circle-check fa-2x text-info opacity-75"></i>
                </div>
            </div>

            <!-- Tổng doanh thu (Giữ nguyên) -->
            <div class="col-sm-6 col-xl-3">
                <div class="bg-light rounded d-flex align-items-center justify-content-between p-4 shadow-sm border-start border-4 border-warning">
                    <div>
                        <p class="mb-2 text-uppercase text-muted fw-bold" style="font-size: 12px;">Tổng doanh thu</p>
                        <h4 class="mb-0 fw-bold text-dark">
                            <fmt:formatNumber value="${totalRevenue}" type="currency" currencySymbol="đ" maxFractionDigits="0"/>
                        </h4>
                    </div>
                    <i class="fa-solid fa-money-bill-wave fa-2x text-warning opacity-75"></i>
                </div>
            </div>
        </div>

    <!-- CÁC LỐI TẮT HÀNH ĐỘNG NHANH -->
    <div class="row g-4">
        <div class="col-md-6">
            <div class="card shadow-sm border-0 p-4 rounded-4 h-100">
                <h5 class="fw-bold text-primary mb-3"><i class="fa-solid fa-building me-2"></i>Quản lý Bất Động Sản</h5>
                <p class="text-muted">Đăng tin BĐS mới lên hệ thống, cập nhật bảng giá, mô tả, thông số và quản lý bộ
                    sưu tập tối đa 5 ảnh minh họa.</p>
                <div class="mt-auto">
                    <a href="${pageContext.request.contextPath}/staff/bds"
                       class="btn btn-primary fw-bold px-4 py-2 rounded-pill">
                        <i class="fa-solid fa-arrow-right me-1"></i> Đến trang Quản lý BĐS
                    </a>
                </div>
            </div>
        </div>
        <div class="col-md-6">
            <div class="card shadow-sm border-0 p-4 rounded-4 h-100">
                <h5 class="fw-bold text-success mb-3"><i class="fa-solid fa-file-signature me-2"></i>Xử lý Giao dịch Đặt
                    cọc / Mua</h5>
                <p class="text-muted">Kiểm tra các yêu cầu đặt cọc giữ chỗ và mua BĐS của khách hàng, phê duyệt giao
                    dịch hoặc từ chối kèm lý do gửi email tự động.</p>
                <div class="mt-auto">
                    <a href="${pageContext.request.contextPath}/staff/transactions"
                       class="btn btn-success fw-bold px-4 py-2 rounded-pill">
                        <i class="fa-solid fa-arrow-right me-1"></i> Đến trang Xử lý Giao dịch
                    </a>
                </div>
            </div>
        </div>
    </div>
</div>

<%@ include file="staffFooter.jsp" %>