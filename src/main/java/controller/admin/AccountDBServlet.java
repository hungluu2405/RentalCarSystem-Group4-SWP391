package controller.admin;

import dao.implement.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import model.Booking;
import model.Car;
import model.User;

@WebServlet(name = "AccountDashBoard", urlPatterns = {"/accountDB"})
public class AccountDBServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        UserDAO userDAO = new UserDAO();
        CarDAO carDAO = new CarDAO();
        BookingDAO bookingDAO = new BookingDAO();

        List<User> listU = userDAO.getAllUsersForAdmin();

        int totalUsers = userDAO.countAllUsers();
        int totalCars = carDAO.countAllCars();
        int totalBookings = bookingDAO.countAllBookings();

        request.setAttribute("listU", listU);
        request.setAttribute("totalUsers",totalUsers);
        request.setAttribute("totalCars",totalCars);
        request.setAttribute("totalBookings",totalBookings);

        request.getRequestDispatcher("/view/admin/accountdashboard.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
