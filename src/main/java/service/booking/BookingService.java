package service.booking;

import dao.implement.*;
import model.*;
import service.NotificationService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class BookingService {

    private final BookingDAO bookingDAO = new BookingDAO();
    private final CarDAO carDAO = new CarDAO();
    private final PromotionDAO promoDAO = new PromotionDAO();
    private final BookingPromotionDAO bookingPromoDAO = new BookingPromotionDAO();
    private final Driver_LicenseDAO licenseDAO = new Driver_LicenseDAO();
    private final NotificationService notificationService = new NotificationService();

    public String createBooking(Booking booking, String promoCode, double frontendDiscount) {
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

        if (booking.getStartDate().isAfter(today.plusMonths(6))) {
            return "❌ Cannot book more than 6 months in advance!";
        }

        long days = ChronoUnit.DAYS.between(booking.getStartDate(), booking.getEndDate());
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

        boolean available = bookingDAO.isCarAvailable(
                booking.getCarId(),
                booking.getStartDate(),
                booking.getEndDate()
        );
        if (!available) {
            return "❌ This car is already booked for the selected period!";
        }

        double discountAmount = frontendDiscount;
        double finalPrice = booking.getTotalPrice();

        Promotion promo = null;
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
        }

        booking.setStatus("Pending");
        booking.setCreatedAt(LocalDateTime.now());

        boolean success = bookingDAO.insert(booking);
        if (!success) {
            return "❌ Booking failed. Please try again!";
        }


        notificationService.notifyBookingCreated(
                booking.getBookingId(),
                booking.getUserId(),
                car.getOwnerId(),
                car.getModel()
        );
        // ==============================================

        if (discountAmount > 0 && promo != null) {
            BookingPromotion bp = new BookingPromotion();
            bp.setBookingId(booking.getBookingId());
            bp.setPromoId(promo.getPromoId());
            bp.setDiscountAmount(discountAmount);
            bp.setFinalPrice(finalPrice);
            bp.setAppliedAt(LocalDateTime.now());
            bp.setStatus("Applied");
            bookingPromoDAO.insert(bp);
        }

        return "success";
    }



    public boolean approveBooking(int bookingId) {
        boolean success = bookingDAO.updateStatus(bookingId, "Approved");
        if (success) {
            notificationService.notifyBookingApproved(bookingId);
        }
        return success;
    }

    public boolean rejectBooking(int bookingId) {
        boolean success = bookingDAO.updateStatus(bookingId, "Rejected");
        if (success) {
            notificationService.notifyBookingRejected(bookingId);
        }
        return success;
    }

    public boolean cancelBooking(int bookingId) {
        boolean success = bookingDAO.updateStatus(bookingId, "Cancelled");
        if (success) {
            notificationService.notifyBookingCancelled(bookingId);
        }
        return success;
    }

    public boolean markAsPaid(int bookingId) {
        boolean success = bookingDAO.updateStatus(bookingId, "Paid");
        if (success) {
            notificationService.notifyBookingPaid(bookingId);
        }
        return success;
    }

    public boolean completeBooking(int bookingId) {
        boolean success = bookingDAO.updateStatus(bookingId, "Completed");
        if (success) {
            notificationService.notifyBookingCompleted(bookingId);
        }
        return success;
    }
}