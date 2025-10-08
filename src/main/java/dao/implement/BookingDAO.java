package dao.implement;

import dao.GenericDAO;
import model.Booking;
import java.util.List;
import java.util.Map;

public class BookingDAO extends GenericDAO<Booking> {

    @Override
    public List<Booking> findAll() {
        return queryGenericDAO(Booking.class);
    }

    @Override
    public int insert(Booking booking) {
        return insertGenericDAO(booking);
    }

    // Đếm số booking theo user
    public int countByUser(int userId) {
        String sql = "SELECT COUNT(*) FROM BOOKING WHERE userId = ?";
        return findTotalRecordGenericDAO(Booking.class, sql, Map.of("userId", userId));
    }

    // Đếm theo trạng thái (upcoming, cancelled, completed)
    public int countByStatus(int userId, String status) {
        String sql = "SELECT COUNT(*) FROM BOOKING WHERE userId = ? AND STATUS = ?";
        return findTotalRecordGenericDAO(Booking.class, sql, Map.of("userId", userId, "STATUS", status));
    }

    // Lấy danh sách booking gần nhất
    public List<Booking> getRecentBookings(int userId, int limit) {
        String sql = "SELECT TOP " + limit + " * FROM BOOKING WHERE userId = ? ORDER BY BOOKING_DATE DESC";
        return queryGenericDAO(Booking.class, sql, Map.of("userId", userId));
    }
}
