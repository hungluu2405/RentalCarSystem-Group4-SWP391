package service.booking;

import dao.implement.*;
import model.*;
import service.NotificationService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class BookingService {

    private final BookingDAO bookingDAO = new BookingDAO();
    private final CarDAO carDAO = new CarDAO();
    private final PromotionDAO promoDAO = new PromotionDAO();
    private final BookingPromotionDAO bookingPromoDAO = new BookingPromotionDAO();
    private final Driver_LicenseDAO licenseDAO = new Driver_LicenseDAO();
    private final NotificationService notificationService = new NotificationService();

    public String createBooking(Booking booking, String promoCode) {
        Driver_License license = licenseDAO.getLicenseByUserId(booking.getUserId());
        LocalDate today = LocalDate.now();

        if (license == null) {
            return "❌ You must upload your driver license before booking!";
        }

        if (booking.getStartDate().isBefore(today)) {
            return "❌ The pickup date cannot be earlier than the current date!";
        }

        if (booking.getEndDate().isBefore(booking.getStartDate()) ||
                booking.getEndDate().equals(booking.getStartDate())) {
            return "❌ Return date must be after pickup date (minimum 1 day)!";
        }

        // check đủ 1 ngày (phải lớn hơn 24 tiếng)
        LocalTime pickupTime = booking.getPickupTime();
        LocalTime dropoffTime = booking.getDropoffTime();

        if (pickupTime == null || dropoffTime == null) {
            return "❌ Pickup time and return time are required!";
        }

        LocalDateTime pickupDateTime = LocalDateTime.of(booking.getStartDate(), pickupTime);
        LocalDateTime returnDateTime = LocalDateTime.of(booking.getEndDate(), dropoffTime);
        long hoursBetween = ChronoUnit.HOURS.between(pickupDateTime, returnDateTime);

        if (hoursBetween < 24) {
            return "❌ Minimum rental period is 24 hours!";
        }

        long days = ChronoUnit.DAYS.between(booking.getStartDate(), booking.getEndDate());

        if (booking.getStartDate().isAfter(today.plusMonths(6))) {
            return "❌ Cannot book more than 6 months in advance!";
        }

        if (days > 90) {
            return "❌ Maximum rental period is 90 days!";
        }

        Car car = carDAO.findById(booking.getCarId());
        if (car == null) {
            return "❌ The selected car does not exist!";
        }
        if (car.getOwnerId() == booking.getUserId()) {
            return "❌ You cannot book your own car!";
        }
        //kiểm tra về mặt thời gian có trùng lặp k
        boolean available = bookingDAO.isCarAvailable(
                booking.getCarId(),
                booking.getStartDate(),
                booking.getPickupTime(),
                booking.getEndDate(),
                booking.getDropoffTime()
        );
        if (!available) {
            return "❌ This car is already booked for the selected period!";
        }

        //tính giá thuê xe
        BigDecimal pricePerDay = car.getPricePerDay();
        BigDecimal daysDecimal = BigDecimal.valueOf(days);
        BigDecimal basePrice = pricePerDay.multiply(daysDecimal);  // basePrice = pricePerDay * days

        BigDecimal discountAmount = BigDecimal.ZERO;
        BigDecimal finalPrice = basePrice;
        Promotion promo = null;

        // ===== PROMOTION HANDLING =====
        if (promoCode != null && !promoCode.trim().isEmpty()) {
            promo = promoDAO.findByCode(promoCode.trim());
            if (promo == null) {
                return "❌ The promo code does not exist!";
            }

            if (!promo.isActive()) {
                return "❌ This promo code is no longer active!";
            }

            if (promo.getStartDate().toLocalDate().isAfter(today) ||
                    promo.getEndDate().toLocalDate().isBefore(today)) {
                return "❌ This promo code has expired!";
            }

            // Calculate discount
            double discountRate = promo.getDiscountRate();  // double từ DB

            if ("PERCENTAGE".equalsIgnoreCase(promo.getDiscountType()) ||
                    "PERCENT".equalsIgnoreCase(promo.getDiscountType())) {

                discountAmount = basePrice.multiply(BigDecimal.valueOf(discountRate))
                        .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

            } else if ("FIXED".equalsIgnoreCase(promo.getDiscountType())) {
                // Discount cố định
                discountAmount = BigDecimal.valueOf(discountRate);
            } else {
                // Default: coi như percent
                discountAmount = basePrice.multiply(BigDecimal.valueOf(discountRate))
                        .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
            }

            // Discount không được lớn hơn basePrice
            if (discountAmount.compareTo(basePrice) > 0) {
                discountAmount = basePrice;
            }

            // finalPrice = basePrice - discountAmount
            finalPrice = basePrice.subtract(discountAmount);

            // Safety check
            if (finalPrice.compareTo(BigDecimal.ZERO) < 0) {
                finalPrice = BigDecimal.ZERO;
            }
        }


        booking.setTotalPrice(finalPrice.doubleValue());
        booking.setStatus("Pending");
        booking.setCreatedAt(LocalDateTime.now());


        try {
            boolean bookingSuccess = bookingDAO.insert(booking);
            if (!bookingSuccess) {
                return "❌ Booking failed. Please try again!";
            }

            if (discountAmount.compareTo(BigDecimal.ZERO) > 0 && promo != null) {
                BookingPromotion bp = new BookingPromotion();
                bp.setBookingId(booking.getBookingId());
                bp.setPromoId(promo.getPromoId());
                bp.setDiscountAmount(discountAmount.doubleValue());
                bp.setFinalPrice(finalPrice.doubleValue());
                bp.setAppliedAt(LocalDateTime.now());
                bp.setStatus("Applied");
                bookingPromoDAO.insert(bp);
            }

            try {
                notificationService.notifyBookingCreated(
                        booking.getBookingId(),
                        booking.getUserId(),
                        car.getOwnerId(),
                        car.getModel()
                );
            } catch (Exception e) {
                System.err.println("⚠️ Warning: Failed to send notification");
                e.printStackTrace();
            }

            return "success";

        } catch (Exception e) {
            System.err.println("❌ Error creating booking: " + e.getMessage());
            e.printStackTrace();
            return "❌ Booking failed due to system error. Please try again!";
        }
    }

    public boolean approveBooking(int bookingId) {
        boolean success = bookingDAO.updateStatus(bookingId, "Approved");
        if (success) {
            try {
                notificationService.notifyBookingApproved(bookingId);
            } catch (Exception e) {
                System.err.println("⚠️ Warning: Failed to send approval notification");
                e.printStackTrace();
            }
        }
        return success;
    }

    public boolean rejectBooking(int bookingId) {
        boolean success = bookingDAO.updateStatus(bookingId, "Rejected");
        if (success) {
            try {
                notificationService.notifyBookingRejected(bookingId);
            } catch (Exception e) {
                System.err.println("⚠️ Warning: Failed to send rejection notification");
                e.printStackTrace();
            }
        }
        return success;
    }

    public boolean cancelBooking(int bookingId) {
        boolean success = bookingDAO.updateStatus(bookingId, "Cancelled");
        if (success) {
            try {
                notificationService.notifyBookingCancelled(bookingId);
            } catch (Exception e) {
                System.err.println("⚠️ Warning: Failed to send cancellation notification");
                e.printStackTrace();
            }
        }
        return success;
    }

    public boolean markAsPaid(int bookingId) {
        boolean success = bookingDAO.updateStatus(bookingId, "Paid");
        if (success) {
            try {
                notificationService.notifyBookingPaid(bookingId);
            } catch (Exception e) {
                System.err.println("⚠️ Warning: Failed to send payment notification");
                e.printStackTrace();
            }
        }
        return success;
    }

    public boolean completeBooking(int bookingId) {
        boolean success = bookingDAO.updateStatus(bookingId, "Completed");
        if (success) {
            try {
                notificationService.notifyBookingCompleted(bookingId);
            } catch (Exception e) {
                System.err.println("⚠️ Warning: Failed to send completion notification");
                e.printStackTrace();
            }
        }
        return success;
    }
}