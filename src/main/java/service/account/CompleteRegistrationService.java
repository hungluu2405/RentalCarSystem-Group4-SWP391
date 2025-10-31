package service.account;

import jakarta.servlet.http.HttpSession;

/**
 * Service xử lý logic kiểm tra và chuẩn bị cho việc hoàn tất đăng ký bằng Google.
 */
public class CompleteRegistrationService {

    /**
     * ✅ Kiểm tra xem người dùng Google đã có trong session chưa.
     *
     * @param session HttpSession hiện tại
     * @return true nếu có googleUser trong session, ngược lại false
     */
    public boolean hasGoogleUser(HttpSession session) {
        return session != null && session.getAttribute("googleUser") != null;
    }
}
