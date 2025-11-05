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

        // ✅ LẤY DISCOUNT VÀ FINAL PRICE TỪ HIDDEN INPUTS
        String calculatedDiscountStr = request.getParameter("calculatedDiscount");
        String finalCalculatedPriceStr = request.getParameter("finalCalculatedPrice");

        try {
            // Validation
            if (carIdStr == null || startDateStr == null || endDateStr == null ||
                    pickupTimeStr == null || dropoffTimeStr == null ||
                    carIdStr.isEmpty() || startDateStr.isEmpty() ||
                    endDateStr.isEmpty() || pickupTimeStr.isEmpty() ||
                    dropoffTimeStr.isEmpty()) {

                forwardWithError(request, response, "Please fill in all required fields!",
                        carIdStr, startDateStr, endDateStr, pickupTimeStr,
                        dropoffTimeStr, location, appliedPromoCode,
                        calculatedDiscountStr, finalCalculatedPriceStr);
                return;
            }

            // Check login
            User user = (User) request.getSession().getAttribute("user");
            if (user == null) {
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            }

            // Parse dates and times
            int carId;
            LocalDate startDate;
            LocalDate endDate;
            LocalTime pickupTime;
            LocalTime dropoffTime;

            try {
                carId = Integer.parseInt(carIdStr);
                startDate = LocalDate.parse(startDateStr);
                endDate = LocalDate.parse(endDateStr);

                DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
                pickupTime = LocalTime.parse(pickupTimeStr, timeFormatter);
                dropoffTime = LocalTime.parse(dropoffTimeStr, timeFormatter);

            } catch (NumberFormatException | DateTimeParseException e) {
                forwardWithError(request, response, "Invalid date or time format!",
                        carIdStr, startDateStr, endDateStr, pickupTimeStr,
                        dropoffTimeStr, location, appliedPromoCode,
                        calculatedDiscountStr, finalCalculatedPriceStr);
                return;
            }

            // ✅ PARSE DISCOUNT VÀ FINAL PRICE
            double discountAmount = 0.0;
            double finalPrice = 0.0;

            try {
                if (calculatedDiscountStr != null && !calculatedDiscountStr.isEmpty()) {
                    discountAmount = Double.parseDouble(calculatedDiscountStr);
                }
                if (finalCalculatedPriceStr != null && !finalCalculatedPriceStr.isEmpty()) {
                    finalPrice = Double.parseDouble(finalCalculatedPriceStr);
                }
            } catch (NumberFormatException e) {
                System.err.println("⚠️ Error parsing discount/price: " + e.getMessage());
            }

            // Create booking
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

            // ✅ SET TOTAL PRICE
            if (finalPrice > 0) {
                booking.setTotalPrice(finalPrice);
            }

            String finalPromoCode = (appliedPromoCode != null && !appliedPromoCode.trim().isEmpty())
                    ? appliedPromoCode.trim()
                    : null;

            // Call service
            String result = bookingService.createBooking(booking, finalPromoCode);

            if (result.equals("success")) {

                // Check booking ID
                if (booking.getBookingId() == 0) {
                    System.err.println("❌ Booking ID is 0!");
                    forwardWithError(request, response, "❌ Booking failed!",
                            carIdStr, startDateStr, endDateStr, pickupTimeStr,
                            dropoffTimeStr, location, appliedPromoCode,
                            calculatedDiscountStr, finalCalculatedPriceStr);
                    return;
                }

                // ✅ LƯU VÀO SESSION
                HttpSession session = request.getSession();
                session.setAttribute("confirmedBooking", booking);

                // ✅ LƯU DISCOUNT (QUAN TRỌNG!)
                if (discountAmount > 0) {
                    session.setAttribute("bookingDiscount", discountAmount);
                    System.out.println("💰 Saved discount: " + discountAmount);
                }

                if (finalPromoCode != null) {
                    session.setAttribute("bookingPromoCode", finalPromoCode);
                }

                response.sendRedirect(request.getContextPath() + "/booking-confirmation");

            } else {
                forwardWithError(request, response, result,
                        carIdStr, startDateStr, endDateStr, pickupTimeStr,
                        dropoffTimeStr, location, appliedPromoCode,
                        calculatedDiscountStr, finalCalculatedPriceStr);
            }

        } catch (Exception e) {
            e.printStackTrace();
            forwardWithError(request, response, "⚠️ Booking Failed: " + e.getMessage(),
                    carIdStr, startDateStr, endDateStr, pickupTimeStr,
                    dropoffTimeStr, location, appliedPromoCode,
                    calculatedDiscountStr, finalCalculatedPriceStr);
        }
    }

    private void forwardWithError(HttpServletRequest request, HttpServletResponse response,
                                  String errorMessage, String carIdStr, String startDateStr,
                                  String endDateStr, String pickupTimeStr, String dropoffTimeStr,
                                  String location, String appliedPromoCode,
                                  String calculatedDiscount, String finalCalculatedPrice)
            throws ServletException, IOException {

        request.setAttribute("error", errorMessage);
        request.setAttribute("input_startDate", startDateStr);
        request.setAttribute("input_endDate", endDateStr);
        request.setAttribute("input_pickupTime", pickupTimeStr);
        request.setAttribute("input_dropoffTime", dropoffTimeStr);
        request.setAttribute("input_location", location);
        request.setAttribute("input_appliedPromoCode", appliedPromoCode);
        request.setAttribute("input_calculatedDiscount", calculatedDiscount);
        request.setAttribute("input_finalCalculatedPrice", finalCalculatedPrice);

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