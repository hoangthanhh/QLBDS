package com.qlbds.controller.admin;

import com.qlbds.dto.admin.AdminTransactionDTO;
import com.qlbds.service.TransactionService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/admin/transactions")
public class AdminTransactionController extends HttpServlet {

    private TransactionService transactionService = new TransactionService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int page = 1;
        int pageSize = 10;

        // Lấy tham số bộ lọc
        String statusFilter = req.getParameter("status");
        if (statusFilter == null || statusFilter.trim().isEmpty()) {
            statusFilter = "ALL";
        }

        String pageParam = req.getParameter("page");
        if (pageParam != null && !pageParam.trim().isEmpty()) {
            try { page = Integer.parseInt(pageParam); } catch (NumberFormatException ignored) {}
        }

        // Truyền thêm statusFilter
        List<AdminTransactionDTO> txList = transactionService.getTransactionsForAdmin(statusFilter, page, pageSize);
        int totalPages = transactionService.getTotalPagesForAdmin(statusFilter, pageSize);

        req.setAttribute("txList", txList);
        req.setAttribute("currentPage", page);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("currentStatus", statusFilter); // Đẩy lại lên giao diện để giữ trạng thái thẻ select

        req.getRequestDispatcher("/WEB-INF/views/admin/admin-transactions.jsp").forward(req, resp);
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
                if ("SUCCESS".equals(result)) req.getSession().setAttribute("msgSuccess", "Đã duyệt giao dịch và gửi Email cho khách hàng!");
                else req.getSession().setAttribute("msgError", result);

            } else if ("REJECT".equals(action)) {
                String reason = req.getParameter("rejectReason");
                if (reason == null || reason.trim().isEmpty()) {
                    req.getSession().setAttribute("msgError", "Vui lòng nhập lý do từ chối!");
                } else {
                    result = transactionService.rejectTransaction(txId, reason);
                    if ("SUCCESS".equals(result)) req.getSession().setAttribute("msgSuccess", "Đã từ chối giao dịch!");
                    else req.getSession().setAttribute("msgError", result);
                }
            }
        } catch (Exception e) {
            req.getSession().setAttribute("msgError", "Lỗi dữ liệu đầu vào!");
        }

        resp.sendRedirect(req.getContextPath() + "/admin/transactions");
    }
}