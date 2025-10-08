package controller.customer;

import dao.implement.BookingDAO;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;
import model.Booking;
import model.User;

@WebServlet("/customer/customerDashboard")
public class CustomerDashboardController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        // TẠO MOCK USER CHO DEMO
        User mockUser = new User();
        mockUser.setUserId(1);
        mockUser.setEmail("demo@carrental.com");
        session.setAttribute("user", mockUser);

        // LẤY USER TỪ SESSION
        User user = (User) session.getAttribute("user");

        // (Không cần redirect login nữa trong giai đoạn demo)
        // if (user == null) {
        //     response.sendRedirect(request.getContextPath() + "/login.jsp");
        //     return;
        // }

        BookingDAO bookingDAO = new BookingDAO();

        int upcoming = bookingDAO.countByStatus(user.getUserId(), "upcoming");
        int total = bookingDAO.countByUser(user.getUserId());
        int cancelled = bookingDAO.countByStatus(user.getUserId(), "cancelled");

        List<Booking> recentBookings = bookingDAO.getRecentBookings(user.getUserId(), 5);

        request.setAttribute("user", user);
        request.setAttribute("upcoming", upcoming);
        request.setAttribute("total", total);
        request.setAttribute("cancelled", cancelled);
        request.setAttribute("recentBookings", recentBookings);

        request.getRequestDispatcher("/view/customer/customerDashboard.jsp").forward(request, response);
    }
}
