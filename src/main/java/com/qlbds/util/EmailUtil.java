package com.qlbds.util;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailUtil {

    // Cấu hình tài khoản Gmail gửi đi (Bạn thay bằng Email & App Password của bạn)
    private static final String SENDER_EMAIL = "your-email@gmail.com";
    private static final String SENDER_PASSWORD = "your-app-password"; // Mật khẩu ứng dụng 16 ký tự của Gmail

    public static boolean sendTransactionEmail(String recipientEmail, String customerName, String propertyTitle, String type, double amount) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SENDER_EMAIL, SENDER_PASSWORD);
            }
        });

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
}