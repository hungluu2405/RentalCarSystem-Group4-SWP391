package service;

import dao.implement.BookingDAO;
import dao.implement.CarDAO;
import dao.implement.PromotionDAO;
import dao.implement.BookingPromotionDAO;
import model.Booking;
import model.Promotion;
import model.BookingPromotion;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class BookingService {

    private final BookingDAO bookingDAO = new BookingDAO();
    private final CarDAO carDAO = new CarDAO();
    private final PromotionDAO promoDAO = new PromotionDAO();
    private final BookingPromotionDAO bookingPromoDAO = new BookingPromotionDAO();

    public String createBooking(Booking booking, String promoCode, double frontendDiscount) {

        LocalDate today = LocalDate.now();

        // Kiểm tra logic ngày
        if (booking.getStartDate().isBefore(today)) {
            return "❌The pickup date cannot be earlier than the current date!";
        }
        if (booking.getEndDate().isBefore(booking.getStartDate())) {
            return "❌ Please select a return date that is after the pickup date!";
        }

        //  Kiểm tra xe có bị trùng lịch không
        boolean available = bookingDAO.isCarAvailable(
                booking.getCarId(),
                booking.getStartDate(),
                booking.getEndDate()
        );
        if (!available) {
            return "❌This car is already booked for the selected period!";
        }

        double discountAmount = frontendDiscount;
        double finalPrice = booking.getTotalPrice();

        //  VALIDATE mã khuyến mãi (nếu có)
        if (promoCode != null && !promoCode.trim().isEmpty()) {
            Promotion promo = promoDAO.findByCode(promoCode.trim());
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

        //  Nếu có mã khuyến mãi, lưu vào bảng BOOKING_PROMOTION
        if (discountAmount > 0 && promoCode != null && !promoCode.trim().isEmpty()) {
            Promotion promo = promoDAO.findByCode(promoCode.trim());
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
        return bookingDAO.updateStatus(bookingId, "Approved");
    }

    public boolean rejectBooking(int bookingId) {
        return bookingDAO.updateStatus(bookingId, "Rejected");
    }

    public boolean completeBooking(int bookingId) {
        return bookingDAO.updateStatus(bookingId, "Completed");
    }

    public boolean cancelBooking(int bookingId) {
        return bookingDAO.updateStatus(bookingId, "Cancelled");
    }
}
