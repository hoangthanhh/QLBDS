package com.qlbds.controller.staff;

import com.qlbds.dto.admin.DashboardDTO;
import com.qlbds.service.ReportService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "StaffDashboardController", value = {"/staff/dashboard"})
public class StaffDashboardController extends HttpServlet {

    private final ReportService reportService = new ReportService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DashboardDTO dashboardData = reportService.getDashboardData(null, null);

        // Đẩy đúng các biến sang JSP
        request.setAttribute("totalAvailable", dashboardData.getTotalAvailableBDS());
        request.setAttribute("totalDepositAmount", dashboardData.getTotalDepositAmount());
        request.setAttribute("totalSold", dashboardData.getTotalSoldBDS());
        request.setAttribute("totalRevenue", dashboardData.getTotalRevenue());
        request.setAttribute("monthlyData", dashboardData.getMonthlyData().toString());

        request.getRequestDispatcher("/WEB-INF/views/staff/staffDashboard.jsp").forward(request, response);
    }
}