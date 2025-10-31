package service.account;

import dao.implement.UserDAO;
import model.User;

public class ChangePasswordService {

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
