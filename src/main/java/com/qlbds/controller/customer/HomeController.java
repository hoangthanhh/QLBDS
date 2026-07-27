package com.qlbds.controller.customer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// Nếu dự án dùng Jakarta EE (Servlet 5.0+ / Tomcat 10+), thay javax.servlet bằng jakarta.servlet

@WebServlet(urlPatterns = {"/home", "/trang-chu"})
public class HomeController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Forward request từ Controller sang View home.jsp trong WEB-INF
        req.getRequestDispatcher("/WEB-INF/views/customer/home.jsp").forward(req, resp);
    }
}