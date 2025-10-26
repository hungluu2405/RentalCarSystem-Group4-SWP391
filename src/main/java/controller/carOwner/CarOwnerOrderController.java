package controller.carOwner;

import dao.implement.BookingDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

// Import model mới và xóa model Booking không cần thiết
import model.BookingDetail;
import model.User;
import model.UserProfile;

@WebServlet("/owner/myBooking")
public class CarOwnerOrderController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        BookingDAO bookingDAO = new BookingDAO();
        int userId = user.getUserId();


        int upcoming = bookingDAO.countByStatus(userId, "Pending");
        int total = bookingDAO.countByUser(userId);
        int cancelled = bookingDAO.countByStatus(userId, "Cancelled");


        List<BookingDetail> allBookings = bookingDAO.getBookingDetailsByUserId(userId, 100);



        request.setAttribute("upcoming", upcoming);
        request.setAttribute("total", total);
        request.setAttribute("cancelled", cancelled);
        request.setAttribute("allBookings", allBookings);


        request.getRequestDispatcher("/view/carOwner/carOwnerOrder.jsp").forward(request, response);
        System.out.println("Bookings found: " + allBookings.size());
        for (BookingDetail b : allBookings) {
            System.out.println(b.getBookingId() + " - " + b.getStatus());
        }

    }
}
