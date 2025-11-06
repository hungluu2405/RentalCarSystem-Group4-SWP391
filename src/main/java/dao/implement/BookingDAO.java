package dao.implement;

import dao.DBContext;
import model.Booking;
import model.BookingDetail;
import model.UserProfile;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class BookingDAO extends DBContext {


    public boolean insert(Booking booking) {
        String sql = """
                    INSERT INTO BOOKING (CAR_ID, USER_ID, START_DATE, END_DATE, PICKUP_TIME, DROPOFF_TIME, TOTAL_PRICE, STATUS, CREATED_AT, LOCATION)
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        // Khai báo Statement và ResultSet bên ngoài try-with-resources để sử dụng getGeneratedKeys
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection conn = null;

        try {
            conn = getConnection(); // Lấy kết nối từ DBContext

            // ✅ BƯỚC 1: Yêu cầu JDBC trả về ID tự động tăng
            // Sử dụng Statement.RETURN_GENERATED_KEYS
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            // Thiết lập tham số (Giữ nguyên logic của bạn)
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
                // ✅ BƯỚC 2: Lấy ResultSet chứa ID vừa tạo
                rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    // ✅ BƯỚC 3: Gán ID vừa tạo TRỞ LẠI đối tượng Booking
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
            // Đóng các tài nguyên
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (conn != null) conn.close(); // Sử dụng hàm đóng kết nối của DBContext nếu có
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isCarAvailable(int carId, LocalDate startDate, LocalTime startTime,
                                  LocalDate endDate, LocalTime endTime) {

        // Tạo LocalDateTime để so sánh chính xác
        LocalDateTime requestStart = LocalDateTime.of(startDate, startTime);
        LocalDateTime requestEnd = LocalDateTime.of(endDate, endTime);

        String sql = """
            SELECT COUNT(*) FROM BOOKING
            WHERE CAR_ID = ? 
              AND STATUS IN ('Pending', 'Approved', 'Paid')
              AND (
                  
                  CONCAT(START_DATE, ' ', PICKUP_TIME) < ?
                  AND CONCAT(END_DATE, ' ', DROPOFF_TIME) > ?
              )
        """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, carId);


            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            ps.setString(2, requestEnd.format(formatter));
            ps.setString(3, requestStart.format(formatter));

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);

                if (count > 0) {
                    System.out.println("⚠️ Car " + carId + " is NOT available!");
                    System.out.println("   Request: " + requestStart + " → " + requestEnd);
                    System.out.println("   Conflicts with " + count + " existing booking(s)");
                }

                return count == 0; // true = available
            }
        } catch (SQLException e) {
            System.err.println("❌ Error checking availability: " + e.getMessage());
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


    // =========================================
// LẤY TẤT CẢ BOOKING CHI TIẾT THEO USER ID
// =========================================
    public List<BookingDetail> getBookingDetailsByUserId(int userId, int limit) {
        List<BookingDetail> list = new ArrayList<>();
        String sql = """
                    SELECT TOP (?) 
                           b.BOOKING_ID, 
                           c.MODEL + ' ' + c.BRAND AS carName, -- 👈 Nối chuỗi để có tên xe đầy đủ
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
            ps.setInt(1, limit);   // số lượng bản ghi muốn giới hạn
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
                    AND b.STATUS IN ('Approved', 'Completed', 'Rejected')
                    ORDER BY b.BOOKING_ID DESC
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, ownerId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                BookingDetail bd = new BookingDetail();
                UserProfile up = new UserProfile();

                bd.setBookingId(rs.getInt("BOOKING_ID"));
                bd.setCarName(rs.getString("CAR_NAME"));
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
        String sql = "SELECT COUNT(*) FROM Booking WHERE userId = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, ownerId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return 0;
    }

    public List<BookingDetail> getBookingsByOwnerWithPaging(int ownerId, int page, int pageSize) {
        List<BookingDetail> list = new ArrayList<>();
        int offset = (page - 1) * pageSize;

        String sql = "SELECT * FROM BookingDetail WHERE userId = ? ORDER BY bookingId DESC LIMIT ? OFFSET ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, ownerId);
            ps.setInt(2, pageSize);
            ps.setInt(3, offset);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                // map như bình thường
            }
        } catch (Exception e) { e.printStackTrace(); }

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
        String sql = "SELECT COUNT(*) AS total FROM [Booking]";
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
            ps.setString(5, paymentId); // Chèn ID giao dịch PayPal vào cột mới

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Booking> getBookingsByCarAndMonth(int carId, int year, int month) {
        String sql = "SELECT * FROM BOOKING " +
                "WHERE CAR_ID = ? " +
                "AND ((YEAR(START_DATE) = ? AND MONTH(START_DATE) = ?) " +
                "OR (YEAR(END_DATE) = ? AND MONTH(END_DATE) = ?) " +
                "OR (START_DATE <= ? AND END_DATE >= ?)) " +
                "ORDER BY START_DATE";

        List<Booking> bookings = new ArrayList<>();

        try (
                PreparedStatement ps = connection.prepareStatement(sql)) {

            // Tạo first day và last day của tháng
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
}


