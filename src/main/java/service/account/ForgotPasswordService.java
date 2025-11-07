package service.account;

import dao.implement.UserDAO;
import java.util.Random;
import model.User;
import util.EmailUtil;
import util.VerificationCodeStore;

public class ForgotPasswordService {

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
