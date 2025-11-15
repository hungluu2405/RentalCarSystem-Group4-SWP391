package service.booking;

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
                    "Yêu cầu thuê xe đã được gửi!",
                    "Yêu cầu thuê xe của bạn đã được gửi đến chủ xe. Vui lòng chờ chủ xe xác nhận..",
                    customerUrl
            ));

            // Thông báo cho Owner (chủ xe)
            notificationDAO.insertNotification(new Notification(
                    ownerId,
                    "BOOKING_NEW",
                    "Có yêu cầu thuê xe mới!",
                    "Bạn có yêu cầu thuê xe mới cho xe " + carModel + ". Vui lòng xem chi tiết và xác nhận.",
                    "/owner/ownerBooking"
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


            String customerUrl = getBookingUrl(booking.getUserId(), bookingId);

            notificationDAO.insertNotification(new Notification(
                    booking.getUserId(),
                    "BOOKING_APPROVED",
                    "Yêu cầu thuê xe được chấp nhận!",
                    "Yêu cầu thuê xe " + car.getModel() + " đã được chấp thuận! Vui lòng bắt đầu thanh toán.",
                    customerUrl
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
                    "Yêu cầu thuê xe đã bị từ chối.",
                    "Yêu cầu thuê xe " + car.getModel() + " đã bị từ chối bởi chủ xe.",
                    "/home"
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

            String customerUrl = getBookingUrl(customerId, bookingId);

            // Thông báo cho Customer/Owner (người đặt xe)
            notificationDAO.insertNotification(new Notification(
                    customerId,
                    "BOOKING_CANCELLED",
                    "Đã hủy yêu cầu thuê xe.",
                    "Bạn đã hủy yêu cầu thuê xe " + car.getModel() + "thành công",
                    customerUrl
            ));

            // Thông báo cho Owner (chủ xe)
            notificationDAO.insertNotification(new Notification(
                    ownerId,
                    "BOOKING_CANCELLED",
                    "Khách hàng đã hủy đặt xe!",
                    "Yêu cầu thuê xe " + car.getModel() + " đã bị hủy bởi khách hàng.",
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
                    "Thanh toán thành công!",
                    "Thanh toán cho xe " + car.getModel() + " đã hoàn tất. Chúc bạn có chuyến đi vui vẻ!",
                    customerUrl  // ← SỬA
            ));

            // Thông báo cho Owner (chủ xe)
            notificationDAO.insertNotification(new Notification(
                    ownerId,
                    "BOOKING_PAID",
                    "Khách hàng thanh toán thành công!",
                    "Khách hàng đã thanh toán thành công cho xe" + car.getModel() + ". Vui lòng chuẩn bị xe và liên hệ khách để giao xe.",
                    "/owner/ownerBooking?id=" + bookingId
            ));

        } catch (Exception e) {
            System.err.println("❌ Error sending booking paid notification: " + e.getMessage());
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
                    "Yêu cầu trả xe đã gửi!",
                    "Yêu cầu trả xe " + car.getModel() + " đã được gửi. Vui lòng chờ chủ xe xác nhận.",
                    customerUrl
            ));

            // Thông báo cho Owner (chủ xe)
            notificationDAO.insertNotification(new Notification(
                    ownerId,
                    "BOOKING_RETURNING",
                    "Khách yêu cầu trả xe!",
                    "Khách hàng muốn trả xe " + car.getModel() + ". Vui lòng xác nhận việc nhận xe lại.",
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
                    "Chủ xe đã xác nhận nhận lại xe!",
                    "Chủ xe đã xác nhận nhận lại xe " + car.getModel() + ". Chuyến đi của bạn đã hoàn thành!",
                    customerUrl
            ));

            // Thông báo cho Owner (chủ xe) - optional
            notificationDAO.insertNotification(new Notification(
                    ownerId,
                    "RETURN_CONFIRMED",
                    "Bạn đã xác nhận nhận lại xe!",
                    "Bạn đã xác nhận nhận lại xe " + car.getModel() + ". Chuyến đi đã hoàn thành thành công.",
                    "/owner/ownerBooking"
            ));

        } catch (Exception e) {
            System.err.println("❌ Error sending return confirmed notification: " + e.getMessage());
            e.printStackTrace();
        }
    }
}