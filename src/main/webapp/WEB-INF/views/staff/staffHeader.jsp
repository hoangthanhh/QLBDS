<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>REMS - Cổng Nhân Viên</title>
    <!-- FontAwesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <!-- Bootstrap 5 CSS -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css">
    <!-- SB Admin 2 CSS & Custom Styles -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/admin/css/sb-admin-2.min.css">
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/admin/css/style.css?v=20260806-green-sidebar">
    <style>
        .sidebar-brand-static, .sidebar-brand-static * {
            text-decoration: none !important;
            cursor: default !important;
            user-select: none !important;
            pointer-events: none !important;
        }
    </style>
</head>
<body id="page-top">
<div id="wrapper">
    <!-- Sidebar Staff -->
    <ul class="navbar-nav sidebar sidebar-dark accordion" id="accordionSidebar">

        <!-- LOGO REMS CỐ ĐỊNH -->
        <div class="sidebar-brand d-flex align-items-center justify-content-center sidebar-brand-static py-3">
            <i class="fa-solid fa-user-tie fa-2x me-2 text-white"></i>
            <div class="sidebar-brand-text text-white text-start">
                <span class="fw-bold" style="font-size: 20px; line-height: 1.1; display: block;">REMS</span>
                <small class="d-block fw-normal text-white-50" style="font-size: 11px;">Cổng Nhân Viên (Staff)</small>
            </div>
        </div>
        <hr class="sidebar-divider my-0">

        <!-- Menu Staff Dashboard -->
        <li class="nav-item">
            <a class="nav-link" href="${pageContext.request.contextPath}/staff/dashboard">
                <span class="menu-icon">&#128202;</span>
                <span>Bàn làm việc</span>
            </a>
        </li>

        <div class="sidebar-heading">Nghiệp vụ nhân viên</div>

        <!-- 1. Quản lý danh sách BDS -->
        <li class="nav-item">
            <a class="nav-link" href="${pageContext.request.contextPath}/staff/bds">
                <span class="menu-icon">&#127970;</span>
                <span>Quản lý Bất Động Sản</span>
            </a>
        </li>

        <!-- 2. Xử lý giao dịch -->
        <li class="nav-item">
            <a class="nav-link" href="${pageContext.request.contextPath}/staff/transactions">
                <span class="menu-icon">&#128196;</span>
                <span>Xử lý Giao dịch</span>
            </a>
        </li>

        <hr class="sidebar-divider">

        <!-- Đăng xuất -->
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
            <nav class="navbar topbar mb-0 shadow-sm bg-white">
                <div class="d-flex align-items-center justify-content-between w-100 px-4">
                    <div>
                        <strong class="text-primary">REMS</strong> <span
                            class="text-muted">/ KHU VỰC LÀM VIỆC NHÂN VIÊN</span>
                    </div>
                    <div class="text-muted">
                        <span class="badge bg-info text-dark me-2">STAFF</span>
                        <i class="fas fa-user-circle me-1"></i> ${sessionScope.currentUser.fullName}
                    </div>
                </div>
            </nav>
            <div class="container-fluid">