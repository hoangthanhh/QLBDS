package com.qlbds.config;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

// Bắt mọi request gửi đến server
@WebFilter(urlPatterns = "/*")
public class SecurityFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Có thể khởi tạo các tham số mặc định ở đây nếu cần
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        // Lấy đường dẫn URI hiện tại (bỏ qua phần tên project context)
        String path = req.getRequestURI().substring(req.getContextPath().length());

        // =========================================================
        // [TAM THỜI TẮT BẢO MẬT ĐỂ SOI GIAO DIỆN ADMIN / STAFF]
        // Bỏ qua toàn bộ luồng kiểm tra Login & Role dưới đây
        // =========================================================
        chain.doFilter(request, response);
        return;

        /* ---------- COMMENT TOÀN BỘ LOGIC BẢO MẬT BAN ĐẦU ----------

        // 1. CHO PHÉP TRUY CẬP CÔNG KHAI (Không cần đăng nhập)
        if (path.isEmpty() || path.equals("/") || path.equals("/index.jsp") ||
                path.equals("/home") || path.equals("/trang-chu") ||
                path.startsWith("/assets/") || path.startsWith("/acc") ||
                path.startsWith("/test-admin")) {

            chain.doFilter(request, response);
            return;
        }

        if (path.startsWith("/assets/") || path.equals("/home") || path.equals("/trang-chu") || path.startsWith("/acc")) {
            chain.doFilter(request, response);
            return;
        }

        // 2. KIỂM TRA ĐĂNG NHẬP (AUTHENTICATION)
        HttpSession session = req.getSession(false);
        Object currentUser = (session != null) ? session.getAttribute("currentUser") : null;

        if (currentUser == null) {
            resp.sendRedirect(req.getContextPath() + "/acc/login");
            return;
        }

        // 3. KIỂM TRA PHÂN QUYỀN (AUTHORIZATION)
        String role = (String) session.getAttribute("userRole");

        // Ràng buộc cho URL của Admin
        if (path.startsWith("/admin")) {
            if (!"ADMIN".equals(role)) {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Bạn không có quyền truy cập khu vực Quản trị viên.");
                return;
            }
        }

        // Ràng buộc cho URL của Staff
        if (path.startsWith("/staff")) {
            if (!"STAFF".equals(role) && !"ADMIN".equals(role)) {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Bạn không có quyền truy cập khu vực Nhân viên.");
                return;
            }
        }

        // Ràng buộc cho URL của Customer
        if (path.startsWith("/customer")) {
            if (!"CUSTOMER".equals(role)) {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Khu vực này chỉ dành cho Khách hàng.");
                return;
            }
        }

        chain.doFilter(request, response);
        ------------------------------------------------------------- */
    }

    @Override
    public void destroy() {
        // Dọn dẹp tài nguyên khi Filter bị hủy
    }
}