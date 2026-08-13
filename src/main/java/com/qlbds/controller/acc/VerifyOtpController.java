package com.qlbds.controller.acc;

import com.qlbds.dto.user.UserDTO;
import com.qlbds.dto.acc.VerifyOtpDTO;
import com.qlbds.service.OtpService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/acc/verify-otp")
public class VerifyOtpController extends HttpServlet {

    private OtpService otpService = new OtpService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        UserDTO currentUser = (UserDTO) session.getAttribute("currentUser"); // Đọc UserDTO từ Session

        if (currentUser == null) {
            resp.sendRedirect(req.getContextPath() + "/acc/login");
            return;
        }

        // Đã xác thực rồi thì về trang chủ
        if (currentUser.getIsVerified() != null && currentUser.getIsVerified()) {
            resp.sendRedirect(req.getContextPath() + "/home");
            return;
        }

        req.getRequestDispatcher("/WEB-INF/views/acc/verify-otp.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        HttpSession session = req.getSession();
        UserDTO currentUser = (UserDTO) session.getAttribute("currentUser"); // Đọc UserDTO từ Session

        // Đóng gói tham số vào VerifyOtpDTO
        VerifyOtpDTO verifyOtpDTO = new VerifyOtpDTO(req.getParameter("otpCode"));

        // Gọi Service xử lý nghiệp vụ với DTO
        String result = otpService.verifyUserOtp(currentUser, verifyOtpDTO);

        if ("SUCCESS".equals(result)) {
            // Đồng bộ lại cờ isVerified trên Session DTO để tránh bị bộ lọc chặn
            currentUser.setIsVerified(true);
            session.setAttribute("currentUser", currentUser);

            resp.sendRedirect(req.getContextPath() + "/customer/profile");
            return;
        } else {
            req.setAttribute("error", result);
        }
        req.getRequestDispatcher("/WEB-INF/views/acc/verify-otp.jsp").forward(req, resp);
    }
}