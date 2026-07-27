<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="pageTitle" value="Đăng ký tài khoản - REMS" scope="request"/>
<jsp:include page="../common/header.jsp"/>
<jsp:include page="../common/navbar.jsp"/>

<div class="container my-5">
    <div class="row justify-content-center">
        <div class="col-lg-6 col-md-8">
            <div class="card border-0 shadow-lg rounded-4 overflow-hidden">
                <div class="card-body p-5">

                    <div class="text-center mb-4">
                        <div class="bg-info-subtle text-info d-inline-flex p-3 rounded-circle mb-3">
                            <i class="fa-solid fa-user-plus fs-2 text-rems-primary"></i>
                        </div>
                        <h3 class="fw-bold text-dark">Tạo tài khoản mới</h3>
                        <p class="text-muted">Tham gia mạng lưới giao dịch bất động sản REMS</p>
                    </div>

                    <c:if test="${not empty errorMessage}">
                        <div class="alert alert-danger alert-dismissible fade show rounded-3" role="alert">
                            <i class="fa-solid fa-circle-exclamation me-2"></i> <c:out value="${errorMessage}"/>
                            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                        </div>
                    </c:if>

                    <form action="${pageContext.request.contextPath}/register" method="post">

                        <div class="form-floating mb-3">
                            <input type="text" class="form-control rounded-3" id="fullName" name="fullName"
                                   placeholder="Nguyễn Văn A" value="${param.fullName}" required>
                            <label for="fullName"><i class="fa-solid fa-user me-2"></i>Họ và tên</label>
                        </div>

                        <div class="form-floating mb-3">
                            <input type="email" class="form-control rounded-3" id="email" name="email"
                                   placeholder="name@example.com" value="${param.email}" required>
                            <label for="email"><i class="fa-solid fa-envelope me-2"></i>Địa chỉ Email</label>
                        </div>

                        <div class="form-floating mb-3">
                            <input type="tel" class="form-control rounded-3" id="phone" name="phone"
                                   placeholder="0912345678" value="${param.phone}" required>
                            <label for="phone"><i class="fa-solid fa-phone me-2"></i>Số điện thoại</label>
                        </div>

                        <div class="form-floating mb-3">
                            <input type="password" class="form-control rounded-3" id="password" name="password"
                                   placeholder="Mật khẩu" required>
                            <label for="password"><i class="fa-solid fa-key me-2"></i>Mật khẩu</label>
                        </div>

                        <div class="form-floating mb-4">
                            <input type="password" class="form-control rounded-3" id="confirmPassword" name="confirmPassword"
                                   placeholder="Xác nhận mật khẩu" required>
                            <label for="confirmPassword"><i class="fa-solid fa-lock me-2"></i>Xác nhận mật khẩu</label>
                        </div>

                        <div class="form-check mb-4">
                            <input class="form-check-input" type="checkbox" id="terms" required>
                            <label class="form-check-label text-muted small" for="terms">
                                Tôi đồng ý với các <a href="#" class="text-rems-primary fw-medium">Điều khoản dịch vụ</a> và <a href="#" class="text-rems-primary fw-medium">Chính sách bảo mật</a> của REMS.
                            </label>
                        </div>

                        <button type="submit" class="btn btn-rems-primary w-100 py-3 rounded-3 fw-bold fs-5 shadow-sm mb-3">
                            ĐĂNG KÝ
                        </button>

                        <div class="text-center mt-4">
                            <span class="text-muted">Đã có tài khoản?</span>
                            <a href="${pageContext.request.contextPath}/login" class="text-decoration-none text-rems-primary fw-bold ms-1">
                                Đăng nhập tại đây
                            </a>
                        </div>
                    </form>

                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="../common/footer.jsp"/>