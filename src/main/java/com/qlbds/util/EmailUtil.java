package com.qlbds.util;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailUtil {

    // ĐÃ ĐỒNG BỘ: Điền tài khoản thật lên đầu file để toàn bộ các hàm bên dưới dùng chung
    private static final String SENDER_EMAIL = "thanhkuka72005@gmail.com";
    private static final String SENDER_PASSWORD = "yivd lvip oave ceps";

    // Hàm thiết lập cấu hình SMTP dùng chung cho gọn và tối ưu code
    private static Session getMailSession() {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        return Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SENDER_EMAIL, SENDER_PASSWORD);
            }
        });
    }

    // 1. HÀM GỬI MAIL GIAO DỊCH (ĐẶT CỌC / MUA)
    public static boolean sendTransactionEmail(String recipientEmail, String customerName, String propertyTitle, String type, double amount) {
        Session session = getMailSession();

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(SENDER_EMAIL, "REMS Real Estate"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));

            String subject = "BUY".equals(type) ? "[REMS] XÁC NHẬN YÊU CẦU MUA BẤT ĐỘNG SẢN" : "[REMS] XÁC NHẬN ĐẶT CỌC BẤT ĐỘNG SẢN";
            message.setSubject(subject);

            String actionText = "BUY".equals(type) ? "Yêu cầu Mua" : "Đặt cọc";
            String content = "<h3>Kính gửi " + customerName + ",</h3>"
                    + "<p>Cảm ơn bạn đã tin tưởng dịch vụ của REMS! Dưới đây là thông tin giao dịch mới nhất của bạn:</p>"
                    + "<ul>"
                    + "<li><strong>Loại giao dịch:</strong> " + actionText + "</li>"
                    + "<li><strong>Bất động sản:</strong> " + propertyTitle + "</li>"
                    + "<li><strong>Số tiền thanh toán:</strong> " + String.format("%,.0f", amount) + " VNĐ</li>"
                    + "<li><strong>Trạng thái:</strong> Đã ghi nhận hệ thống</li>"
                    + "</ul>"
                    + "<p>Chuyên viên tư vấn của REMS sẽ liên hệ trực tiếp với bạn qua số điện thoại đăng ký trong vòng 24h tới.</p>"
                    + "<br><p>Trân trọng,<br><strong>Đội ngũ REMS Real Estate</strong></p>";

            message.setContent(content, "text/html; charset=UTF-8");
            Transport.send(message);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 2. HÀM GỬI MAIL OTP KÍCH HOẠT TÀI KHOẢN
    public static boolean sendOtpEmail(String recipientEmail, String customerName, String otpCode) {
        Session session = getMailSession();

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(SENDER_EMAIL, "REMS Real Estate"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("[REMS] MÃ OTP XÁC THỰC TÀI KHOẢN");

            String content = "<h3>Chào " + customerName + ",</h3>"
                    + "<p>Bạn đã yêu cầu mã xác thực tài khoản tại REMS. Vui lòng không chia sẻ mã này cho bất kỳ ai.</p>"
                    + "<div style='font-size: 24px; font-weight: bold; color: #00B98E; letter-spacing: 5px; margin: 20px 0;'>"
                    + otpCode
                    + "</div>"
                    + "<p>Mã OTP này có hiệu lực trong vòng 5 phút.</p>"
                    + "<br><p>Trân trọng,<br>Đội ngũ REMS</p>";

            message.setContent(content, "text/html; charset=UTF-8");
            Transport.send(message);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}