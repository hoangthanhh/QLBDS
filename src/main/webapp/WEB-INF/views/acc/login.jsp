<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng Nhập - REMS Real Estate</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        body {
            background-color: #f4f7f6;
            font-family: 'Inter', sans-serif;
        }

        .card-auth {
            border: none;
            border-radius: 15px;
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.05);
        }

        .btn-makaan {
            background-color: #00B98E;
            color: white;
            border: none;
        }

        .btn-makaan:hover {
            background-color: #009e79;
            color: white;
        }

        .text-makaan {
            color: #00B98E;
        }
    </style>
</head>
<body class="d-flex align-items-center min-vh-100">
<div class="container">
    <div class="row justify-content-center">
        <div class="col-md-5">
            <div class="text-center mb-4">
                <h2 class="fw-bold text-makaan"><i class="fa fa-home me-2"></i>REMS</h2>
                <p class="text-muted">Hệ thống Quản lý Bất động sản</p>
            </div>
            <div class="card card-auth p-4">
                <h4 class="fw-bold text-center mb-4">Đăng Nhập</h4>

                <!-- ĐOẠN NÀY HIỆN MÀU XANH KHI ĐĂNG KÝ THÀNH CÔNG -->
                <c:if test="${param.registerSuccess == 'true'}">
                    <div class="alert alert-success py-2 small text-center fw-semibold">
                        <i class="fa fa-check-circle me-1"></i> Đăng ký thành công! Vui lòng đăng nhập.
                    </div>
                </c:if>

                <c:if test="${not empty error}">
                    <div class="alert alert-danger py-2 small text-center fw-semibold">${error}</div>
                </c:if>

                <form action="${pageContext.request.contextPath}/acc/login" method="POST">
                    <div class="mb-3">
                        <label class="form-label small fw-semibold">Email đăng nhập</label>
                        <input type="email" name="email" class="form-control" placeholder="name@example.com" required>
                    </div>
                    <div class="mb-3">
                        <label class="form-label small fw-semibold">Mật khẩu</label>
                        <input type="password" name="password" class="form-control" placeholder="••••••••" required>
                    </div>
                    <div class="d-flex justify-content-between align-items-center mb-4">
                        <div class="form-check">
                            <input type="checkbox" class="form-check-input" id="rememberMe">
                            <label class="form-check-label small text-muted" for="rememberMe">Ghi nhớ đăng nhập</label>
                        </div>
                        <a href="${pageContext.request.contextPath}/acc/forgot-password"
                           class="small text-makaan text-decoration-none">Quên mật khẩu?</a>
                    </div>
                    <button type="submit" class="btn btn-makaan w-100 py-2 fw-bold">Đăng Nhập</button>
                </form>

                <div class="text-center mt-4">
                    <p class="small text-muted mb-0">Chưa có tài khoản?
                        <a href="${pageContext.request.contextPath}/acc/register"
                           class="text-makaan fw-semibold text-decoration-none">Đăng ký ngay</a>
                    </p>
                    <hr class="text-muted">
                    <a href="${pageContext.request.contextPath}/home" class="small text-secondary text-decoration-none"><i
                            class="fa fa-arrow-left me-1"></i> Quay lại trang chủ</a>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>