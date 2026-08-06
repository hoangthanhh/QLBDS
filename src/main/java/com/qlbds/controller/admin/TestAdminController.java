
package com.qlbds.controller.admin;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/test-admin")
public class TestAdminController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String view = req.getParameter("view");

        if (view == null || view.isEmpty()) {
            view = "admin"; // Mặc định mở admin.jsp
        }

        // Forward dữ liệu tới thư mục trong WEB-INF/views/admin/
        req.getRequestDispatcher("/WEB-INF/views/admin/" + view + ".jsp").forward(req, resp);
    }
}