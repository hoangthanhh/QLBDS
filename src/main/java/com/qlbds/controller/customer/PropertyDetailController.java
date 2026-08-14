package com.qlbds.controller.customer;

import com.qlbds.dto.property.PropertyDetailDTO;
import com.qlbds.dto.user.UserDTO;
import com.qlbds.service.PropertyService;
import com.qlbds.service.ViewLogService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/property/detail")
public class PropertyDetailController extends HttpServlet {

    private PropertyService propertyService = new PropertyService();
    private ViewLogService viewLogService = new ViewLogService(); // Khởi tạo Service

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idParam = req.getParameter("id");
        if (idParam == null || idParam.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/home");
            return;
        }

        // Trong hàm doGet, khối try-catch thay thế thành:
        try {
            Integer id = Integer.parseInt(idParam);
            // CHUẨN KIẾN TRÚC: Gọi Service lấy thẳng DTO Detail
            PropertyDetailDTO property = propertyService.getPropertyDetail(id);

            if (property == null) {
                resp.sendRedirect(req.getContextPath() + "/home");
                return;
            }

            HttpSession session = req.getSession(false);
            if (session != null && session.getAttribute("currentUser") != null) {
                UserDTO currentUser = (UserDTO) session.getAttribute("currentUser");
                if ("CUSTOMER".equals(currentUser.getRole())) {
                    viewLogService.logPropertyView(currentUser.getId(), id);
                }
            }

            req.setAttribute("property", property);
            // Kiểm tra xem khách hàng có đang Pending giao dịch này không
            boolean hasPendingTx = false;
            if (session != null && session.getAttribute("currentUser") != null) {
                UserDTO currentUser =  (UserDTO) session.getAttribute("currentUser");
                // Cần khởi tạo TransactionRepository ở đầu class controller để dùng hàm này
                com.qlbds.repository.TransactionRepository txRepo = new com.qlbds.repository.TransactionRepository();
                hasPendingTx = txRepo.hasPendingTransaction(currentUser.getId(), id);
            }
            // Truyền cờ này sang JSP
            req.setAttribute("hasPendingTx", hasPendingTx);
            req.getRequestDispatcher("/WEB-INF/views/customer/property-detail.jsp").forward(req, resp);
        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/home");
        }
    }
}