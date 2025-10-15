package dao.implement;

import dao.DBContext;
import model.BookingPromotion;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BookingPromotionDAO extends DBContext {

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
}
