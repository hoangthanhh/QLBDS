package com.qlbds.controller.acc;

import com.qlbds.entity.User;
import com.qlbds.service.OtpService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/acc/resend-otp")
public class ResendOtpController extends HttpServlet {

    private OtpService otpService = new OtpService();

    // Hứng sự kiện từ nút "Bấm vào đây để lấy mã OTP" trên giao diện JSP
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        HttpSession session = req.getSession();
        User currentUser = (User) session.getAttribute("currentUser");

        // Gọi luồng nghiệp vụ Service để check giới hạn 5 lần/ngày và gửi mail ngầm
        String result = otpService.generateAndSendOtp(currentUser);

        if ("SUCCESS".equals(result)) {
            req.setAttribute("message", "Mã xác thực OTP đã được gửi thành công vào Hộp thư đến của bạn!");
        } else {
            req.setAttribute("error", result);
        }

        req.getRequestDispatcher("/WEB-INF/views/acc/verify-otp.jsp").forward(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.sendRedirect(req.getContextPath() + "/acc/verify-otp");
    }
}