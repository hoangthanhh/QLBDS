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

        request.setAttribute("totalDeposited", dashboardData.getTotalDepositedBDS());
        request.setAttribute("totalSold", dashboardData.getTotalSoldBDS());
        request.setAttribute("totalTransactions", dashboardData.getTotalTransactions());
        request.setAttribute("totalRevenue", dashboardData.getTotalRevenue());

        request.getRequestDispatcher("/WEB-INF/views/staff/staffDashboard.jsp").forward(request, response);
    }
}