<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng Ký Tài Khoản - REMS Real Estate</title>
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
    <div class="container my-5">
        <div class="row justify-content-center">
            <div class="col-md-6">
                <div class="text-center mb-4">
                    <h2 class="fw-bold text-makaan"><i class="fa fa-home me-2"></i>REMS</h2>
                </div>

                <div class="card card-auth p-4">
                    <h4 class="fw-bold text-center mb-4">Đăng Ký Tài Khoản</h4>

                    <form action="${pageContext.request.contextPath}/acc/register" method="POST">
                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label class="form-label small fw-semibold">Họ và tên</label>
                                <input type="text" name="fullName" class="form-control"
                                       value="${userDto.fullName}" placeholder="Nguyễn Văn A" required>
                            </div>
                            <div class="col-md-6 mb-3">
                                <label class="form-label small fw-semibold">Số điện thoại</label>
                                <input type="tel" name="phone" class="form-control"
                                       value="${userDto.phone}" placeholder="0987654321" required>
                            </div>
                        </div>

                        <div class="mb-3">
                            <label class="form-label small fw-semibold">Địa chỉ Email</label>
                            <input type="email" name="email" class="form-control"
                                   value="${userDto.email}" placeholder="name@example.com" required>
                        </div>

                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label class="form-label small fw-semibold">Mật khẩu</label>
                                <input type="password" name="password" class="form-control"
                                       value="${userDto.password}" placeholder="Tối thiểu 6 ký tự" required>
                            </div>
                            <div class="col-md-6 mb-3">
                                <label class="form-label small fw-semibold">Xác nhận mật khẩu</label>
                                <input type="password" name="confirmPassword" class="form-control"
                                       value="${userDto.confirmPassword}" placeholder="••••••••" required>
                            </div>
                        </div>

                        <c:if test="${not empty error}">
                            <div class="text-danger small mb-3 text-center fw-bold">
                                <i class="fa fa-exclamation-circle me-1"></i> ${error}
                            </div>
                        </c:if>

                        <button type="submit" class="btn btn-makaan w-100 py-2 fw-bold mt-2">Đăng Ký</button>
                    </form>

                    <div class="text-center mt-4">
                        <p class="small text-muted mb-0">Đã có tài khoản?
                            <a href="${pageContext.request.contextPath}/acc/login" class="text-makaan fw-semibold text-decoration-none">Đăng nhập ngay</a>
                        </p>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>