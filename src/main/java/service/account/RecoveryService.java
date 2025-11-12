package service.account;

import dao.implement.UserDAO;
import model.User;
import util.EmailUtil;
import util.VerificationCodeStore;

import java.util.Random;

public class RecoveryService {

    /**
     * ✅ Dịch vụ đổi mật khẩu khi người dùng đã đăng nhập
     */
    public static class ChangePasswordService {

        private final UserDAO userDAO;

        public ChangePasswordService() {
            this.userDAO = new UserDAO();
        }

        // ✅ Hàm xử lý logic đổi mật khẩu
        public String changePassword(User user, String oldPass, String newPass, String confirmPass) {

            // 🟩 Kiểm tra user hợp lệ
            if (user == null) {
                return "Người dùng chưa đăng nhập!";
            }

            // 🟩 Kiểm tra nhập thiếu
            if (oldPass == null || oldPass.isEmpty()) {
                return "Vui lòng nhập mật khẩu hiện tại!";
            }
            if (newPass == null || newPass.isEmpty()) {
                return "Vui lòng nhập mật khẩu mới!";
            }
            if (confirmPass == null || confirmPass.isEmpty()) {
                return "Vui lòng nhập lại mật khẩu mới!";
            }

            // 🟩 Kiểm tra mật khẩu cũ có đúng không
            User existingUser = userDAO.checkLoginByEmailOrUsername(user.getEmail(), oldPass);
            if (existingUser == null) {
                return "Mật khẩu hiện tại không chính xác!";
            }

            // 🟩 Kiểm tra độ dài mật khẩu
            if (newPass.length() < 6) {
                return "Mật khẩu mới phải có ít nhất 6 ký tự!";
            }

            // 🟩 Kiểm tra xác nhận mật khẩu
            if (!newPass.equals(confirmPass)) {
                return "Mật khẩu nhập lại không khớp!";
            }

            // 🟩 Không cho phép trùng mật khẩu cũ
            if (newPass.equals(oldPass)) {
                return "Mật khẩu mới không được trùng với mật khẩu hiện tại!";
            }

            // 🟩 Cập nhật mật khẩu trong cơ sở dữ liệu
            boolean success = userDAO.changePassword(user.getEmail(), oldPass, newPass);
            if (!success) {
                return "Không thể cập nhật mật khẩu. Vui lòng thử lại sau!";
            }

            return null; // null = không có lỗi
        }
    }

    /**
     * ✅ Dịch vụ quên mật khẩu (gửi mã OTP đến email)
     */
    public static class ForgotPasswordService {

        private final UserDAO userDAO;

        public ForgotPasswordService() {
            this.userDAO = new UserDAO();
        }

        /**
         * Kiểm tra email có tồn tại hay không.
         *
         * @return User nếu tồn tại, null nếu không.
         */
        public User findUserByEmail(String email) {
            return userDAO.findUserByEmail(email);
        }

        /**
         * Gửi mã OTP khôi phục mật khẩu đến email.
         */
        public String sendResetCode(String email) {
            String otp = String.format("%06d", new Random().nextInt(999999));

            // Lưu OTP tạm thời
            VerificationCodeStore.saveCode(email, otp);

            // Gửi email xác nhận
            String subject = "Mã xác minh khôi phục mật khẩu Rentaly";
            String body = "Mã xác minh của bạn là: <h2><b>" + otp + "</b></h2>";

            EmailUtil.sendEmail(email, subject, body);
            return otp;
        }
    }

    /**
     * ✅ Dịch vụ đặt lại mật khẩu mới sau khi xác minh OTP
     */
    public static class ResetPasswordService {

        private final UserDAO userDAO;

        public ResetPasswordService() {
            this.userDAO = new UserDAO();
        }

        /**
         * ✅ Kiểm tra hợp lệ dữ liệu đầu vào
         */
        public String validatePassword(String password, String rePassword) {
            if (password == null || password.isEmpty() || rePassword == null || rePassword.isEmpty()) {
                return "Vui lòng nhập đầy đủ mật khẩu mới và xác nhận!";
            }

            if (!password.equals(rePassword)) {
                return "Mật khẩu nhập lại không khớp!";
            }

            if (password.length() < 6) {
                return "Mật khẩu phải có ít nhất 6 ký tự!";
            }

            return null;
        }

        /**
         * ✅ Cập nhật mật khẩu người dùng
         */
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
