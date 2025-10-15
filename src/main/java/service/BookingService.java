package service;

import dao.implement.BookingDAO;
import dao.implement.CarDAO;
import model.Booking;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class BookingService {

    private final BookingDAO bookingDAO = new BookingDAO();
    private final CarDAO carDAO = new CarDAO();

    /**
     * Tạo booking mới (khi user đặt xe)
     */
    public boolean createBooking(Booking booking) {

        LocalDate today = LocalDate.now();

        if (booking.getStartDate().isBefore(today)) {
            System.out.println("❌ Ngày bắt đầu nhỏ hơn ngày hiện tại!");
            return false;
        }

        if (booking.getEndDate().isBefore(booking.getStartDate())) {
            System.out.println("❌ Ngày kết thúc nhỏ hơn ngày bắt đầu!");
            return false;
        }


        // 1️⃣ Kiểm tra xe có bị trùng lịch không
        boolean available = bookingDAO.isCarAvailable(
                booking.getCarId(),
                booking.getStartDate(),
                booking.getEndDate()
        );

        if (!available) {
            return false; // Xe đã có người thuê trong khoảng thời gian này
        }

        // 2️⃣ Tính tổng tiền thuê (theo giờ)
        double pricePerDay = carDAO.getCarPrice(booking.getCarId());
        long hours = ChronoUnit.HOURS.between(
                booking.getStartDate().atTime(booking.getPickupTime()),
                booking.getEndDate().atTime(booking.getDropoffTime())
        );
        if (hours <= 0) hours = 1;

        double total = (pricePerDay / 24.0) * hours;
        booking.setTotalPrice(total);

        // 3️⃣ Set trạng thái ban đầu
        booking.setStatus("Pending");
        booking.setCreatedAt(LocalDateTime.now());

        // 4️⃣ Lưu booking vào DB
        return bookingDAO.insert(booking);
    }

    /**
     * Chủ xe duyệt đơn thuê
     */
    public boolean approveBooking(int bookingId) {
        return bookingDAO.updateStatus(bookingId, "Approved");
    }

    /**
     * Chủ xe từ chối đơn thuê
     */
    public boolean rejectBooking(int bookingId) {
        return bookingDAO.updateStatus(bookingId, "Rejected");
    }

    /**
     * Khách hàng thanh toán xong
     */
    public boolean completeBooking(int bookingId) {
        return bookingDAO.updateStatus(bookingId, "Paid");
    }
}
