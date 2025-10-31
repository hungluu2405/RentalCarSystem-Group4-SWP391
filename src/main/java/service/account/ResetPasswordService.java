package service.account;

import dao.implement.UserDAO;
import model.User;

public class ResetPasswordService {

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
