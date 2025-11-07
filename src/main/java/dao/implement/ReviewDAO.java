package dao.implement;

import dao.DBContext;
import model.Review;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReviewDAO extends DBContext {

    public boolean insertReview(Review review) {
        String sql = "INSERT INTO REVIEW (BOOKING_ID, RATING, COMMENT, CREATED_AT) VALUES (?, ?, ?, GETDATE())";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, review.getBookingId());
            ps.setInt(2, review.getRating());
            ps.setString(3, review.getComment());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean hasUserReviewed(int bookingId) {
        String sql = "SELECT 1 FROM REVIEW WHERE BOOKING_ID = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, bookingId);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Review> getAllReviewsByCarId(int carId) {
        List<Review> list = new ArrayList<>();
        String sql = "SELECT r.REVIEW_ID, r.RATING, r.COMMENT, r.CREATED_AT " +
                "FROM REVIEW r JOIN BOOKING b ON r.BOOKING_ID = b.BOOKING_ID " +
                "WHERE b.CAR_ID = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, carId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Review rv = new Review();
                rv.setReviewId(rs.getInt("REVIEW_ID"));
                rv.setRating(rs.getInt("RATING"));
                rv.setComment(rs.getString("COMMENT"));
                rv.setCreatedAt(rs.getTimestamp("CREATED_AT").toLocalDateTime());
                list.add(rv);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
