package dao.implement;

import dao.DBContext;
import model.Payment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentDAO extends DBContext {

    // Lấy tất cả payment trong bảng PAYMENT
    public List<Payment> getAll() {
        List<Payment> list = new ArrayList<>();
        String sql = "SELECT PAYMENT_ID, BOOKING_ID, AMOUNT, METHOD, STATUS, PAID_AT, PAYPAL_TRANSACTION_ID FROM PAYMENT";

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Payment p = new Payment();
                p.setPaymentId(rs.getInt("PAYMENT_ID"));
                p.setBookingId(rs.getInt("BOOKING_ID"));
                p.setAmount(rs.getDouble("AMOUNT"));
                p.setMethod(rs.getString("METHOD"));
                p.setStatus(rs.getString("STATUS"));

                Timestamp paidAtTs = rs.getTimestamp("PAID_AT");
                if (paidAtTs != null) {
                    p.setPaidAt(paidAtTs.toLocalDateTime());
                }

                p.setPaypalTransactionId(rs.getString("PAYPAL_TRANSACTION_ID"));
                list.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
    public int countAllReport() {
        String sql = "SELECT COUNT(*) FROM PAYMENT";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    // ==============================
    // 1️⃣ Lấy danh sách payment theo tuần / tháng
    // ==============================
    public List<Payment> getPaymentsByWeekOrMonth(String type) {
        List<Payment> list = new ArrayList<>();
        String sql = "";

        if ("week".equalsIgnoreCase(type)) {
            sql = """
                SELECT * FROM PAYMENT
                WHERE STATUS = 'Paid'
                  AND DATEPART(YEAR, PAID_AT) = DATEPART(YEAR, GETDATE())
                  AND DATEPART(WEEK, PAID_AT) = DATEPART(WEEK, GETDATE())
                ORDER BY PAID_AT DESC
            """;
        } else if ("month".equalsIgnoreCase(type)) {
            sql = """
                SELECT * FROM PAYMENT
                WHERE STATUS = 'Paid'
                  AND YEAR(PAID_AT) = YEAR(GETDATE())
                  AND MONTH(PAID_AT) = MONTH(GETDATE())
                ORDER BY PAID_AT DESC
            """;
        } else {
            System.err.println("Invalid type: " + type);
            return list;
        }

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapPayment(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // ==============================
    // 2️⃣ Tính tổng doanh thu theo tuần / tháng
    // ==============================
    public double getTotalRevenueByWeekOrMonth(String type) {
        String sql = "";

        if ("week".equalsIgnoreCase(type)) {
            sql = """
                SELECT SUM(AMOUNT) AS Total
                FROM PAYMENT
                WHERE STATUS = 'Paid'
                  AND DATEPART(YEAR, PAID_AT) = DATEPART(YEAR, GETDATE())
                  AND DATEPART(WEEK, PAID_AT) = DATEPART(WEEK, GETDATE())
            """;
        } else if ("month".equalsIgnoreCase(type)) {
            sql = """
                SELECT SUM(AMOUNT) AS Total
                FROM PAYMENT
                WHERE STATUS = 'Paid'
                  AND YEAR(PAID_AT) = YEAR(GETDATE())
                  AND MONTH(PAID_AT) = MONTH(GETDATE())
            """;
        } else {
            System.err.println("Invalid type: " + type);
            return 0;
        }

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getDouble("Total");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }


    private Payment mapPayment(ResultSet rs) throws SQLException {
        Payment p = new Payment();
        p.setPaymentId(rs.getInt("PAYMENT_ID"));
        p.setBookingId(rs.getInt("BOOKING_ID"));
        p.setAmount(rs.getDouble("AMOUNT"));
        p.setMethod(rs.getString("METHOD"));
        p.setStatus(rs.getString("STATUS"));
        Timestamp ts = rs.getTimestamp("PAID_AT");
        if (ts != null) {
            p.setPaidAt(ts.toLocalDateTime());
        }
        p.setPaypalTransactionId(rs.getString("PAYPAL_TRANSACTION_ID"));
        return p;
    }


    public Payment getPaymentById(int paymentId) {
        String sql = "SELECT PAYMENT_ID, BOOKING_ID, AMOUNT, METHOD, STATUS, PAID_AT, PAYPAL_TRANSACTION_ID FROM PAYMENT WHERE PAYMENT_ID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, paymentId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapPayment(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public Payment getPaymentByBookingId(int bookingId) {
        String sql = "SELECT PAYMENT_ID, BOOKING_ID, AMOUNT, METHOD, STATUS, PAID_AT, PAYPAL_TRANSACTION_ID FROM PAYMENT WHERE BOOKING_ID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, bookingId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapPayment(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}