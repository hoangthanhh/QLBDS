<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="pageTitle" value="Xác thực OTP - REMS" scope="request"/>
<jsp:include page="../common/header.jsp"/>
<jsp:include page="../common/navbar.jsp"/>

<div class="container my-5">
    <div class="row justify-content-center">
        <div class="col-lg-5 col-md-7">
            <div class="card border-0 shadow-lg rounded-4 overflow-hidden">
                <div class="card-body p-5 text-center">

                    <div class="bg-warning-subtle text-warning d-inline-flex p-3 rounded-circle mb-3">
                        <i class="fa-solid fa-envelope-open-text fs-2 text-warning"></i>
                    </div>
                    <h3 class="fw-bold text-dark">Xác thực Email</h3>
                    <p class="text-muted">Chúng tôi đã gửi một mã xác thực gồm 6 chữ số đến email của bạn. Vui lòng kiểm tra và nhập vào bên dưới.</p>

                    <c:if test="${not empty errorMessage}">
                        <div class="alert alert-danger rounded-3 text-start" role="alert">
                            <i class="fa-solid fa-circle-exclamation me-2"></i> <c:out value="${errorMessage}"/>
                        </div>
                    </c:if>

                    <form action="${pageContext.request.contextPath}/verify-otp" method="post" class="mt-4">
                        <input type="hidden" name="email" value="${sessionScope.registeringEmail}"/>

                        <div class="mb-4">
                            <input type="text" class="form-control form-control-lg rounded-3 text-center fw-bold fs-3 tracking-widest letter-spacing-lg"
                                   id="otpCode" name="otpCode" placeholder="000000" maxlength="6" pattern="\d{6}" required autocomplete="off">
                            <div class="form-text text-start mt-2">Mã OTP có hiệu lực trong vòng 5 phút.</div>
                        </div>

                        <button type="submit" class="btn btn-rems-primary w-100 py-3 rounded-3 fw-bold fs-5 shadow-sm mb-3">
                            XÁC NHẬN KÍCH HOẠT
                        </button>
                    </form>

                    <div class="mt-4">
                        <span class="text-muted">Không nhận được mã?</span>
                        <a href="${pageContext.request.contextPath}/resend-otp" class="text-decoration-none text-rems-primary fw-bold ms-1">
                            Gửi lại mã
                        </a>
                    </div>

                </div>
            </div>
        </div>
    </div>
</div>

<style>
    /* CSS phụ trợ tạo khoảng cách chữ cho ô nhập OTP giống các app ngân hàng */
    #otpCode {
        letter-spacing: 0.5rem;
    }
</style>

<jsp:include page="../common/footer.jsp"/>