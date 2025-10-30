package controller.account;

import dao.implement.CarDAO;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.CarViewModel;
import model.User;
import service.NotificationService;

@WebServlet(name = "HomeServlet", urlPatterns = {"/home"})
public class HomeServlet extends HttpServlet {

    private final CarDAO carDAO = new CarDAO();
    private final NotificationService notificationService = new NotificationService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("--- HomeServlet is running! ---");

        HttpSession session = request.getSession();

        User user = (User) session.getAttribute("user");

        if (user != null) {

            int userId = user.getUserId();

            try {
                // 1. Lấy danh sách thông báo mới nhất
                List<model.Notification> latestNotifications = notificationService.getHeaderNotifications(userId);

                // 2. ✅ THÊM: Lấy số lượng thông báo chưa đọc
                int unreadCount = notificationService.getUnreadCount(userId); // Gọi hàm mới trong Service

                // 3. Đặt vào SESSION
                session.setAttribute("latestNotifications", latestNotifications);
                session.setAttribute("unreadNotificationCount", unreadCount); // ✅ ĐẶT COUNT VÀO SESSION

            } catch (Exception e) {
                System.err.println("Lỗi khi tải thông báo cho User " + userId + ": " + e.getMessage());
            }
        }
        // ----------------------------------------------------------------------


        // --- LOGIC GỌI DATABASE (Giữ nguyên) ---
        List<CarViewModel> topBookedCars = carDAO.findTopBookedCars(6);
        System.out.println("DAO returned " + topBookedCars.size() + " cars.");
        request.setAttribute("topBookedCars", topBookedCars);
        // ------------------------------------

        // Chuyển tiếp đến trang JSP để hiển thị
        request.getRequestDispatcher("/view/homePage/home.jsp").forward(request, response);
    }
}
