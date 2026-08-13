package com.qlbds.controller.customer;

import com.qlbds.dto.user.UserDTO;
import com.qlbds.dto.user.ViewHistoryDTO; // Import package mới
import com.qlbds.service.ViewLogService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/customer/view-history")
public class ViewHistoryController extends HttpServlet {

    private ViewLogService viewLogService = new ViewLogService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);

        if (session == null || session.getAttribute("currentUser") == null) {
            resp.sendRedirect(req.getContextPath() + "/acc/login");
            return;
        }

        UserDTO currentUser = (UserDTO) session.getAttribute("currentUser");

        List<ViewHistoryDTO> historyList = viewLogService.getViewHistory(currentUser.getId());

        req.setAttribute("historyList", historyList);
        req.getRequestDispatcher("/WEB-INF/views/customer/view-history.jsp").forward(req, resp);
    }
}