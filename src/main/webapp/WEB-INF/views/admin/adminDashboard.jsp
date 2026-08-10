<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ include file="adminHeader.jsp" %>

<div class="container-fluid pt-4 px-4">
    <div class="d-flex align-items-center justify-content-between mb-4">
        <h4 class="mb-0 text-primary fw-bold"><i class="fa-solid fa-chart-line me-2"></i>Báo cáo & Thống kê</h4>
    </div>

    <!-- 4 THẺ TỔNG QUAN -->
    <div class="row g-4 mb-4">
        <div class="col-sm-6 col-xl-3">
            <div class="bg-light rounded d-flex align-items-center justify-content-between p-4 shadow-sm border-start border-4 border-primary">
                <div>
                    <p class="mb-2 text-uppercase text-muted fw-bold" style="font-size: 12px;">Tổng tài khoản</p>
                    <h4 class="mb-0 fw-bold text-dark">${totalAccounts}</h4>
                </div>
                <i class="fa-solid fa-users fa-2x text-primary"></i>
            </div>
        </div>
        <div class="col-sm-6 col-xl-3">
            <div class="bg-light rounded d-flex align-items-center justify-content-between p-4 shadow-sm border-start border-4 border-success">
                <div>
                    <p class="mb-2 text-uppercase text-muted fw-bold" style="font-size: 12px;">Bất động sản (Đang bán)</p>
                    <h4 class="mb-0 fw-bold text-dark">${totalBDS}</h4>
                </div>
                <i class="fa-solid fa-building fa-2x text-success"></i>
            </div>
        </div>
        <div class="col-sm-6 col-xl-3">
            <div class="bg-light rounded d-flex align-items-center justify-content-between p-4 shadow-sm border-start border-4 border-info">
                <div>
                    <p class="mb-2 text-uppercase text-muted fw-bold" style="font-size: 12px;">Giao dịch thành công</p>
                    <h4 class="mb-0 fw-bold text-dark">${totalTransactions}</h4>
                </div>
                <i class="fa-solid fa-handshake fa-2x text-info"></i>
            </div>
        </div>
        <div class="col-sm-6 col-xl-3">
            <div class="bg-light rounded d-flex align-items-center justify-content-between p-4 shadow-sm border-start border-4 border-warning">
                <div>
                    <p class="mb-2 text-uppercase text-muted fw-bold" style="font-size: 12px;">Tổng doanh thu</p>
                    <h4 class="mb-0 fw-bold text-dark">
                        <fmt:formatNumber value="${totalRevenue}" type="currency" currencySymbol="đ" maxFractionDigits="0"/>
                    </h4>
                </div>
                <i class="fa-solid fa-money-bill-wave fa-2x text-warning"></i>
            </div>
        </div>
    </div>

    <!-- KHU VỰC BIỂU ĐỒ VÀ BÁO CÁO DOANH THU -->
    <div class="row g-4">
        <!-- Biểu đồ -->
        <div class="col-sm-12 col-lg-7">
            <div class="bg-light rounded p-4 shadow-sm h-100">
                <h6 class="mb-4 text-primary fw-bold"><i class="fa-solid fa-chart-bar me-2"></i>Thống kê số lượng giao dịch theo tháng</h6>
                <div style="position: relative; height: 320px;">
                    <canvas id="monthlyChart"></canvas>
                </div>
            </div>
        </div>

        <!-- Khung báo cáo doanh thu -->
        <div class="col-sm-12 col-lg-5">
            <div class="bg-light rounded p-4 shadow-sm h-100 d-flex flex-column justify-content-between">
                <div>
                    <h6 class="mb-4 text-success fw-bold"><i class="fa-solid fa-filter me-2"></i>Báo cáo doanh thu theo kỳ</h6>
                    <form action="${pageContext.request.contextPath}/admin/dashboard" method="get">
                        <div class="mb-3">
                            <label class="form-label fw-bold text-secondary">Từ ngày:</label>
                            <input type="date" name="startDate" class="form-control shadow-none" value="${param.startDate}">
                        </div>
                        <div class="mb-3">
                            <label class="form-label fw-bold text-secondary">Đến ngày:</label>
                            <input type="date" name="endDate" class="form-control shadow-none" value="${param.endDate}">
                        </div>
                        <button type="submit" class="btn btn-success w-100 fw-bold py-2 mt-2 shadow-sm">
                            <i class="fa-solid fa-magnifying-glass me-1"></i> Lọc dữ liệu
                        </button>
                    </form>
                </div>

                <div class="mt-4 p-3 rounded bg-white border border-success text-center">
                    <span class="text-muted fw-bold text-uppercase d-block mb-1" style="font-size: 12px;">Doanh thu trong kỳ lọc:</span>
                    <h3 class="text-success fw-bold mb-0">
                        <fmt:formatNumber value="${filteredRevenue}" type="currency" currencySymbol="đ" maxFractionDigits="0"/>
                    </h3>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<script>
    document.addEventListener("DOMContentLoaded", function () {
        const ctx = document.getElementById('monthlyChart').getContext('2d');
        const rawData = ${monthlyData != null ? monthlyData : '[0,0,0,0,0,0,0,0,0,0,0,0]'};

        new Chart(ctx, {
            type: 'bar',
            data: {
                labels: ['Tháng 1', 'Tháng 2', 'Tháng 3', 'Tháng 4', 'Tháng 5', 'Tháng 6', 'Tháng 7', 'Tháng 8', 'Tháng 9', 'Tháng 10', 'Tháng 11', 'Tháng 12'],
                datasets: [{
                    label: 'Số giao dịch',
                    data: rawData,
                    backgroundColor: '#4e73df',
                    borderRadius: 4
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: { display: false }
                },
                scales: {
                    x: {
                        ticks: {
                            // Ép chữ Tháng 1, 2... NẰM NGANG HOÀN TOÀN
                            maxRotation: 0,
                            minRotation: 0,
                            font: { size: 11, weight: 'bold' }
                        },
                        grid: { display: false }
                    },
                    y: {
                        beginAtZero: true,
                        ticks: { precision: 0 }
                    }
                }
            }
        });
    });
</script>

<%@ include file="adminFooter.jsp" %>