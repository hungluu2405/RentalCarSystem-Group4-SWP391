package service.account;

import jakarta.servlet.http.HttpSession;

/**
 * LogoutService - Xử lý logic đăng xuất người dùng.
 */
public class LogoutService {

    /**
     * ✅ Xóa session hiện tại và đăng xuất người dùng.
     * @param session phiên làm việc hiện tại
     */
    public void logout(HttpSession session) {
        if (session != null) {
            // Có thể mở rộng thêm: ghi log, cập nhật DB, v.v.
            session.invalidate();
        }
    }
}
