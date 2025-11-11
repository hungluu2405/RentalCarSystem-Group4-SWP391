package dao.implement;

import dao.DBContext;
import model.Booking;
import model.BookingDetail;
import model.UserProfile;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class BookingDAO extends DBContext {

    public boolean insert(Booking booking) {
        String sql = """
                    INSERT INTO BOOKING (CAR_ID, USER_ID, START_DATE, END_DATE, PICKUP_TIME, DROPOFF_TIME, TOTAL_PRICE, STATUS, CREATED_AT, LOCATION)
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection conn = null;

        try {
            conn = getConnection();
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setInt(1, booking.getCarId());
            ps.setInt(2, booking.getUserId());
            ps.setObject(3, booking.getStartDate());
            ps.setObject(4, booking.getEndDate());
            ps.setObject(5, booking.getPickupTime());
            ps.setObject(6, booking.getDropoffTime());
            ps.setDouble(7, booking.getTotalPrice());
            ps.setString(8, booking.getStatus());
            ps.setObject(9, booking.getCreatedAt());
            ps.setString(10, booking.getLocation());

            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    int newBookingId = rs.getInt(1);
                    booking.setBookingId(newBookingId);
                    return true;
                }
            }
            return false;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isCarAvailable(int carId, LocalDate startDate, LocalTime startTime,
                                  LocalDate endDate, LocalTime endTime) {

        String sql = """
        SELECT START_DATE, END_DATE, PICKUP_TIME, DROPOFF_TIME
        FROM BOOKING
        WHERE CAR_ID = ?
          AND STATUS IN ('Pending', 'Approved', 'Paid', 'Returning')
    """;

        LocalDateTime requestStart = LocalDateTime.of(startDate, startTime);
        LocalDateTime requestEnd = LocalDateTime.of(endDate, endTime);

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, carId);
            ResultSet rs = ps.executeQuery();

            int conflictCount = 0;
            while (rs.next()) {
                LocalDate existingStart = rs.getObject("START_DATE", LocalDate.class);
                LocalDate existingEnd = rs.getObject("END_DATE", LocalDate.class);
                LocalTime existingPickup = rs.getObject("PICKUP_TIME", LocalTime.class);
                LocalTime existingDropoff = rs.getObject("DROPOFF_TIME", LocalTime.class);

                LocalDateTime existingStartDateTime = LocalDateTime.of(existingStart, existingPickup);
                LocalDateTime existingEndDateTime = LocalDateTime.of(existingEnd, existingDropoff);

                boolean isOverlap = !(existingEndDateTime.isBefore(requestStart) ||
                        existingEndDateTime.isEqual(requestStart) ||
                        existingStartDateTime.isAfter(requestEnd) ||
                        existingStartDateTime.isEqual(requestEnd));

                if (isOverlap) {
                    conflictCount++;
                    System.out.println("⚠️ Conflict found:");
                    System.out.println("   Existing: " + existingStartDateTime + " → " + existingEndDateTime);
                }
            }

            if (conflictCount > 0) {
                System.out.println("⚠️ Car " + carId + " is NOT available! Conflicts: " + conflictCount);
                System.out.println("   Request: " + requestStart + " → " + requestEnd);
            } else {
                System.out.println("✅ Car " + carId + " is available!");
            }

            return conflictCount == 0;

        } catch (SQLException e) {
            System.err.println("❌ Error: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

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
                detail.setPickupTime(rs.getObject("PICKUP_TIME", LocalTime.class));
                detail.setDropoffTime(rs.getObject("DROPOFF_TIME", LocalTime.class));
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
                    WHERE c.USER_ID = ?
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
                    WHERE c.USER_ID = ? AND b.STATUS = ?
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

    public List<BookingDetail> getRecentBookingsByOwner(int ownerId, int limit) {
        List<BookingDetail> list = new ArrayList<>();
        String sql = """
                    SELECT TOP (?) 
                           b.BOOKING_ID, 
                           c.MODEL AS carName,
                           b.START_DATE, b.END_DATE, 
                           b.PICKUP_TIME, b.DROPOFF_TIME, 
                           b.TOTAL_PRICE, b.STATUS
                    FROM BOOKING b
                    JOIN CAR c ON b.CAR_ID = c.CAR_ID
                    WHERE c.USER_ID = ?
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
                detail.setPickupTime(rs.getObject("PICKUP_TIME", LocalTime.class));
                detail.setDropoffTime(rs.getObject("DROPOFF_TIME", LocalTime.class));
                detail.setStatus(rs.getString("STATUS"));
                detail.setTotalPrice(rs.getDouble("TOTAL_PRICE"));
                list.add(detail);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

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

    public List<BookingDetail> getBookingDetailsByUserId(int userId, int limit) {
        List<BookingDetail> list = new ArrayList<>();
        String sql = """
                    SELECT TOP (?) 
                           b.BOOKING_ID, 
                           c.MODEL + ' ' + c.BRAND AS carName,
                           b.START_DATE, b.END_DATE, 
                           b.PICKUP_TIME, b.DROPOFF_TIME, 
                           b.TOTAL_PRICE, b.STATUS, c.LOCATION
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
                detail.setPickupTime(rs.getObject("PICKUP_TIME", LocalTime.class));
                detail.setDropoffTime(rs.getObject("DROPOFF_TIME", LocalTime.class));
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

    // ĐẾM SỐ BOOKING THEO TRẠNG THÁI

    public int countPendingBookingsByOwner(int ownerId) {
        String sql = "SELECT COUNT(*) FROM BOOKING B " +
                "JOIN CAR C ON B.CAR_ID = C.CAR_ID " +
                "        JOIN USER_PROFILE u ON b.USER_ID = u.USER_ID"+
                "WHERE C.USER_ID = ? AND B.STATUS = 'Pending'";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, ownerId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int countActiveBookingsByOwner(int userId) {
        String sql = "SELECT COUNT(*) FROM BOOKING B " +
                "JOIN CAR C ON B.CAR_ID = C.CAR_ID " +
                "JOIN USER_PROFILE u ON b.USER_ID = u.USER_ID " +
                "WHERE C.USER_ID = ? AND B.STATUS IN ('Approved', 'Paid', 'Returning')";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int countHistoryBookingsByOwner(int ownerId) {
        String sql = "SELECT COUNT(*) FROM BOOKING B " +
                "JOIN CAR C ON B.CAR_ID = C.CAR_ID " +
                "JOIN USER_PROFILE u ON b.USER_ID = u.USER_ID " +
                "WHERE C.USER_ID = ? AND B.STATUS IN ('Rejected', 'Completed')";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, ownerId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Get pending bookings (status = 'Pending') by owner with pagination
     */
    public List<BookingDetail> getPendingBookingsByOwner(int userId, int offset, int limit) {
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
            b.TOTAL_PRICE,
            b.STATUS
            FROM BOOKING b
            JOIN CAR c ON b.CAR_ID = c.CAR_ID
            JOIN USER_PROFILE u ON b.USER_ID = u.USER_ID
            WHERE c.USER_ID = ?
        AND b.STATUS = 'Pending'
         ORDER BY b.CREATED_AT DESC
        OFFSET ? ROWS FETCH NEXT ? ROWS ONLY
        """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, offset);
            ps.setInt(3, limit);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                BookingDetail bd = new BookingDetail();
                UserProfile up = new UserProfile();

                bd.setBookingId(rs.getInt("BOOKING_ID"));
                bd.setCarName(rs.getString("carName"));
                bd.setStartDate(rs.getObject("START_DATE", LocalDate.class));
                bd.setEndDate(rs.getObject("END_DATE", LocalDate.class));
                bd.setPickupTime(rs.getObject("PICKUP_TIME", LocalTime.class));
                bd.setDropoffTime(rs.getObject("DROPOFF_TIME", LocalTime.class));
                bd.setTotalPrice(rs.getDouble("TOTAL_PRICE"));
                bd.setStatus(rs.getString("STATUS"));

                up.setFullName(rs.getString("customerName"));
                up.setPhone(rs.getString("customerPhone"));
                bd.setCustomerProfile(up);
                list.add(bd);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Get active bookings (status = 'Approved' or 'Paid') by owner with pagination
     */
    public List<BookingDetail> getActiveBookingsByOwner(int userId, int offset, int limit) {
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
            b.TOTAL_PRICE,
            b.STATUS
            FROM BOOKING b
            JOIN CAR c ON b.CAR_ID = c.CAR_ID
            JOIN USER_PROFILE u ON b.USER_ID = u.USER_ID
            WHERE c.USER_ID = ?
        AND b.STATUS IN ('Approved', 'Paid', 'Returning')
         ORDER BY b.CREATED_AT DESC
        OFFSET ? ROWS FETCH NEXT ? ROWS ONLY
        """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, offset);
            ps.setInt(3, limit);

            System.out.println("🔍 DEBUG getActiveBookingsByOwner - OwnerID: " + userId + ", Offset: " + offset + ", Limit: " + limit);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                BookingDetail bd = new BookingDetail();
                UserProfile up = new UserProfile();

                bd.setBookingId(rs.getInt("BOOKING_ID"));
                bd.setCarName(rs.getString("carName"));
                bd.setStartDate(rs.getObject("START_DATE", LocalDate.class));
                bd.setEndDate(rs.getObject("END_DATE", LocalDate.class));
                bd.setPickupTime(rs.getObject("PICKUP_TIME", LocalTime.class));
                bd.setDropoffTime(rs.getObject("DROPOFF_TIME", LocalTime.class));
                bd.setTotalPrice(rs.getDouble("TOTAL_PRICE"));
                bd.setStatus(rs.getString("STATUS"));

                up.setFullName(rs.getString("customerName"));
                up.setPhone(rs.getString("customerPhone"));
                bd.setCustomerProfile(up);
                list.add(bd);

                System.out.println("   📋 Found booking: ID=" + bd.getBookingId() + ", Status=" + bd.getStatus() + ", Car=" + bd.getCarName());

            }
            System.out.println("   ✅ Total active bookings found: " + list.size());
        } catch (SQLException e) {
            System.err.println("❌ Error in getActiveBookingsByOwner: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Get history bookings (status = 'Rejected' or 'Completed') by owner with pagination
     */
    public List<BookingDetail> getHistoryBookingsByOwner(int userId, int offset, int limit) {
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
            b.TOTAL_PRICE,
            b.STATUS
            FROM BOOKING b
            JOIN CAR c ON b.CAR_ID = c.CAR_ID
            JOIN USER_PROFILE u ON b.USER_ID = u.USER_ID
            WHERE c.USER_ID = ?
            AND b.STATUS IN ('Rejected', 'Completed')
            ORDER BY b.CREATED_AT DESC
             OFFSET ? ROWS FETCH NEXT ? ROWS ONLY
        """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, offset);
            ps.setInt(3, limit);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                BookingDetail bd = new BookingDetail();
                UserProfile up = new UserProfile();

                bd.setBookingId(rs.getInt("BOOKING_ID"));
                bd.setCarName(rs.getString("carName"));
                bd.setStartDate(rs.getObject("START_DATE", LocalDate.class));
                bd.setEndDate(rs.getObject("END_DATE", LocalDate.class));
                bd.setPickupTime(rs.getObject("PICKUP_TIME", LocalTime.class));
                bd.setDropoffTime(rs.getObject("DROPOFF_TIME", LocalTime.class));
                bd.setTotalPrice(rs.getDouble("TOTAL_PRICE"));
                bd.setStatus(rs.getString("STATUS"));

                up.setFullName(rs.getString("customerName"));
                up.setPhone(rs.getString("customerPhone"));
                bd.setCustomerProfile(up);
                list.add(bd);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<BookingDetail> getAllBookingsForOwner(int ownerId, int limit) {
        List<BookingDetail> list = new ArrayList<>();

        String sql = """
                    SELECT TOP (?) 
                        b.BOOKING_ID,
                        c.BRAND + ' ' + c.MODEL AS carName,
                        u.FULL_NAME AS customerName,
                        u.PHONE AS customerPhone,
                        b.START_DATE,
                        b.END_DATE,
                        b.PICKUP_TIME,
                        b.DROPOFF_TIME,
                        b.TOTAL_PRICE,
                        b.STATUS
                    FROM BOOKING b
                    JOIN CAR c ON b.CAR_ID = c.CAR_ID
                    JOIN USER_PROFILE u ON b.USER_ID = u.USER_ID
                    WHERE c.USER_ID = ?
                    ORDER BY b.BOOKING_ID DESC
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, limit);
            ps.setInt(2, ownerId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                BookingDetail bd = new BookingDetail();
                UserProfile up = new UserProfile();

                bd.setBookingId(rs.getInt("BOOKING_ID"));
                bd.setCarName(rs.getString("carName"));
                bd.setStartDate(rs.getObject("START_DATE", LocalDate.class));
                bd.setEndDate(rs.getObject("END_DATE", LocalDate.class));
                bd.setPickupTime(rs.getObject("PICKUP_TIME", LocalTime.class));
                bd.setDropoffTime(rs.getObject("DROPOFF_TIME", LocalTime.class));
                bd.setTotalPrice(rs.getDouble("TOTAL_PRICE"));
                bd.setStatus(rs.getString("STATUS"));

                up.setFullName(rs.getString("customerName"));
                up.setPhone(rs.getString("customerPhone"));
                bd.setCustomerProfile(up);

                list.add(bd);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

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
                        b.TOTAL_PRICE,
                        b.STATUS
                    FROM BOOKING b
                    JOIN CAR c ON b.CAR_ID = c.CAR_ID
                    JOIN USER_PROFILE u ON b.USER_ID = u.USER_ID
                    WHERE c.USER_ID = ?
                    AND b.STATUS = 'Pending'
                    ORDER BY b.BOOKING_ID DESC
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, ownerId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                BookingDetail bd = new BookingDetail();
                UserProfile up = new UserProfile();

                bd.setBookingId(rs.getInt("BOOKING_ID"));
                bd.setCarName(rs.getString("carName"));
                bd.setStartDate(rs.getObject("START_DATE", LocalDate.class));
                bd.setEndDate(rs.getObject("END_DATE", LocalDate.class));
                bd.setPickupTime(rs.getObject("PICKUP_TIME", LocalTime.class));
                bd.setDropoffTime(rs.getObject("DROPOFF_TIME", LocalTime.class));
                bd.setTotalPrice(rs.getDouble("TOTAL_PRICE"));
                bd.setStatus(rs.getString("STATUS"));

                up.setFullName(rs.getString("customerName"));
                up.setPhone(rs.getString("customerPhone"));

                bd.setCustomerProfile(up);

                list.add(bd);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<BookingDetail> getHistoryBookingsForOwner(int ownerId) {
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
                        b.TOTAL_PRICE,
                        b.STATUS
                    FROM BOOKING b
                    JOIN CAR c ON b.CAR_ID = c.CAR_ID
                    JOIN USER_PROFILE u ON b.USER_ID = u.USER_ID
                    WHERE c.USER_ID = ?
                    AND b.STATUS IN ('Approved', 'Completed', 'Rejected', 'Paid')
                    ORDER BY b.BOOKING_ID DESC
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, ownerId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                BookingDetail bd = new BookingDetail();
                UserProfile up = new UserProfile();

                bd.setBookingId(rs.getInt("BOOKING_ID"));
                bd.setCarName(rs.getString("carName"));
                bd.setStartDate(rs.getObject("START_DATE", LocalDate.class));
                bd.setEndDate(rs.getObject("END_DATE", LocalDate.class));
                bd.setPickupTime(rs.getObject("PICKUP_TIME", LocalTime.class));
                bd.setDropoffTime(rs.getObject("DROPOFF_TIME", LocalTime.class));
                bd.setTotalPrice(rs.getDouble("TOTAL_PRICE"));
                bd.setStatus(rs.getString("STATUS"));

                up.setFullName(rs.getString("customerName"));
                up.setPhone(rs.getString("customerPhone"));

                bd.setCustomerProfile(up);

                list.add(bd);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public int countBookingsByOwner(int ownerId) {
        String sql = """
                    SELECT COUNT(*) 
                    FROM BOOKING b 
                    JOIN CAR c ON b.CAR_ID = c.CAR_ID
                    WHERE c.USER_ID = ?
                """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, ownerId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int countBookingsByStatus(int ownerId, String status) {
        String sql = """
                    SELECT COUNT(*) 
                    FROM BOOKING b
                    JOIN CAR c ON b.CAR_ID = c.CAR_ID
                    WHERE c.USER_ID = ? AND b.STATUS = ?
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, ownerId);
            ps.setString(2, status);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<BookingDetail> getBookingsByStatus(int ownerId, String status, int offset, int limit) {
        String sql = """
                    SELECT b.BOOKING_ID, b.START_DATE, b.END_DATE, b.PICKUP_TIME, b.DROPOFF_TIME,
                           b.TOTAL_PRICE, b.STATUS,
                           u.FULL_NAME, u.PHONE, 
                           c.BRAND + ' ' + c.MODEL AS carName
                    FROM BOOKING b
                    JOIN CAR c ON b.CAR_ID = c.CAR_ID
                    JOIN USER_PROFILE u ON b.USER_ID = u.USER_ID
                    WHERE c.USER_ID = ? AND b.STATUS = ?
                    ORDER BY b.BOOKING_ID DESC
                    OFFSET ? ROWS FETCH NEXT ? ROWS ONLY
                """;

        List<BookingDetail> list = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, ownerId);
            ps.setString(2, status);
            ps.setInt(3, offset);
            ps.setInt(4, limit);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                BookingDetail bd = new BookingDetail();
                UserProfile up = new UserProfile();

                bd.setBookingId(rs.getInt("BOOKING_ID"));
                bd.setCarName(rs.getString("carName"));
                bd.setStartDate(rs.getObject("START_DATE", LocalDate.class));
                bd.setEndDate(rs.getObject("END_DATE", LocalDate.class));
                bd.setPickupTime(rs.getObject("PICKUP_TIME", LocalTime.class));
                bd.setDropoffTime(rs.getObject("DROPOFF_TIME", LocalTime.class));
                bd.setTotalPrice(rs.getDouble("TOTAL_PRICE"));
                bd.setStatus(rs.getString("STATUS"));

                up.setFullName(rs.getString("FULL_NAME"));
                up.setPhone(rs.getString("PHONE"));
                bd.setCustomerProfile(up);

                list.add(bd);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<BookingDetail> getBookingsByOwnerWithPaging(int ownerId, int page, int pageSize) {
        List<BookingDetail> list = new ArrayList<>();
        int offset = (page - 1) * pageSize;

        String sql = """
                    SELECT b.BOOKING_ID, b.START_DATE, b.END_DATE, b.PICKUP_TIME, b.DROPOFF_TIME,
                           b.TOTAL_PRICE, b.STATUS,
                           c.BRAND + ' ' + c.MODEL AS carName,
                           u.FULL_NAME, u.PHONE
                    FROM BOOKING b
                    JOIN CAR c ON b.CAR_ID = c.CAR_ID
                    JOIN USER_PROFILE u ON b.USER_ID = u.USER_ID
                    WHERE c.USER_ID = ?
                    ORDER BY b.BOOKING_ID DESC
                    OFFSET ? ROWS FETCH NEXT ? ROWS ONLY
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, ownerId);
            ps.setInt(2, offset);
            ps.setInt(3, pageSize);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                BookingDetail bd = new BookingDetail();
                UserProfile up = new UserProfile();

                bd.setBookingId(rs.getInt("BOOKING_ID"));
                bd.setCarName(rs.getString("carName"));
                bd.setStartDate(rs.getObject("START_DATE", LocalDate.class));
                bd.setEndDate(rs.getObject("END_DATE", LocalDate.class));
                bd.setPickupTime(rs.getObject("PICKUP_TIME", LocalTime.class));
                bd.setDropoffTime(rs.getObject("DROPOFF_TIME", LocalTime.class));
                bd.setTotalPrice(rs.getDouble("TOTAL_PRICE"));
                bd.setStatus(rs.getString("STATUS"));

                up.setFullName(rs.getString("FULL_NAME"));
                up.setPhone(rs.getString("PHONE"));
                bd.setCustomerProfile(up);

                list.add(bd);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<Booking> getAllBookings() throws SQLException {
        List<Booking> list = new ArrayList<>();
        String sql = """
                    SELECT b.BOOKING_ID, c.MODEL, u.FULL_NAME, 
                           b.START_DATE, b.END_DATE, b.TOTAL_PRICE, 
                           b.STATUS, b.CREATED_AT, b.PICKUP_TIME, b.DROPOFF_TIME,
                           b.CAR_ID, b.USER_ID
                    FROM BOOKING b
                    JOIN USER_PROFILE u ON b.USER_ID = u.USER_ID
                    JOIN CAR c ON b.CAR_ID = c.CAR_ID
                    ORDER BY b.BOOKING_ID DESC
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Booking b = new Booking();

                b.setBookingId(rs.getInt("BOOKING_ID"));
                b.setCarId(rs.getInt("CAR_ID"));
                b.setUserId(rs.getInt("USER_ID"));
                b.setStartDate(rs.getDate("START_DATE").toLocalDate());
                b.setEndDate(rs.getDate("END_DATE").toLocalDate());
                b.setTotalPrice(rs.getDouble("TOTAL_PRICE"));
                b.setStatus(rs.getString("STATUS"));

                Timestamp createdAt = rs.getTimestamp("CREATED_AT");
                if (createdAt != null) {
                    b.setCreatedAt(createdAt.toLocalDateTime());
                }

                Time pickup = rs.getTime("PICKUP_TIME");
                if (pickup != null) {
                    b.setPickupTime(pickup.toLocalTime());
                }

                Time dropoff = rs.getTime("DROPOFF_TIME");
                if (dropoff != null) {
                    b.setDropoffTime(dropoff.toLocalTime());
                }

                b.setCarModel(rs.getString("MODEL"));
                b.setUserFullName(rs.getString("FULL_NAME"));

                list.add(b);
            }
        }
        return list;
    }

    public int countAllBookings() {
        String sql = "SELECT COUNT(*) AS total FROM BOOKING";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    public Booking getBookingById(int bookingId) {
        Booking booking = null;
        String sql = """
                    SELECT BOOKING_ID, CAR_ID, USER_ID, START_DATE, END_DATE, 
                           PICKUP_TIME, DROPOFF_TIME, TOTAL_PRICE, STATUS, LOCATION, CREATED_AT 
                    FROM BOOKING 
                    WHERE BOOKING_ID = ?
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, bookingId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                booking = new Booking();

                booking.setBookingId(rs.getInt("BOOKING_ID"));
                booking.setCarId(rs.getInt("CAR_ID"));
                booking.setUserId(rs.getInt("USER_ID"));

                booking.setTotalPrice(rs.getDouble("TOTAL_PRICE"));
                booking.setStatus(rs.getString("STATUS"));

                booking.setStartDate(rs.getObject("START_DATE", LocalDate.class));
                booking.setEndDate(rs.getObject("END_DATE", LocalDate.class));
                booking.setPickupTime(rs.getObject("PICKUP_TIME", LocalTime.class));
                booking.setDropoffTime(rs.getObject("DROPOFF_TIME", LocalTime.class));
                booking.setLocation(rs.getString("LOCATION"));

                Timestamp createdAt = rs.getTimestamp("CREATED_AT");
                if (createdAt != null) {
                    booking.setCreatedAt(createdAt.toLocalDateTime());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return booking;
    }

    public boolean insertPaymentRecord(int bookingId, String paymentId, double amount, String status) {
        String sql = """
                    INSERT INTO PAYMENT (BOOKING_ID, AMOUNT, METHOD, STATUS, PAYPAL_TRANSACTION_ID, PAID_AT)
                    VALUES (?, ?, ?, ?, ?, GETDATE())
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, bookingId);
            ps.setDouble(2, amount);
            ps.setString(3, "PayPal");
            ps.setString(4, status);
            ps.setString(5, paymentId);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Booking> getBookingsByCarAndMonth(int carId, int year, int month) {
        String sql = """
                    SELECT * FROM BOOKING 
                    WHERE CAR_ID = ? 
                    AND ((YEAR(START_DATE) = ? AND MONTH(START_DATE) = ?) 
                    OR (YEAR(END_DATE) = ? AND MONTH(END_DATE) = ?) 
                    OR (START_DATE <= ? AND END_DATE >= ?)) 
                    ORDER BY START_DATE
                """;

        List<Booking> bookings = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            LocalDate firstDay = LocalDate.of(year, month, 1);
            LocalDate lastDay = firstDay.withDayOfMonth(firstDay.lengthOfMonth());

            ps.setInt(1, carId);
            ps.setInt(2, year);
            ps.setInt(3, month);
            ps.setInt(4, year);
            ps.setInt(5, month);
            ps.setDate(6, Date.valueOf(lastDay));
            ps.setDate(7, Date.valueOf(firstDay));

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Booking booking = new Booking();
                booking.setBookingId(rs.getInt("BOOKING_ID"));
                booking.setCarId(rs.getInt("CAR_ID"));
                booking.setUserId(rs.getInt("USER_ID"));
                booking.setStartDate(rs.getDate("START_DATE").toLocalDate());
                booking.setEndDate(rs.getDate("END_DATE").toLocalDate());
                booking.setStatus(rs.getString("STATUS"));
                booking.setTotalPrice(rs.getDouble("TOTAL_PRICE"));
                booking.setCreatedAt(rs.getTimestamp("CREATED_AT").toLocalDateTime());
                bookings.add(booking);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bookings;
    }
    public boolean isBookingOwnedByUser(int bookingId, int userId) {
        String sql = """
        SELECT COUNT(*) 
        FROM BOOKING 
        WHERE BOOKING_ID = ? AND USER_ID = ?
    """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, bookingId);
            ps.setInt(2, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public int countCurrentTripsByUserId(int userId) {
        String sql = """
        SELECT COUNT(*)
        FROM BOOKING
        WHERE USER_ID = ?
        AND STATUS IN ('Pending', 'Approved', 'Paid', 'Returning')
    """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Count history trips (Completed, Rejected, Cancelled)
     */
    public int countHistoryTripsByUserId(int userId) {
        String sql = """
        SELECT COUNT(*) 
        FROM BOOKING 
        WHERE USER_ID = ? 
        AND STATUS IN ('Completed', 'Rejected', 'Cancelled')
    """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Get current trips with pagination
     */
    public List<BookingDetail> getCurrentTripsByUserId(int userId, int offset, int limit) {
        List<BookingDetail> list = new ArrayList<>();
        String sql = """
        SELECT
            b.BOOKING_ID,
            c.MODEL + ' ' + c.BRAND AS carName,
            b.START_DATE, b.END_DATE,
            b.PICKUP_TIME, b.DROPOFF_TIME,
            b.TOTAL_PRICE, b.STATUS, c.LOCATION
        FROM BOOKING b
        JOIN CAR c ON b.CAR_ID = c.CAR_ID
        WHERE b.USER_ID = ?
        AND b.STATUS IN ('Pending', 'Approved', 'Paid', 'Returning')
        ORDER BY b.CREATED_AT DESC
        OFFSET ? ROWS FETCH NEXT ? ROWS ONLY
    """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, offset);
            ps.setInt(3, limit);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                BookingDetail detail = new BookingDetail();
                detail.setBookingId(rs.getInt("BOOKING_ID"));
                detail.setCarName(rs.getString("carName"));
                detail.setStartDate(rs.getObject("START_DATE", LocalDate.class));
                detail.setEndDate(rs.getObject("END_DATE", LocalDate.class));
                detail.setPickupTime(rs.getObject("PICKUP_TIME", LocalTime.class));
                detail.setDropoffTime(rs.getObject("DROPOFF_TIME", LocalTime.class));
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

    /**
     * Get history trips with pagination
     */
    public List<BookingDetail> getHistoryTripsByUserId(int userId, int offset, int limit) {
        List<BookingDetail> list = new ArrayList<>();
        String sql = """
        SELECT 
            b.BOOKING_ID, 
            c.MODEL + ' ' + c.BRAND AS carName,
            b.START_DATE, b.END_DATE, 
            b.PICKUP_TIME, b.DROPOFF_TIME, 
            b.TOTAL_PRICE, b.STATUS, c.LOCATION
        FROM BOOKING b
        JOIN CAR c ON b.CAR_ID = c.CAR_ID
        WHERE b.USER_ID = ?
        AND b.STATUS IN ('Completed', 'Rejected', 'Cancelled')
        ORDER BY b.CREATED_AT DESC
        OFFSET ? ROWS FETCH NEXT ? ROWS ONLY
    """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, offset);
            ps.setInt(3, limit);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                BookingDetail detail = new BookingDetail();
                detail.setBookingId(rs.getInt("BOOKING_ID"));
                detail.setCarName(rs.getString("carName"));
                detail.setStartDate(rs.getObject("START_DATE", LocalDate.class));
                detail.setEndDate(rs.getObject("END_DATE", LocalDate.class));
                detail.setPickupTime(rs.getObject("PICKUP_TIME", LocalTime.class));
                detail.setDropoffTime(rs.getObject("DROPOFF_TIME", LocalTime.class));
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

    public List<Booking> getBookingsFiltered(String dateRange, String priceRange) throws SQLException {
        List<Booking> list = new ArrayList<>();

        StringBuilder sql = new StringBuilder("""
        SELECT b.BOOKING_ID, c.MODEL, u.FULL_NAME, 
               b.START_DATE, b.END_DATE, b.TOTAL_PRICE, 
               b.STATUS, b.CREATED_AT, b.PICKUP_TIME, b.DROPOFF_TIME,
               b.CAR_ID, b.USER_ID
        FROM BOOKING b
        JOIN USER_PROFILE u ON b.USER_ID = u.USER_ID
        JOIN CAR c ON b.CAR_ID = c.CAR_ID
        WHERE 1=1
    """);

        // ✅ Lọc theo thời gian
        if (dateRange != null && !dateRange.isEmpty()) {
            switch (dateRange) {
                case "today":
                    sql.append(" AND CAST(b.START_DATE AS DATE) = CAST(GETDATE() AS DATE)");
                    break;
                case "week":
                    sql.append(" AND DATEPART(WEEK, b.START_DATE) = DATEPART(WEEK, GETDATE())");
                    sql.append(" AND DATEPART(YEAR, b.START_DATE) = DATEPART(YEAR, GETDATE())");
                    break;
                case "month":
                    sql.append(" AND MONTH(b.START_DATE) = MONTH(GETDATE())");
                    sql.append(" AND YEAR(b.START_DATE) = YEAR(GETDATE())");
                    break;
                case "year":
                    sql.append(" AND YEAR(b.START_DATE) = YEAR(GETDATE())");
                    break;
            }
        }

        // ✅ Lọc theo khoảng giá
        if (priceRange != null && !priceRange.isEmpty()) {
            switch (priceRange) {
                case "under1tr":
                    sql.append(" AND b.TOTAL_PRICE < 1000000");
                    break;
                case "1trto1tr5":
                    sql.append(" AND b.TOTAL_PRICE BETWEEN 1000000 AND 1500000");
                    break;
                case "over1tr5":
                    sql.append(" AND b.TOTAL_PRICE > 1500000");
                    break;
            }
        }

        sql.append(" ORDER BY b.BOOKING_ID DESC");

        try (PreparedStatement ps = connection.prepareStatement(sql.toString());
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Booking b = new Booking();
                b.setBookingId(rs.getInt("BOOKING_ID"));
                b.setCarId(rs.getInt("CAR_ID"));
                b.setUserId(rs.getInt("USER_ID"));
                b.setStartDate(rs.getDate("START_DATE").toLocalDate());
                b.setEndDate(rs.getDate("END_DATE").toLocalDate());
                b.setTotalPrice(rs.getDouble("TOTAL_PRICE"));
                b.setStatus(rs.getString("STATUS"));

                Timestamp createdAt = rs.getTimestamp("CREATED_AT");
                if (createdAt != null) {
                    b.setCreatedAt(createdAt.toLocalDateTime());
                }

                Time pickup = rs.getTime("PICKUP_TIME");
                if (pickup != null) {
                    b.setPickupTime(pickup.toLocalTime());
                }

                Time dropoff = rs.getTime("DROPOFF_TIME");
                if (dropoff != null) {
                    b.setDropoffTime(dropoff.toLocalTime());
                }

                b.setCarModel(rs.getString("MODEL"));
                b.setUserFullName(rs.getString("FULL_NAME"));

                list.add(b);
            }
        }

        return list;
    }

    public List<BookingDetail> getCurrentBookingsByUserId(int userId, int page, int pageSize) {
        List<BookingDetail> bookings = new ArrayList<>();
        String sql = """
        SELECT b.booking_id, c.car_name, c.location,
               b.start_date, b.end_date, b.total_price,
               b.pickup_time, b.dropoff_time, b.status
        FROM Bookings b
        JOIN Cars c ON b.car_id = c.car_id
        WHERE b.user_id = ?
        AND b.status IN ('Pending', 'Approved', 'Paid', 'Returning')
        ORDER BY b.booking_id DESC
        LIMIT ? OFFSET ?
    """;

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setInt(2, pageSize);
            ps.setInt(3, (page - 1) * pageSize);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                BookingDetail bd = new BookingDetail();
                bd.setBookingId(rs.getInt("booking_id"));
                bd.setCarName(rs.getString("car_name"));
                bd.setLocation(rs.getString("location"));
                bd.setStartDate(LocalDate.parse(rs.getDate("start_date").toString()));
                bd.setEndDate(LocalDate.parse(rs.getDate("end_date").toString()));
                bd.setTotalPrice(rs.getDouble("total_price"));
                bd.setPickupTime(LocalTime.parse(rs.getString("pickup_time")));
                bd.setDropoffTime(LocalTime.parse(rs.getString("dropoff_time")));
                bd.setStatus(rs.getString("status"));
                bookings.add(bd);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }

    /**
     * Get history bookings (Completed, Rejected, Cancelled) with pagination
     */
    public List<BookingDetail> getHistoryBookingsByUserId(int userId, int page, int pageSize) {
        List<BookingDetail> bookings = new ArrayList<>();
        String sql = """
        SELECT b.booking_id, c.car_name, c.location,
               b.start_date, b.end_date, b.total_price,
               b.pickup_time, b.dropoff_time, b.status
        FROM Bookings b
        JOIN Cars c ON b.car_id = c.car_id
        WHERE b.user_id = ?
        AND b.status IN ('Completed', 'Rejected', 'Cancelled')
        ORDER BY b.booking_id DESC
        LIMIT ? OFFSET ?
    """;

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setInt(2, pageSize);
            ps.setInt(3, (page - 1) * pageSize);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                BookingDetail bd = new BookingDetail();
                bd.setBookingId(rs.getInt("booking_id"));
                bd.setCarName(rs.getString("car_name"));
                bd.setLocation(rs.getString("location"));
                bd.setStartDate(LocalDate.parse(rs.getDate("start_date").toString()));
                bd.setEndDate(LocalDate.parse(rs.getDate("end_date").toString()));
                bd.setTotalPrice(rs.getDouble("total_price"));
                bd.setPickupTime(LocalTime.parse(rs.getString("pickup_time")));
                bd.setDropoffTime(LocalTime.parse(rs.getString("dropoff_time")));
                bd.setStatus(rs.getString("status"));
                bookings.add(bd);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }

    /**
     * Count current bookings for pagination
     */
    public int countCurrentBookings(int userId) {
        String sql = """
        SELECT COUNT(*) FROM Bookings
        WHERE user_id = ?
        AND status IN ('Pending', 'Approved', 'Paid', 'Returning')
    """;

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Count history bookings for pagination
     */
    public int countHistoryBookings(int userId) {
        String sql = """
        SELECT COUNT(*) FROM Bookings
        WHERE user_id = ?
        AND status IN ('Completed', 'Rejected', 'Cancelled')
    """;

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<BookingDetail> getCurrentTripsWithFilters(
            int userId,
            String status,
            String startDateFrom,
            String startDateTo,
            String endDateFrom,
            String endDateTo,
            String carName,
            String priceRange,
            int offset,
            int limit) {

        List<BookingDetail> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("""
        SELECT
            b.BOOKING_ID,
            c.MODEL + ' ' + c.BRAND AS carName,
            b.START_DATE, b.END_DATE,
            b.PICKUP_TIME, b.DROPOFF_TIME,
            b.TOTAL_PRICE, b.STATUS, c.LOCATION
        FROM BOOKING b
        JOIN CAR c ON b.CAR_ID = c.CAR_ID
        WHERE b.USER_ID = ?
        AND b.STATUS IN ('Pending', 'Approved', 'Paid', 'Returning')
    """);

        // Filter by specific status
        if (status != null && !status.isEmpty() && !status.equals("All")) {
            sql.append(" AND b.STATUS = ?");
        }

        // Filter by start date range
        if (startDateFrom != null && !startDateFrom.isEmpty()) {
            sql.append(" AND b.START_DATE >= ?");
        }
        if (startDateTo != null && !startDateTo.isEmpty()) {
            sql.append(" AND b.START_DATE <= ?");
        }

        // Filter by end date range
        if (endDateFrom != null && !endDateFrom.isEmpty()) {
            sql.append(" AND b.END_DATE >= ?");
        }
        if (endDateTo != null && !endDateTo.isEmpty()) {
            sql.append(" AND b.END_DATE <= ?");
        }

        // Filter by car name
        if (carName != null && !carName.isEmpty()) {
            sql.append(" AND (c.MODEL + ' ' + c.BRAND) LIKE ?");
        }

        // Filter by price range
        if (priceRange != null && !priceRange.isEmpty()) {
            switch (priceRange) {
                case "under1m":
                    sql.append(" AND b.TOTAL_PRICE < 1000000");
                    break;
                case "1m-3m":
                    sql.append(" AND b.TOTAL_PRICE BETWEEN 1000000 AND 3000000");
                    break;
                case "3m-5m":
                    sql.append(" AND b.TOTAL_PRICE BETWEEN 3000000 AND 5000000");
                    break;
                case "over5m":
                    sql.append(" AND b.TOTAL_PRICE > 5000000");
                    break;
            }
        }

        sql.append(" ORDER BY b.CREATED_AT DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");

        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            int paramIndex = 1;
            ps.setInt(paramIndex++, userId);

            if (status != null && !status.isEmpty() && !status.equals("All")) {
                ps.setString(paramIndex++, status);
            }

            if (startDateFrom != null && !startDateFrom.isEmpty()) {
                ps.setString(paramIndex++, startDateFrom);
            }
            if (startDateTo != null && !startDateTo.isEmpty()) {
                ps.setString(paramIndex++, startDateTo);
            }

            if (endDateFrom != null && !endDateFrom.isEmpty()) {
                ps.setString(paramIndex++, endDateFrom);
            }
            if (endDateTo != null && !endDateTo.isEmpty()) {
                ps.setString(paramIndex++, endDateTo);
            }

            if (carName != null && !carName.isEmpty()) {
                ps.setString(paramIndex++, "%" + carName + "%");
            }

            ps.setInt(paramIndex++, offset);
            ps.setInt(paramIndex++, limit);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                BookingDetail detail = new BookingDetail();
                detail.setBookingId(rs.getInt("BOOKING_ID"));
                detail.setCarName(rs.getString("carName"));
                detail.setStartDate(rs.getObject("START_DATE", LocalDate.class));
                detail.setEndDate(rs.getObject("END_DATE", LocalDate.class));
                detail.setPickupTime(rs.getObject("PICKUP_TIME", LocalTime.class));
                detail.setDropoffTime(rs.getObject("DROPOFF_TIME", LocalTime.class));
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

    /**
     * Count current trips with filters
     */
    public int countCurrentTripsWithFilters(
            int userId,
            String status,
            String startDateFrom,
            String startDateTo,
            String endDateFrom,
            String endDateTo,
            String carName,
            String priceRange) {

        StringBuilder sql = new StringBuilder("""
        SELECT COUNT(*)
        FROM BOOKING b
        JOIN CAR c ON b.CAR_ID = c.CAR_ID
        WHERE b.USER_ID = ?
        AND b.STATUS IN ('Pending', 'Approved', 'Paid', 'Returning')
    """);

        if (status != null && !status.isEmpty() && !status.equals("All")) {
            sql.append(" AND b.STATUS = ?");
        }

        if (startDateFrom != null && !startDateFrom.isEmpty()) {
            sql.append(" AND b.START_DATE >= ?");
        }
        if (startDateTo != null && !startDateTo.isEmpty()) {
            sql.append(" AND b.START_DATE <= ?");
        }

        if (endDateFrom != null && !endDateFrom.isEmpty()) {
            sql.append(" AND b.END_DATE >= ?");
        }
        if (endDateTo != null && !endDateTo.isEmpty()) {
            sql.append(" AND b.END_DATE <= ?");
        }

        if (carName != null && !carName.isEmpty()) {
            sql.append(" AND (c.MODEL + ' ' + c.BRAND) LIKE ?");
        }

        if (priceRange != null && !priceRange.isEmpty()) {
            switch (priceRange) {
                case "under1m":
                    sql.append(" AND b.TOTAL_PRICE < 1000000");
                    break;
                case "1m-3m":
                    sql.append(" AND b.TOTAL_PRICE BETWEEN 1000000 AND 3000000");
                    break;
                case "3m-5m":
                    sql.append(" AND b.TOTAL_PRICE BETWEEN 3000000 AND 5000000");
                    break;
                case "over5m":
                    sql.append(" AND b.TOTAL_PRICE > 5000000");
                    break;
            }
        }

        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            int paramIndex = 1;
            ps.setInt(paramIndex++, userId);

            if (status != null && !status.isEmpty() && !status.equals("All")) {
                ps.setString(paramIndex++, status);
            }

            if (startDateFrom != null && !startDateFrom.isEmpty()) {
                ps.setString(paramIndex++, startDateFrom);
            }
            if (startDateTo != null && !startDateTo.isEmpty()) {
                ps.setString(paramIndex++, startDateTo);
            }

            if (endDateFrom != null && !endDateFrom.isEmpty()) {
                ps.setString(paramIndex++, endDateFrom);
            }
            if (endDateTo != null && !endDateTo.isEmpty()) {
                ps.setString(paramIndex++, endDateTo);
            }

            if (carName != null && !carName.isEmpty()) {
                ps.setString(paramIndex++, "%" + carName + "%");
            }

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Get history trips with filters
     */
    public List<BookingDetail> getHistoryTripsWithFilters(
            int userId,
            String status,
            String startDateFrom,
            String startDateTo,
            String endDateFrom,
            String endDateTo,
            String carName,
            String priceRange,
            int offset,
            int limit) {

        List<BookingDetail> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("""
        SELECT 
            b.BOOKING_ID, 
            c.MODEL + ' ' + c.BRAND AS carName,
            b.START_DATE, b.END_DATE, 
            b.PICKUP_TIME, b.DROPOFF_TIME, 
            b.TOTAL_PRICE, b.STATUS, c.LOCATION
        FROM BOOKING b
        JOIN CAR c ON b.CAR_ID = c.CAR_ID
        WHERE b.USER_ID = ?
        AND b.STATUS IN ('Completed', 'Rejected', 'Cancelled')
    """);

        if (status != null && !status.isEmpty() && !status.equals("All")) {
            sql.append(" AND b.STATUS = ?");
        }

        if (startDateFrom != null && !startDateFrom.isEmpty()) {
            sql.append(" AND b.START_DATE >= ?");
        }
        if (startDateTo != null && !startDateTo.isEmpty()) {
            sql.append(" AND b.START_DATE <= ?");
        }

        if (endDateFrom != null && !endDateFrom.isEmpty()) {
            sql.append(" AND b.END_DATE >= ?");
        }
        if (endDateTo != null && !endDateTo.isEmpty()) {
            sql.append(" AND b.END_DATE <= ?");
        }

        if (carName != null && !carName.isEmpty()) {
            sql.append(" AND (c.MODEL + ' ' + c.BRAND) LIKE ?");
        }

        if (priceRange != null && !priceRange.isEmpty()) {
            switch (priceRange) {
                case "under1m":
                    sql.append(" AND b.TOTAL_PRICE < 1000000");
                    break;
                case "1m-3m":
                    sql.append(" AND b.TOTAL_PRICE BETWEEN 1000000 AND 3000000");
                    break;
                case "3m-5m":
                    sql.append(" AND b.TOTAL_PRICE BETWEEN 3000000 AND 5000000");
                    break;
                case "over5m":
                    sql.append(" AND b.TOTAL_PRICE > 5000000");
                    break;
            }
        }

        sql.append(" ORDER BY b.CREATED_AT DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");

        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            int paramIndex = 1;
            ps.setInt(paramIndex++, userId);

            if (status != null && !status.isEmpty() && !status.equals("All")) {
                ps.setString(paramIndex++, status);
            }

            if (startDateFrom != null && !startDateFrom.isEmpty()) {
                ps.setString(paramIndex++, startDateFrom);
            }
            if (startDateTo != null && !startDateTo.isEmpty()) {
                ps.setString(paramIndex++, startDateTo);
            }

            if (endDateFrom != null && !endDateFrom.isEmpty()) {
                ps.setString(paramIndex++, endDateFrom);
            }
            if (endDateTo != null && !endDateTo.isEmpty()) {
                ps.setString(paramIndex++, endDateTo);
            }

            if (carName != null && !carName.isEmpty()) {
                ps.setString(paramIndex++, "%" + carName + "%");
            }

            ps.setInt(paramIndex++, offset);
            ps.setInt(paramIndex++, limit);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                BookingDetail detail = new BookingDetail();
                detail.setBookingId(rs.getInt("BOOKING_ID"));
                detail.setCarName(rs.getString("carName"));
                detail.setStartDate(rs.getObject("START_DATE", LocalDate.class));
                detail.setEndDate(rs.getObject("END_DATE", LocalDate.class));
                detail.setPickupTime(rs.getObject("PICKUP_TIME", LocalTime.class));
                detail.setDropoffTime(rs.getObject("DROPOFF_TIME", LocalTime.class));
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

    /**
     * Count history trips with filters
     */
    public int countHistoryTripsWithFilters(
            int userId,
            String status,
            String startDateFrom,
            String startDateTo,
            String endDateFrom,
            String endDateTo,
            String carName,
            String priceRange) {

        StringBuilder sql = new StringBuilder("""
        SELECT COUNT(*) 
        FROM BOOKING b 
        JOIN CAR c ON b.CAR_ID = c.CAR_ID
        WHERE b.USER_ID = ? 
        AND b.STATUS IN ('Completed', 'Rejected', 'Cancelled')
    """);

        if (status != null && !status.isEmpty() && !status.equals("All")) {
            sql.append(" AND b.STATUS = ?");
        }

        if (startDateFrom != null && !startDateFrom.isEmpty()) {
            sql.append(" AND b.START_DATE >= ?");
        }
        if (startDateTo != null && !startDateTo.isEmpty()) {
            sql.append(" AND b.START_DATE <= ?");
        }

        if (endDateFrom != null && !endDateFrom.isEmpty()) {
            sql.append(" AND b.END_DATE >= ?");
        }
        if (endDateTo != null && !endDateTo.isEmpty()) {
            sql.append(" AND b.END_DATE <= ?");
        }

        if (carName != null && !carName.isEmpty()) {
            sql.append(" AND (c.MODEL + ' ' + c.BRAND) LIKE ?");
        }

        if (priceRange != null && !priceRange.isEmpty()) {
            switch (priceRange) {
                case "under1m":
                    sql.append(" AND b.TOTAL_PRICE < 1000000");
                    break;
                case "1m-3m":
                    sql.append(" AND b.TOTAL_PRICE BETWEEN 1000000 AND 3000000");
                    break;
                case "3m-5m":
                    sql.append(" AND b.TOTAL_PRICE BETWEEN 3000000 AND 5000000");
                    break;
                case "over5m":
                    sql.append(" AND b.TOTAL_PRICE > 5000000");
                    break;
            }
        }

        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            int paramIndex = 1;
            ps.setInt(paramIndex++, userId);

            if (status != null && !status.isEmpty() && !status.equals("All")) {
                ps.setString(paramIndex++, status);
            }

            if (startDateFrom != null && !startDateFrom.isEmpty()) {
                ps.setString(paramIndex++, startDateFrom);
            }
            if (startDateTo != null && !startDateTo.isEmpty()) {
                ps.setString(paramIndex++, startDateTo);
            }

            if (endDateFrom != null && !endDateFrom.isEmpty()) {
                ps.setString(paramIndex++, endDateFrom);
            }
            if (endDateTo != null && !endDateTo.isEmpty()) {
                ps.setString(paramIndex++, endDateTo);
            }

            if (carName != null && !carName.isEmpty()) {
                ps.setString(paramIndex++, "%" + carName + "%");
            }

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

}


