<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><c:out value="${pageTitle}" default="REMS - Hệ Thống Quản Lý Bất Động Sản" /></title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
    <link href="https://fonts.googleapis.com/css2?family=Urbanist:wght@400;500;600;700&display=swap" rel="stylesheet">

    <style>
        :root {
            --rems-dark: #0f172a;
            --rems-card-bg: #1e293b;
            --rems-primary: #0284c7;
            --rems-primary-hover: #0369a1;
            --rems-accent: #38bdf8;
        }
        body {
            font-family: 'Urbanist', sans-serif;
            background-color: #f8fafc;
            color: #334155;
            min-height: 100vh;
            display: flex;
            flex-direction: column;
        }
        .bg-rems-dark {
            background-color: var(--rems-dark) !important;
        }
        .text-rems-primary {
            color: var(--rems-primary) !important;
        }
        .btn-rems-primary {
            background-color: var(--rems-primary);
            color: #fff;
            border: none;
            transition: all 0.3s ease;
        }
        .btn-rems-primary:hover {
            background-color: var(--rems-primary-hover);
            color: #fff;
            transform: translateY(-2px);
        }
        .card-property {
            border: none;
            border-radius: 12px;
            overflow: hidden;
            box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1);
            transition: transform 0.3s ease, box-shadow 0.3s ease;
        }
        .card-property:hover {
            transform: translateY(-5px);
            box-shadow: 0 10px 15px -3px rgba(0, 0, 0, 0.15);
        }
    </style>
</head>
<body>