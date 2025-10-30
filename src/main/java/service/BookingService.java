package service;

import dao.implement.BookingDAO;
import dao.implement.CarDAO;
import dao.implement.PromotionDAO;
import dao.implement.BookingPromotionDAO;
import dao.implement.NotificationDAO; // Đã có
import model.Booking;
import model.Car;
import model.Promotion;
import model.BookingPromotion;
import model.Notification; // Import Model Notification

import java.time.LocalDate;
import java.time.LocalDateTime;

public class BookingService {

    private final BookingDAO bookingDAO = new BookingDAO();
    private final CarDAO carDAO = new CarDAO();
    private final PromotionDAO promoDAO = new PromotionDAO();
    private final BookingPromotionDAO bookingPromoDAO = new BookingPromotionDAO();
    private final NotificationDAO notificationDAO = new NotificationDAO(); // Đã có


    public String createBooking(Booking booking, String promoCode, double frontendDiscount) {

        LocalDate today = LocalDate.now();

        // --- Logic kiểm tra ngày, xe, trùng lịch, promo code giữ nguyên ---
        Car car = carDAO.findById(booking.getCarId());
        if (car == null) {
            return "❌ The selected car does not exist!";
        }
        if (car.getOwnerId() == booking.getUserId()) {
            return "❌ You cannot book your own car!";
        }
        // ... (Các kiểm tra khác giữ nguyên) ...

        // --- Khi tạo booking mới ---
        booking.setStatus("Pending");
        booking.setCreatedAt(LocalDateTime.now());

        boolean success = bookingDAO.insert(booking);
        if (!success) {
            return "❌ Booking failed. Please try again!";
        }

        try {
            int customerId = booking.getUserId();
            int ownerId = car.getOwnerId(); // Lấy Owner ID từ Car Model đã fix
            int bookingId = booking.getBookingId();

            // 1. THÔNG BÁO CHO CUSTOMER: Chờ duyệt
            notificationDAO.insertNotification(new Notification(
                    customerId,
                    "BOOKING_PENDING",
                    "Your car booking has been received!",
                    "Your car booking has been received! Please wait for the Owner to approve it.",
                    "/customer/customerOrder?id=" + bookingId
            ));

            // 2. THÔNG BÁO CHO OWNER: Yêu cầu mới cần duyệt
            notificationDAO.insertNotification(new Notification(
                    ownerId,
                    "BOOKING_NEW",
                    "New car booking request.!",
                    "A new booking has been made for" + car.getModel() + ". Please review the request.",
                    "/owner/ownerBooking?id=" + bookingId
            ));

        } catch (Exception e) {
            // Log lỗi thông báo (không nên làm thất bại Booking)
            System.err.println("Lỗi tạo thông báo sau khi Booking: " + e.getMessage());
        }
        // ----------------------------------------------------------------------

        // --- Logic xử lý mã khuyến mãi giữ nguyên ---
        // ...
        return "success";
    }


    private void insertStatusNotification(int bookingId, String status) {
        try {
            Booking booking = bookingDAO.getBookingById(bookingId);
            Car car = carDAO.findById(booking.getCarId());

            int customerId = booking.getUserId();
            int ownerId = car.getOwnerId();

            if ("Approved".equals(status)) {
                // THÔNG BÁO CHO CUSTOMER: Đã duyệt -> Payment
                notificationDAO.insertNotification(new Notification(
                        customerId, "BOOKING_APPROVED", "The booking has been approved!",
                        "Your booking for " + car.getModel() + " has been approved! Please proceed to payment.",
                        "/customer/customerOrder?bookingId=" + bookingId
                ));
            } else if ("Rejected".equals(status)) {
                // THÔNG BÁO CHO CUSTOMER: Đã từ chối
                notificationDAO.insertNotification(new Notification(
                        customerId, "BOOKING_REJECTED", "The booking has been rejected.",
                        "Your booking for " + car.getModel() + " was rejected by the Owner.", "/home"
                ));
            } else if ("Cancelled".equals(status)) {

                notificationDAO.insertNotification(new Notification(
                        customerId, "BOOKING_CANCELLED", "Booking Cancelled Successfully.",
                        "You have successfully cancelled your booking for " + car.getModel() + ".", "/customer/customerOrder?id=" + bookingId
                ));

                notificationDAO.insertNotification(new Notification(
                        ownerId, "BOOKING_CANCELLED", "Customer Cancelled Booking!",
                        "The booking for " + car.getModel() + " has been cancelled by the Customer.", "/home"
                ));
            }
        } catch (Exception e) {
            System.err.println("Lỗi tạo thông báo khi cập nhật trạng thái " + status + ": " + e.getMessage());
        }
    }



    public boolean approveBooking(int bookingId) {
        boolean success = bookingDAO.updateStatus(bookingId, "Approved");
        if (success) { insertStatusNotification(bookingId, "Approved"); }
        return success;
    }

    public boolean rejectBooking(int bookingId) {
        boolean success = bookingDAO.updateStatus(bookingId, "Rejected");
        if (success) { insertStatusNotification(bookingId, "Rejected"); }
        return success;
    }

    public boolean cancelBooking(int bookingId) {
        boolean success = bookingDAO.updateStatus(bookingId, "Cancelled");
        if (success) { insertStatusNotification(bookingId, "Cancelled"); }
        return success;
    }

    public boolean completeBooking(int bookingId) {

        return bookingDAO.updateStatus(bookingId, "Completed");
    }

    public boolean markAsPaid(int bookingId) {

        return bookingDAO.updateStatus(bookingId, "Paid");
    }
}
