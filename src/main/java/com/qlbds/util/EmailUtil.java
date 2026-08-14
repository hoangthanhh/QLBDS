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
    // 1. ĐÃ SỬA: HÀM GỬI MAIL DUYỆT GIAO DỊCH THÀNH CÔNG
    public static boolean sendTransactionEmail(String recipientEmail, String customerName, String propertyTitle, String type, double amount) {
        Session session = getMailSession();

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(SENDER_EMAIL, "REMS Real Estate"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));

            String subject = "BUY".equals(type) ? "[REMS] CHÚC MỪNG: GIAO DỊCH MUA BẤT ĐỘNG SẢN THÀNH CÔNG" : "[REMS] CHÚC MỪNG: ĐẶT CỌC BẤT ĐỘNG SẢN THÀNH CÔNG";
            message.setSubject(subject);

            String actionText = "BUY".equals(type) ? "Mua BĐS" : "Đặt cọc";
            String content = "<h3>Kính gửi " + customerName + ",</h3>"
                    + "<p>Chúc mừng bạn! Yêu cầu giao dịch của bạn đã được Admin của REMS <strong>duyệt thành công</strong>. Dưới đây là thông tin chi tiết:</p>"
                    + "<ul>"
                    + "<li><strong>Loại giao dịch:</strong> " + actionText + "</li>"
                    + "<li><strong>Bất động sản:</strong> " + propertyTitle + "</li>"
                    + "<li><strong>Số tiền:</strong> " + String.format("%,.0f", amount) + " VNĐ</li>"
                    + "<li><strong>Trạng thái:</strong> <span style='color:green'>Đã xác nhận</span></li>"
                    + "</ul>"
                    + "<p>Bộ phận CSKH sẽ sớm liên hệ với bạn để tiến hành các thủ tục ký kết hợp đồng tiếp theo.</p>"
                    + "<br><p>Trân trọng,<br><strong>Đội ngũ REMS Real Estate</strong></p>";

            message.setContent(content, "text/html; charset=UTF-8");
            Transport.send(message);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 2. THÊM MỚI: HÀM GỬI MAIL TỪ CHỐI GIAO DỊCH
    public static boolean sendRejectEmail(String recipientEmail, String customerName, String propertyTitle, String reason) {
        Session session = getMailSession();

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(SENDER_EMAIL, "REMS Real Estate"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("[REMS] THÔNG BÁO TỪ CHỐI YÊU CẦU GIAO DỊCH");

            String content = "<h3>Kính gửi " + customerName + ",</h3>"
                    + "<p>Chúng tôi rất tiếc phải thông báo rằng yêu cầu giao dịch đối với bất động sản <strong>" + propertyTitle + "</strong> của bạn không thể thực hiện được.</p>"
                    + "<p><strong>Lý do từ chối:</strong> <span style='color:red'>" + reason + "</span></p>"
                    + "<p>Nếu bạn có bất kỳ thắc mắc nào, xin vui lòng liên hệ lại với bộ phận Hỗ trợ khách hàng của chúng tôi để được giải đáp chi tiết.</p>"
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