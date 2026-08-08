package com.qlbds.controller.admin;

import com.qlbds.constant.RoleTypeEnum;
import com.qlbds.constant.UserStatusEnum;
import com.qlbds.dto.UserDTO;
import com.qlbds.entity.User;
import com.qlbds.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {
        "/admin-accounts",
        "/toggle-status",
        "/change-role",
        "/delete-account",
        "/edit-account",
        "/change-password",
        "/add-account"
})
public class AdminAccountController extends HttpServlet {

    private UserService userService = new UserService();
    private static final int PAGE_SIZE = 5;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getServletPath();
        User currentAdmin = (User) req.getSession().getAttribute("currentUser");

        if ("/toggle-status".equals(action)) {
            try {
                int id = Integer.parseInt(req.getParameter("id"));
                if (currentAdmin != null && currentAdmin.getId() == id) {
                    req.getSession().setAttribute("msg", "Lỗi: Bạn không thể tự khóa tài khoản của chính mình!");
                    req.getSession().setAttribute("msgType", "danger");
                } else {
                    User user = userService.getUserById(id);
                    if (user != null) {
                        user.setStatus(user.getStatus() == UserStatusEnum.ACTIVE ? UserStatusEnum.INACTIVE : UserStatusEnum.ACTIVE);
                        userService.updateUser(user);
                        req.getSession().setAttribute("msg", "Đã cập nhật trạng thái tài khoản!");
                        req.getSession().setAttribute("msgType", "success");
                    }
                }
            } catch (NumberFormatException e) {
                req.getSession().setAttribute("msg", "Tham số ID không hợp lệ!");
                req.getSession().setAttribute("msgType", "danger");
            }
            resp.sendRedirect(req.getContextPath() + "/admin-accounts");

        } else if ("/edit-account".equals(action)) {
            try {
                int id = Integer.parseInt(req.getParameter("id"));
                User user = userService.getUserById(id);
                req.setAttribute("userEdit", user);
                req.getRequestDispatcher("/WEB-INF/views/admin/modalEditAccount.jsp").forward(req, resp);
            } catch (Exception e) {
                resp.sendRedirect(req.getContextPath() + "/admin-accounts");
            }

        } else if ("/change-password".equals(action)) {
            try {
                int id = Integer.parseInt(req.getParameter("id"));
                req.setAttribute("userId", id);
                req.getRequestDispatcher("/WEB-INF/views/admin/modalChangePassword.jsp").forward(req, resp);
            } catch (Exception e) {
                resp.sendRedirect(req.getContextPath() + "/admin-accounts");
            }

        } else {
            // HIỂN THỊ DANH SÁCH (MẶC ĐỊNH)
            int currentPage = 1;
            try {
                String pageParam = req.getParameter("page");
                if (pageParam != null && !pageParam.isEmpty()) {
                    currentPage = Integer.parseInt(pageParam);
                    if (currentPage < 1) currentPage = 1;
                }
            } catch (NumberFormatException e) {
                currentPage = 1;
            }

            int offset = (currentPage - 1) * PAGE_SIZE;
            long totalRecords = userService.countTotalUsers();
            int totalPage = (int) Math.ceil((double) totalRecords / PAGE_SIZE);

            if (currentPage > totalPage && totalPage > 0) {
                currentPage = totalPage;
                offset = (currentPage - 1) * PAGE_SIZE;
            }

            List<User> accountList = userService.getUsersWithPagination(offset, PAGE_SIZE);

            req.setAttribute("accountList", accountList);
            req.setAttribute("currentPage", currentPage);
            req.setAttribute("totalPage", totalPage);
            req.setAttribute("pageSize", PAGE_SIZE);

            req.getRequestDispatcher("/WEB-INF/views/admin/adminAccount.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getServletPath();
        User currentAdmin = (User) req.getSession().getAttribute("currentUser");

        try {
            if ("/add-account".equals(action)) {
                // ĐÓNG GÓI DỮ LIỆU TỪ FORM VÀO DTO
                UserDTO dto = new UserDTO(
                        req.getParameter("fullName"),
                        req.getParameter("phone"),
                        req.getParameter("email"),
                        req.getParameter("password"),
                        req.getParameter("role")
                );

                // GỌI SERVICE XỬ LÝ
                String result = userService.createAccountByAdmin(dto);
                if ("SUCCESS".equals(result)) {
                    req.getSession().setAttribute("msg", "Tạo tài khoản thành công!");
                    req.getSession().setAttribute("msgType", "success");
                } else {
                    req.getSession().setAttribute("msg", result);
                    req.getSession().setAttribute("msgType", "danger");
                }
            } else {
                int id = Integer.parseInt(req.getParameter("id"));

                if ("/change-role".equals(action)) {
                    if (currentAdmin != null && currentAdmin.getId() == id) {
                        req.getSession().setAttribute("msg", "Lỗi: Không thể tự thay đổi phân quyền của chính mình!");
                        req.getSession().setAttribute("msgType", "danger");
                    } else {
                        String newRole = req.getParameter("role");
                        User user = userService.getUserById(id);
                        if (user != null) {
                            user.setRole(RoleTypeEnum.valueOf(newRole));
                            userService.updateUser(user);
                            req.getSession().setAttribute("msg", "Cập nhật phân quyền thành công!");
                            req.getSession().setAttribute("msgType", "success");
                        }
                    }
                } else if ("/delete-account".equals(action)) {
                    if (currentAdmin != null && currentAdmin.getId() == id) {
                        req.getSession().setAttribute("msg", "Lỗi: Bạn không thể tự xóa tài khoản của chính mình!");
                        req.getSession().setAttribute("msgType", "danger");
                    } else {
                        boolean success = userService.deleteUser(id);
                        if (success) {
                            req.getSession().setAttribute("msg", "Đã xóa tài khoản thành công!");
                            req.getSession().setAttribute("msgType", "success");
                        } else {
                            req.getSession().setAttribute("msg", "Lỗi: Không thể xóa tài khoản này!");
                            req.getSession().setAttribute("msgType", "danger");
                        }
                    }
                } else if ("/edit-account".equals(action)) {
                    String fullName = req.getParameter("fullName");
                    String phone = req.getParameter("phone");

                    User user = userService.getUserById(id);
                    if (user != null) {
                        user.setFullName(fullName);
                        user.setPhone(phone);
                        userService.updateUser(user);
                        req.getSession().setAttribute("msg", "Cập nhật thông tin thành công!");
                        req.getSession().setAttribute("msgType", "success");
                    }
                } else if ("/change-password".equals(action)) {
                    String newPassword = req.getParameter("newPassword");
                    boolean success = userService.changePassword(id, newPassword);
                    if (success) {
                        req.getSession().setAttribute("msg", "Đổi mật khẩu thành công!");
                        req.getSession().setAttribute("msgType", "success");
                    } else {
                        req.getSession().setAttribute("msg", "Đổi mật khẩu thất bại!");
                        req.getSession().setAttribute("msgType", "danger");
                    }
                }
            }
        } catch (Exception e) {
            req.getSession().setAttribute("msg", "Lỗi thao tác dữ liệu!");
            req.getSession().setAttribute("msgType", "danger");
        }

        resp.sendRedirect(req.getContextPath() + "/admin-accounts");
    }
}