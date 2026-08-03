package com.qlbds.controller.acc;

import com.qlbds.entity.User;
import com.qlbds.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/acc/login")
public class LoginController extends HttpServlet {

    private UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/acc/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String email = req.getParameter("email");
        String password = req.getParameter("password");

        User user = userService.loginUser(email, password);

        if (user != null) {
            HttpSession session = req.getSession();
            session.setAttribute("currentUser", user);
            session.setAttribute("userRole", user.getRole().name()); // Gán Role dạng chuỗi cho Filter dễ xử lý

            // Căn cứ vào RoleTypeEnum để điều hướng
            switch (user.getRole()) {
                case ADMIN:
                    resp.sendRedirect(req.getContextPath() + "/admin/dashboard");
                    break;
                case STAFF:
                    resp.sendRedirect(req.getContextPath() + "/staff/dashboard");
                    break;
                case CUSTOMER:
                default:
                    resp.sendRedirect(req.getContextPath() + "/home");
                    break;
            }
        } else {
            req.setAttribute("error", "Email hoặc mật khẩu không chính xác, hoặc tài khoản đã bị khóa!");
            req.getRequestDispatcher("/WEB-INF/views/acc/login.jsp").forward(req, resp);
        }
    }
}