package com.qlbds.controller.customer;

import com.qlbds.entity.Property;
import com.qlbds.repository.PropertyRepository;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/property/detail")
public class PropertyDetailController extends HttpServlet {

    private PropertyRepository propertyRepository = new PropertyRepository();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idParam = req.getParameter("id");
        if (idParam == null || idParam.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/home");
            return;
        }

        try {
            Integer id = Integer.parseInt(idParam);
            Property property = propertyRepository.findById(id);

            if (property == null) {
                resp.sendRedirect(req.getContextPath() + "/home");
                return;
            }

            req.setAttribute("property", property);
            req.getRequestDispatcher("/WEB-INF/views/customer/property-detail.jsp").forward(req, resp);
        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/home");
        }
    }
}