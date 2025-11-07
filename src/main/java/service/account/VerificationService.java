package service.account;

import dao.implement.NotificationDAO;
import dao.implement.UserDAO;
import model.Address;
import model.Notification;
import model.User;
import model.UserProfile;
import util.EmailUtil;
import java.util.Random;
import util.VerificationCodeStore;


public class VerificationService {
    public static class ResendCodeService {

        /**
         * ✅ Gửi lại mã xác minh qua email
         */
        public boolean resendVerificationCode(String email) {
            if (email == null || email.isEmpty()) {
                return false;
            }

            try {
                // Tạo mã 6 chữ số ngẫu nhiên
                String otp = String.format("%06d", new Random().nextInt(999999));

                // Lưu mã vào bộ nhớ tạm (có thời hạn)
                VerificationCodeStore.saveCode(email, otp);

                // Gửi email cho người dùng
                String subject = "Your Rentaly Verification Code";
                String body = "Your new code is: <h2><b>" + otp + "</b></h2>";

                EmailUtil.sendEmail(email, subject, body);
                return true;

            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
    }

    public static class VerifyCodeService {

        /**
         * ✅ Xác thực mã OTP gửi đến email người dùng.
         *
         * @param email email cần xác minh
         * @param code mã OTP người dùng nhập
         * @return true nếu mã hợp lệ và chưa hết hạn
         */
        public boolean verifyCode(String email, String code) {
            if (email == null || email.isEmpty() || code == null || code.isEmpty()) {
                return false;
            }
            return VerificationCodeStore.validateCode(email, code);
        }
    }

    public static class VerifyEmailService {

        private final UserDAO userDAO;
        private final NotificationDAO notificationDAO = new NotificationDAO();

        public VerifyEmailService() {
            this.userDAO = new UserDAO();
        }

        /** ✅ Kiểm tra mã xác thực OTP */
        public boolean verifyCode(String email, String code) {
            return VerificationCodeStore.validateCode(email, code);
        }

        /** ✅ Đăng ký tài khoản khi OTP hợp lệ */
        public boolean registerAfterVerification(User user, UserProfile profile, Address address) {
            if (user == null || profile == null || address == null) {
                return false;
            }

            boolean success = userDAO.registerUser(user, profile, address);

            // 🔹 Chỉ gửi thông báo khi đăng ký thành công
            if (success) {
                sendWelcomeNotification(user.getUserId());
            }

            return success;
        }

        /** ✅ Gửi thông báo chào mừng */
        public void sendWelcomeNotification(int userId) {
            try {
                notificationDAO.insertNotification(new Notification(
                        userId,
                        "WELCOME_VOUCHER",
                        "Welcome to Rentaly! 🎉",
                        "Welcome! As a new member, you receive a special voucher code: NEWUSER. Get 15% off your first booking!",
                        "/home"
                ));
                System.out.println("✅ Notification sent for userId = " + userId);
            } catch (Exception e) {
                System.err.println("❌ Failed to send welcome notification: " + e.getMessage());
            }
        }
    }
}
