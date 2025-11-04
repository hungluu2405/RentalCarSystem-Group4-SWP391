package controller.carOwner;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.booking.BookingService;

import java.io.IOException;

@WebServlet("/owner/returnCar")
public class ReturnCarServletCarOwner extends HttpServlet {

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

            // --- Gọi service để cập nhật trạng thái sang "Completed" ---
            boolean updated = bookingService.completeBooking(bookingId);

            if (updated) {
                // ✅ Cập nhật thành công
                request.getSession().setAttribute("successMessage", "✅ Bạn đã xác nhận trả xe thành công!");
            } else {

                request.getSession().setAttribute("errorMessage", "❌ Không thể cập nhật trạng thái thuê xe.");
            }


            response.sendRedirect(request.getContextPath() + "/owner/myBooking");

        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("errorMessage", "❌ Lỗi hệ thống: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/owner/myBooking");
        }
    }
}

