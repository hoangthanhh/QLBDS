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

        // 1. KIỂM TRA XEM ĐƯỜNG DẪN CÓ THUỘC KHU VỰC CẦN BẢO VỆ HAY KHÔNG
        boolean isProtectedArea = path.startsWith("/customer") ||
                path.startsWith("/admin") ||
                path.startsWith("/staff");

        // 2. NẾU KHÔNG PHẢI KHU VỰC BẢO VỆ (Trang chủ, Login, Register, Assets hoặc URL lạ như /okk)
        if (!isProtectedArea) {
            // Cho phép đi tiếp: Nếu URL đúng thì hiển thị, nếu URL gõ bậy (/okk) Tomcat sẽ tự trả lỗi 404
            chain.doFilter(request, response);
            return;
        }

        // 3. NẾU TRUY CẬP KHU VỰC BẢO VỆ -> BẮT BUỘC KIỂM TRA ĐĂNG NHẬP (AUTHENTICATION)
        HttpSession session = req.getSession(false);
        Object currentUser = (session != null) ? session.getAttribute("currentUser") : null;

        if (currentUser == null) {
            // Chưa đăng nhập mà đòi truy cập /customer, /admin, /staff -> Chuyển hướng về Login
            resp.sendRedirect(req.getContextPath() + "/acc/login");
            return;
        }

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
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {}
}