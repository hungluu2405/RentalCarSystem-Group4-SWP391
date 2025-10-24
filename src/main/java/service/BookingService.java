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

    /**
     * Tạo booking mới, có hỗ trợ áp dụng mã giảm giá
     */
    /**
     * Tạo booking mới, sử dụng giá đã tính từ frontend
     */
    public String createBooking(Booking booking, String promoCode, double frontendDiscount) {

        LocalDate today = LocalDate.now();

        // 1️⃣ Kiểm tra logic ngày
        if (booking.getStartDate().isBefore(today)) {
            return "❌ Ngày nhận xe không được nhỏ hơn ngày hiện tại!";
        }
        if (booking.getEndDate().isBefore(booking.getStartDate())) {
            return "❌ Ngày trả xe phải sau ngày nhận xe!";
        }

        // 2️⃣ Kiểm tra xe có bị trùng lịch không
        boolean available = bookingDAO.isCarAvailable(
                booking.getCarId(),
                booking.getStartDate(),
                booking.getEndDate()
        );
        if (!available) {
            return "❌ Xe đã có người thuê trong thời gian này!";
        }

        double discountAmount = frontendDiscount;
        double finalPrice = booking.getTotalPrice();

        // 3️⃣ VALIDATE mã khuyến mãi (nếu có)
        if (promoCode != null && !promoCode.trim().isEmpty()) {
            Promotion promo = promoDAO.findByCode(promoCode.trim());
            if (promo == null) {
                return "❌ Mã khuyến mãi không tồn tại!";
            }

            if (!promo.isActive()) {
                return "❌ Mã khuyến mãi đã bị ngừng hoạt động!";
            }

            if (promo.getStartDate().toLocalDate().isAfter(today) ||
                    promo.getEndDate().toLocalDate().isBefore(today)) {
                return "❌ Mã khuyến mãi đã hết hạn!";
            }


            // Không tính lại discount, chỉ validate mã
        }

        booking.setStatus("Pending");
        booking.setCreatedAt(LocalDateTime.now());

        // 4️⃣ Lưu booking
        boolean success = bookingDAO.insert(booking);

        if (!success) {
            return "❌ Đặt xe thất bại, vui lòng thử lại!";
        }

        // 5️⃣ Nếu có mã khuyến mãi, lưu vào bảng BOOKING_PROMOTION
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
        return bookingDAO.updateStatus(bookingId, "Paid");
    }

    public boolean cancelBooking(int bookingId) {
        return bookingDAO.updateStatus(bookingId, "Cancelled");
    }
}
