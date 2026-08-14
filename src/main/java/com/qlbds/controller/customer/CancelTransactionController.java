package com.qlbds.controller.customer;

import com.qlbds.dto.user.UserDTO;
import com.qlbds.service.TransactionService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/customer/property/transaction/cancel")
public class CancelTransactionController extends HttpServlet {

    private TransactionService transactionService = new TransactionService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        UserDTO currentUser = (session != null) ? (UserDTO) session.getAttribute("currentUser") : null;

        if (currentUser == null) {
            resp.sendRedirect(req.getContextPath() + "/acc/login");
            return;
        }

        String propertyIdParam = req.getParameter("propertyId");
        try {
            Integer propertyId = Integer.parseInt(propertyIdParam);
            // Gọi Service để Hủy
            boolean success = transactionService.cancelTransaction(currentUser.getId(), propertyId);

            if (success) {
                session.setAttribute("txSuccess", "Đã hủy yêu cầu giao dịch thành công! Bạn có thể thực hiện giao dịch mới.");
            } else {
                session.setAttribute("txError", "Không tìm thấy yêu cầu nào để hủy hoặc có lỗi xảy ra.");
            }
        } catch (Exception e) {
            session.setAttribute("txError", "Lỗi dữ liệu!");
        }

        resp.sendRedirect(req.getContextPath() + "/property/detail?id=" + propertyIdParam);
    }
}