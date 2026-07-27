<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="pageTitle" value="Đăng nhập - Hệ thống REMS" scope="request"/>
<jsp:include page="../common/header.jsp"/>
<jsp:include page="../common/navbar.jsp"/>

<div class="container my-5">
    <div class="row justify-content-center align-items-center">
        <div class="col-lg-5 col-md-7">
            <div class="card border-0 shadow-lg rounded-4 overflow-hidden">
                <div class="card-body p-5">

                    <div class="text-center mb-4">
                        <div class="bg-primary-subtle text-primary d-inline-flex p-3 rounded-circle mb-3">
                            <i class="fa-solid fa-lock fs-2 text-rems-primary"></i>
                        </div>
                        <h3 class="fw-bold text-dark">Chào mừng trở lại!</h3>
                        <p class="text-muted">Đăng nhập để tiếp tục quản lý và giao dịch BĐS</p>
                    </div>

                    <c:if test="${not empty errorMessage}">
                        <div class="alert alert-danger alert-dismissible fade show rounded-3" role="alert">
                            <i class="fa-solid fa-circle-exclamation me-2"></i> <c:out value="${errorMessage}"/>
                            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                        </div>
                    </c:if>

                    <c:if test="${not empty successMessage}">
                        <div class="alert alert-success alert-dismissible fade show rounded-3" role="alert">
                            <i class="fa-solid fa-circle-check me-2"></i> <c:out value="${successMessage}"/>
                            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                        </div>
                    </c:if>

                    <form action="${pageContext.request.contextPath}/login" method="post">
                        <div class="form-floating mb-3">
                            <input type="email" class="form-control rounded-3" id="email" name="email"
                                   placeholder="name@example.com" value="${param.email}" required>
                            <label for="email"><i class="fa-solid fa-envelope me-2"></i>Địa chỉ Email</label>
                        </div>

                        <div class="form-floating mb-3">
                            <input type="password" class="form-control rounded-3" id="password" name="password"
                                   placeholder="Mật khẩu" required>
                            <label for="password"><i class="fa-solid fa-key me-2"></i>Mật khẩu</label>
                        </div>

                        <div class="d-flex justify-content-between align-items-center mb-4">
                            <div class="form-check">
                                <input class="form-check-input" type="checkbox" id="rememberMe" name="rememberMe">
                                <label class="form-check-label text-muted" for="rememberMe">Ghi nhớ đăng nhập</label>
                            </div>
                            <a href="${pageContext.request.contextPath}/forgot-password" class="text-decoration-none text-rems-primary fw-medium small">
                                Quên mật khẩu?
                            </a>
                        </div>

                        <button type="submit" class="btn btn-rems-primary w-100 py-3 rounded-3 fw-bold fs-5 shadow-sm mb-3">
                            ĐĂNG NHẬP
                        </button>

                        <div class="text-center mt-4">
                            <span class="text-muted">Chưa có tài khoản?</span>
                            <a href="${pageContext.request.contextPath}/register" class="text-decoration-none text-rems-primary fw-bold ms-1">
                                Đăng ký ngay
                            </a>
                        </div>
                    </form>

                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="../common/footer.jsp"/>