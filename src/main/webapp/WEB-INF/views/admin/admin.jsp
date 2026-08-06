<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ include file="adminHeader.jsp" %>

<h1 class="h3">T&#7893;ng quan</h1>
<div class="row mb-4">
    <div class="col-xl-3 col-md-6 mb-4"><div class="card border-left-primary h-100"><div class="card-body"><div class="row align-items-center"><div class="col"><div class="text-xs font-weight-bold text-primary text-uppercase mb-1">T&#192;I KHO&#7842;N</div><div class="h5 mb-0 font-weight-bold">${totalUsers != null ? totalUsers : 0}</div></div><div class="col-auto"><i class="fas fa-users fa-2x text-gray-300"></i></div></div></div></div></div>
    <div class="col-xl-3 col-md-6 mb-4"><div class="card border-left-success h-100"><div class="card-body"><div class="row align-items-center"><div class="col"><div class="text-xs font-weight-bold text-success text-uppercase mb-1">S&#7842;N PH&#7848;M</div><div class="h5 mb-0 font-weight-bold">${totalProperties != null ? totalProperties : 0}</div></div><div class="col-auto"><i class="fas fa-boxes fa-2x text-gray-300"></i></div></div></div></div></div>
    <div class="col-xl-3 col-md-6 mb-4"><div class="card border-left-info h-100"><div class="card-body"><div class="row align-items-center"><div class="col"><div class="text-xs font-weight-bold text-info text-uppercase mb-1">&#272;&#416;N H&#192;NG</div><div class="h5 mb-0 font-weight-bold">${totalTransactions != null ? totalTransactions : 0}</div></div><div class="col-auto"><i class="fas fa-file-alt fa-2x text-gray-300"></i></div></div></div></div></div>
    <div class="col-xl-3 col-md-6 mb-4"><div class="card border-left-warning h-100"><div class="card-body"><div class="row align-items-center"><div class="col"><div class="text-xs font-weight-bold text-warning text-uppercase mb-1">DOANH THU</div><div class="h5 mb-0 font-weight-bold"><fmt:formatNumber value="${totalAllRevenue != null ? totalAllRevenue : 0}" type="number" maxFractionDigits="0"/> &#273;</div></div><div class="col-auto"><i class="fas fa-bell fa-2x text-gray-300"></i></div></div></div></div></div>
</div>

<div class="card mb-4">
    <div class="card-header text-primary"><i class="fas fa-chart-bar mr-2"></i>Th&#7889;ng k&#234; &#273;&#417;n h&#224;ng theo th&#225;ng</div>
    <div class="card-body"><div class="chart-bar"><canvas id="monthlyTransactionChart"></canvas></div></div>
</div>

<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<script>
document.addEventListener('DOMContentLoaded', function () {
    var chart = document.getElementById('monthlyTransactionChart');
    if (!chart || typeof Chart === 'undefined') return;
    new Chart(chart, {type: 'bar', data: {labels: ['T1','T2','T3','T4','T5','T6','T7','T8','T9','T10','T11','T12'], datasets: [{label: 'Đơn hàng', data: [${monthlyStats != null ? monthlyStats[1] : 0},${monthlyStats != null ? monthlyStats[2] : 0},${monthlyStats != null ? monthlyStats[3] : 0},${monthlyStats != null ? monthlyStats[4] : 0},${monthlyStats != null ? monthlyStats[5] : 0},${monthlyStats != null ? monthlyStats[6] : 0},${monthlyStats != null ? monthlyStats[7] : 0},${monthlyStats != null ? monthlyStats[8] : 0},${monthlyStats != null ? monthlyStats[9] : 0},${monthlyStats != null ? monthlyStats[10] : 0},${monthlyStats != null ? monthlyStats[11] : 0},${monthlyStats != null ? monthlyStats[12] : 0}], backgroundColor: '#5879e3', borderRadius: 2} ]}, options: {maintainAspectRatio:false, plugins:{legend:{display:false}}, scales:{y:{beginAtZero:true, grid:{color:'#eef1f7'}},x:{grid:{display:false}}}}});
});
</script>
<%@ include file="adminFooter.jsp" %>
