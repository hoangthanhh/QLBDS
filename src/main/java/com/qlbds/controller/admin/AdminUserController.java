package com.qlbds.controller.admin;

import com.qlbds.dto.user.UserDTO;
import com.qlbds.dto.user.UserCreateDTO;
import com.qlbds.dto.user.UserUpdateDTO;
import com.qlbds.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "AdminUserController", value = {"/admin/account", "/admin/user"})
public class AdminUserController extends HttpServlet {

    private UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int page = 1;
        int pageSize = 5;

        String pageParam = request.getParameter("page");
        if (pageParam != null && !pageParam.trim().isEmpty()) {
            try {
                page = Integer.parseInt(pageParam);
            } catch (NumberFormatException ignored) {}
        }

        // ĐỌC THAM SỐ TÌM KIẾM
        String keyword = request.getParameter("keyword");

        List<UserDTO> userList = userService.getUserList(keyword, page, pageSize);
        int totalPage = userService.getTotalPages(keyword, pageSize);

        request.setAttribute("userList", userList);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPage", totalPage);

        request.getRequestDispatcher("/WEB-INF/views/admin/adminAccount.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        HttpSession session = request.getSession();

        if ("add-user".equals(action) || "edit-user".equals(action) || "change-password".equals(action)) {
            List<String> errors = null;

            if ("add-user".equals(action)) {
                // SỬ DỤNG DTO CHUYÊN BIỆT CHO THÊM MỚI
                UserCreateDTO dto = new UserCreateDTO();
                dto.setFullName(request.getParameter("fullName"));
                dto.setEmail(request.getParameter("email"));
                dto.setPhone(request.getParameter("phone"));
                dto.setPassword(request.getParameter("password"));
                dto.setRole(request.getParameter("role"));

                errors = userService.addAdminUser(dto);
            }
            else if ("edit-user".equals(action)) {
                // SỬ DỤNG DTO CHUYÊN BIỆT CHO CHỈNH SỬA
                UserUpdateDTO dto = new UserUpdateDTO();
                try {
                    dto.setId(Integer.parseInt(request.getParameter("id")));
                } catch (Exception ignored) {}
                dto.setFullName(request.getParameter("fullName"));
                dto.setEmail(request.getParameter("email"));
                dto.setPhone(request.getParameter("phone"));
                dto.setRole(request.getParameter("role"));

                errors = userService.editUser(dto);
            }
            else if ("change-password".equals(action)) {
                int id = 0;
                try {
                    id = Integer.parseInt(request.getParameter("id"));
                } catch (Exception ignored) {}
                String newPass = request.getParameter("newPassword");
                String confirmPass = request.getParameter("confirmPassword");

                errors = userService.changePasswordAsAdmin(id, newPass, confirmPass);
            }

            boolean isSuccess = (errors != null && errors.isEmpty());

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            StringBuilder jsonErrors = new StringBuilder("[");
            if (errors != null) {
                for (int i = 0; i < errors.size(); i++) {
                    jsonErrors.append("\"").append(errors.get(i).replace("\"", "\\\"")).append("\"");
                    if (i < errors.size() - 1) jsonErrors.append(",");
                }
            }
            jsonErrors.append("]");

            String jsonResponse = "{\"success\":" + isSuccess + ", \"errors\":" + jsonErrors.toString() + "}";
            response.getWriter().write(jsonResponse);
            return;
        }

        if ("change-role".equals(action)) {
            try {
                int id = Integer.parseInt(request.getParameter("id"));
                String role = request.getParameter("role");
                String res = userService.changeUserRole(id, role);

                session.setAttribute("msg", "SUCCESS".equals(res) ? "Thay đổi vai trò thành công!" : res);
                session.setAttribute("msgType", "SUCCESS".equals(res) ? "success" : "error");
            } catch (Exception e) {
                session.setAttribute("msg", "Dữ liệu không hợp lệ!");
                session.setAttribute("msgType", "error");
            }
        }
        else if ("toggle-status".equals(action)) {
            try {
                int id = Integer.parseInt(request.getParameter("id"));
                String res = userService.toggleUserStatus(id);

                session.setAttribute("msg", "SUCCESS".equals(res) ? "Thực hiện khóa/mở khóa tài khoản thành công!" : res);
                session.setAttribute("msgType", "SUCCESS".equals(res) ? "success" : "error");
            } catch (Exception e) {
                session.setAttribute("msg", "Không thể thực hiện hành động này!");
                session.setAttribute("msgType", "error");
            }
        }

        response.sendRedirect(request.getContextPath() + "/admin/user");
    }
}