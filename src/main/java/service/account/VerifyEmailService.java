package service.account;

import dao.implement.UserDAO;
import model.Address;
import model.User;
import model.UserProfile;
import util.ResetCodeStore;

public class VerifyEmailService {

    private final UserDAO userDAO;

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
        return userDAO.registerUser(user, profile, address);
    }
}
