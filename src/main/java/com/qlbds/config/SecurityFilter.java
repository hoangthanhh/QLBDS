package com.qlbds.config;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(urlPatterns = "/*")
public class SecurityFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        // Lấy đường dẫn URI (Ví dụ: /home, /customer/profile, /okk...)
        String path = req.getRequestURI().substring(req.getContextPath().length());

<<<<<<< HEAD
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
=======
        // 1. KIỂM TRA XEM ĐƯỜNG DẪN CÓ THUỘC KHU VỰC CẦN BẢO VỆ HAY KHÔNG
        boolean isProtectedArea = path.startsWith("/customer") ||
                path.startsWith("/admin") ||
                path.startsWith("/staff");
>>>>>>> bff680a4b85b8c1095d04161649212f444a010c0

        // 2. NẾU KHÔNG PHẢI KHU VỰC BẢO VỆ (Trang chủ, Login, Register, Assets hoặc URL lạ như /okk)
        if (!isProtectedArea) {
            // Cho phép đi tiếp: Nếu URL đúng thì hiển thị, nếu URL gõ bậy (/okk) Tomcat sẽ tự trả lỗi 404
            chain.doFilter(request, response);
            return;
        }

<<<<<<< HEAD
        if (path.startsWith("/assets/") || path.equals("/home") || path.equals("/trang-chu") || path.startsWith("/acc")) {
            chain.doFilter(request, response);
            return;
        }

        // 2. KIỂM TRA ĐĂNG NHẬP (AUTHENTICATION)
=======
        // 3. NẾU TRUY CẬP KHU VỰC BẢO VỆ -> BẮT BUỘC KIỂM TRA ĐĂNG NHẬP (AUTHENTICATION)
>>>>>>> bff680a4b85b8c1095d04161649212f444a010c0
        HttpSession session = req.getSession(false);
        Object currentUser = (session != null) ? session.getAttribute("currentUser") : null;

        if (currentUser == null) {
<<<<<<< HEAD
=======
            // Chưa đăng nhập mà đòi truy cập /customer, /admin, /staff -> Chuyển hướng về Login
>>>>>>> bff680a4b85b8c1095d04161649212f444a010c0
            resp.sendRedirect(req.getContextPath() + "/acc/login");
            return;
        }

<<<<<<< HEAD
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

=======
        // 4. KIỂM TRA PHÂN QUYỀN (AUTHORIZATION) DỰA TRÊN ROLE
        String role = (String) session.getAttribute("userRole"); // "ADMIN", "STAFF", "CUSTOMER"

        if (path.startsWith("/admin") && !"ADMIN".equals(role)) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Bạn không có quyền truy cập khu vực Quản trị viên.");
            return;
        }

        if (path.startsWith("/staff") && (!"STAFF".equals(role) && !"ADMIN".equals(role))) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Bạn không có quyền truy cập khu vực Nhân viên.");
            return;
        }

        if (path.startsWith("/customer") && !"CUSTOMER".equals(role)) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Khu vực này chỉ dành cho Khách hàng.");
            return;
        }

        // Đã đăng nhập và hợp lệ Role -> Cho phép truy cập
>>>>>>> bff680a4b85b8c1095d04161649212f444a010c0
        chain.doFilter(request, response);
        ------------------------------------------------------------- */
    }

    @Override
<<<<<<< HEAD
    public void destroy() {
        // Dọn dẹp tài nguyên khi Filter bị hủy
    }
=======
    public void destroy() {}
>>>>>>> bff680a4b85b8c1095d04161649212f444a010c0
}