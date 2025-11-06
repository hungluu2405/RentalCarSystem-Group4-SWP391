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

        if (license == null) return "❌ You must upload your driver license before booking!";
        if (booking.getStartDate().isBefore(today)) return "❌ The pickup date cannot be earlier than today!";

        //check tgian trả > nhận
        if (booking.getEndDate().isBefore(booking.getStartDate()))
            return "❌ Return date must be after pickup date!";
        if (booking.getEndDate().equals(booking.getStartDate()) &&
                booking.getDropoffTime().isBefore(booking.getPickupTime()))
            return "❌ Invalid time range!";

        LocalTime pickupTime = booking.getPickupTime();
        LocalTime dropoffTime = booking.getDropoffTime();
        if (pickupTime == null || dropoffTime == null) return "❌ Pickup and dropoff times are required!";

        LocalDateTime pickupDateTime = LocalDateTime.of(booking.getStartDate(), pickupTime);
        LocalDateTime returnDateTime = LocalDateTime.of(booking.getEndDate(), dropoffTime);

        long totalHours = ChronoUnit.HOURS.between(pickupDateTime, returnDateTime);
        if (totalHours < 24) return "❌ Minimum rental period is 24 hours!";

        Car car = carDAO.findById(booking.getCarId());
        if (car == null) return "❌ The selected car does not exist!";
        if (car.getOwnerId() == booking.getUserId()) return "❌ You cannot book your own car!";

        boolean available = bookingDAO.isCarAvailable(
                booking.getCarId(),
                booking.getStartDate(),
                booking.getPickupTime(),
                booking.getEndDate(),
                booking.getDropoffTime()
        );
        if (!available) return "❌ This car is already booked for the selected period!";

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
            extraFee = hourlyRate
                    .multiply(BigDecimal.valueOf(1.2))
                    .multiply(BigDecimal.valueOf(chargeableHours))
                    .setScale(2, RoundingMode.HALF_UP);
        } else {
            extraFee = pricePerDay;
        }

        BigDecimal finalPriceBeforeDiscount = basePrice.add(extraFee);
        BigDecimal discountAmount = BigDecimal.ZERO;
        BigDecimal finalPrice = finalPriceBeforeDiscount;
        Promotion promo = null;

        if (promoCode != null && !promoCode.trim().isEmpty()) {
            promo = promoDAO.findByCode(promoCode.trim());
            if (promo == null) return "❌ Promo code not found!";
            if (!promo.isActive()) return "❌ Promo code is not active!";
            if (promo.getStartDate().toLocalDate().isAfter(today) ||
                    promo.getEndDate().toLocalDate().isBefore(today)) {
                return "❌ Promo code expired!";
            }

            double discountRate = promo.getDiscountRate();
            if ("PERCENT".equalsIgnoreCase(promo.getDiscountType())) {
                discountAmount = finalPriceBeforeDiscount.multiply(BigDecimal.valueOf(discountRate))
                        .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
            } else if ("FIXED".equalsIgnoreCase(promo.getDiscountType())) {
                discountAmount = BigDecimal.valueOf(discountRate);
            }

            if (discountAmount.compareTo(finalPriceBeforeDiscount) > 0) discountAmount = finalPriceBeforeDiscount;
            finalPrice = finalPriceBeforeDiscount.subtract(discountAmount);
            if (finalPrice.compareTo(BigDecimal.ZERO) < 0) finalPrice = BigDecimal.ZERO;
        }

        booking.setTotalPrice(finalPrice.doubleValue());
        booking.setStatus("Pending");
        booking.setCreatedAt(LocalDateTime.now());

        try {
            boolean bookingSuccess = bookingDAO.insert(booking);
            if (!bookingSuccess) return "❌ Booking failed. Please try again!";

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

            notificationService.notifyBookingCreated(booking.getBookingId(),
                    booking.getUserId(), car.getOwnerId(), car.getModel());

            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "❌ Booking failed due to system error.";
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