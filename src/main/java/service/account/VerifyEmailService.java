package service.account;

import dao.implement.UserDAO;
import dao.implement.NotificationDAO;
import model.Address;
import model.User;
import model.UserProfile;
import model.Notification;
import util.ResetCodeStore;

public class VerifyEmailService {

    private final UserDAO userDAO;
    private final NotificationDAO notificationDAO = new NotificationDAO();

    public VerifyEmailService() {
        this.userDAO = new UserDAO();
    }

    /** ✅ Kiểm tra mã xác thực OTP */
    public boolean verifyCode(String email, String code) {
        return ResetCodeStore.validateCode(email, code);
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
    private void sendWelcomeNotification(int userId) {
        try {
            notificationDAO.insertNotification(new Notification(
                    userId,
                    "WELCOME_VOUCHER",
                    "Welcome to Rentaly! 🎉",
                    "Welcome! As a new member, you receive a special voucher code: NEWUSER. Get 10% off your first booking!",
                    "/customer/carListing"
            ));
            System.out.println("✅ Notification sent for userId = " + userId);
        } catch (Exception e) {
            System.err.println("❌ Failed to send welcome notification: " + e.getMessage());
        }
    }
}
