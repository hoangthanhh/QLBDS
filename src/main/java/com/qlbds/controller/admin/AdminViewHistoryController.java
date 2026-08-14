package com.qlbds.controller.admin;

import com.qlbds.dto.admin.AdminViewLogDTO;
import com.qlbds.service.ViewLogService;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/admin/view-history")
public class AdminViewHistoryController extends HttpServlet {
    private ViewLogService viewLogService = new ViewLogService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Có thể thêm phân quyền kiểm tra xem có phải ADMIN không ở đây...

        int page = 1;
        int pageSize = 10; // Admin hiển thị dạng bảng nên 10-15 dòng nhìn sẽ gọn

        String pageParam = req.getParameter("page");
        if (pageParam != null && !pageParam.trim().isEmpty()) {
            try {
                page = Integer.parseInt(pageParam);
            } catch (NumberFormatException e) { page = 1; }
        }

        List<AdminViewLogDTO> logList = viewLogService.getLogsForAdmin(page, pageSize);
        int totalPages = viewLogService.getTotalPagesForAdmin(pageSize);

        req.setAttribute("logList", logList);
        req.setAttribute("currentPage", page);
        req.setAttribute("totalPages", totalPages);

        req.getRequestDispatcher("/WEB-INF/views/admin/admin-view-history.jsp").forward(req, resp);
    }
}