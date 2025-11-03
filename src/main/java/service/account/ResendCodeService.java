package service.account;

import util.EmailUtil;
import util.ResetCodeStore;
import java.util.Random;

public class ResendCodeService {

    /**
     * ✅ Gửi lại mã xác minh qua email
     */
    public boolean resendVerificationCode(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }

        try {
            // Tạo mã 6 chữ số ngẫu nhiên
            String code = String.format("%06d", new Random().nextInt(999999));

            // Lưu mã vào bộ nhớ tạm (có thời hạn)
            ResetCodeStore.saveCode(email, code);

            // Gửi email cho người dùng
            String subject = "Your Rentaly Verification Code";
            String body = "Your verification code is: <h2><b>" + code + "</b></h2>";

            EmailUtil.sendEmail(email, subject, body);
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
