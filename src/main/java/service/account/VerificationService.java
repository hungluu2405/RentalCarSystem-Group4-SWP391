package service.account;

import dao.implement.NotificationDAO;
import dao.implement.UserDAO;
import model.Address;
import model.Notification;
import model.User;
import model.UserProfile;
import util.EmailUtil;
import util.VerificationCodeStore;

import java.util.Random;

public class VerificationService {

    /**
     * ✅ Gửi lại mã xác minh qua email
     */
    public static class ResendCodeService {

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
                String subject = "Mã xác minh tài khoản Rentaly của bạn";
                String body = "Mã xác minh mới của bạn là: <h2><b>" + otp + "</b></h2>"
                        + "<p>Mã này có hiệu lực trong 15 phút. Vui lòng không chia sẻ cho bất kỳ ai.</p>";

                EmailUtil.sendEmail(email, subject, body);
                return true;

            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
    }

    /**
     * ✅ Xác thực mã OTP được gửi đến email
     */
    public static class VerifyCodeService {

        /**
         * Kiểm tra mã OTP có hợp lệ và chưa hết hạn.
         */
        public boolean verifyCode(String email, String code) {
            if (email == null || email.isEmpty() || code == null || code.isEmpty()) {
                return false;
            }
            return VerificationCodeStore.validateCode(email, code);
        }
    }

    /**
     * ✅ Dịch vụ xác minh email và hoàn tất đăng ký
     */
    public static class VerifyEmailService {

        private final UserDAO userDAO;
        private final NotificationDAO notificationDAO = new NotificationDAO();

        public VerifyEmailService() {
            this.userDAO = new UserDAO();
        }

        /**
         * Kiểm tra mã xác thực OTP hợp lệ.
         */
        public boolean verifyCode(String email, String code) {
            return VerificationCodeStore.validateCode(email, code);
        }

        /**
         * Hoàn tất đăng ký tài khoản khi OTP hợp lệ.
         */
        public boolean registerAfterVerification(User user, UserProfile profile, Address address) {
            if (user == null || profile == null || address == null) {
                return false;
            }

            boolean success = userDAO.registerUser(user, profile, address);

            // 🔹 Gửi thông báo chào mừng khi đăng ký thành công
            if (success) {
                sendWelcomeNotification(user.getUserId());
            }

            return success;
        }

        /**
         * Gửi thông báo chào mừng người dùng mới.
         */
        public void sendWelcomeNotification(int userId) {
            try {
                notificationDAO.insertNotification(new Notification(
                        userId,
                        "WELCOME_VOUCHER",
                        "Chào mừng đến với Rentaly! 🎉",
                        "Xin chào! Cảm ơn bạn đã tham gia Rentaly. 🎁 Như một món quà chào mừng, bạn nhận được mã khuyến mãi <b>NEWUSER</b> giảm 15% cho lần thuê xe đầu tiên của mình!",
                        "/home"
                ));
                System.out.println("✅ Đã gửi thông báo chào mừng cho userId = " + userId);
            } catch (Exception e) {
                System.err.println("❌ Không thể gửi thông báo chào mừng: " + e.getMessage());
            }
        }
    }
}
