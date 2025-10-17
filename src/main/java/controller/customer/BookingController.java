package controller.customer;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Booking;
import model.CarViewModel;
import model.User;
import service.BookingService;
import dao.implement.CarDAO;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@WebServlet("/booking")
public class BookingController extends HttpServlet {

    private final BookingService bookingService = new BookingService();
    private final CarDAO carDAO = new CarDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {

            String carIdStr = request.getParameter("carId");
            String startDateStr = request.getParameter("startDate");
            String endDateStr = request.getParameter("endDate");
            String pickupTimeStr = request.getParameter("pickupTime");
            String dropoffTimeStr = request.getParameter("dropoffTime");
            String location = request.getParameter("location");


            String calculatedDiscountStr = request.getParameter("calculatedDiscount");
            String finalCalculatedPriceStr = request.getParameter("finalCalculatedPrice");
            String appliedPromoCode = request.getParameter("appliedPromoCode");


            if (carIdStr == null || startDateStr == null || endDateStr == null ||
                    pickupTimeStr == null || dropoffTimeStr == null ||
                    carIdStr.isEmpty() || startDateStr.isEmpty() ||
                    endDateStr.isEmpty() || pickupTimeStr.isEmpty() ||
                    dropoffTimeStr.isEmpty()) {

                request.setAttribute("error", "Please fill in the booking information completely!");
                request.getRequestDispatcher("/view/car/car-single.jsp").forward(request, response);
                return;
            }

            int carId = Integer.parseInt(carIdStr);


            User user = (User) request.getSession().getAttribute("user");
            if (user == null) {
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            }

            int userId = user.getUserId();


            LocalDate startDate = LocalDate.parse(startDateStr);
            LocalDate endDate = LocalDate.parse(endDateStr);

            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm[:ss]");
            LocalTime pickupTime = LocalTime.parse(pickupTimeStr, timeFormatter);
            LocalTime dropoffTime = LocalTime.parse(dropoffTimeStr, timeFormatter);


            Booking booking = new Booking();
            booking.setCarId(carId);
            booking.setUserId(userId);
            booking.setStartDate(startDate);
            booking.setEndDate(endDate);
            booking.setPickupTime(pickupTime);
            booking.setDropoffTime(dropoffTime);
            booking.setLocation(location);
            booking.setStatus("Pending");
            booking.setCreatedAt(LocalDateTime.now());


            if (startDate.isBefore(LocalDate.now())) {
                request.setAttribute("error", "❌ The pick-up date cannot be in the past!");
                CarViewModel car = carDAO.getCarById(carId);
                request.setAttribute("car", car);
                request.getRequestDispatcher("/view/car/car-single.jsp").forward(request, response);
                return;
            }

            if (endDate.isBefore(startDate)) {
                request.setAttribute("error", "❌ The drop-off date must be after the pick-up date!");
                CarViewModel car = carDAO.getCarById(carId);
                request.setAttribute("car", car);
                request.getRequestDispatcher("/view/car/car-single.jsp").forward(request, response);
                return;
            }


            double finalPrice;
            double discountAmount = 0;

            if (finalCalculatedPriceStr != null && !finalCalculatedPriceStr.isEmpty()) {
                // Sử dụng giá từ frontend (đã áp discount)
                finalPrice = Double.parseDouble(finalCalculatedPriceStr);

                if (calculatedDiscountStr != null && !calculatedDiscountStr.isEmpty()) {
                    discountAmount = Double.parseDouble(calculatedDiscountStr);
                }
            } else {
                // Fallback: tính toán như cũ nếu không có giá từ frontend
                finalPrice = calculatePrice(booking, carId);
            }

            booking.setTotalPrice(finalPrice);


            String finalPromoCode = (appliedPromoCode != null && !appliedPromoCode.trim().isEmpty()) ? appliedPromoCode.trim() : null;


            String result = bookingService.createBooking(booking, finalPromoCode, discountAmount);

            // =============== XỬ LÝ KẾT QUẢ ===============
            if (result.equals("success")) {

                HttpSession session = request.getSession();
                session.setAttribute("confirmedBooking", booking);

                // Lưu thông tin giảm giá vào session
                if (discountAmount > 0) {
                    session.setAttribute("bookingDiscount", discountAmount);
                }
                if (finalPromoCode != null) {
                    session.setAttribute("bookingPromoCode", finalPromoCode);
                }

                response.sendRedirect(request.getContextPath() + "/booking-confirmation");

            } else {
                //  BOOKING THẤT BẠI - Hiển thị lỗi trên trang booking
                CarViewModel car = carDAO.getCarById(carId);
                request.setAttribute("car", car);
                request.setAttribute("error", result);
                request.getRequestDispatcher("/view/car/car-single.jsp").forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "⚠️ Booking Failed: " + e.getMessage());
            request.getRequestDispatcher("/view/car/car-single.jsp").forward(request, response);
        }
    }

    // Hàm tính giá fallback
    private double calculatePrice(Booking booking, int carId) {
        CarDAO carDAO = new CarDAO();
        double pricePerDay = carDAO.getCarPrice(carId);

        long hours = ChronoUnit.HOURS.between(
                booking.getStartDate().atTime(booking.getPickupTime()),
                booking.getEndDate().atTime(booking.getDropoffTime())
        );
        if (hours <= 0) hours = 1;

        return (pricePerDay / 24.0) * hours;
    }
}