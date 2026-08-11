<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>REMS - Quản trị hệ thống</title>
    <!-- FontAwesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">

    <!-- Bootstrap 5 CSS -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css">

    <!-- SB Admin 2 CSS & Custom Styles -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/admin/css/sb-admin-2.min.css">
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/admin/css/style.css?v=20260806-green-sidebar">

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
    </style>
</head>
<body>
<div id="wrapper">
    <!-- Sidebar Navbar -->
    <ul class="navbar-nav sidebar sidebar-dark accordion" id="accordionSidebar">

        <!-- LOGO REMS KÈM ICON NGÔI NHÀ CỐ ĐỊNH (KHÔNG CLICK, KHÔNG GẠCH CHÂN) -->
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

        <div class="sidebar-heading">Quản lý hệ thống</div>

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

        <!-- Link Lịch sử xem -->
        <li class="nav-item">
            <a class="nav-link" href="${pageContext.request.contextPath}/admin/history">
                <span class="menu-icon">&#128338;</span>
                <span>Lịch sử xem</span>
            </a>
        </li>

        <hr class="sidebar-divider">
        <hr class="sidebar-divider d-none d-md-block">

        <!-- Link Đăng xuất -->
        <li class="nav-item mt-auto">
            <a class="nav-link text-warning" href="${pageContext.request.contextPath}/logout">
                <span class="menu-icon">&#10162;</span>
                <span>Đăng xuất</span>
            </a>
        </li>
    </ul>

    <!-- Content Wrapper -->
    <div id="content-wrapper" class="d-flex flex-column">
        <div id="content">
            <!-- Topbar Navigation -->
            <nav class="navbar topbar mb-0 shadow-sm">
                <div class="d-flex align-items-center justify-content-between w-100 px-4">
                    <div>
                        <strong>REMS</strong> <span class="text-muted">/ HỆ THỐNG QUẢN LÝ BẤT ĐỘNG SẢN</span>
                    </div>
                    <div class="text-muted">
                        <i class="fas fa-user-circle me-1"></i> ${sessionScope.currentUser.fullName}
                    </div>
                </div>
            </nav>
            <div class="container-fluid">