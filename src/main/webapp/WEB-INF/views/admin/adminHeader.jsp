<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>REMS - Quản trị hệ thống</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/admin/lib/fontawesome/fontawesome.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/admin/lib/bootstrap/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/admin/css/sb-admin-2.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/admin/css/style.css?v=20260806-green-sidebar">
</head>
<body><div id="wrapper">
<ul class="navbar-nav sidebar sidebar-dark accordion" id="accordionSidebar">
    <a class="sidebar-brand d-flex align-items-center justify-content-center" href="${pageContext.request.contextPath}/test-admin?view=admin">
        <img class="brand-logo" src="${pageContext.request.contextPath}/assets/admin/img/rems-logo.svg" alt="REMS logo">
        <div class="sidebar-brand-text mx-2">REMS <small class="d-block font-weight-normal">Quản trị hệ thống</small></div>
    </a>
    <hr class="sidebar-divider my-0">
    <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/test-admin?view=admin"><span class="menu-icon">&#128202;</span><span>Báo cáo & thống kê</span></a></li>
    <div class="sidebar-heading">Quản lý hệ thống</div>
    <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/test-admin?view=adminAccount"><span class="menu-icon">&#128101;</span><span>Quản lý người dùng</span></a></li>
    <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/test-admin?view=adminBDS"><span class="menu-icon">&#127970;</span><span>Quản lý BDS</span></a></li>
    <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/test-admin?view=adminTransaction"><span class="menu-icon">&#128196;</span><span>Quản lý giao dịch</span></a></li>
    <hr class="sidebar-divider">
    <hr class="sidebar-divider d-none d-md-block">
    <li class="nav-item mt-auto"><a class="nav-link text-warning" href="${pageContext.request.contextPath}/logout"><span class="menu-icon">&#10162;</span><span>Đăng xuất</span></a></li>
</ul>
<div id="content-wrapper" class="d-flex flex-column"><div id="content">
<nav class="navbar topbar mb-0"><div class="d-flex align-items-center justify-content-between w-100 px-4"><div><strong>REMS</strong> <span class="text-muted">/ HỆ THỐNG QUẢN LÝ BẤT ĐỘNG SẢN</span></div><div class="text-muted"><i class="fas fa-user-circle"></i> ${sessionScope.currentUser.fullName}</div></div></nav>
<div class="container-fluid">
