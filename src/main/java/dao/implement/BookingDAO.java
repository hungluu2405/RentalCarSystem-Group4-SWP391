package dao.implement;

import dao.DBContext;
import model.Booking;
import model.BookingDetail;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BookingDAO extends DBContext {

    // ============================================================
    // 1️⃣ INSERT BOOKING
    // ============================================================
    public boolean insert(Booking booking) {
        String sql = """
            INSERT INTO BOOKING (CAR_ID, USER_ID, START_DATE, END_DATE, TOTAL_PRICE, STATUS, CREATED_AT, LOCATION)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, booking.getCarId());
            ps.setInt(2, booking.getUserId());
            ps.setObject(3, booking.getStartDate());
            ps.setObject(4, booking.getEndDate());
            ps.setDouble(5, booking.getTotalPrice());
            ps.setString(6, booking.getStatus());
            ps.setTimestamp(7, Timestamp.valueOf(booking.getCreatedAt()));
            ps.setString(8, booking.getLocation());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("❌ Insert booking failed: " + e.getMessage());
            return false;
        }
    }

    // ============================================================
    // 2️⃣ CHECK CAR AVAILABILITY (tránh trùng lịch)
    // ============================================================
    public boolean isCarAvailable(int carId, LocalDate start, LocalDate end) {
        String sql = """
            SELECT COUNT(*) FROM BOOKING
            WHERE CAR_ID = ? AND STATUS IN ('Pending', 'Approved')
              AND (
                    (START_DATE <= ? AND END_DATE >= ?)
                 OR (START_DATE <= ? AND END_DATE >= ?)
                 OR (START_DATE >= ? AND END_DATE <= ?)
              )
        """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, carId);
            ps.setObject(2, end);
            ps.setObject(3, start);
            ps.setObject(4, start);
            ps.setObject(5, end);
            ps.setObject(6, start);
            ps.setObject(7, end);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) == 0; // true nếu xe trống
            }
        } catch (SQLException e) {
            System.err.println("❌ Check availability failed: " + e.getMessage());
        }
        return false;
    }

    // ============================================================
    // 3️⃣ LẤY BOOKING THEO USER & STATUS
    // ============================================================
    public List<BookingDetail> getBookingsByUserAndStatus(int userId, String status) {
        List<BookingDetail> list = new ArrayList<>();
        String sql = """
            SELECT 
                b.BOOKING_ID,
                c.MODEL AS carName,
                b.START_DATE, b.END_DATE, b.TOTAL_PRICE,
                b.STATUS, b.LOCATION
            FROM BOOKING b
            JOIN CAR c ON b.CAR_ID = c.CAR_ID
            WHERE b.USER_ID = ? AND b.STATUS = ?
            ORDER BY b.CREATED_AT DESC
        """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, status);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                BookingDetail detail = new BookingDetail();
                detail.setBookingId(rs.getInt("BOOKING_ID"));
                detail.setCarName(rs.getString("carName"));
                detail.setStartDate(rs.getDate("START_DATE").toLocalDate());
                detail.setEndDate(rs.getDate("END_DATE").toLocalDate());
                detail.setStatus(rs.getString("STATUS"));
                detail.setLocation(rs.getString("LOCATION"));
                detail.setTotalPrice(rs.getDouble("TOTAL_PRICE"));
                list.add(detail);
            }
        } catch (SQLException e) {
            System.err.println("❌ Get booking list failed: " + e.getMessage());
        }
        return list;
    }
    // Lấy giá xe để tính tổng tiền
    public double getCarPrice(int carId) {
        String sql = "SELECT PRICE_PER_DAY FROM CAR WHERE CAR_ID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, carId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getDouble("PRICE_PER_DAY");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Cập nhật trạng thái booking
    public boolean updateStatus(int bookingId, String status) {
        String sql = "UPDATE BOOKING SET STATUS = ? WHERE BOOKING_ID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, bookingId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
