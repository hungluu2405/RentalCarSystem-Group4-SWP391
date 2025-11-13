package service;

import dao.implement.BookingDAO;
import dao.implement.CarDAO;
import dao.implement.NotificationDAO;
import dao.implement.UserDAO;
import model.Booking;
import model.Car;
import model.Notification;
import model.User;

import java.util.List;

public class NotificationService {

    private final NotificationDAO notificationDAO = new NotificationDAO();
    private final BookingDAO bookingDAO = new BookingDAO();
    private final CarDAO carDAO = new CarDAO();
    private final UserDAO userDAO = new UserDAO();



    public List<Notification> getHeaderNotifications(int userId) {
        return notificationDAO.getLatestNotificationsByUserId(userId);
    }

    public int getUnreadCount(int userId) {
        return notificationDAO.getUnreadCountByUserId(userId);
    }

    public void markNotificationAsRead(int notificationId) {
        notificationDAO.markAsRead(notificationId);
    }

    public void markAllAsRead(int userId) {
        notificationDAO.markAllAsRead(userId);
    }

    private String getBookingUrl(int userId, int bookingId) {
        try {
            User user = userDAO.getUserById(userId);

            if (user != null) {
                int roleId = user.getRoleId();

                // Role 2 = Customer
                if (roleId == 3) {
                    return "/customer/customerOrder?id=" + bookingId;
                }
                // Role 3 = Car Owner
                else if (roleId == 2) {
                    return "/owner/myBooking?id=" + bookingId;
                }
                // Role 1 = Admin
                else if (roleId == 1) {
                    return "/admin/dashboard";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "/home";
    }

    // ==================== BOOKING NOTIFICATION METHODS ====================
    /**
     * Gửi thông báo khi tạo booking mới (Pending)
     */
    public void notifyBookingCreated(int bookingId, int customerId, int ownerId, String carModel) {
        try {
            String customerUrl = getBookingUrl(customerId, bookingId);

            // Thông báo cho Customer/Owner (người đặt xe)
            notificationDAO.insertNotification(new Notification(
                    customerId,
                    "BOOKING_PENDING",
                    "Your car booking has been received!",
                    "Your car booking has been received! Please wait for the Owner to approve it.",
                    customerUrl
            ));

            // Thông báo cho Owner (chủ xe)
            notificationDAO.insertNotification(new Notification(
                    ownerId,
                    "BOOKING_NEW",
                    "New car booking request!",
                    "A new booking has been made for " + carModel + ". Please review the request.",
                    "/owner/ownerBooking"  // ← BỎ ?id=
            ));

        } catch (Exception e) {
            System.err.println("❌ Error sending booking created notification: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Gửi thông báo khi Owner approve booking
     */
    public void notifyBookingApproved(int bookingId) {
        try {
            Booking booking = bookingDAO.getBookingById(bookingId);
            Car car = carDAO.findById(booking.getCarId());

            // ===== THAY ĐỔI: Dùng getBookingUrl() =====
            String customerUrl = getBookingUrl(booking.getUserId(), bookingId);

            notificationDAO.insertNotification(new Notification(
                    booking.getUserId(),
                    "BOOKING_APPROVED",
                    "The booking has been approved!",
                    "Your booking for " + car.getModel() + " has been approved! Please proceed to payment.",
                    customerUrl  // ← SỬA
            ));

        } catch (Exception e) {
            System.err.println("❌ Error sending booking approved notification: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Gửi thông báo khi Owner reject booking
     */
    public void notifyBookingRejected(int bookingId) {
        try {
            Booking booking = bookingDAO.getBookingById(bookingId);
            Car car = carDAO.findById(booking.getCarId());

            notificationDAO.insertNotification(new Notification(
                    booking.getUserId(),
                    "BOOKING_REJECTED",
                    "The booking has been rejected.",
                    "Your booking for " + car.getModel() + " was rejected by the Owner.",
                    "/home"  // ← GIỮ NGUYÊN (redirect về home)
            ));

        } catch (Exception e) {
            System.err.println("❌ Error sending booking rejected notification: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Gửi thông báo khi Customer/Owner cancel booking
     */
    public void notifyBookingCancelled(int bookingId) {
        try {
            Booking booking = bookingDAO.getBookingById(bookingId);
            Car car = carDAO.findById(booking.getCarId());

            int customerId = booking.getUserId();
            int ownerId = car.getOwnerId();

            // ===== THAY ĐỔI: Dùng getBookingUrl() =====
            String customerUrl = getBookingUrl(customerId, bookingId);

            // Thông báo cho Customer/Owner (người đặt xe)
            notificationDAO.insertNotification(new Notification(
                    customerId,
                    "BOOKING_CANCELLED",
                    "Booking Cancelled Successfully.",
                    "You have successfully cancelled your booking for " + car.getModel() + ".",
                    customerUrl  // ← SỬA
            ));

            // Thông báo cho Owner (chủ xe)
            notificationDAO.insertNotification(new Notification(
                    ownerId,
                    "BOOKING_CANCELLED",
                    "Customer Cancelled Booking!",
                    "The booking for " + car.getModel() + " has been cancelled by the Customer.",
                    "/owner/ownerBooking?id=" + bookingId
            ));

        } catch (Exception e) {
            System.err.println("❌ Error sending booking cancelled notification: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Gửi thông báo khi thanh toán thành công
     */
    public void notifyBookingPaid(int bookingId) {
        try {
            Booking booking = bookingDAO.getBookingById(bookingId);
            Car car = carDAO.findById(booking.getCarId());

            int customerId = booking.getUserId();
            int ownerId = car.getOwnerId();

            // ===== THAY ĐỔI: Dùng getBookingUrl() =====
            String customerUrl = getBookingUrl(customerId, bookingId);

            // Thông báo cho Customer/Owner (người đặt xe)
            notificationDAO.insertNotification(new Notification(
                    customerId,
                    "BOOKING_PAID",
                    "Payment Successful!",
                    "Your payment for " + car.getModel() + " has been confirmed. Enjoy your trip!",
                    customerUrl  // ← SỬA
            ));

            // Thông báo cho Owner (chủ xe)
            notificationDAO.insertNotification(new Notification(
                    ownerId,
                    "BOOKING_PAID",
                    "Payment Received!",
                    "Customer has paid for the booking of " + car.getModel() + ". Prepare the car for pickup.",
                    "/owner/ownerBooking?id=" + bookingId
            ));

        } catch (Exception e) {
            System.err.println("❌ Error sending booking paid notification: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Gửi thông báo khi booking hoàn thành
     */
    public void notifyBookingCompleted(int bookingId) {
        try {
            Booking booking = bookingDAO.getBookingById(bookingId);
            Car car = carDAO.findById(booking.getCarId());

            int customerId = booking.getUserId();
            int ownerId = car.getOwnerId();

            // ===== THAY ĐỔI: Dùng getBookingUrl() =====
            String customerUrl = getBookingUrl(customerId, bookingId);

            // Thông báo cho Customer/Owner (người đặt xe)
            notificationDAO.insertNotification(new Notification(
                    customerId,
                    "BOOKING_COMPLETED",
                    "Car Returned Successfully!",
                    "Your trip with " + car.getModel() + " has been completed. Thank you for using Rentaly!",
                    customerUrl  // ← SỬA
            ));

            // Thông báo cho Owner (chủ xe)
            notificationDAO.insertNotification(new Notification(
                    ownerId,
                    "BOOKING_COMPLETED",
                    "Trip Completed by Customer!",
                    "Customer has done the trip with " + car.getModel() + ". Please check the car after trip.",
                    "/owner/ownerBooking?id=" + bookingId
            ));

        } catch (Exception e) {
            System.err.println("❌ Error sending booking completed notification: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Gửi thông báo khi customer request return car (status = Returning)
     */
    public void notifyBookingReturning(int bookingId) {
        try {
            Booking booking = bookingDAO.getBookingById(bookingId);
            Car car = carDAO.findById(booking.getCarId());

            int customerId = booking.getUserId();
            int ownerId = car.getOwnerId();

            String customerUrl = getBookingUrl(customerId, bookingId);

            // Thông báo cho Customer/Owner (người đặt xe)
            notificationDAO.insertNotification(new Notification(
                    customerId,
                    "BOOKING_RETURNING",
                    "Return Request Sent!",
                    "Your return request for " + car.getModel() + " has been sent. Waiting for owner confirmation.",
                    customerUrl
            ));

            // Thông báo cho Owner (chủ xe)
            notificationDAO.insertNotification(new Notification(
                    ownerId,
                    "BOOKING_RETURNING",
                    "Customer Requesting to Return Car!",
                    "Customer wants to return " + car.getModel() + ". Please confirm the car return.",
                    "/owner/ownerBooking"
            ));

        } catch (Exception e) {
            System.err.println("❌ Error sending booking returning notification: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Gửi thông báo khi owner confirm return (status = Completed)
     */
    public void notifyReturnConfirmed(int bookingId) {
        try {
            Booking booking = bookingDAO.getBookingById(bookingId);
            Car car = carDAO.findById(booking.getCarId());

            int customerId = booking.getUserId();
            int ownerId = car.getOwnerId();

            String customerUrl = getBookingUrl(customerId, bookingId);

            // Thông báo cho Customer/Owner (người đặt xe)
            notificationDAO.insertNotification(new Notification(
                    customerId,
                    "RETURN_CONFIRMED",
                    "Return Confirmed!",
                    "Owner has confirmed the return of " + car.getModel() + ". Your trip is now completed!",
                    customerUrl
            ));

            // Thông báo cho Owner (chủ xe) - optional
            notificationDAO.insertNotification(new Notification(
                    ownerId,
                    "RETURN_CONFIRMED",
                    "You Confirmed Car Return!",
                    "You have confirmed the return of " + car.getModel() + ". Trip completed successfully.",
                    "/owner/ownerBooking"
            ));

        } catch (Exception e) {
            System.err.println("❌ Error sending return confirmed notification: " + e.getMessage());
            e.printStackTrace();
        }
    }
}