package controller.admin;

import dao.implement.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import model.Booking;

@WebServlet(urlPatterns = {"/bookingDB"})
public class BookingDBServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String dateRange = request.getParameter("dateRange");
        String priceRange = request.getParameter("price");

        UserDAO userDAO = new UserDAO();
        CarDAO carDAO = new CarDAO();
        BookingDAO bookingDAO = new BookingDAO();
        PaymentDAO paymentDAO = new PaymentDAO();
        ContactDAO contactDAO = new ContactDAO();

        int totalUsers = userDAO.countAllUsers();
        int totalCars = carDAO.countAllCars();
        int totalBookings = bookingDAO.countAllBookings();
        int totalReports = paymentDAO.countAllReport();
        int totalContacts = contactDAO.countUnresolvedContacts();
        List<Booking> listB;

        try {
            if ((dateRange != null && !dateRange.isEmpty()) ||
                    (priceRange != null && !priceRange.isEmpty())) {
                listB = bookingDAO.getBookingsFiltered(dateRange, priceRange);
            } else {
                listB = bookingDAO.getAllBookings();
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }
        request.setAttribute("listB", listB);
        request.setAttribute("selectedDateRange", dateRange);
        request.setAttribute("selectedPrice", priceRange);

        request.setAttribute("totalUsers",totalUsers);
        request.setAttribute("totalCars",totalCars);
        request.setAttribute("totalBookings",totalBookings);
        request.setAttribute("totalReports",totalReports);
        request.setAttribute("totalContacts", totalContacts);
        request.setAttribute("activePage", "booking");
        request.getRequestDispatcher("/view/admin/BookingDashboard.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}