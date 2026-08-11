package com.qlbds.controller.acc;

import com.qlbds.constant.RoleTypeEnum;
import com.qlbds.entity.User;
import com.qlbds.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "LoginController", value = {"/acc/login"})
public class LoginController extends HttpServlet {

    private UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/acc/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        User user = userService.loginUser(email, password);

        if (user != null) {
            HttpSession session = request.getSession();
            session.setAttribute("currentUser", user);

            if (RoleTypeEnum.ADMIN.equals(user.getRole())) {
                response.sendRedirect(request.getContextPath() + "/admin/dashboard");
            } else if (RoleTypeEnum.STAFF.equals(user.getRole())) {
                response.sendRedirect(request.getContextPath() + "/staff/dashboard");
            } else {
                response.sendRedirect(request.getContextPath() + "/home");
            }
        } else {
            // Giữ lại câu báo lỗi chi tiết và email của bạn, đã fix tên biến request
            request.setAttribute("error", "Email hoặc mật khẩu không chính xác, hoặc tài khoản đã bị khóa!");
            request.setAttribute("email", email);
            request.getRequestDispatcher("/WEB-INF/views/acc/login.jsp").forward(request, response);
        }
    }
}