package dao.implement;

import dao.DBContext;
import model.Booking;
import model.BookingDetail;
import model.UserProfile;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class BookingDAO extends DBContext {

    // =========================================
    // INSERT BOOKING
    // =========================================
    public boolean insert(Booking booking) {
        String sql = """
            INSERT INTO BOOKING (CAR_ID, USER_ID, START_DATE, END_DATE, PICKUP_TIME, DROPOFF_TIME, TOTAL_PRICE, STATUS, CREATED_AT, LOCATION)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, booking.getCarId());
            ps.setInt(2, booking.getUserId());
            ps.setObject(3, booking.getStartDate());
            ps.setObject(4, booking.getEndDate());
            ps.setObject(5, booking.getPickupTime());   // 👈 thêm giờ nhận
            ps.setObject(6, booking.getDropoffTime());  // 👈 thêm giờ trả
            ps.setDouble(7, booking.getTotalPrice());
            ps.setString(8, booking.getStatus());
            ps.setObject(9, booking.getCreatedAt());
            ps.setString(10, booking.getLocation());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // =========================================
    // CHECK CAR AVAILABILITY (tránh trùng lịch)
    // =========================================
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
                int count = rs.getInt(1);
                return count == 0; // true = available
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // =========================================
    // LẤY BOOKING THEO USER
    // =========================================
    public List<BookingDetail> getRecentBookingDetails(int userId, int limit) {
        List<BookingDetail> list = new ArrayList<>();
        String sql = """
            SELECT TOP (?) 
                   b.BOOKING_ID, 
                   c.MODEL AS carName,
                   b.START_DATE, b.END_DATE, 
                   b.PICKUP_TIME, b.DROPOFF_TIME, 
                   b.TOTAL_PRICE, b.STATUS, b.LOCATION
            FROM BOOKING b
            JOIN CAR c ON b.CAR_ID = c.CAR_ID
            WHERE b.USER_ID = ?
            ORDER BY b.CREATED_AT DESC
        """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, limit);
            ps.setInt(2, userId);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                BookingDetail detail = new BookingDetail();
                detail.setBookingId(rs.getInt("BOOKING_ID"));
                detail.setCarName(rs.getString("carName"));
                detail.setStartDate(rs.getObject("START_DATE", LocalDate.class));
                detail.setEndDate(rs.getObject("END_DATE", LocalDate.class));
                detail.setPickupTime(rs.getObject("PICKUP_TIME", LocalTime.class));   // 👈 thêm
                detail.setDropoffTime(rs.getObject("DROPOFF_TIME", LocalTime.class)); // 👈 thêm
                detail.setStatus(rs.getString("STATUS"));
                detail.setLocation(rs.getString("LOCATION"));
                detail.setTotalPrice(rs.getDouble("TOTAL_PRICE"));
                list.add(detail);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // =========================================
    // COUNT BOOKING BY USER / OWNER / STATUS
    // =========================================
    public int countByUser(int userId) {
        String sql = "SELECT COUNT(*) FROM BOOKING WHERE USER_ID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int countByStatus(int userId, String status) {
        String sql = "SELECT COUNT(*) FROM BOOKING WHERE USER_ID = ? AND STATUS = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, status);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int countByOwner(int ownerId) {
        String sql = """
            SELECT COUNT(*) 
            FROM BOOKING b 
            JOIN CAR c ON b.CAR_ID = c.CAR_ID
            WHERE c.OWNER_ID = ?
        """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, ownerId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int countByOwnerAndStatus(int ownerId, String status) {
        String sql = """
            SELECT COUNT(*) 
            FROM BOOKING b 
            JOIN CAR c ON b.CAR_ID = c.CAR_ID
            WHERE c.OWNER_ID = ? AND b.STATUS = ?
        """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, ownerId);
            ps.setString(2, status);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // =========================================
    // GET RECENT BOOKINGS BY OWNER
    // =========================================
    public List<BookingDetail> getRecentBookingsByOwner(int ownerId, int limit) {
        List<BookingDetail> list = new ArrayList<>();
        String sql = """
            SELECT TOP (?) 
                   b.BOOKING_ID, 
                   c.MODEL AS carName,
                   b.START_DATE, b.END_DATE, 
                   b.PICKUP_TIME, b.DROPOFF_TIME, 
                   b.TOTAL_PRICE, b.STATUS, b.LOCATION
            FROM BOOKING b
            JOIN CAR c ON b.CAR_ID = c.CAR_ID
            WHERE c.OWNER_ID = ?
            ORDER BY b.CREATED_AT DESC
        """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, limit);
            ps.setInt(2, ownerId);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                BookingDetail detail = new BookingDetail();
                detail.setBookingId(rs.getInt("BOOKING_ID"));
                detail.setCarName(rs.getString("carName"));
                detail.setStartDate(rs.getObject("START_DATE", LocalDate.class));
                detail.setEndDate(rs.getObject("END_DATE", LocalDate.class));
                detail.setPickupTime(rs.getObject("PICKUP_TIME", LocalTime.class));   // 👈 thêm
                detail.setDropoffTime(rs.getObject("DROPOFF_TIME", LocalTime.class)); // 👈 thêm
                detail.setStatus(rs.getString("STATUS"));
                detail.setLocation(rs.getString("LOCATION"));
                detail.setTotalPrice(rs.getDouble("TOTAL_PRICE"));
                list.add(detail);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // =========================================
    // CẬP NHẬT TRẠNG THÁI BOOKING
    // =========================================
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

    // Lấy danh sách yêu cầu booking của chủ xe

    public List<BookingDetail> getPendingBookingsForOwner(int ownerId) {
        List<BookingDetail> list = new ArrayList<>();
        String sql = """
        SELECT 
            b.BOOKING_ID,
            c.BRAND + ' ' + c.MODEL AS carName,
            u.FULL_NAME AS customerName,
            u.PHONE AS customerPhone,
            b.START_DATE,
            b.END_DATE,
            b.PICKUP_TIME,
            b.DROPOFF_TIME,
            b.LOCATION,
            b.TOTAL_PRICE,
            b.STATUS
        FROM BOOKING b
        JOIN CAR c ON b.CAR_ID = c.CAR_ID
        JOIN USER_PROFILE u ON b.USER_ID = u.USER_ID
        WHERE c.USER_ID = ?
        ORDER BY b.BOOKING_ID DESC
    """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, ownerId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                BookingDetail bd = new BookingDetail();
                UserProfile up = new UserProfile();

                // === Booking detail ===
                bd.setBookingId(rs.getInt("BOOKING_ID"));
                bd.setCarName(rs.getString("carName"));
                bd.setStartDate(rs.getObject("START_DATE", LocalDate.class));
                bd.setEndDate(rs.getObject("END_DATE", LocalDate.class));
                bd.setPickupTime(rs.getObject("PICKUP_TIME", LocalTime.class));
                bd.setDropoffTime(rs.getObject("DROPOFF_TIME", LocalTime.class));
                bd.setLocation(rs.getString("LOCATION"));
                bd.setTotalPrice(rs.getDouble("TOTAL_PRICE"));
                bd.setStatus(rs.getString("STATUS"));

                // === Customer info ===
                up.setFullName(rs.getString("customerName"));
                up.setPhone(rs.getString("customerPhone"));

                // Gắn thông tin userProfile vào booking detail
                bd.setCustomerProfile(up);

                list.add(bd);

//                BookingDetail bd = new BookingDetail();
//                UserProfile up = new UserProfile();
//                bd.setBookingId(rs.getInt("BOOKING_ID"));
//                bd.setCarName(rs.getString("carName"));
//                up.setFullName(rs.getString("customerName"));
//                up.setPhone(rs.getString("customerPhone"));
//                bd.setStartDate(rs.getObject("START_DATE", LocalDate.class));
//                bd.setEndDate(rs.getObject("END_DATE", LocalDate.class));
//                bd.setPickupTime(rs.getObject("PICKUP_TIME", LocalTime.class));
//                bd.setDropoffTime(rs.getObject("DROPOFF_TIME", LocalTime.class));
//                bd.setLocation(rs.getString("LOCATION"));
//                bd.setTotalPrice(rs.getDouble("TOTAL_PRICE"));
//                bd.setStatus(rs.getString("STATUS"));
//                list.add(bd);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Cập nhật trạng thái bookin
    public boolean updateBookingStatus(int bookingId, String newStatus) {
        String sql = "UPDATE BOOKING SET STATUS = ? WHERE BOOKING_ID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, newStatus);
            ps.setInt(2, bookingId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


}
