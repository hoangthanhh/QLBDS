package com.qlbds.controller.customer;

import com.qlbds.entity.Property;
<<<<<<< HEAD
=======
import com.qlbds.service.PropertyService;
>>>>>>> bff680a4b85b8c1095d04161649212f444a010c0

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
<<<<<<< HEAD
import java.util.ArrayList;
=======
>>>>>>> bff680a4b85b8c1095d04161649212f444a010c0
import java.util.List;

@WebServlet(urlPatterns = {"/home", "/trang-chu"})
public class HomeController extends HttpServlet {

    private PropertyService propertyService = new PropertyService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

<<<<<<< HEAD
        // dữ liệu giả để JSP không lỗi
        List<Property> realEstates = new ArrayList<>();

        req.setAttribute("realEstates", realEstates);

        req.getRequestDispatcher("/WEB-INF/views/customer/home.jsp")
                .forward(req, resp);
=======
        int page = 1;
        int pageSize = 9;

        String pageParam = req.getParameter("page");
        if (pageParam != null && !pageParam.isEmpty()) {
            try {
                page = Integer.parseInt(pageParam);
                if (page < 1) page = 1;
            } catch (NumberFormatException e) {
                page = 1;
            }
        }

        // LẤY THAM SỐ TÌM KIẾM TỪ REQUEST
        String address = req.getParameter("address");
        String priceRange = req.getParameter("priceRange");
        String propertyType = req.getParameter("propertyType");

        // Gọi Service có truyền tham số lọc
        List<Property> propertyList = propertyService.getPropertiesByPage(page, pageSize, address, priceRange, propertyType);
        int totalPages = propertyService.getTotalPages(pageSize, address, priceRange, propertyType);

        req.setAttribute("realEstates", propertyList);
        req.setAttribute("currentPage", page);
        req.setAttribute("totalPages", totalPages);

        String requestedWith = req.getHeader("X-Requested-With");
        if ("XMLHttpRequest".equals(requestedWith)) {
            req.getRequestDispatcher("/WEB-INF/views/customer/property-grid.jsp").forward(req, resp);
        } else {
            req.getRequestDispatcher("/WEB-INF/views/customer/home.jsp").forward(req, resp);
        }
>>>>>>> bff680a4b85b8c1095d04161649212f444a010c0
    }
}