package com.qlbds.controller.acc;

import com.qlbds.dto.user.UserDTO;
import com.qlbds.dto.acc.LoginDTO;
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

        // Đóng gói request vào LoginDTO
        LoginDTO loginDto = new LoginDTO();
        loginDto.setEmail(request.getParameter("email"));
        loginDto.setPassword(request.getParameter("password"));

        // Service trả về thẳng UserDTO sạch sẽ
        UserDTO currentUser = userService.loginUser(loginDto);

        if (currentUser != null) {
            HttpSession session = request.getSession();
            session.setAttribute("currentUser", currentUser); // Lưu DTO vào Session

            // Điều hướng theo Role
            if ("ADMIN".equals(currentUser.getRole())) {
                response.sendRedirect(request.getContextPath() + "/admin/dashboard");
            } else if ("STAFF".equals(currentUser.getRole())) {
                response.sendRedirect(request.getContextPath() + "/staff/dashboard");
            } else {
                response.sendRedirect(request.getContextPath() + "/home");
            }
        } else {
            request.setAttribute("error", "Email hoặc mật khẩu không chính xác, hoặc tài khoản đã bị khóa!");
            request.setAttribute("email", loginDto.getEmail());
            request.getRequestDispatcher("/WEB-INF/views/acc/login.jsp").forward(request, response);
        }
    }
}