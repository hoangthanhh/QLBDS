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

@WebServlet("/customer/property/transaction")
public class PropertyTransactionController extends HttpServlet {

    private TransactionService transactionService = new TransactionService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        HttpSession session = req.getSession(false);

        // CHUẨN KIẾN TRÚC: Đọc UserDTO từ Session
        UserDTO currentUser = (session != null) ? (UserDTO) session.getAttribute("currentUser") : null;
        if (currentUser == null) {
            resp.sendRedirect(req.getContextPath() + "/acc/login");
            return;
        }

        String propertyIdParam = req.getParameter("propertyId");
        String type = req.getParameter("type");

        if (currentUser.getIsVerified() == null || !currentUser.getIsVerified()) {
            session.setAttribute("updateError", "Tài khoản của bạn chưa được xác thực! Vui lòng xác thực trước khi thực hiện đặt cọc hoặc mua.");
            resp.sendRedirect(req.getContextPath() + "/customer/profile");
            return;
        }

        try {
            Integer propertyId = Integer.parseInt(propertyIdParam);
            // Gửi ID User xuống Service
            String result = transactionService.processTransaction(currentUser.getId(), propertyId, type);

            if ("SUCCESS".equals(result)) {
                String actionName = "BUY".equals(type) ? "Mua Bất động sản" : "Đặt cọc";
                // ĐÃ SỬA: Sửa lại câu thông báo do chưa gửi email ở bước này
                session.setAttribute("txSuccess", "Gửi yêu cầu " + actionName + " thành công! Vui lòng chờ bộ phận CSKH của REMS liên hệ xác nhận.");
            } else {
                session.setAttribute("txError", result);
            }
        } catch (Exception e) {
            session.setAttribute("txError", "Lỗi dữ liệu giao dịch!");
        }

        resp.sendRedirect(req.getContextPath() + "/property/detail?id=" + propertyIdParam);
    }
}