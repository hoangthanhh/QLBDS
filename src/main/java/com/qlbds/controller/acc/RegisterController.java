package com.qlbds.controller.acc;

import com.qlbds.dto.UserDTO;
import com.qlbds.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "RegisterController", value = {"/acc/register", "/register", "/auth/register"})
public class RegisterController extends HttpServlet {

    private UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/acc/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        UserDTO dto = new UserDTO();
        dto.setFullName(request.getParameter("fullName"));
        dto.setEmail(request.getParameter("email"));
        dto.setPhone(request.getParameter("phone"));
        dto.setPassword(request.getParameter("password"));
        dto.setConfirmPassword(request.getParameter("confirmPassword"));

        String result = userService.registerUser(dto);

        if ("SUCCESS".equals(result)) {
            // Chuyển hướng kèm theo cờ báo thành công để hiển thị lên View
            response.sendRedirect(request.getContextPath() + "/acc/login?registerSuccess=true");
        } else {
            request.setAttribute("error", result);
            request.getRequestDispatcher("/WEB-INF/views/acc/register.jsp").forward(request, response);
        }
    }
}