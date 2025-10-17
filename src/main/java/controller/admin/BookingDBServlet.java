package controller.admin;

import dao.implement.BookingDAO;
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


        BookingDAO bookingDAO = new BookingDAO();
        List<Booking> listB = null;
        try {
            listB = bookingDAO.getAllBookings();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        request.setAttribute("listB", listB);

        request.getRequestDispatcher("/view/admin/BookingDashboard.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
