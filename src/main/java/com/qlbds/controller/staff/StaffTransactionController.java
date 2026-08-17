package com.qlbds.controller.staff;

import com.qlbds.dto.admin.AdminTransactionDTO;
import com.qlbds.service.TransactionService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/staff/transactions")
public class StaffTransactionController extends HttpServlet {

    private final TransactionService transactionService = new TransactionService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int page = 1;
        int pageSize = 10;

        String keyword = req.getParameter("keyword");
        String startDate = req.getParameter("startDate");
        String endDate = req.getParameter("endDate");
        String statusFilter = req.getParameter("status");
        if (statusFilter == null || statusFilter.trim().isEmpty()) {
            statusFilter = "ALL";
        }

        String pageParam = req.getParameter("page");
        if (pageParam != null && !pageParam.trim().isEmpty()) {
            try { page = Integer.parseInt(pageParam); } catch (NumberFormatException ignored) {}
        }

        // Gọi đúng tên hàm getManagementTransactions và getTotalManagementPages
        List<AdminTransactionDTO> txList = transactionService.getManagementTransactions(keyword, startDate, endDate, statusFilter, page, pageSize);
        int totalPages = transactionService.getTotalManagementPages(keyword, startDate, endDate, statusFilter, pageSize);

        req.setAttribute("txList", txList);
        req.setAttribute("currentPage", page);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("keyword", keyword);
        req.setAttribute("startDate", startDate);
        req.setAttribute("endDate", endDate);
        req.setAttribute("currentStatus", statusFilter);

        req.getRequestDispatcher("/WEB-INF/views/staff/staffTransactions.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");
        String idParam = req.getParameter("id");

        try {
            Integer txId = Integer.parseInt(idParam);
            String result = "";

            if ("APPROVE".equals(action)) {
                result = transactionService.approveTransaction(txId);
                if ("SUCCESS".equals(result)) {
                    req.getSession().setAttribute("msgSuccess", "Đã duyệt giao dịch thành công và gửi Email xác nhận bằng JavaMail API!");
                } else {
                    req.getSession().setAttribute("msgError", result);
                }
            } else if ("REJECT".equals(action)) {
                String reason = req.getParameter("rejectReason");
                if (reason == null || reason.trim().isEmpty()) {
                    req.getSession().setAttribute("msgError", "Vui lòng nhập lý do từ chối giao dịch!");
                } else {
                    result = transactionService.rejectTransaction(txId, reason);
                    if ("SUCCESS".equals(result)) {
                        req.getSession().setAttribute("msgSuccess", "Đã từ chối giao dịch và gửi Email thông báo!");
                    } else {
                        req.getSession().setAttribute("msgError", result);
                    }
                }
            }
        } catch (Exception e) {
            req.getSession().setAttribute("msgError", "Dữ liệu yêu cầu không hợp lệ!");
        }

        resp.sendRedirect(req.getContextPath() + "/staff/transactions");
    }
}