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
}