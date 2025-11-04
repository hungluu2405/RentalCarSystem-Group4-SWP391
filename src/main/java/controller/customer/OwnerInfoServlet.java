package controller.customer;

import com.google.gson.Gson;
import dao.implement.BookingDAO;
import dao.implement.UserDAO;
import dao.implement.CarDAO;
import model.Booking;
import model.User;
import model.Car;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/customer/owner-info")
public class OwnerInfoServlet extends HttpServlet {

    private final BookingDAO bookingDAO = new BookingDAO();
    private final CarDAO carDAO = new CarDAO();
    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            String bookingIdParam = request.getParameter("bookingId");

            if (bookingIdParam == null || bookingIdParam.isEmpty()) {
                response.getWriter().write("{\"error\": \"Missing bookingId\"}");
                return;
            }

            int bookingId = Integer.parseInt(bookingIdParam);

            // Get booking
            Booking booking = bookingDAO.getBookingById(bookingId);
            if (booking == null) {
                response.getWriter().write("{\"error\": \"Booking not found\"}");
                return;
            }

            // Get car
            Car car = carDAO.findById(booking.getCarId());
            if (car == null) {
                response.getWriter().write("{\"error\": \"Car not found\"}");
                return;
            }

            // Get owner
            User owner = userDAO.getUserById(car.getOwnerId());
            if (owner == null || owner.getUserProfile() == null) {
                response.getWriter().write("{\"error\": \"Owner not found\"}");
                return;
            }

            // Build response
            Map<String, Object> ownerInfo = new HashMap<>();
            ownerInfo.put("fullName", owner.getUserProfile().getFullName());
            ownerInfo.put("phone", owner.getUserProfile().getPhone());
            ownerInfo.put("avatar", owner.getUserProfile().getProfileImage());

            Gson gson = new Gson();
            String json = gson.toJson(ownerInfo);
            response.getWriter().write(json);

        } catch (NumberFormatException e) {
            response.getWriter().write("{\"error\": \"Invalid bookingId format\"}");
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("{\"error\": \"Server error: " + e.getMessage() + "\"}");
        }
    }
}
