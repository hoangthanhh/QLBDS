package com.qlbds.controller.acc;

import com.qlbds.dto.acc.RegisterDTO;
import com.qlbds.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "RegisterController", value = {"/acc/register"})
public class RegisterController extends HttpServlet {

    private UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/acc/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        RegisterDTO registerDto = new RegisterDTO();
        registerDto.setFullName(request.getParameter("fullName"));
        registerDto.setEmail(request.getParameter("email"));
        registerDto.setPhone(request.getParameter("phone"));
        registerDto.setPassword(request.getParameter("password"));
        registerDto.setConfirmPassword(request.getParameter("confirmPassword"));

        String result = userService.registerUser(registerDto);

        if ("SUCCESS".equals(result)) {
            response.sendRedirect(request.getContextPath() + "/acc/login?registerSuccess=true");
        } else {
            request.setAttribute("error", result);
            request.setAttribute("registerDto", registerDto);
            request.setAttribute("userDto", registerDto); // Giữ biến này để tương thích với file register.jsp hiện tại
            request.getRequestDispatcher("/WEB-INF/views/acc/register.jsp").forward(request, response);
        }
    }
}