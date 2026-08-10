package com.qlbds.controller.admin;

import com.qlbds.dto.StatisticDTO;
import com.qlbds.service.StatisticService;
import com.qlbds.util.DateValidationUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;

@WebServlet(name = "StatisticController", value = "/admin/dashboard")
public class StatisticController extends HttpServlet {

    private StatisticService statisticService = new StatisticService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Hứng tham số lọc ngày
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");

        // Validate lỗi ngày tháng để hiển thị thông báo ra View (nếu có)
        String dateError = DateValidationUtil.validateDateRange(startDate, endDate);
        if (dateError != null) {
            request.setAttribute("errorMsg", dateError);
        }

        // Lấy năm hiện tại để thống kê biểu đồ 12 tháng
        int currentYear = LocalDate.now().getYear();

        // ĐÃ SỬA: Khai báo rõ kiểu dữ liệu StatisticDTO ở đầu
        StatisticDTO statisticDTO = statisticService.getDashboardData(startDate, endDate, currentYear);

        // Gửi Object chứa thống kê sang View
        request.setAttribute("dashboardData", statisticDTO);

        // Lưu lại giá trị ngày lọc để hiển thị lại trên thanh input form
        request.setAttribute("startDate", startDate);
        request.setAttribute("endDate", endDate);

        // Forward sang JSP
        request.getRequestDispatcher("/WEB-INF/views/admin/adminDashboard.jsp").forward(request, response);
    }
}