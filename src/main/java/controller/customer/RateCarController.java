package controller.customer;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedReader;

import dao.implement.BookingDAO;
import model.User;
import service.ReviewService;

@WebServlet("/customer/rateCar")
public class RateCarController extends HttpServlet {

    private final ReviewService reviewService = new ReviewService();
    private final BookingDAO bookingDAO = new BookingDAO();
    private final Gson gson = new Gson(); // ✅ Tái sử dụng instance

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();

        // ==== Kiểm tra user đăng nhập ====
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            out.print("{\"success\":false, \"message\":\"Unauthorized\"}");
            return;
        }

        User currentUser = (User) session.getAttribute("user");

        try {
            // ==== Đọc JSON body ====
            StringBuilder sb = new StringBuilder();
            String line;
            try (BufferedReader reader = request.getReader()) {
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            }

            String body = sb.toString();
            System.out.println("🟢 JSON BODY = " + body);

            // ==== Parse JSON ====
            JsonObject json = gson.fromJson(body, JsonObject.class);

            // ✅ Validate JSON structure
            if (json == null || !json.has("bookingId") || !json.has("rating") || !json.has("feedback")) {
                out.print("{\"success\":false, \"message\":\"Missing required fields\"}");
                return;
            }

            int bookingId = json.get("bookingId").getAsInt();
            int rating = json.get("rating").getAsInt();
            String feedback = json.get("feedback").getAsString();

            // ✅ Sanitize feedback
            if (feedback == null) feedback = "";
            feedback = feedback.trim();

            System.out.println("✅ Parsed → bookingId=" + bookingId +
                    ", rating=" + rating +
                    ", feedback=[" + feedback + "]");

            // ==== Validate rating ====
            if (rating < 1 || rating > 5) {
                out.print("{\"success\":false, \"message\":\"Rating must be between 1 and 5\"}");
                return;
            }

            // ==== Kiểm tra quyền sở hữu booking ====
            if (!bookingDAO.isBookingOwnedByUser(bookingId, currentUser.getUserId())) {
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

        } catch (JsonSyntaxException e) {
            System.err.println("❌ Invalid JSON: " + e.getMessage());
            out.print("{\"success\":false, \"message\":\"Invalid JSON format\"}");
        } catch (NumberFormatException e) {
            System.err.println("❌ Invalid number: " + e.getMessage());
            out.print("{\"success\":false, \"message\":\"Invalid number format\"}");
        } catch (Exception e) {
            System.err.println("❌ Server error:");
            e.printStackTrace();
            out.print("{\"success\":false, \"message\":\"Server error\"}");
        }
    }
}