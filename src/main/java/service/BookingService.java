package service;

import dao.implement.BookingDAO;
import dao.implement.CarDAO;
import dao.implement.PromotionDAO;
import dao.implement.BookingPromotionDAO;
import model.Booking;
import model.Car;
import model.Promotion;
import model.BookingPromotion;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class BookingService {

    private final BookingDAO bookingDAO = new BookingDAO();
    private final CarDAO carDAO = new CarDAO();
    private final PromotionDAO promoDAO = new PromotionDAO();
    private final BookingPromotionDAO bookingPromoDAO = new BookingPromotionDAO();

    public String createBooking(Booking booking, String promoCode, double frontendDiscount) {

        LocalDate today = LocalDate.now();

        // --- Kiểm tra logic ngày ---
        if (booking.getStartDate().isBefore(today)) {
            return "❌The pickup date cannot be earlier than the current date!";
        }
        if (booking.getEndDate().isBefore(booking.getStartDate())) {
            return "❌ Please select a return date that is after the pickup date!";
        }

        // --- Kiểm tra xe hợp lệ ---
        Car car = carDAO.findById(booking.getCarId());
        if (car == null) {
            return "❌ The selected car does not exist!";
        }
        if (car.getOwnerId() == booking.getUserId()) {
            return "❌ You cannot book your own car!";
        }

        // --- Kiểm tra xe có bị trùng lịch không ---
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

        // --- VALIDATE mã khuyến mãi (nếu có) ---
        if (promoCode != null && !promoCode.trim().isEmpty()) {
            Promotion promo = promoDAO.findByCode(promoCode.trim());
            if (promo == null) {
                return "❌ The promo code does not exist!";
            }

            if (!promo.isActive()) {
                return "❌ This promo code is no longer active!";
            }

            if (promo.getStartDate().toLocalDate().isAfter(today)
                    || promo.getEndDate().toLocalDate().isBefore(today)) {
                return "❌ This promo code has expired!";
            }
        }

        // --- Khi tạo booking mới ---
        booking.setStatus("Pending"); // khách vừa đặt
        booking.setCreatedAt(LocalDateTime.now());

        boolean success = bookingDAO.insert(booking);
        if (!success) {
            return "❌ Booking failed. Please try again!";
        }

        // --- Nếu có mã khuyến mãi ---
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

    // --- Các thao tác cập nhật trạng thái ---
    public boolean approveBooking(int bookingId) {
        return bookingDAO.updateStatus(bookingId, "Approved");
    }

    public boolean rejectBooking(int bookingId) {
        return bookingDAO.updateStatus(bookingId, "Rejected");
    }

    public boolean completeBooking(int bookingId) {
        // ⚙️ Giờ 'Completed' nghĩa là KHÁCH ĐÃ TRẢ XE
        return bookingDAO.updateStatus(bookingId, "Completed");
    }

    public boolean cancelBooking(int bookingId) {
        return bookingDAO.updateStatus(bookingId, "Cancelled");
    }

    public boolean markAsPaid(int bookingId) {
        // ⚙️ Thêm mới: đánh dấu đã thanh toán
        return bookingDAO.updateStatus(bookingId, "Paid");
    }
}
