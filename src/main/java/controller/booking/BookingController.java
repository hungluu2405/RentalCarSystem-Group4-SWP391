package controller.booking;

import dao.implement.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Booking;
import model.CarViewModel;
import model.User;
import service.booking.BookingService;
import model.UserProfile;
import dao.implement.ReviewDAO;
import model.Review;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

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

                // ✅ LẤY SESSION
                HttpSession session = request.getSession();
                session.setAttribute("confirmedBooking", booking);

                // ✅ TÍNH GIÁ GỐC (ORIGINAL PRICE) - QUAN TRỌNG!
                try {
                    CarViewModel car = carDAO.getCarById(booking.getCarId());

                    // Tính lại giá gốc (giống logic trong BookingService)
                    LocalDateTime pickupDateTime = LocalDateTime.of(booking.getStartDate(), booking.getPickupTime());
                    LocalDateTime returnDateTime = LocalDateTime.of(booking.getEndDate(), booking.getDropoffTime());
                    long totalHours = ChronoUnit.HOURS.between(pickupDateTime, returnDateTime);

                    BigDecimal pricePerDay = car.getPricePerDay();
                    long fullDays = totalHours / 24;
                    long remainingHours = totalHours % 24;

                    BigDecimal basePrice = pricePerDay.multiply(BigDecimal.valueOf(fullDays));
                    BigDecimal hourlyRate = pricePerDay.divide(BigDecimal.valueOf(24), 2, RoundingMode.HALF_UP);
                    BigDecimal extraFee = BigDecimal.ZERO;

                    if (remainingHours <= 1) {
                        extraFee = BigDecimal.ZERO;
                    } else if (remainingHours > 1 && remainingHours <= 6) {
                        long chargeableHours = remainingHours - 1;
                        extraFee = hourlyRate.multiply(BigDecimal.valueOf(1.2))
                                .multiply(BigDecimal.valueOf(chargeableHours))
                                .setScale(2, RoundingMode.HALF_UP);
                    } else {
                        extraFee = pricePerDay;
                    }

                    BigDecimal originalPrice = basePrice.add(extraFee);

                    // Lưu giá gốc vào session
                    session.setAttribute("bookingOriginalPrice", originalPrice.doubleValue());
                    System.out.println("💰 Saved original price: " + originalPrice.doubleValue());

                } catch (Exception e) {
                    System.err.println("⚠️ Error calculating original price: " + e.getMessage());
                    e.printStackTrace();
                }

                // LƯU DISCOUNT
                if (discountAmount > 0) {
                    session.setAttribute("bookingDiscount", discountAmount);
                    System.out.println("💰 Saved discount: " + discountAmount);
                } else {
                    // Nếu discount = 0, XÓA discount khỏi session
                    session.removeAttribute("bookingDiscount");
                }


                if (finalPromoCode != null) {
                    session.setAttribute("bookingPromoCode", finalPromoCode);
                } else {
                    session.removeAttribute("bookingPromoCode");
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


        double discount = 0;
        double finalPrice = 0;

        if (calculatedDiscount != null && !calculatedDiscount.trim().isEmpty()) {
            try {
                discount = Double.parseDouble(calculatedDiscount);
            } catch (NumberFormatException e) {
                System.err.println("⚠️ Invalid discount value: " + calculatedDiscount);
            }
        }

        if (finalCalculatedPrice != null && !finalCalculatedPrice.trim().isEmpty()) {
            try {
                finalPrice = Double.parseDouble(finalCalculatedPrice);
            } catch (NumberFormatException e) {
                System.err.println("⚠️ Invalid price value: " + finalCalculatedPrice);
            }
        }

        request.setAttribute("input_calculatedDiscount", discount);
        request.setAttribute("input_finalCalculatedPrice", finalPrice);

        if (carIdStr != null && !carIdStr.isEmpty()) {
            try {
                int carId = Integer.parseInt(carIdStr);

                // Load thông tin xe
                CarViewModel car = carDAO.getCarById(carId);
                request.setAttribute("car", car);

                // Load thông tin chủ xe
                UserProfileDAO profileDAO = new UserProfileDAO();
                UserProfile ownerProfile = profileDAO.findByUserId(car.getOwnerId());
                request.setAttribute("ownerProfile", ownerProfile);

                // Lấy tham số lọc rating
                String ratingParam = request.getParameter("rating");
                Integer ratingFilter = null;
                if (ratingParam != null && !ratingParam.trim().isEmpty()) {
                    try {
                        ratingFilter = Integer.parseInt(ratingParam);
                    } catch (NumberFormatException e) {
                        // Ignore invalid rating
                    }
                }

                // Thiết lập phân trang cho reviews
                int page = 1;
                int pageSize = 5;
                String pageParam = request.getParameter("page");
                if (pageParam != null && !pageParam.trim().isEmpty()) {
                    try {
                        page = Integer.parseInt(pageParam);
                    } catch (NumberFormatException e) {
                        page = 1;
                    }
                }
                int offset = (page - 1) * pageSize;

                // Load danh sách review với phân trang
                ReviewDAO reviewDAO = new ReviewDAO();
                List<Map<String, Object>> reviews = reviewDAO.getReviewsByCarId(carId, ratingFilter, offset, pageSize);
                int totalReviews = reviewDAO.countReviewsByCarId(carId, ratingFilter);
                int totalPages = (int) Math.ceil((double) totalReviews / pageSize);

                // Kiểm tra trạng thái favourite
                boolean isFavourite = false;
                HttpSession session = request.getSession(false);
                if (session != null) {
                    User user = (User) session.getAttribute("user");
                    if (user != null && user.getRoleId() == 3) {
                        FavouriteCarDAO favouriteCarDAO = new FavouriteCarDAO();
                        isFavourite = favouriteCarDAO.isFavourite(user.getUserId(), carId);
                    }
                }

                // Set tất cả attributes
                request.setAttribute("isFavourite", isFavourite);
                request.setAttribute("reviews", reviews);
                request.setAttribute("ratingFilter", ratingFilter);
                request.setAttribute("currentPage", page);
                request.setAttribute("totalPages", totalPages);

            } catch (Exception e) {
                System.err.println("❌ Error loading car details: " + e.getMessage());
                e.printStackTrace();
            }
        }

        request.getRequestDispatcher("/view/car/car-single.jsp").forward(request, response);
    }
}