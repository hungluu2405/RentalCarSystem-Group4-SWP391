package service.account;

import util.VerificationCodeStore;

public class VerifyCodeService {

    /**
     * ✅ Xác thực mã OTP gửi đến email người dùng.
     *
     * @param email email cần xác minh
     * @param code mã OTP người dùng nhập
     * @return true nếu mã hợp lệ và chưa hết hạn
     */
    public boolean verifyCode(String email, String code) {
        if (email == null || email.isEmpty() || code == null || code.isEmpty()) {
            return false;
        }
        return VerificationCodeStore.validateCode(email, code);
    }
}
