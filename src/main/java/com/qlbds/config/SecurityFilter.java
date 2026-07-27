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
        // 1. CHO PHÉP TRUY CẬP CÔNG KHAI (Không cần đăng nhập)
        // =========================================================
        // Bao gồm: thư mục assets, trang chủ, và các API/Controller xử lý Auth (login, register)
        if (path.isEmpty() || path.equals("/") || path.equals("/index.jsp") ||
                path.equals("/home") || path.equals("/trang-chu") ||
                path.startsWith("/assets/") || path.startsWith("/auth")) {

            chain.doFilter(request, response);
            return;
        }

        if (path.startsWith("/assets/") || path.equals("/home") || path.equals("/trang-chu") || path.startsWith("/auth")) {
            chain.doFilter(request, response);
            return;
        }

        // =========================================================
        // 2. KIỂM TRA ĐĂNG NHẬP (AUTHENTICATION)
        // =========================================================
        HttpSession session = req.getSession(false);

        // Giả sử khi login thành công, bạn lưu thông tin vào session với key là "currentUser"
        Object currentUser = (session != null) ? session.getAttribute("currentUser") : null;

        if (currentUser == null) {
            // Chưa đăng nhập -> Điều hướng về trang đăng nhập của Controller auth
            resp.sendRedirect(req.getContextPath() + "/auth/login");
            return;
        }

        // =========================================================
        // 3. KIỂM TRA PHÂN QUYỀN (AUTHORIZATION)
        // =========================================================
        // Giả sử bạn lưu Role trong session dưới dạng String (hoặc lấy từ entity currentUser)
        // Bạn cần sửa lại đoạn ép kiểu này cho khớp với Enum bạn đã tạo ở package constant
        String role = (String) session.getAttribute("userRole");

        // Ràng buộc cho URL của Admin
        if (path.startsWith("/admin")) {
            if (!"ADMIN".equals(role)) {
                // Trả về lỗi 403 Forbidden nếu không phải Admin
                resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Bạn không có quyền truy cập khu vực Quản trị viên.");
                return;
            }
        }

        // Ràng buộc cho URL của Staff
        if (path.startsWith("/staff")) {
            if (!"STAFF".equals(role) && !"ADMIN".equals(role)) {
                // Admin cũng có thể xem được trang của Staff (tuỳ logic hệ thống của bạn)
                resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Bạn không có quyền truy cập khu vực Nhân viên.");
                return;
            }
        }

        // Ràng buộc cho URL của Customer (chỉ những người dùng là Customer mới vào được trang cá nhân/giao dịch)
        if (path.startsWith("/customer")) {
            if (!"CUSTOMER".equals(role)) {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Khu vực này chỉ dành cho Khách hàng.");
                return;
            }
        }

        // Nếu vượt qua mọi điều kiện, cho phép request đi tiếp đến Controller tương ứng
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // Dọn dẹp tài nguyên khi Filter bị hủy
    }
}
