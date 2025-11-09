package controller.booking;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.booking.BookingService;

import java.io.IOException;

@WebServlet("/customer/returnCar")
public class ReturnCarServlet extends HttpServlet {

    private BookingService bookingService;

    @Override
    public void init() throws ServletException {
        bookingService = new BookingService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // --- Lấy bookingId từ form ---
            String bookingIdParam = request.getParameter("bookingId");
            if (bookingIdParam == null || bookingIdParam.isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing bookingId");
                return;
            }

            int bookingId = Integer.parseInt(bookingIdParam);

            // --- Gọi service để cập nhật trạng thái sang "Returning" ---
            boolean updated = bookingService.returnBooking(bookingId);

            if (updated) {
                // ✅ Cập nhật thành công
                request.getSession().setAttribute("successMessage", "✅ Yêu cầu trả xe đã được gửi! Chờ chủ xe xác nhận.");
            } else {

                request.getSession().setAttribute("errorMessage", "❌ Không thể cập nhật trạng thái thuê xe.");
            }


            response.sendRedirect(request.getContextPath() + "/customer/customerOrder");

        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("errorMessage", "❌ Lỗi hệ thống: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/customer/customerOrder");
        }
    }
}
