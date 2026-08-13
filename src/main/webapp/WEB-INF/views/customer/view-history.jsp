<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="utf-8">
    <title>Bất động sản đã xem - REMS</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
</head>
<body class="bg-light">
    <div class="container my-5">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h3 class="fw-bold text-primary"><i class="fa fa-history me-2"></i>Bất Động Sản Đã Xem</h3>
            <a href="${pageContext.request.contextPath}/home" class="btn btn-outline-secondary">Quay lại trang chủ</a>
        </div>

        <div class="row g-4">
            <c:choose>
                <c:when test="${empty historyList}">
                    <div class="col-12 text-center py-5 bg-white rounded shadow-sm">
                        <i class="fa fa-folder-open fa-3x text-muted mb-3"></i>
                        <h5 class="text-muted">Bạn chưa xem bất động sản nào!</h5>
                    </div>
                </c:when>
                <c:otherwise>
                    <c:forEach var="item" items="${historyList}">
                        <div class="col-lg-4 col-md-6">
                            <div class="card h-100 shadow-sm border-0">
                                <img src="${pageContext.request.contextPath}/${item.thumbnail}" class="card-img-top" alt="Hình ảnh" style="height: 200px; object-fit: cover;">
                                <div class="card-body">
                                    <h5 class="card-title text-truncate" title="${item.title}"><c:out value="${item.title}"/></h5>
                                    <p class="text-muted small mb-2"><i class="fa fa-map-marker-alt text-primary me-2"></i><c:out value="${item.address}"/></p>
                                    <div class="d-flex justify-content-between text-primary fw-bold">
                                        <span><fmt:formatNumber value="${item.price}" pattern="#,###"/> VNĐ</span>
                                        <span><c:out value="${item.area}"/> m²</span>
                                    </div>
                                </div>
                                <div class="card-footer bg-white border-0 text-center pb-3">
                                    <a href="${pageContext.request.contextPath}/property/detail?id=${item.propertyId}" class="btn btn-primary w-100">Xem Lại Chi Tiết</a>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</body>
</html>