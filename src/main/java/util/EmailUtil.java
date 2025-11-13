package util;

import java.util.Properties;
import jakarta.mail.*;
import jakarta.mail.internet.*;

public class EmailUtil {

    public static void sendEmail(String to, String subject, String body) {

        final String from = "systemrentalcar@gmail.com";
        final String password = "dxfwzbofmfefwpnb"; // App password

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);

            // ✔ From với UTF-8 để không lỗi tên tiếng Việt
            message.setFrom(new InternetAddress(from, "Rentaly Support", "UTF-8"));

            // ✔ To
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));

            // ✔ Subject UTF-8
            message.setSubject(subject, "UTF-8");

            // ✔ Nội dung email UTF-8 (HTML)
            message.setContent(body, "text/html; charset=UTF-8");

            Transport.send(message);
            System.out.println("Email sent successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
