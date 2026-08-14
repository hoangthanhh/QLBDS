<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Lịch sử xem BĐS - REMS</title>
    <!-- FontAwesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">

    <!-- Bootstrap 5 CSS -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css">

    <!-- SB Admin 2 CSS & Custom Styles -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/admin/css/sb-admin-2.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/admin/css/style.css?v=20260806-green-sidebar">

    <style>
        /* CSS Khóa gạch chân & Khóa click tuyệt đối cho Logo REMS */
        .sidebar-brand-static,
        .sidebar-brand-static *,
        .sidebar-brand-text,
        .sidebar-brand-text * {
            text-decoration: none !important;
            border-bottom: none !important;
            box-shadow: none !important;
            cursor: default !important;
            user-select: none !important;
            pointer-events: none !important;
        }

        /* CSS Custom cho bảng giống ảnh của bạn */
        .table-custom th {
            color: #5a5c69;
            font-weight: bold;
            background-color: #f8f9fc;
            border-bottom: 2px solid #e3e6f0;
        }
        .table-custom td {
            vertical-align: middle;
            color: #3a3b45;
        }
        .pagination .page-link {
            color: #4e73df;
        }
        .pagination .page-item.active .page-link {
            background-color: #0d6efd;
            border-color: #0d6efd;
            color: white;
        }
    </style>
</head>
<body id="page-top">
<div id="wrapper">
    <!-- Sidebar Navbar -->
    <ul class="navbar-nav sidebar sidebar-dark accordion" id="accordionSidebar">

        <!-- LOGO REMS KÈM ICON NGÔI NHÀ CỐ ĐỊNH -->
        <div class="sidebar-brand d-flex align-items-center justify-content-center sidebar-brand-static py-3">
            <i class="fa-solid fa-house-chimney fa-2x me-2 text-white"></i>
            <div class="sidebar-brand-text text-white text-start">
                <span class="fw-bold" style="font-size: 20px; line-height: 1.1; display: block;">REMS</span>
                <small class="d-block fw-normal text-white-50" style="font-size: 11px;">Quản trị hệ thống</small>
            </div>
        </div>
        <hr class="sidebar-divider my-0">

        <!-- Link Báo cáo thống kê -->
        <li class="nav-item">
            <a class="nav-link" href="${pageContext.request.contextPath}/admin/dashboard">
                <span class="menu-icon">&#128202;</span>
                <span>Báo cáo & thống kê</span>
            </a>
        </li>

        <div class="sidebar-heading mt-3">Quản lý hệ thống</div>

        <!-- Link Quản lý tài khoản -->
        <li class="nav-item">
            <a class="nav-link" href="${pageContext.request.contextPath}/admin/user">
                <span class="menu-icon">&#128101;</span>
                <span>Quản lý tài khoản</span>
            </a>
        </li>

        <!-- Link Quản lý BDS -->
        <li class="nav-item">
            <a class="nav-link" href="${pageContext.request.contextPath}/admin/bds">
                <span class="menu-icon">&#127970;</span>
                <span>Quản lý BDS</span>
            </a>
        </li>

        <!-- Link Quản lý Giao dịch -->
        <li class="nav-item">
            <a class="nav-link" href="${pageContext.request.contextPath}/admin/transactions">
                <span class="menu-icon">&#128196;</span>
                <span>Quản lý giao dịch</span>
            </a>
        </li>

        <!-- Link Lịch sử xem (Active) -->
        <li class="nav-item active">
            <a class="nav-link" href="${pageContext.request.contextPath}/admin/view-history">
                <span class="menu-icon">&#128338;</span>
                <span>Lịch sử xem</span>
            </a>
        </li>

        <hr class="sidebar-divider mt-3">

        <!-- Link Đăng xuất -->
        <li class="nav-item mt-auto">
            <a class="nav-link text-warning" href="${pageContext.request.contextPath}/acc/logout">
                <span class="menu-icon">&#10162;</span>
                <span>Đăng xuất</span>
            </a>
        </li>
    </ul>

    <!-- Content Wrapper -->
    <div id="content-wrapper" class="d-flex flex-column">
        <div id="content">
            <!-- Topbar Navigation -->
            <nav class="navbar topbar mb-4 shadow-sm" style="background-color: #fff;">
                <div class="d-flex align-items-center justify-content-between w-100 px-4">
                    <div>
                        <strong class="text-primary">REMS</strong> <span class="text-muted">/ HỆ THỐNG QUẢN LÝ BẤT ĐỘNG SẢN</span>
                    </div>
                    <div class="text-muted">
                        <i class="fas fa-user-circle me-1"></i> ${sessionScope.currentUser.fullName}
                    </div>
                </div>
            </nav>

            <!-- Begin Page Content -->
            <div class="container-fluid px-4">

                <!-- Tiêu đề trang -->
                <div class="d-flex align-items-center mb-4">
                    <h4 class="h5 mb-0 text-secondary"><i class="fa-solid fa-clock-rotate-left me-2"></i> Lịch sử truy cập BĐS</h4>
                </div>

                <!-- Bảng dữ liệu -->
                <div class="card shadow-sm border-0 mb-4">
                    <div class="card-body p-4">
                        <div class="table-responsive">
                            <table class="table table-bordered table-custom">
                                <thead>
                                    <tr>
                                        <th style="width: 5%;" class="text-center">STT</th>
                                        <th style="width: 20%;">Khách hàng</th>
                                        <th style="width: 20%;">Email</th>
                                        <th style="width: 30%;">Bất động sản quan tâm</th>
                                        <th style="width: 15%;">Thời gian xem</th>
                                        <th style="width: 10%;" class="text-center">Hành động</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:choose>
                                        <c:when test="${empty logList}">
                                            <tr>
                                                <td colspan="6" class="text-center text-muted py-4">Chưa có dữ liệu lịch sử xem BĐS.</td>
                                            </tr>
                                        </c:when>
                                        <c:otherwise>
                                            <c:forEach var="log" items="${logList}" varStatus="status">
                                                <tr>
                                                    <td class="text-center">${(currentPage - 1) * 10 + status.count}</td>
                                                    <td class="fw-bold"><c:out value="${log.customerName}"/></td>
                                                    <td><c:out value="${log.customerEmail}"/></td>
                                                    <td><c:out value="${log.propertyTitle}"/></td>
                                                    <td>
                                                        <fmt:parseDate value="${log.viewedAt}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDate" type="both" />
                                                        <fmt:formatDate pattern="dd/MM/yyyy HH:mm" value="${parsedDate}" />
                                                    </td>
                                                    <td class="text-center">
                                                        <a href="${pageContext.request.contextPath}/property/detail?id=${log.propertyId}" target="_blank" class="btn btn-info btn-sm text-white shadow-sm">
                                                            <i class="fa-solid fa-eye me-1"></i> Xem BĐS
                                                        </a>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </c:otherwise>
                                    </c:choose>
                                </tbody>
                            </table>
                        </div>

                        <!-- Phân trang -->
                        <c:if test="${totalPages > 1}">
                            <div class="d-flex justify-content-end mt-3">
                                <nav>
                                    <ul class="pagination mb-0">
                                        <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                                            <a class="page-link" href="?page=${currentPage - 1}">Trước</a>
                                        </li>
                                        <c:forEach begin="1" end="${totalPages}" var="i">
                                            <li class="page-item ${currentPage == i ? 'active' : ''}">
                                                <a class="page-link" href="?page=${i}">${i}</a>
                                            </li>
                                        </c:forEach>
                                        <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                                            <a class="page-link" href="?page=${currentPage + 1}">Sau</a>
                                        </li>
                                    </ul>
                                </nav>
                            </div>
                        </c:if>
                    </div>
                </div>

            </div>
            <!-- /.container-fluid -->
        </div>
        <!-- End of Main Content -->
    </div>
    <!-- End of Content Wrapper -->
</div>
<!-- End of Wrapper -->

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>