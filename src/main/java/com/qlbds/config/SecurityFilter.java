package com.qlbds.config;

import com.qlbds.entity.User;

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

        // 2. NẾU KHÔNG PHẢI KHU VỰC BẢO VỆ -> ĐI TIẾP BÌNH THƯỜNG
        if (!isProtectedArea) {
            chain.doFilter(request, response);
            return;
        }

        // 3. NẾU TRUY CẬP KHU VỰC BẢO VỆ -> BẮT BUỘC KIỂM TRA ĐĂNG NHẬP
        HttpSession session = req.getSession(false);
        Object currentUser = (session != null) ? session.getAttribute("currentUser") : null;

        if (currentUser == null) {
            // Chưa đăng nhập mà đòi truy cập -> Chuyển hướng về Login
            resp.sendRedirect(req.getContextPath() + "/acc/login");
            return;
        }

        // 4. KIỂM TRA PHÂN QUYỀN (AUTHORIZATION) DỰA TRÊN ROLE
        String role = "";
        if (currentUser instanceof User) {
            role = ((User) currentUser).getRole().name();
        } else {
            role = (String) session.getAttribute("userRole"); // Fallback
        }

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