package com.qlbds.controller.customer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/properties")
public class PropertyExploreController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 1. Tạo danh sách dữ liệu giả (Mock Data) để test hiển thị giao diện giống Rever
        List<MockProperty> mockList = new ArrayList<>();

        mockList.add(new MockProperty(1,
                "Căn hộ cao cấp Vinhomes Metropolis 2PN ban công thoáng",
                "29 Liễu Giai, Ngọc Khánh, Ba Đình, Hà Nội",
                "APARTMENT", 4500000000L, "AVAILABLE",
                "https://images.unsplash.com/photo-1560448204-e02f11c3d0e2?auto=format&fit=crop&w=600&q=80"));

        mockList.add(new MockProperty(2,
                "Biệt thự sân vườn Khu đô thị Ciputra Nam Thăng Long",
                "Xuân Đỉnh, Bắc Từ Liêm, Hà Nội",
                "HOUSE", 25000000000L, "DEPOSITED",
                "https://images.unsplash.com/photo-1600585154340-be6161a56a0c?auto=format&fit=crop&w=600&q=80"));

        mockList.add(new MockProperty(3,
                "Đất nền phân lô sổ đỏ chính chủ Hòa Lạc",
                "Thạch Hòa, Thạch Thất, Hà Nội",
                "LAND", 1800000000L, "AVAILABLE",
                "https://images.unsplash.com/photo-1500382017468-9049fed747ef?auto=format&fit=crop&w=600&q=80"));

        // 2. Đẩy danh sách ra thuộc tính "properties" để file JSP dùng thẻ c:forEach duyệt qua
        req.setAttribute("properties", mockList);

        // 3. Chuyển tiếp tới giao diện customer
        req.getRequestDispatcher("/WEB-INF/views/customer/properties.jsp").forward(req, resp);
    }
}