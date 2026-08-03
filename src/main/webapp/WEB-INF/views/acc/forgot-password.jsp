<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quên Mật Khẩu - REMS Real Estate</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        body { background-color: #f4f7f6; font-family: 'Inter', sans-serif; }
        .card-auth { border: none; border-radius: 15px; box-shadow: 0 4px 15px rgba(0,0,0,0.05); }
        .btn-makaan { background-color: #00B98E; color: white; border: none; }
        .btn-makaan:hover { background-color: #009e79; color: white; }
        .text-makaan { color: #00B98E; }
    </style>
</head>
<body class="d-flex align-items-center min-vh-100">
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-md-5">
                <div class="text-center mb-4">
                    <h2 class="fw-bold text-makaan"><i class="fa fa-home me-2"></i>REMS</h2>
                </div>
                <div class="card card-auth p-4">
                    <h4 class="fw-bold text-center mb-2">Quên Mật Khẩu?</h4>
                    <p class="text-muted text-center small mb-4">Nhập email đã đăng ký của bạn. Hệ thống sẽ gửi một mã OTP xác thực để đặt lại mật khẩu mới.</p>

                    <c:if test="${not empty message}">
                        <div class="alert alert-success py-2 small">${message}</div>
                    </c:if>
                    <c:if test="${not empty error}">
                        <div class="alert alert-danger py-2 small">${error}</div>
                    </c:if>

                    <form action="${pageContext.request.contextPath}/acc/forgot-password" method="POST">
                        <div class="mb-4">
                            <label class="form-label small fw-semibold">Địa chỉ Email</label>
                            <div class="input-group">
                                <span class="input-group-text bg-white border-end-0 text-muted"><i class="fa fa-envelope"></i></span>
                                <input type="email" name="email" class="form-control border-start-0" placeholder="name@example.com" required>
                            </div>
                        </div>
                        <button type="submit" class="btn btn-makaan w-100 py-2 fw-bold">Gửi mã xác thực OTP</button>
                    </form>

                    <div class="text-center mt-4">
                        <p class="small text-muted mb-0">Quay lại
                            <a href="${pageContext.request.contextPath}/acc/login" class="text-makaan fw-semibold text-decoration-none">Đăng nhập</a> hoặc
                            <a href="${pageContext.request.contextPath}/acc/register" class="text-makaan fw-semibold text-decoration-none">Đăng ký</a>
                        </p>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>