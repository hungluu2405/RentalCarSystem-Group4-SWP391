package service.booking;

import dao.implement.*;
import model.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class BookingService {

    private final BookingDAO bookingDAO = new BookingDAO();
    private final CarDAO carDAO = new CarDAO();
    private final PromotionDAO promoDAO = new PromotionDAO();
    private final BookingPromotionDAO bookingPromoDAO = new BookingPromotionDAO();
    private final NotificationDAO notificationDAO = new NotificationDAO();
    private final Driver_LicenseDAO licenseDAO = new Driver_LicenseDAO();

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

        try {
            int customerId = booking.getUserId();
            int ownerId = car.getOwnerId();
            int bookingId = booking.getBookingId();

            notificationDAO.insertNotification(new Notification(
                    customerId,
                    "BOOKING_PENDING",
                    "Your car booking has been received!",
                    "Your car booking has been received! Please wait for the Owner to approve it.",
                    "/customer/customerOrder?id=" + bookingId
            ));

            notificationDAO.insertNotification(new Notification(
                    ownerId,
                    "BOOKING_NEW",
                    "New car booking request!",
                    "A new booking has been made for " + car.getModel() + ". Please review the request.",
                    "/owner/ownerBooking?id=" + bookingId
            ));

        } catch (Exception e) {
            System.err.println("Lỗi tạo thông báo sau khi Booking: " + e.getMessage());
        }

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

    private void insertStatusNotification(int bookingId, String status) {
        try {
            Booking booking = bookingDAO.getBookingById(bookingId);
            Car car = carDAO.findById(booking.getCarId());

            int customerId = booking.getUserId();
            int ownerId = car.getOwnerId();

            if ("Approved".equals(status)) {
                notificationDAO.insertNotification(new Notification(
                        customerId, "BOOKING_APPROVED",
                        "The booking has been approved!",
                        "Your booking for " + car.getModel() + " has been approved! Please proceed to payment.",
                        "/customer/customerOrder?id=" + bookingId
                ));
            } else if ("Rejected".equals(status)) {
                notificationDAO.insertNotification(new Notification(
                        customerId, "BOOKING_REJECTED",
                        "The booking has been rejected.",
                        "Your booking for " + car.getModel() + " was rejected by the Owner.",
                        "/home"
                ));
            } else if ("Cancelled".equals(status)) {
                notificationDAO.insertNotification(new Notification(
                        customerId, "BOOKING_CANCELLED",
                        "Booking Cancelled Successfully.",
                        "You have successfully cancelled your booking for " + car.getModel() + ".",
                        "/customer/customerOrder?id=" + bookingId
                ));
                notificationDAO.insertNotification(new Notification(
                        ownerId, "BOOKING_CANCELLED",
                        "Customer Cancelled Booking!",
                        "The booking for " + car.getModel() + " has been cancelled by the Customer.",
                        "/owner/ownerBooking?id=" + bookingId
                ));
            } else if ("Paid".equals(status)) {
                // Thông báo cho Customer
                notificationDAO.insertNotification(new Notification(
                        customerId, "BOOKING_PAID",
                        "Payment Successful!",
                        "Your payment for " + car.getModel() + " has been confirmed. Enjoy your trip!",
                        "/customer/customerOrder?id=" + bookingId
                ));
                // Thông báo cho Owner
                notificationDAO.insertNotification(new Notification(
                        ownerId, "BOOKING_PAID",
                        "Payment Received!",
                        "Customer has paid for the booking of " + car.getModel() + ". Prepare the car for pickup.",
                        "/owner/ownerBooking?id=" + bookingId
                ));
            }
            else if ("Completed".equals(status)) {
                // Thông báo cho Customer
                notificationDAO.insertNotification(new Notification(
                        customerId, "BOOKING_COMPLETED",
                        "Car Returned Successfully!",
                        "Your trip with " + car.getModel() + " has been completed. Thank you for using Rentaly!",
                        "/customer/customerOrder?id=" + bookingId
                ));
                // Thông báo cho Owner
                notificationDAO.insertNotification(new Notification(
                        ownerId, "BOOKING_COMPLETED",
                        "Trip Completed by Customer!",
                        "Customer has done the trip with " + car.getModel() + ". Please check the car after trip.",
                        "/owner/ownerBooking?id=" + bookingId
                ));
            }
        } catch (Exception e) {
            System.err.println("Lỗi tạo thông báo khi cập nhật trạng thái " + status + ": " + e.getMessage());
        }
    }

    public boolean approveBooking(int bookingId) {
        boolean success = bookingDAO.updateStatus(bookingId, "Approved");
        if (success) {
            insertStatusNotification(bookingId, "Approved");
        }
        return success;
    }

    public boolean rejectBooking(int bookingId) {
        boolean success = bookingDAO.updateStatus(bookingId, "Rejected");
        if (success) {
            insertStatusNotification(bookingId, "Rejected");
        }
        return success;
    }

    public boolean cancelBooking(int bookingId) {
        boolean success = bookingDAO.updateStatus(bookingId, "Cancelled");
        if (success) {
            insertStatusNotification(bookingId, "Cancelled");
        }
        return success;
    }

    public boolean markAsPaid(int bookingId) {
        boolean success = bookingDAO.updateStatus(bookingId, "Paid");
        if (success) {
            insertStatusNotification(bookingId, "Paid");
        }
        return success;
    }

    public boolean completeBooking(int bookingId) {
        boolean success = bookingDAO.updateStatus(bookingId, "Completed");
        if (success) {
            insertStatusNotification(bookingId, "Completed");
        }
        return success;
    }
}