package com.qlbds.controller.customer;

import com.qlbds.entity.User;
import com.qlbds.service.TransactionService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/customer/property/transaction")
public class PropertyTransactionController extends HttpServlet {

    private TransactionService transactionService = new TransactionService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        HttpSession session = req.getSession(false);

        // BƯỚC 1: Bắt buộc Đăng nhập
        User currentUser = (session != null) ? (User) session.getAttribute("currentUser") : null;
        if (currentUser == null) {
            resp.sendRedirect(req.getContextPath() + "/acc/login");
            return;
        }

        String propertyIdParam = req.getParameter("propertyId");
        String type = req.getParameter("type"); // "DEPOSIT" hoặc "BUY"

        // BƯỚC 2: Kiểm tra Xác thực tài khoản
        if (!currentUser.getIsVerified()) {
            session.setAttribute("updateError", "Tài khoản của bạn chưa được xác thực! Vui lòng xác thực trước khi thực hiện đặt cọc hoặc mua.");
            resp.sendRedirect(req.getContextPath() + "/customer/profile");
            return;
        }

        // BƯỚC 3: Xử lý giao dịch
        try {
            Integer propertyId = Integer.parseInt(propertyIdParam);
            String result = transactionService.processTransaction(currentUser, propertyId, type);

            if ("SUCCESS".equals(result)) {
                String actionName = "BUY".equals(type) ? "Mua Bất động sản" : "Đặt cọc";
                session.setAttribute("txSuccess", "Tạo yêu cầu " + actionName + " thành công! Email xác nhận đã được gửi tới " + currentUser.getEmail());
            } else {
                session.setAttribute("txError", result);
            }
        } catch (Exception e) {
            session.setAttribute("txError", "Lỗi dữ liệu giao dịch!");
        }

        resp.sendRedirect(req.getContextPath() + "/property/detail?id=" + propertyIdParam);
    }
}