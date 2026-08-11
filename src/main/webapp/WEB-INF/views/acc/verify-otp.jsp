<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Xác Thực Tài Khoản - REMS</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        body { background-color: #f4f7f6; font-family: 'Inter', sans-serif; }
        .card-auth { border: none; border-radius: 15px; box-shadow: 0 4px 15px rgba(0,0,0,0.05); }
        .btn-makaan { background-color: #00B98E; color: white; border: none; }
        .btn-makaan:hover { background-color: #009e79; color: white; }
        .text-makaan { color: #00B98E; }
        .btn-send-otp { background-color: #e8f5e9; color: #00B98E; border: 1px dashed #00B98E; font-weight: 600; }
        .btn-send-otp:hover { background-color: #c8e6c9; color: #00796b; }
    </style>
</head>
<body>
    <div class="container vh-100 d-flex align-items-center justify-content-center">
        <div class="row w-100 justify-content-center">
            <div class="col-md-5">
                <div class="text-center mb-4">
                    <h2 class="fw-bold text-makaan"><i class="fa fa-home me-2"></i>REMS</h2>
                </div>
                <div class="card card-auth p-4 p-sm-5 bg-white">
                    <h3 class="fw-bold text-dark text-center mb-3">Xác Thực OTP</h3>
                    <p class="text-muted text-center small mb-4">
                        Vui lòng bấm <strong>"Lấy mã OTP"</strong> để nhận mã kích hoạt qua email đăng ký của bạn.
                    </p>

                    <c:if test="${not empty error}">
                        <div class="alert alert-danger text-center small py-2 mb-3">${error}</div>
                    </c:if>
                    <c:if test="${not empty message}">
                        <div class="alert alert-success text-center small py-2 mb-3">${message}</div>
                    </c:if>

                    <div class="text-center mb-4">
                        <form action="${pageContext.request.contextPath}/acc/resend-otp" method="POST">
                            <button type="submit" class="btn btn-send-otp w-100 py-2.5">
                                <i class="fas fa-paper-plane me-2"></i> Bấm vào đây để lấy mã OTP
                            </button>
                        </form>
                    </div>

                    <hr class="text-muted my-4">

                    <form action="${pageContext.request.contextPath}/acc/verify-otp" method="POST">
                        <div class="mb-4">
                            <label class="form-label small fw-semibold text-secondary">Nhập Mã OTP Nhận Được</label>
                            <input type="text" name="otpCode" class="form-control form-control-lg text-center fw-bold"
                                   placeholder="••••••" maxlength="10" required autocomplete="off">
                        </div>
                        <button type="submit" class="btn btn-makaan w-100 py-2.5 fw-bold">Xác Nhận Kích Hoạt</button>
                    </form>

                    <div class="text-center mt-4">
                        <a href="${pageContext.request.contextPath}/home" class="text-secondary small text-decoration-none">
                            <i class="fas fa-arrow-left me-1"></i> Quay lại trang chủ
                        </a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>