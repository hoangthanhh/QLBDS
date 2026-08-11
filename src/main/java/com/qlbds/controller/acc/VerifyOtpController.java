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

@WebServlet("/acc/verify-otp")
public class VerifyOtpController extends HttpServlet {

    private OtpService otpService = new OtpService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User currentUser = (User) session.getAttribute("currentUser");

        // Chưa đăng nhập thì về trang login
        if (currentUser == null) {
            resp.sendRedirect(req.getContextPath() + "/acc/login");
            return;
        }

        // Đã xác thực rồi thì về trang chủ
        if (currentUser.getIsVerified()) {
            resp.sendRedirect(req.getContextPath() + "/home");
            return;
        }

        // CHUẨN NGHIỆP VỤ: Không làm gì cả, chỉ chuyển tiếp giao diện lên cho người dùng thấy
        req.getRequestDispatcher("/WEB-INF/views/acc/verify-otp.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        HttpSession session = req.getSession();
        User currentUser = (User) session.getAttribute("currentUser");

        String inputOtp = req.getParameter("otpCode");
        String result = otpService.verifyUserOtp(currentUser, inputOtp);

        if ("SUCCESS".equals(result)) {
            session.setAttribute("currentUser", currentUser);

            // Thay đổi đường dẫn "/acc/profile" tương ứng với URL Servlet hiển thị trang profile.jsp của bạn
            resp.sendRedirect(req.getContextPath() + "/customer/profile");
            return; // Kết thúc hàm luôn, không forward về trang OTP nữa
        } else {
            req.setAttribute("error", result);
        }
        req.getRequestDispatcher("/WEB-INF/views/acc/verify-otp.jsp").forward(req, resp);
    }
}