package controller.notification;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.booking.NotificationService;
import java.io.IOException;

@WebServlet("/mark-read")
public class MarkReadServlet extends HttpServlet {

    private final NotificationService notificationService = new NotificationService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Lấy tham số Notification ID và URL chuyển hướng
        String notificationIdStr = request.getParameter("id");
        String redirectUrl = request.getParameter("redirectUrl");

        // Lấy Context Path để đảm bảo URL là tuyệt đối
        String contextPath = request.getContextPath();

        if (notificationIdStr != null && !notificationIdStr.isEmpty()) {
            try {
                int notificationId = Integer.parseInt(notificationIdStr);

                // 2. Gọi Service để đánh dấu là đã đọc
                notificationService.markNotificationAsRead(notificationId);

                // Sau khi đánh dấu đã đọc, cần phải cập nhật lại count trong session
                // CÁCH ĐƠN GIẢN NHẤT: Bắt người dùng refresh trang (sẽ được cập nhật ở HomeServlet)

            } catch (NumberFormatException e) {
                System.err.println("Lỗi định dạng ID thông báo: " + e.getMessage());
                // Tiếp tục chuyển hướng để tránh màn hình trắng
            }
        }

        // 3. Chuyển hướng người dùng đến URL nội dung chính
        if (redirectUrl != null && !redirectUrl.isEmpty()) {
            // Chuyển hướng đến URL nội dung chính (ví dụ: /customer/order-details?id=123)
            response.sendRedirect(contextPath + redirectUrl);
        } else {
            // Nếu không có URL chuyển hướng, về trang chủ
            response.sendRedirect(contextPath + "/home");
        }
    }
}
