package com.qlbds.controller.acc;

import com.qlbds.dto.UserDTO;
import com.qlbds.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/acc/register")
public class RegisterController extends HttpServlet {

    private UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/acc/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        UserDTO dto = new UserDTO();
        dto.setFullName(req.getParameter("fullName"));
        dto.setPhone(req.getParameter("phone"));
        dto.setEmail(req.getParameter("email"));
        dto.setPassword(req.getParameter("password"));
        dto.setConfirmPassword(req.getParameter("confirmPassword"));

        String result = userService.registerUser(dto);

        if ("SUCCESS".equals(result)) {
            req.setAttribute("message", "Đăng ký thành công! Vui lòng đăng nhập.");
            req.getRequestDispatcher("/WEB-INF/views/acc/login.jsp").forward(req, resp);
        } else {
            // Giữ lại toàn bộ dữ liệu vừa nhập và câu thông báo lỗi
            req.setAttribute("error", result);
            req.setAttribute("userDto", dto);
            req.getRequestDispatcher("/WEB-INF/views/acc/register.jsp").forward(req, resp);
        }
    }
}