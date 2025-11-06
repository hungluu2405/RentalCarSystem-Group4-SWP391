package dao.implement;

import dao.DBContext;
import model.BookingPromotion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BookingPromotionDAO extends DBContext {

    // ✅ Original insert method - backward compatible
    public boolean insert(BookingPromotion bp) {
        String sql = """
            INSERT INTO BOOKING_PROMOTION (BOOKING_ID, PROMO_ID, DISCOUNT_AMOUNT, FINAL_PRICE, APPLIED_AT, STATUS)
            VALUES (?, ?, ?, ?, ?, ?)
        """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, bp.getBookingId());
            ps.setInt(2, bp.getPromoId());
            ps.setDouble(3, bp.getDiscountAmount());
            ps.setDouble(4, bp.getFinalPrice());
            ps.setObject(5, bp.getAppliedAt());
            ps.setString(6, bp.getStatus());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ✅ NEW: Transaction-aware insert method
    public boolean insert(BookingPromotion bp, Connection conn) throws SQLException {
        String sql = """
            INSERT INTO BOOKING_PROMOTION (BOOKING_ID, PROMO_ID, DISCOUNT_AMOUNT, FINAL_PRICE, APPLIED_AT, STATUS)
            VALUES (?, ?, ?, ?, ?, ?)
        """;
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, bp.getBookingId());
            ps.setInt(2, bp.getPromoId());
            ps.setDouble(3, bp.getDiscountAmount());
            ps.setDouble(4, bp.getFinalPrice());
            ps.setObject(5, bp.getAppliedAt());
            ps.setString(6, bp.getStatus());
            return ps.executeUpdate() > 0;
        } finally {
            if (ps != null) ps.close();
            // ⚠️ KHÔNG đóng connection ở đây vì nó được quản lý bởi caller
        }
    }
}
