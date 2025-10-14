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
     * Xử lý khi user nhấn “Đặt xe”
     */
    public boolean createBooking(Booking booking) {
        // 1️⃣ Kiểm tra xem xe có bị trùng lịch không
        boolean available = bookingDAO.isCarAvailable(
                booking.getCarId(),
                booking.getStartDate(),
                booking.getEndDate()
        );

        if (!available) {
            return false; // Xe đã có người thuê trong khoảng thời gian này
        }

        // 2️⃣ Tính tổng tiền thuê xe
//        double pricePerDay =carDAO.getCarPrice(booking.getCarId());
        long days = ChronoUnit.DAYS.between(booking.getStartDate(), booking.getEndDate());
        if (days <= 0) days = 1; // ít nhất 1 ngày thuê
//        booking.setTotalPrice(pricePerDay * days);

        // 3️⃣ Set trạng thái ban đầu (chờ duyệt)
        booking.setStatus("Pending");
        booking.setCreatedAt(LocalDateTime.now());

        // 4️⃣ Thêm booking vào DB
        return bookingDAO.insert(booking);
    }

    /**
     * Duyệt đơn thuê xe (Owner)
     */
    public boolean approveBooking(int bookingId) {
        return bookingDAO.updateStatus(bookingId, "Approved");
    }

    /**
     * Từ chối đơn thuê xe (Owner)
     */
    public boolean rejectBooking(int bookingId) {
        return bookingDAO.updateStatus(bookingId, "Rejected");
    }

    /**
     * Cập nhật trạng thái thành “Paid” sau khi thanh toán thành công
     */
    public boolean completeBooking(int bookingId) {
        return bookingDAO.updateStatus(bookingId, "Paid");
    }
}
