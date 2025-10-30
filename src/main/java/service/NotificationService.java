package service;

import dao.implement.NotificationDAO;
import model.Notification;
import java.util.List;

public class NotificationService {

    private final NotificationDAO notificationDAO = new NotificationDAO();

    // 1. GET HEADER NOTIFICATIONS (Logic giữ nguyên)
    public List<Notification> getHeaderNotifications(int userId) {
        return notificationDAO.getLatestNotificationsByUserId(userId);
    }

    // 2. ✅ THÊM PHƯƠNG THỨC LẤY SỐ LƯỢNG CHƯA ĐỌC
    /**
     * Lấy tổng số thông báo chưa đọc (IS_READ = FALSE) của người dùng cụ thể.
     */
    public int getUnreadCount(int userId) {
        return notificationDAO.getUnreadCountByUserId(userId);
    }

    // 3. ✅ THÊM PHƯƠNG THỨC ĐÁNH DẤU ĐÃ ĐỌC (Cho logic xử lý khi người dùng click)
    /**
     * Đánh dấu một thông báo cụ thể là đã đọc (IS_READ = TRUE).
     */
    public void markNotificationAsRead(int notificationId) {
        notificationDAO.markAsRead(notificationId);
    }
}
