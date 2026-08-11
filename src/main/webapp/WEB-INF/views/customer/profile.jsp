<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">

<head>
    <meta charset="utf-8">
    <title>Thông Tin Cá Nhân - REMS</title>
    <meta content="width=device-width, initial-scale=1.0" name="viewport">

    <link href="${pageContext.request.contextPath}/assets/customer/img/favicon.ico" rel="icon">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Heebo:wght@400;500;600&family=Inter:wght@700;800&display=swap" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.10.0/css/all.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.4.1/font/bootstrap-icons.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/assets/customer/css/bootstrap.min.css" rel="stylesheet">

    <style>
        body {
            background-color: #f4f6f9;
            font-family: 'Inter', 'Heebo', sans-serif;
        }
        .profile-container {
            max-width: 1140px;
            margin: 50px auto;
            padding: 0 20px;
        }
        .profile-card {
            border: none;
            border-radius: 20px;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.04);
            background: #ffffff;
            padding: 35px;
        }
        .user-icon-wrapper {
            width: 80px;
            height: 80px;
            background-color: #e6f7f3;
            color: #00B98E;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 2.5rem;
            margin: 0 auto 20px auto;
        }
        .info-list-item {
            display: flex;
            align-items: center;
            padding: 14px 0;
            border-bottom: 1px dashed #e9ecef;
            font-size: 0.95rem;
        }
        .info-list-item:last-child {
            border-bottom: none;
        }
        .info-list-item i {
            color: #6c757d;
            width: 25px;
            font-size: 1.1rem;
        }
        .badge-verified {
            background-color: #d1e7dd;
            color: #0f5132;
            border-radius: 30px;
            padding: 6px 14px;
            font-size: 0.85rem;
            font-weight: 600;
            display: inline-flex;
            align-items: center;
        }
        .badge-unverified {
            background-color: #fff3cd;
            color: #664d03;
            border-radius: 12px;
            padding: 15px;
            font-size: 0.85rem;
        }
        .form-label {
            font-weight: 600;
            color: #343a40;
            margin-bottom: 10px;
            font-size: 0.9rem;
            display: flex;
            align-items: center;
        }
        .form-label i {
            color: #00B98E;
            margin-right: 8px;
        }
        .form-control {
            border-radius: 12px;
            padding: 14px 18px;
            border: 1px solid #ced4da;
            font-size: 0.95rem;
            transition: all 0.25s ease;
        }
        .form-control:focus {
            border-color: #00B98E;
            box-shadow: 0 0 0 4px rgba(0, 185, 142, 0.12);
        }
        .form-control[readonly] {
            background-color: #f8f9fa;
            color: #6c757d;
            border-color: #e9ecef;
        }
        .btn-custom-primary {
            background-color: #00B98E;
            color: white;
            border: none;
            border-radius: 12px;
            padding: 14px 30px;
            font-weight: 600;
            box-shadow: 0 4px 12px rgba(0, 185, 142, 0.2);
            transition: all 0.2s ease;
        }
        .btn-custom-primary:hover {
            background-color: #009673;
            color: white;
            transform: translateY(-1px);
        }
        .btn-custom-outline {
            border: 2px solid #e0e0e0;
            background-color: transparent;
            color: #495057;
            border-radius: 12px;
            padding: 12px 28px;
            font-weight: 600;
            transition: all 0.2s ease;
        }
        .btn-custom-outline:hover {
            background-color: #f8f9fa;
            border-color: #ced4da;
            color: #212529;
        }
        .btn-back-home {
            background-color: #ffffff;
            color: #2b303a;
            border: 1px solid #e0e0e0;
            border-radius: 30px;
            padding: 8px 22px;
            font-weight: 600;
            text-decoration: none;
            box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
            transition: all 0.2s ease;
        }
        .btn-back-home:hover {
            background-color: #00B98E;
            color: #ffffff;
            border-color: #00B98E;
        }
        .section-title {
            font-weight: 800;
            color: #2b303a;
            margin-bottom: 4px;
        }
        .custom-modal .modal-content {
            border: none;
            border-radius: 20px;
            padding: 15px;
        }
    </style>
</head>

<body>

    <div class="profile-container">

        <div class="d-flex justify-content-between align-items-center mb-4 pb-2">
            <a href="${pageContext.request.contextPath}/home" class="btn-back-home">
                <i class="bi bi-arrow-left me-2"></i> Quay lại trang chủ
            </a>
            <span class="text-muted fw-bold" style="letter-spacing: 0.5px;"><i class="bi bi-shield-check me-1 text-primary"></i> REMS PORTAL</span>
        </div>

        <div id="globalAlert" class="alert d-none alert-dismissible fade show border-0 shadow-sm mb-4 p-3 rounded-3" role="alert">
            <span id="globalAlertMessage"></span>
            <button type="button" class="btn-close" onclick="$('#globalAlert').addClass('d-none');"></button>
        </div>

        <c:if test="${not empty sessionScope.updateSuccess}">
            <div class="alert alert-success alert-dismissible fade show border-0 shadow-sm mb-4 p-3 rounded-3" role="alert">
                <i class="bi bi-check-circle-fill me-2 fs-5"></i> ${sessionScope.updateSuccess}
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
            <% session.removeAttribute("updateSuccess"); %>
        </c:if>
        <c:if test="${not empty sessionScope.updateError}">
            <div class="alert alert-danger alert-dismissible fade show border-0 shadow-sm mb-4 p-3 rounded-3" role="alert">
                <i class="bi bi-exclamation-octagon-fill me-2 fs-5"></i> ${sessionScope.updateError}
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
            <% session.removeAttribute("updateError"); %>
        </c:if>

        <div class="row g-4">
            <div class="col-lg-4 col-md-5">
                <div class="card profile-card text-center">
                    <div class="user-icon-wrapper shadow-sm">
                        <i class="bi bi-person"></i>
                    </div>
                    <h4 class="mb-1 text-dark fw-bold"><c:out value="${sessionScope.currentUser.fullName}"/></h4>
                    <p class="text-muted small mb-4">
                        <span class="badge bg-light text-secondary border px-2.5 py-1.5 rounded-pill">
                            <i class="bi bi-person-badge me-1 text-primary"></i> Khách hàng
                        </span>
                    </p>

                    <div class="text-start mt-2 mb-4">

                        <div class="info-list-item">
                            <i class="bi bi-calendar-check text-muted"></i>
                            <div class="ms-2">
                                <small class="text-muted d-block" style="font-size: 0.75rem;">Thành viên từ</small>
                                <strong class="text-dark">
                                    <fmt:parseDate value="${sessionScope.currentUser.createdAt}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDate" type="both" />
                                    <fmt:formatDate pattern="dd' Thg 'MM', 'yyyy" value="${parsedDate}" />
                                </strong>
                            </div>
                        </div>
                    </div>

                    <div class="mt-2">
                        <c:choose>
                            <c:when test="${sessionScope.currentUser.isVerified}">
                                <div class="badge-verified shadow-sm w-100 justify-content-center py-2.5">
                                    <i class="bi bi-patch-check-fill fs-5 me-2"></i> Tài khoản đã xác thực
                                </div>
                            </c:when>
                            <c:otherwise>
                                <div class="badge-unverified text-start border border-warning-subtle shadow-sm">
                                    <div class="d-flex align-items-center mb-1.5 fw-bold text-dark">
                                        <i class="bi bi-exclamation-triangle-fill fs-5 me-2 text-warning"></i> Chưa xác thực
                                    </div>
                                    <p class="small text-muted mb-3" style="line-height: 1.4;">Xác thực ngay để kích hoạt đầy đủ các quyền lợi và giao dịch an toàn.</p>
                                    <a href="${pageContext.request.contextPath}/acc/verify-otp" class="btn btn-custom-primary btn-sm w-100 py-2">
                                        <i class="bi bi-shield-check me-1"></i> Xác thực ngay
                                    </a>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>

            <div class="col-lg-8 col-md-7">
                <div class="card profile-card">
                    <h3 class="section-title">Quản lý thông tin tài khoản</h3>
                    <p class="text-muted small mb-4 pb-2">Cập nhật thông tin cá nhân của bạn để đảm bảo tính xác thực và nhận thông báo mới nhất.</p>

                    <form action="${pageContext.request.contextPath}/customer/profile/update" method="POST">
                        <div class="row g-4">
                            <div class="col-md-6">
                                <label class="form-label"><i class="bi bi-person-fill"></i> Họ và Tên</label>
                                <input type="text" name="fullName" class="form-control"
                                       value="${sessionScope.currentUser.fullName}" required>
                            </div>
                            <div class="col-md-6">
                                <label class="form-label"><i class="bi bi-telephone-fill"></i> Số Điện Thoại</label>
                                <input type="tel" name="phone" class="form-control"
                                       value="${sessionScope.currentUser.phone}" required>
                            </div>
                            <div class="col-md-12">
                                <label class="form-label"><i class="bi bi-envelope-open-fill"></i> Địa Chỉ Email (Tên đăng nhập)</label>
                                <input type="email" class="form-control"
                                       value="${sessionScope.currentUser.email}" readonly>
                                <div class="form-text text-muted mt-2" style="font-size: 0.8rem;">
                                    <i class="bi bi-info-circle me-1 text-primary"></i>Email đăng nhập cố định và không thể chỉnh sửa.
                                </div>
                            </div>

                            <div class="col-12 border-top pt-4 mt-4 d-flex justify-content-between align-items-center">
                                <button type="submit" class="btn btn-custom-primary">
                                    <i class="bi bi-check2-circle me-2"></i> Lưu thay đổi
                                </button>
                                <button type="button" class="btn btn-custom-outline" data-bs-toggle="modal" data-bs-target="#changePasswordModal">
                                    <i class="bi bi-key me-1"></i> Đổi mật khẩu
                                </button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <div class="modal fade custom-modal" id="changePasswordModal" tabindex="-1" aria-labelledby="changePasswordModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content shadow-lg border-0">
                <div class="modal-header border-0 pb-0">
                    <h4 class="modal-title fw-bold text-dark" id="changePasswordModalLabel">
                        <i class="bi bi-shield-lock text-primary me-2"></i> Thay đổi mật khẩu
                    </h4>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>

                <form id="changePasswordForm">
                    <div class="modal-body py-4">
                        <div id="modalAlert" class="alert d-none py-2.5 px-3 mb-3 rounded-3 small fw-bold" role="alert"></div>

                        <div class="mb-3">
                            <label class="form-label"><i class="bi bi-lock"></i> Mật khẩu hiện tại</label>
                            <input type="password" id="oldPassword" class="form-control" placeholder="Nhập mật khẩu cũ" required>
                        </div>
                        <div class="mb-3">
                            <label class="form-label"><i class="bi bi-lock-fill"></i> Mật khẩu mới</label>
                            <input type="password" id="newPassword" class="form-control" placeholder="Nhập mật khẩu mới" required>
                        </div>
                        <div class="mb-0">
                            <label class="form-label"><i class="bi bi-shield-lock-fill"></i> Xác nhận mật khẩu mới</label>
                            <input type="password" id="confirmPassword" class="form-control" placeholder="Nhập lại mật khẩu mới" required>
                        </div>
                    </div>
                    <div class="modal-footer border-0 pt-0 d-flex justify-content-between">
                        <button type="button" class="btn btn-custom-outline py-2.5" data-bs-dismiss="modal">Hủy bỏ</button>
                        <button type="submit" id="btnSubmitChangePassword" class="btn btn-custom-primary py-2.5">
                            <span id="btnText">Xác nhận đổi</span>
                            <span id="btnSpinner" class="spinner-border spinner-border-sm ms-2 d-none" role="status"></span>
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <script src="https://code.jquery.com/jquery-3.4.1.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0/dist/js/bootstrap.bundle.min.js"></script>

    <script>
    $(document).ready(function() {
        // Tự động xóa sạch dữ liệu form và ẩn thông báo mỗi khi người dùng đóng Modal
        $('#changePasswordModal').on('hidden.bs.modal', function () {
            $('#changePasswordForm')[0].reset();
            $('#modalAlert').addClass('d-none').removeClass('alert-success alert-danger').html('');
        });

        // Xử lý gửi dữ liệu Đổi mật khẩu qua AJAX
        $('#changePasswordForm').on('submit', function(e) {
            e.preventDefault(); // Ngăn reload trang

            var oldPassword = $('#oldPassword').val();
            var newPassword = $('#newPassword').val();
            var confirmPassword = $('#confirmPassword').val();

            // Bật hiệu ứng chờ
            $('#btnSubmitChangePassword').prop('disabled', true);
            $('#btnSpinner').removeClass('d-none');

            $.ajax({
                url: '${pageContext.request.contextPath}/customer/change-password',
                type: 'POST',
                data: {
                    oldPassword: oldPassword,
                    newPassword: newPassword,
                    confirmPassword: confirmPassword
                },
                dataType: 'json',
                success: function(res) {
                    // Tắt hiệu ứng chờ
                    $('#btnSubmitChangePassword').prop('disabled', false);
                    $('#btnSpinner').addClass('d-none');

                    if (res.status === 'SUCCESS') {
                        // 1. Hiện thông báo THÀNH CÔNG (màu xanh) trong Modal
                        $('#modalAlert')
                            .removeClass('d-none alert-danger')
                            .addClass('alert-success')
                            .html('<i class="bi bi-check-circle-fill me-2"></i>' + res.message);

                        // 2. Xóa trắng các ô input để người dùng thấy đã xử lý xong
                        $('#changePasswordForm')[0].reset();

                    } else {
                        // Hiện thông báo LỖI (màu đỏ) trong Modal
                        $('#modalAlert')
                            .removeClass('d-none alert-success')
                            .addClass('alert-danger')
                            .html('<i class="bi bi-exclamation-triangle-fill me-2"></i>' + res.message);
                    }
                },
                error: function(xhr) {
                    $('#btnSubmitChangePassword').prop('disabled', false);
                    $('#btnSpinner').addClass('d-none');

                    $('#modalAlert')
                        .removeClass('d-none alert-success')
                        .addClass('alert-danger')
                        .html('<i class="bi bi-exclamation-octagon-fill me-2"></i> Lỗi hệ thống! Vui lòng thử lại sau.');
                }
            });
        });
    });
    </script>
</body>

</html>