package controller.customer;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedReader;

import dao.implement.BookingDAO;
import model.Review;
import model.User;
import service.ReviewService;

@WebServlet("/customer/rateCar")
public class RateCarController extends HttpServlet {

    private final ReviewService reviewService = new ReviewService();
    private final BookingDAO bookingDAO = new BookingDAO(); // ✅ gọi trực tiếp DAO vì DAO đã có sẵn hàm kiểm tra

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();

        // ==== 1️⃣Kiểm tra user đăng nhập ====
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            out.print("{\"success\":false, \"message\":\"Unauthorized\"}");
            return;
        }

        User currentUser = (User) session.getAttribute("user");

        try {
            // ====  Đọc JSON từ body mà không dùng thư viện ====
            StringBuilder sb = new StringBuilder();
            String line;
            try (BufferedReader reader = request.getReader()) {
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            }

            String body = sb.toString();
            System.out.println("🟢 JSON BODY = " + body);

            // Loại bỏ dấu { }, " và tách bằng dấu phẩy
            body = body.replace("{", "").replace("}", "").replace("\"", "");
            String[] parts = body.split(",");

            int bookingId = 0;
            int rating = 0;
            String feedback = "";

            for (String part : parts) {
                String[] kv = part.split(":");
                if (kv.length < 2) continue;
                String key = kv[0].trim();
                String value = kv[1].trim();

                switch (key) {
                    case "bookingId":
                        bookingId = Integer.parseInt(value);
                        break;
                    case "rating":
                        rating = Integer.parseInt(value);
                        break;
                    case "feedback":
                        // Nếu feedback có dấu :, nối lại phần còn lại
                        if (kv.length > 2) value = part.substring(part.indexOf(":") + 1).trim();
                        feedback = value;
                        break;
                }
            }

            System.out.println("✅ Parsed JSON → bookingId=" + bookingId + ", rating=" + rating + ", feedback=" + feedback);



            // ==== Validate rating ====
            if (rating < 1 || rating > 5) {
                out.print("{\"success\":false, \"message\":\"Rating must be between 1 and 5\"}");
                return;
            }

            // ====  Kiểm tra quyền sở hữu booking ====
            boolean owns = bookingDAO.isBookingOwnedByUser(bookingId, currentUser.getUserId());
            if (!owns) {
                out.print("{\"success\":false, \"message\":\"You are not authorized to review this booking\"}");
                return;
            }

            // ==== Kiểm tra đã review chưa ====
            if (reviewService.hasUserReviewed(bookingId)) {
                out.print("{\"success\":false, \"message\":\"This booking has already been reviewed\"}");
                return;
            }

            // ==== Lưu review ====
            boolean saved = reviewService.addReview(bookingId, rating, feedback);

            if (saved) {
                out.print("{\"success\":true, \"message\":\"Thank you for your feedback!\"}");
            } else {
                out.print("{\"success\":false, \"message\":\"Failed to save review\"}");
            }

        } catch (NumberFormatException e) {
            out.print("{\"success\":false, \"message\":\"Invalid number format\"}");
        } catch (Exception e) {
            e.printStackTrace();
            out.print("{\"success\":false, \"message\":\"Server error\"}");
        }
    }
}
