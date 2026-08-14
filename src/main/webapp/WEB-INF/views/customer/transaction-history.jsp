<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="utf-8">
    <title>BĐS đã đặt cọc / mua - REMS</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
</head>
<body class="bg-light">
    <div class="container my-5">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h3 class="fw-bold text-primary"><i class="fa-solid fa-file-invoice-dollar me-2"></i>Tiến độ Giao dịch</h3>
            <a href="${pageContext.request.contextPath}/home" class="btn btn-outline-secondary">Quay lại trang chủ</a>
        </div>

        <div class="card shadow-sm border-0">
            <div class="card-body p-0">
                <c:choose>
                    <c:when test="${empty txList}">
                        <div class="text-center py-5">
                            <i class="fa-solid fa-box-open fa-3x text-muted mb-3"></i>
                            <h5 class="text-muted">Bạn chưa thực hiện giao dịch đặt cọc hoặc mua BĐS nào!</h5>
                            <a href="${pageContext.request.contextPath}/home" class="btn btn-primary mt-3">Tìm kiếm BĐS ngay</a>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="table-responsive">
                            <table class="table table-hover align-middle mb-0">
                                <thead class="bg-light text-secondary">
                                    <tr>
                                        <th class="ps-4">Mã GD / Thời gian</th>
                                        <th>Bất động sản</th>
                                        <th>Loại GD</th>
                                        <th>Số tiền</th>
                                        <th>Trạng thái</th>
                                        <th class="text-center pe-4">Thao tác</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="tx" items="${txList}">
                                        <tr>
                                            <td class="ps-4">
                                                <strong class="text-dark">#${tx.transactionCode}</strong><br>
                                                <small class="text-muted">${tx.formattedDate}</small>
                                            </td>
                                            <td>
                                                <div class="d-flex align-items-center">
                                                    <img src="${pageContext.request.contextPath}/${tx.thumbnail}" class="rounded me-3" style="width: 60px; height: 60px; object-fit: cover;">
                                                    <div class="text-truncate" style="max-width: 250px;" title="${tx.propertyTitle}">
                                                        <strong>${tx.propertyTitle}</strong>
                                                    </div>
                                                </div>
                                            </td>
                                            <td><span class="fw-bold text-primary">${tx.formattedType}</span></td>
                                            <td><strong class="text-success"><fmt:formatNumber value="${tx.amount}" pattern="#,###"/> VNĐ</strong></td>
                                            <td>${tx.statusBadge}</td>
                                            <td class="text-center pe-4">
                                                <a href="${pageContext.request.contextPath}/property/detail?id=${tx.propertyId}" class="btn btn-sm btn-outline-primary">Xem BĐS</a>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>

            <!-- Phân trang -->
            <c:if test="${totalPages > 1}">
                <div class="card-footer bg-white border-0 py-3">
                    <nav>
                        <ul class="pagination justify-content-center mb-0">
                            <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                                <a class="page-link" href="?page=${currentPage - 1}">Trước</a>
                            </li>
                            <c:forEach begin="1" end="${totalPages}" var="i">
                                <li class="page-item ${currentPage == i ? 'active' : ''}">
                                    <a class="page-link" href="?page=${i}">${i}</a>
                                </li>
                            </c:forEach>
                            <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                                <a class="page-link" href="?page=${currentPage + 1}">Sau</a>
                            </li>
                        </ul>
                    </nav>
                </div>
            </c:if>
        </div>
    </div>
</body>
</html>