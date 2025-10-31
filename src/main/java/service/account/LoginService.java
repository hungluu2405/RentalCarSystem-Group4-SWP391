package service.account;

import dao.implement.UserDAO;
import model.User;

public class LoginService {

    private final UserDAO userDAO;

    public LoginService() {
        this.userDAO = new UserDAO();
    }

    /**
     * Kiểm tra thông tin đăng nhập theo email hoặc username.
     * @param loginKey email hoặc username
     * @param password mật khẩu người dùng nhập
     * @return User nếu đăng nhập thành công, null nếu thất bại
     */
    public User authenticate(String loginKey, String password) {
        if (loginKey == null || loginKey.isEmpty() || password == null || password.isEmpty()) {
            return null;
        }
        return userDAO.checkLoginByEmailOrUsername(loginKey, password);
    }

    /**
     * Trả về thông báo lỗi nếu có lỗi trong input.
     */
    public String validateInput(String loginKey, String password) {
        if (loginKey == null || loginKey.isEmpty()) {
            return "Email or username cannot be empty!";
        }
        if (password == null || password.isEmpty()) {
            return "Password cannot be empty!";
        }
        return null;
    }
}
