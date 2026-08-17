package com.qlbds.controller.admin;

import com.qlbds.dto.admin.AdminUserDTO;
import com.qlbds.dto.user.UserDTO;
import com.qlbds.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "AdminUserController", value = {"/admin/account", "/admin/user"})
public class AdminUserController extends HttpServlet {

    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int page = 1;
        try {
            page = Math.max(1, Integer.parseInt(req.getParameter("page")));
        } catch (Exception ignored) {
        }

        String keyword = req.getParameter("keyword");
        int pageSize = 5;

        List<UserDTO> userList = userService.getUserList(keyword, page, pageSize);
        int totalPages = userService.getTotalPages(keyword, pageSize);

        req.setAttribute("userList", userList);
        req.setAttribute("currentPage", page);
        req.setAttribute("pageSize", pageSize);
        req.setAttribute("totalPage", totalPages > 0 ? totalPages : 1);
        req.setAttribute("keyword", keyword);

        req.getRequestDispatcher("/WEB-INF/views/admin/adminAccount.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");
        if (action == null) action = "";
        HttpSession session = req.getSession();

        // 1. AJAX: THÊM MỚI TÀI KHOẢN
        if ("add-user".equals(action)) {
            AdminUserDTO.Create dto = new AdminUserDTO.Create();
            dto.setFullName(req.getParameter("fullName"));
            dto.setEmail(req.getParameter("email"));
            dto.setPhone(req.getParameter("phone"));
            dto.setPassword(req.getParameter("password"));
            dto.setRole(req.getParameter("role"));

            List<String> errors = userService.addAdminUser(dto);
            resp.setContentType("application/json;charset=UTF-8");
            PrintWriter out = resp.getWriter();
            out.print("{\"success\":" + errors.isEmpty() + ",\"errors\":" + formatErrors(errors) + "}");
            return;
        }

        // 2. AJAX: SỬA TÀI KHOẢN
        if ("edit-user".equals(action)) {
            AdminUserDTO.Update dto = new AdminUserDTO.Update();
            try {
                dto.setId(Integer.parseInt(req.getParameter("id")));
            } catch (Exception ignored) {
            }
            dto.setFullName(req.getParameter("fullName"));
            dto.setEmail(req.getParameter("email"));
            dto.setPhone(req.getParameter("phone"));
            dto.setRole(req.getParameter("role"));

            List<String> errors = userService.editUser(dto);
            resp.setContentType("application/json;charset=UTF-8");
            PrintWriter out = resp.getWriter();
            out.print("{\"success\":" + errors.isEmpty() + ",\"errors\":" + formatErrors(errors) + "}");
            return;
        }

        // 3. AJAX: ĐỔI MẬT KHẨU
        if ("change-password".equals(action)) {
            int id = 0;
            try {
                id = Integer.parseInt(req.getParameter("id"));
            } catch (Exception ignored) {
            }
            List<String> errors = userService.changePasswordAsAdmin(id, req.getParameter("newPassword"), req.getParameter("confirmPassword"));

            resp.setContentType("application/json;charset=UTF-8");
            PrintWriter out = resp.getWriter();
            out.print("{\"success\":" + errors.isEmpty() + ",\"errors\":" + formatErrors(errors) + "}");
            return;
        }

        // 4. FORM SUBMIT TRỰC TIẾP TRÊN BẢNG (ĐỔI ROLE / KHÓA TÀI KHOẢN)
        try {
            int id = Integer.parseInt(req.getParameter("id"));
            if ("change-role".equals(action)) {
                String res = userService.changeUserRole(id, req.getParameter("role"));
                if ("SUCCESS".equals(res)) {
                    session.setAttribute("msg", "Thay đổi vai trò tài khoản thành công!");
                    session.setAttribute("msgType", "success");
                } else {
                    session.setAttribute("msg", res);
                    session.setAttribute("msgType", "danger");
                }
            } else if ("toggle-status".equals(action)) {
                String res = userService.toggleUserStatus(id);
                if ("SUCCESS".equals(res)) {
                    session.setAttribute("msg", "Cập nhật trạng thái tài khoản thành công!");
                    session.setAttribute("msgType", "success");
                } else {
                    session.setAttribute("msg", res);
                    session.setAttribute("msgType", "danger");
                }
            }
        } catch (Exception e) {
            session.setAttribute("msg", "Dữ liệu không hợp lệ!");
            session.setAttribute("msgType", "danger");
        }

        resp.sendRedirect(req.getContextPath() + "/admin/user");
    }

    private String formatErrors(List<String> errors) {
        if (errors == null || errors.isEmpty()) return "[]";
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < errors.size(); i++) {
            sb.append("\"").append(errors.get(i).replace("\"", "\\\"")).append("\"");
            if (i < errors.size() - 1) sb.append(",");
        }
        return sb.append("]").toString();
    }
}