package com.qlbds.controller.customer;

import com.qlbds.dto.user.UserDTO;
import com.qlbds.dto.user.ViewHistoryDTO;
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

        // ĐÃ THÊM: Xử lý đọc tham số trang hiện tại công thức phân trang
        int page = 1;
        int pageSize = 6; // Đặt hiển thị mỗi trang 6 bài viết lịch sử cho đẹp giao diện

        String pageParam = req.getParameter("page");
        if (pageParam != null && !pageParam.trim().isEmpty()) {
            try {
                page = Integer.parseInt(pageParam);
                if (page < 1) page = 1;
            } catch (NumberFormatException e) {
                page = 1;
            }
        }

        // Đã sửa: Truyền tham số phân trang vào service
        List<ViewHistoryDTO> historyList = viewLogService.getViewHistory(currentUser.getId(), page, pageSize);
        int totalPages = viewLogService.getTotalPages(currentUser.getId(), pageSize);

        req.setAttribute("historyList", historyList);
        req.setAttribute("currentPage", page);
        req.setAttribute("totalPages", totalPages);

        req.getRequestDispatcher("/WEB-INF/views/customer/view-history.jsp").forward(req, resp);
    }
}