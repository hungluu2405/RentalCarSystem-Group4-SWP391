package controller.booking;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Booking;
import model.CarViewModel;
import model.User;
import service.booking.BookingService;
import dao.implement.CarDAO;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@WebServlet("/booking")
public class BookingController extends HttpServlet {

    private final BookingService bookingService = new BookingService();
    private final CarDAO carDAO = new CarDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String carIdStr = request.getParameter("carId");
        String startDateStr = request.getParameter("startDate");
        String endDateStr = request.getParameter("endDate");
        String pickupTimeStr = request.getParameter("pickupTime");
        String dropoffTimeStr = request.getParameter("dropoffTime");
        String location = request.getParameter("location");
        String appliedPromoCode = request.getParameter("appliedPromoCode");

        try {
            // ===== BASIC VALIDATION =====
            if (carIdStr == null || startDateStr == null || endDateStr == null ||
                    pickupTimeStr == null || dropoffTimeStr == null ||
                    carIdStr.isEmpty() || startDateStr.isEmpty() ||
                    endDateStr.isEmpty() || pickupTimeStr.isEmpty() ||
                    dropoffTimeStr.isEmpty()) {

                forwardWithError(request, response, "Please fill in all required fields!",
                        carIdStr, startDateStr, endDateStr, pickupTimeStr,
                        dropoffTimeStr, location, appliedPromoCode);
                return;
            }

            // ===== CHECK USER LOGIN =====
            User user = (User) request.getSession().getAttribute("user");
            if (user == null) {
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            }

            // ===== PARSE DATA =====
            int carId;
            LocalDate startDate;
            LocalDate endDate;
            LocalTime pickupTime;
            LocalTime dropoffTime;

            try {
                carId = Integer.parseInt(carIdStr);
                startDate = LocalDate.parse(startDateStr);
                endDate = LocalDate.parse(endDateStr);

                DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm[:ss]");
                pickupTime = LocalTime.parse(pickupTimeStr, timeFormatter);
                dropoffTime = LocalTime.parse(dropoffTimeStr, timeFormatter);

            } catch (NumberFormatException | DateTimeParseException e) {
                forwardWithError(request, response, "Invalid date or time format!",
                        carIdStr, startDateStr, endDateStr, pickupTimeStr,
                        dropoffTimeStr, location, appliedPromoCode);
                return;
            }

            // ===== CREATE BOOKING OBJECT =====
            Booking booking = new Booking();
            booking.setCarId(carId);
            booking.setUserId(user.getUserId());
            booking.setStartDate(startDate);
            booking.setEndDate(endDate);
            booking.setPickupTime(pickupTime);
            booking.setDropoffTime(dropoffTime);
            booking.setLocation(location);
            booking.setStatus("Pending");
            booking.setCreatedAt(LocalDateTime.now());

            // ===== CALL SERVICE =====
            String finalPromoCode = (appliedPromoCode != null && !appliedPromoCode.trim().isEmpty())
                    ? appliedPromoCode.trim()
                    : null;

            // ✅ GỌI SERVICE (chỉ 2 parameters)
            String result = bookingService.createBooking(booking, finalPromoCode);

            // ===== CHECK RESULT =====
            if (result.equals("success")) {
                HttpSession session = request.getSession();
                session.setAttribute("confirmedBooking", booking);

                if (finalPromoCode != null) {
                    session.setAttribute("bookingPromoCode", finalPromoCode);
                }

                response.sendRedirect(request.getContextPath() + "/booking-confirmation");

            } else {
                forwardWithError(request, response, result,
                        carIdStr, startDateStr, endDateStr, pickupTimeStr,
                        dropoffTimeStr, location, appliedPromoCode);
            }

        } catch (Exception e) {
            e.printStackTrace();
            forwardWithError(request, response, "⚠️ Booking Failed: " + e.getMessage(),
                    carIdStr, startDateStr, endDateStr, pickupTimeStr,
                    dropoffTimeStr, location, appliedPromoCode);
        }
    }

    private void forwardWithError(HttpServletRequest request, HttpServletResponse response,
                                  String errorMessage, String carIdStr, String startDateStr,
                                  String endDateStr, String pickupTimeStr, String dropoffTimeStr,
                                  String location, String appliedPromoCode)
            throws ServletException, IOException {

        request.setAttribute("error", errorMessage);
        request.setAttribute("input_startDate", startDateStr);
        request.setAttribute("input_endDate", endDateStr);
        request.setAttribute("input_pickupTime", pickupTimeStr);
        request.setAttribute("input_dropoffTime", dropoffTimeStr);
        request.setAttribute("input_location", location);
        request.setAttribute("input_appliedPromoCode", appliedPromoCode);

        if (carIdStr != null && !carIdStr.isEmpty()) {
            try {
                int carId = Integer.parseInt(carIdStr);
                CarViewModel car = carDAO.getCarById(carId);
                request.setAttribute("car", car);
            } catch (Exception e) {
                System.err.println("❌ Error loading car: " + e.getMessage());
            }
        }

        request.getRequestDispatcher("/view/car/car-single.jsp").forward(request, response);
    }
}