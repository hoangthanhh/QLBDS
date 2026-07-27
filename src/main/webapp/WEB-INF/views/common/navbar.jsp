<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<nav class="navbar navbar-expand-lg navbar-dark bg-rems-dark sticky-top shadow-sm py-3">
    <div class="container">
        <a class="navbar-brand d-flex align-items-center fw-bold fs-4" href="${pageContext.request.contextPath}/">
            <i class="fa-solid fa-building-user text-info me-2 fs-3"></i>
            <span>REMS<span class="text-info">.VN</span></span>
        </a>

        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarContent">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="navbarContent">
            <ul class="navbar-nav me-auto mb-2 mb-lg-0 ms-lg-4">
                <li class="nav-item">
                    <a class="nav-link text-white fw-medium" href="${pageContext.request.contextPath}/properties">
                        <i class="fa-solid fa-compass me-1"></i> Khám phá BĐS
                    </a>
                </li>

                <c:if test="${not empty sessionScope.currentUser}">
                    <c:if test="${sessionScope.currentUser.role == 'STAFF' || sessionScope.currentUser.role == 'ADMIN'}">
                        <li class="nav-item">
                            <a class="nav-link text-info fw-medium" href="${pageContext.request.contextPath}/staff/properties">
                                <i class="fa-solid fa-list-check me-1"></i> Quản lý BĐS
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link text-info fw-medium" href="${pageContext.request.contextPath}/staff/transactions">
                                <i class="fa-solid fa-file-invoice-dollar me-1"></i> Giao dịch
                            </a>
                        </li>
                    </c:if>

                    <c:if test="${sessionScope.currentUser.role == 'ADMIN'}">
                        <li class="nav-item">
                            <a class="nav-link text-warning fw-medium" href="${pageContext.request.contextPath}/admin/dashboard">
                                <i class="fa-solid fa-chart-pie me-1"></i> Báo cáo Admin
                            </a>
                        </li>
                    </c:if>
                </c:if>
            </ul>

            <div class="d-flex align-items-center">
                <c:choose>
                    <c:when test="${empty sessionScope.currentUser}">
                        <a href="${pageContext.request.contextPath}/login" class="btn btn-outline-light me-2 px-4 rounded-pill">
                            <i class="fa-solid fa-right-to-bracket me-1"></i> Đăng nhập
                        </a>
                        <a href="${pageContext.request.contextPath}/register" class="btn btn-info text-white px-4 rounded-pill">
                            Đăng ký
                        </a>
                    </c:when>
                    <c:otherwise>
                        <div class="dropdown">
                            <button class="btn btn-outline-info dropdown-toggle rounded-pill px-3 py-2 text-white" type="button" data-bs-toggle="dropdown">
                                <i class="fa-solid fa-circle-user fs-5 me-1 text-info align-middle"></i>
                                <span><c:out value="${sessionScope.currentUser.fullName}"/></span>
                            </button>
                            <ul class="dropdown-menu dropdown-menu-end shadow border-0 mt-2">
                                <li>
                                    <a class="dropdown-item py-2" href="${pageContext.request.contextPath}/profile">
                                        <i class="fa-solid fa-id-card me-2 text-secondary"></i> Thông tin cá nhân
                                    </a>
                                </li>
                                <li>
                                    <a class="dropdown-item py-2" href="${pageContext.request.contextPath}/customer/history">
                                        <i class="fa-solid fa-clock-rotate-left me-2 text-secondary"></i> Lịch sử xem & Giao dịch
                                    </a>
                                </li>
                                <li><hr class="dropdown-divider"></li>
                                <li>
                                    <a class="dropdown-item py-2 text-danger" href="${pageContext.request.contextPath}/logout">
                                        <i class="fa-solid fa-right-from-bracket me-2"></i> Đăng xuất
                                    </a>
                                </li>
                            </ul>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</nav>