package dao.implement;

import dao.DBContext;
import dao.GenericDAO;
import model.Booking;
import model.BookingDetail; // Import model mới

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BookingDAO extends GenericDAO<Booking> {

    //================================================================================
    // CÁC PHƯƠNG THỨC KẾ THỪA TỪ GENERICDAO (DÙNG CHO CÁC TÁC VỤ CƠ BẢN)
    //================================================================================

    @Override
    public List<Booking> findAll() {
        return queryGenericDAO(Booking.class);
    }

    @Override
    public int insert(Booking booking) {
        return insertGenericDAO(booking);
    }

    //================================================================================
    // CÁC PHƯƠNG THỨC ĐẾM (VẪN DÙNG GENERICDAO VÌ TRUY VẤN ĐƠN GIẢN)
    //================================================================================

    /**
     * Đếm tổng số booking của một người dùng.
     */
    public int countByUser(int userId) {
        String sql = "SELECT COUNT(*) FROM BOOKING WHERE userId = ?";
        Map<String, Object> parameters = new LinkedHashMap<>();
        parameters.put("userId", userId);
        return findTotalRecordGenericDAO(Booking.class, sql, parameters);
    }

    /**
     * Đếm số booking theo trạng thái của một người dùng.
     */
    public int countByStatus(int userId, String status) {
        String sql = "SELECT COUNT(*) FROM BOOKING WHERE userId = ? AND status = ?";
        Map<String, Object> parameters = new LinkedHashMap<>();
        parameters.put("userId", userId);
        parameters.put("status", status);
        return findTotalRecordGenericDAO(Booking.class, sql, parameters);
    }

    //================================================================================
    // PHƯƠNG THỨC RIÊNG CHO DASHBOARD (KHÔNG DÙNG GENERICDAO VÌ CẦN JOIN VÀ MAP TÙY CHỈNH)
    //================================================================================

    /**
     * Lấy danh sách các booking gần nhất với thông tin chi tiết (tên xe, địa điểm...).
     */
    public List<BookingDetail> getRecentBookingDetails(int userId, int limit) {
        List<BookingDetail> bookingDetails = new ArrayList<>();
        String sql = "SELECT TOP (?) " +
                "    b.bookingId, " +
                "    c.MODEL AS carName, " +
                "    b.pickUpLocation, " +
                "    b.dropOffLocation, " +
                "    b.START_DATE, " +
                "    b.END_DATE, " +
                "    b.status " +
                "FROM BOOKING b " +
                "JOIN CAR c ON b.carId = c.CAR_ID " +
                "WHERE b.userId = ? " +
                "ORDER BY b.create_at DESC";

        try (PreparedStatement st = getConnection().prepareStatement(sql)) {
            st.setInt(1, limit);
            st.setInt(2, userId);

            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    BookingDetail detail = new BookingDetail();
                    detail.setBookingId(rs.getInt("bookingId"));
                    detail.setCarName(rs.getString("carName"));
                    detail.setPickUpLocation(rs.getString("pickUpLocation"));
                    detail.setDropOffLocation(rs.getString("dropOffLocation"));
                    detail.setStartDate(rs.getObject("START_DATE", LocalDate.class));
                    detail.setEndDate(rs.getObject("END_DATE", LocalDate.class));
                    detail.setStatus(rs.getString("status"));

                    bookingDetails.add(detail);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookingDetails;
    }
}