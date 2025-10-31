package controller.booking;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import service.booking.BookingService;
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
                    request.getSession().setAttribute("message", "Booking #"+bookingId+" has been canceled successfully!");
                } else {
                    request.getSession().setAttribute("error", "Error: Could not cancel booking #"+bookingId+". Please try again.");
                }
            } catch (NumberFormatException e) {
                request.getSession().setAttribute("error", "Invalid booking ID.");
            }
        }


        response.sendRedirect(request.getContextPath() + "/customer/customerOrder");
    }
}