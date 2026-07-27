package com.qlbds.controller.staff;

import com.qlbds.controller.customer.MockProperty;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/staff/properties")
public class PropertyManagementController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 1. Tạo dữ liệu giả phục vụ hiển thị bảng danh sách quản lý của Nhân viên
        List<MockProperty> mockList = new ArrayList<>();

        mockList.add(new MockProperty(1, "Căn hộ cao cấp Vinhomes Metropolis 2PN ban công thoáng", "29 Liễu Giai, Ba Đình, Hà Nội", "APARTMENT", 4500000000L, "AVAILABLE", ""));
        mockList.add(new MockProperty(2, "Biệt thự sân vườn Khu đô thị Ciputra Nam Thăng Long", "Xuân Đỉnh, Bắc Từ Liêm, Hà Nội", "HOUSE", 25000000000L, "DEPOSITED", ""));
        mockList.add(new MockProperty(3, "Đất nền phân lô sổ đỏ chính chủ Hòa Lạc", "Thạch Hòa, Thạch Thất, Hà Nội", "LAND", 1800000000L, "AVAILABLE", ""));

        // 2. Đẩy dữ liệu sang file JSP của Staff
        req.setAttribute("properties", mockList);

        // 3. Điều hướng đến trang quản lý của nhân viên
        req.getRequestDispatcher("/WEB-INF/views/staff/property-list.jsp").forward(req, resp);
    }
}