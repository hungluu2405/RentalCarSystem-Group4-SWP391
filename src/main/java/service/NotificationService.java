package service;

import dao.implement.NotificationDAO;
import model.Notification;
import java.util.List;

public class NotificationService {

    private final NotificationDAO notificationDAO = new NotificationDAO();


    public List<Notification> getHeaderNotifications(int userId) {
        return notificationDAO.getLatestNotificationsByUserId(userId);
    }


    public int getUnreadCount(int userId) {
        return notificationDAO.getUnreadCountByUserId(userId);
    }


    public void markNotificationAsRead(int notificationId) {
        notificationDAO.markAsRead(notificationId);
    }

    public void markAllAsRead(int userId) {
        notificationDAO.markAllAsRead(userId);
    }


}
