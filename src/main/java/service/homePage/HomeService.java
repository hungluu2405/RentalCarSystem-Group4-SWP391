package service.homePage;

import dao.implement.CarDAO;
import java.util.List;
import model.CarViewModel;
import model.Notification;
import service.NotificationService;

public class HomeService {

    private final CarDAO carDAO;
    private final NotificationService notificationService;

    public HomeService() {
        this.carDAO = new CarDAO();
        this.notificationService = new NotificationService();
    }

    /** Lấy danh sách xe được đặt nhiều nhất */
    public List<CarViewModel> getTopBookedCars(int limit) {
        return carDAO.findTopBookedCars(limit);
    }

    /** Lấy danh sách thông báo mới nhất của người dùng */
    public List<Notification> getLatestNotifications(int userId) {
        return notificationService.getHeaderNotifications(userId);
    }

    /** Đếm số thông báo chưa đọc */
    public int getUnreadNotificationCount(int userId) {
        return notificationService.getUnreadCount(userId);
    }
}
