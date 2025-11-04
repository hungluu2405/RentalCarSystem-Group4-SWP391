package controller.notification;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import service.NotificationService;
import java.io.IOException;

@WebServlet("/mark-all-read")
public class MarkAllReadServlet extends HttpServlet {

    private final NotificationService notificationService = new NotificationService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session != null && session.getAttribute("user") != null) {
            // ✅ Lấy userId từ session (giống logic trong MarkReadServlet nếu có user object)
            model.User user = (model.User) session.getAttribute("user");
            int userId = user.getUserId();

            // ✅ Đánh dấu tất cả thông báo của user là đã đọc
            notificationService.markAllAsRead(userId);

            // ✅ Cập nhật lại session để đồng bộ badge
            int unreadCount = notificationService.getUnreadCount(userId);
            session.setAttribute("unreadNotificationCount", unreadCount);
            session.setAttribute("latestNotifications", notificationService.getHeaderNotifications(userId));

            System.out.println("✅ Mark all read done for user " + userId + ", unread = " + unreadCount);
        }

        // ✅ Quay lại trang trước
        String referer = request.getHeader("referer");
        if (referer != null) {
            response.sendRedirect(referer);
        } else {
            response.sendRedirect(request.getContextPath() + "/home");
        }
    }
}
