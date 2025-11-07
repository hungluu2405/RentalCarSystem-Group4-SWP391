package service.account;

import dao.implement.UserDAO;
import model.User;
import util.EmailUtil;
import java.util.Random;
import util.VerificationCodeStore;



public class RecoveryService {
    public static class ChangePasswordService {

        private final UserDAO userDAO;

        public ChangePasswordService() {
            this.userDAO = new UserDAO();
        }

        // ✅ Hàm xử lý logic đổi mật khẩu
        public String changePassword(User user, String oldPass, String newPass, String confirmPass) {
            // 🟩 Kiểm tra user hợp lệ
            if (user == null) {
                return "User not logged in!";
            }

            // 🟩 Kiểm tra nhập thiếu
            if (oldPass == null || oldPass.isEmpty()) {
                return "Old password cannot be empty!";
            }
            if (newPass == null || newPass.isEmpty()) {
                return "New password cannot be empty!";
            }
            if (confirmPass == null || confirmPass.isEmpty()) {
                return "Please confirm your new password!";
            }

            // 🟩 Kiểm tra mật khẩu cũ có đúng không
            User existingUser = userDAO.checkLoginByEmailOrUsername(user.getEmail(), oldPass);
            if (existingUser == null) {
                return "Incorrect old password!";
            }

            // 🟩 Kiểm tra độ dài
            if (newPass.length() < 6) {
                return "Password must be at least 6 characters long!";
            }

            // 🟩 Kiểm tra xác nhận khớp
            if (!newPass.equals(confirmPass)) {
                return "Confirm password does not match!";
            }

            // 🟩 Không cho phép trùng mật khẩu cũ
            if (newPass.equals(oldPass)) {
                return "New password must be different from the old password!";
            }

            // 🟩 Cập nhật mật khẩu trong database
            boolean success = userDAO.changePassword(user.getEmail(), oldPass, newPass);
            if (!success) {
                return "Failed to update password. Please try again later.";
            }

            return null; // null = không có lỗi
        }
    }

    public static class ForgotPasswordService {

        private final UserDAO userDAO;

        public ForgotPasswordService() {
            this.userDAO = new UserDAO();
        }

        /**
         * Kiểm tra email có tồn tại hay không.
         * @return User nếu tồn tại, null nếu không.
         */
        public User findUserByEmail(String email) {
            return userDAO.findUserByEmail(email);
        }

        /**
         * Tạo và gửi mã OTP tới email.
         * @return mã OTP đã tạo.
         */
        public String sendResetCode(String email) {
            String otp = String.format("%06d", new Random().nextInt(999999));

            // Lưu OTP tạm thời
            VerificationCodeStore.saveCode(email, otp);

            // Gửi email xác nhận
            String subject = "Your Rentaly Verification Code";
            String body = "Your reset code is: <h2><b>" + otp + "</b></h2>";

            EmailUtil.sendEmail(email, subject, body);
            return otp;
        }
    }

    public static class ResetPasswordService {

        private final UserDAO userDAO;

        public ResetPasswordService() {
            this.userDAO = new UserDAO();
        }

        /** ✅ Kiểm tra hợp lệ dữ liệu đầu vào */
        public String validatePassword(String password, String rePassword) {
            if (password == null || password.isEmpty() || rePassword == null || rePassword.isEmpty()) {
                return "Please enter both the new password and confirmation!";
            }

            if (!password.equals(rePassword)) {
                return "Passwords do not match!";
            }

            if (password.length() < 6) {
                return "Password must be at least 6 characters long!";
            }

            return null;
        }

        /** ✅ Cập nhật mật khẩu người dùng */
        public boolean resetPassword(String email, String newPassword) {
            User user = userDAO.findUserByEmail(email);
            if (user == null) {
                return false;
            }
            userDAO.updatePassword(user.getUserId(), newPassword);
            return true;
        }
    }
}
