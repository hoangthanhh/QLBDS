package com.qlbds.controller.admin;

import com.qlbds.dto.admin.DashboardDTO;
import com.qlbds.service.ReportService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "AdminDashboardController", value = {"/admin/dashboard", "/admin/report"})
public class AdminDashboardController extends HttpServlet {

    private ReportService reportService = new ReportService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");

        // Gọi Service xử lý logic nghiệp vụ
        DashboardDTO dashboardData = reportService.getDashboardData(startDate, endDate);

        // Đẩy đúng các biến sang JSP
        request.setAttribute("totalAvailable", dashboardData.getTotalAvailableBDS());
        request.setAttribute("totalDepositAmount", dashboardData.getTotalDepositAmount());
        request.setAttribute("totalSold", dashboardData.getTotalSoldBDS());
        request.setAttribute("totalRevenue", dashboardData.getTotalRevenue());
        request.setAttribute("monthlyData", dashboardData.getMonthlyData().toString());

        request.setAttribute("startDate", dashboardData.getStartDate());
        request.setAttribute("endDate", dashboardData.getEndDate());
        request.setAttribute("filteredRevenue", dashboardData.getFilteredRevenue());
        request.setAttribute("dateError", dashboardData.getDateError());

        // Điều hướng về đúng file adminDashboard.jsp
        request.getRequestDispatcher("/WEB-INF/views/admin/adminDashboard.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}