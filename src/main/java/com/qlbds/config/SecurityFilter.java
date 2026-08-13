package com.qlbds.config;

import com.qlbds.dto.user.UserDTO;

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

        String path = req.getRequestURI().substring(req.getContextPath().length());

        boolean isProtectedArea = path.startsWith("/customer") ||
                path.startsWith("/admin") ||
                path.startsWith("/staff");

        if (!isProtectedArea) {
            chain.doFilter(request, response);
            return;
        }

        HttpSession session = req.getSession(false);
        // ĐÃ SỬA: Ép kiểu chuẩn xác sang UserDTO
        UserDTO currentUser = (session != null) ? (UserDTO) session.getAttribute("currentUser") : null;

        if (currentUser == null) {
            resp.sendRedirect(req.getContextPath() + "/acc/login");
            return;
        }

        // ĐÃ SỬA: Lấy role trực tiếp từ DTO
        String role = currentUser.getRole();

        if (path.startsWith("/admin") && !"ADMIN".equals(role)) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Bạn không có quyền truy cập khu vực Quản trị viên.");
            return;
        }

        if (path.startsWith("/staff") && (!"STAFF".equals(role) && !"ADMIN".equals(role))) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Bạn không có quyền truy cập khu vực Nhân viên.");
            return;
        }

        // Lưu ý: Khu vực /customer mở cho cả CUSTOMER, STAFF và ADMIN để ai cũng tự xem được Profile của mình

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {}
}