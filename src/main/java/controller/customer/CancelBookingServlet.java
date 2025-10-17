package controller.customer;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import service.BookingService;
import java.io.IOException;

@WebServlet("/customer/cancelBooking")
public class CancelBookingServlet extends HttpServlet {

    private final BookingService bookingService = new BookingService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {


        String bookingIdStr = request.getParameter("bookingId");

        if (bookingIdStr != null && !bookingIdStr.isEmpty()) {
            try {
                int bookingId = Integer.parseInt(bookingIdStr);


                boolean success = bookingService.cancelBooking(bookingId);

                if (success) {
                    request.getSession().setAttribute("message", "Đơn hàng #"+bookingId+" đã được hủy thành công!");
                } else {
                    request.getSession().setAttribute("error", "Lỗi: Không thể hủy đơn hàng #"+bookingId+". Vui lòng thử lại.");
                }
            } catch (NumberFormatException e) {
                request.getSession().setAttribute("error", "ID đơn hàng không hợp lệ.");
            }
        }


        response.sendRedirect(request.getContextPath() + "/customer/customerOrder");
    }
}