package com.qlbds.controller.customer;

import com.qlbds.dto.user.UserDTO;
import com.qlbds.dto.user.ChangePasswordDTO;
import com.qlbds.dto.user.UserProfileDTO;
import com.qlbds.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

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

        // Đã sửa: Ép kiểu sang UserDTO từ Session
        UserDTO currentUser = (httpSession != null) ? (UserDTO) httpSession.getAttribute("currentUser") : null;

        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/acc/login");
            return;
        }

        String path = request.getServletPath();
        if ("/customer/profile/update".equals(path)) {
            handleUpdateProfile(request, response, httpSession, currentUser);
        } else if ("/customer/change-password".equals(path)) {
            handleChangePassword(request, response, currentUser);
        } else {
            response.sendRedirect(request.getContextPath() + "/customer/profile");
        }
    }

    private void handleUpdateProfile(HttpServletRequest request, HttpServletResponse response,
                                     HttpSession httpSession, UserDTO currentUser) throws IOException {

        // Đóng gói request vào UserProfileDTO
        UserProfileDTO profileDTO = new UserProfileDTO(
                request.getParameter("fullName"),
                request.getParameter("phone")
        );

        String result = userService.updateProfile(currentUser.getId(), profileDTO);

        if ("SUCCESS".equals(result)) {
            // Cập nhật lại thông tin mới lên UserDTO trong Session
            currentUser.setFullName(profileDTO.getFullName().trim());
            currentUser.setPhone(profileDTO.getPhone().trim());
            httpSession.setAttribute("currentUser", currentUser);
            httpSession.setAttribute("updateSuccess", "Cập nhật thông tin cá nhân thành công!");
        } else {
            httpSession.setAttribute("updateError", result);
        }
        response.sendRedirect(request.getContextPath() + "/customer/profile");
    }

    private void handleChangePassword(HttpServletRequest request, HttpServletResponse response,
                                      UserDTO currentUser) throws IOException {

        response.setContentType("application/json;charset=UTF-8");

        // Đóng gói request vào ChangePasswordDTO
        ChangePasswordDTO changePasswordDTO = new ChangePasswordDTO(
                request.getParameter("oldPassword"),
                request.getParameter("newPassword"),
                request.getParameter("confirmPassword")
        );

        List<String> errors = userService.changePasswordAsUser(currentUser.getId(), changePasswordDTO);

        if (errors == null || errors.isEmpty()) {
            response.getWriter().write("{\"status\":\"SUCCESS\", \"message\":\"Đổi mật khẩu thành công!\"}");
        } else {
            response.getWriter().write("{\"status\":\"ERROR\", \"message\":\"" + errors.get(0) + "\"}");
        }
    }
}