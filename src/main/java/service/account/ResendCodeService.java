package service.account;

import util.EmailUtil;
import util.VerificationCodeStore;
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
            String otp = String.format("%06d", new Random().nextInt(999999));

            // Lưu mã vào bộ nhớ tạm (có thời hạn)
            VerificationCodeStore.saveCode(email, otp);

            // Gửi email cho người dùng
            String subject = "Your Rentaly Verification Code";
            String body = "Your new code is: <h2><b>" + otp + "</b></h2>";

            EmailUtil.sendEmail(email, subject, body);
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
