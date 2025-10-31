package controller.homePage;

import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.CarViewModel;
import model.Notification;
import model.User;
import service.homePage.HomeService;

@WebServlet(name = "HomeServlet", urlPatterns = {"/home"})
public class HomeServlet extends HttpServlet {

    private final HomeService homeService = new HomeService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("--- HomeServlet is running! ---");

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        // Nếu người dùng đã đăng nhập thì lấy thông báo
        if (user != null) {
            int userId = user.getUserId();
            try {
                List<Notification> latestNotifications = homeService.getLatestNotifications(userId);
                int unreadCount = homeService.getUnreadNotificationCount(userId);

                session.setAttribute("latestNotifications", latestNotifications);
                session.setAttribute("unreadNotificationCount", unreadCount);
            } catch (Exception e) {
                System.err.println("Lỗi khi tải thông báo cho user " + userId + ": " + e.getMessage());
            }
        }

        // Lấy danh sách xe
        List<CarViewModel> topBookedCars = homeService.getTopBookedCars(6);
        System.out.println("DAO returned " + topBookedCars.size() + " cars.");
        request.setAttribute("topBookedCars", topBookedCars);

        // Chuyển đến trang JSP
        request.getRequestDispatcher("/view/homePage/home.jsp").forward(request, response);
    }
}
