package com.qlbds.controller.customer;

import com.qlbds.dto.user.TransactionHistoryDTO;
import com.qlbds.dto.user.UserDTO;
import com.qlbds.service.TransactionService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/customer/transaction-history")
public class TransactionHistoryController extends HttpServlet {

    private TransactionService transactionService = new TransactionService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        UserDTO currentUser = (session != null) ? (UserDTO) session.getAttribute("currentUser") : null;

        if (currentUser == null) {
            resp.sendRedirect(req.getContextPath() + "/acc/login");
            return;
        }

        int page = 1;
        int pageSize = 5; // Mỗi trang hiển thị 5 giao dịch cho gọn
        String pageParam = req.getParameter("page");
        if (pageParam != null && !pageParam.trim().isEmpty()) {
            try { page = Integer.parseInt(pageParam); } catch (NumberFormatException ignored) {}
        }

        List<TransactionHistoryDTO> txList = transactionService.getTransactionHistory(currentUser.getId(), page, pageSize);
        int totalPages = transactionService.getTotalTransactionPages(currentUser.getId(), pageSize);

        req.setAttribute("txList", txList);
        req.setAttribute("currentPage", page);
        req.setAttribute("totalPages", totalPages);

        req.getRequestDispatcher("/WEB-INF/views/customer/transaction-history.jsp").forward(req, resp);
    }
}