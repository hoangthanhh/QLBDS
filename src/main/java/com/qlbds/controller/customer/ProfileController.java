package com.qlbds.controller.customer;

import com.qlbds.entity.User;
import com.qlbds.service.UserService;
import com.qlbds.util.SecurityUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(urlPatterns = {
        "/customer/profile",
        "/customer/profile/update",
        "/customer/change-password"
})
public class ProfileController extends HttpServlet {

    private UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("currentUser") == null) {
            response.sendRedirect(request.getContextPath() + "/acc/login");
            return;
        }

        request.getRequestDispatcher("/WEB-INF/views/customer/profile.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        HttpSession httpSession = request.getSession(false);
        User currentUser = (httpSession != null) ? (User) httpSession.getAttribute("currentUser") : null;

        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/acc/login");
            return;
        }

        String path = request.getServletPath();

        if ("/customer/profile/update".equals(path)) {
            handleUpdateProfile(request, response, httpSession, currentUser);
        } else if ("/customer/change-password".equals(path)) {
            handleChangePassword(request, response, httpSession, currentUser);
        } else {
            response.sendRedirect(request.getContextPath() + "/customer/profile");
        }
    }

    // XỬ LÝ CẬP NHẬT THÔNG TIN HỒ SƠ
    private void handleUpdateProfile(HttpServletRequest request, HttpServletResponse response,
                                     HttpSession httpSession, User currentUser) throws IOException {
        String fullName = request.getParameter("fullName");
        String phone = request.getParameter("phone");

        String result = userService.updateProfile(currentUser.getId(), fullName, phone);

        if ("SUCCESS".equals(result)) {
            currentUser.setFullName(fullName.trim());
            currentUser.setPhone(phone.trim());
            httpSession.setAttribute("currentUser", currentUser);
            httpSession.setAttribute("updateSuccess", "Cập nhật thông tin cá nhân thành công!");
        } else {
            httpSession.setAttribute("updateError", result);
        }

        response.sendRedirect(request.getContextPath() + "/customer/profile");
    }

    // XỬ LÝ ĐỔI MẬT KHẨU QUA AJAX
    private void handleChangePassword(HttpServletRequest request, HttpServletResponse response,
                                      HttpSession httpSession, User currentUser) throws IOException {

        response.setContentType("application/json;charset=UTF-8");

        String oldPassword = request.getParameter("oldPassword");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        String result = userService.changePassword(
                currentUser.getId(),
                oldPassword != null ? oldPassword : "",
                newPassword != null ? newPassword : "",
                confirmPassword != null ? confirmPassword : ""
        );

        if ("SUCCESS".equals(result)) {
            // Cập nhật lại mật khẩu trong Session
            currentUser.setPassword(SecurityUtil.hashPassword(newPassword));
            httpSession.setAttribute("currentUser", currentUser);

            // Trả về kết quả JSON thành công
            response.getWriter().write("{\"status\":\"SUCCESS\", \"message\":\"Đổi mật khẩu thành công!\"}");
        } else {
            // Trả về kết quả JSON kèm câu báo lỗi chi tiết
            response.getWriter().write("{\"status\":\"ERROR\", \"message\":\"" + result + "\"}");
        }
    }
}