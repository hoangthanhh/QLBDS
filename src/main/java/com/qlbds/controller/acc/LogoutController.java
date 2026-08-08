package com.qlbds.controller.acc;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(urlPatterns = {"/auth/logout"})
public class LogoutController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // 1. Lấy session hiện tại (truyền tham số 'false' để không tự động tạo session mới nếu chưa có)
        HttpSession session = req.getSession(false);

        // 2. Nếu session đang tồn tại, tiến hành xóa thông tin user và hủy session
        if (session != null) {
            session.removeAttribute("currentUser"); // Xóa biến lưu thông tin người dùng
            session.invalidate(); // Xóa hoàn toàn session
        }

        // 3. Chuyển hướng (Redirect) người dùng về lại trang chủ sau khi đăng xuất thành công
        resp.sendRedirect(req.getContextPath() + "/home");
    }
}