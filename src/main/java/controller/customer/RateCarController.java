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

// ======================= FIX JSON PARSE (FINAL) =======================

// Loại bỏ khoảng trắng thừa
            body = body.trim();

// ---- Lấy bookingId ----
            int bookingId = 0;
            {
                String key = "\"bookingId\":";
                int start = body.indexOf(key) + key.length();
                int end = body.indexOf(",", start);
                if (end == -1) end = body.indexOf("}", start);
                bookingId = Integer.parseInt(body.substring(start, end).trim());
            }

// ---- Lấy rating ----
            int rating = 0;
            {
                String key = "\"rating\":";
                int start = body.indexOf(key) + key.length();
                int end = body.indexOf(",", start);
                if (end == -1) end = body.indexOf("}", start);
                rating = Integer.parseInt(body.substring(start, end).trim());
            }

// ---- Lấy feedback (dạng chuỗi) ----
// feedback luôn nằm trong "" nên không bị lỗi khi có dấu phẩy
            String feedback = "";
            {
                String key = "\"feedback\":";
                int start = body.indexOf(key) + key.length();

                // bỏ dấu "
                while (body.charAt(start) == ' ' || body.charAt(start) == ':') start++;
                if (body.charAt(start) == '"') start++;

                int end = body.indexOf("\"", start);
                feedback = body.substring(start, end);
            }

// ==================== END FIX JSON PARSE ======================



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
