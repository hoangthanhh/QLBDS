package com.qlbds.controller.customer;

import com.qlbds.entity.Property;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(urlPatterns = {"/home", "/trang-chu"})
public class HomeController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // dữ liệu giả để JSP không lỗi
        List<Property> realEstates = new ArrayList<>();

        req.setAttribute("realEstates", realEstates);

        req.getRequestDispatcher("/WEB-INF/views/customer/home.jsp")
                .forward(req, resp);
    }
}