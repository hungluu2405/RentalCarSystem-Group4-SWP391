package dao.implement;


import dao.DBContext;
import model.Review;


import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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





    // ============================ REVIEW CHO XE (CAR) ============================


    public List<Map<String, Object>> getReviewsByCarId(int carId, Integer ratingFilter, int offset, int limit) {
        List<Map<String, Object>> list = new ArrayList<>();


        StringBuilder sql = new StringBuilder("""
           SELECT r.REVIEW_ID, r.RATING, r.COMMENT, r.CREATED_AT,
                  p.FULL_NAME, p.profileImage AS PROFILE_IMAGE
           FROM REVIEW r
           JOIN BOOKING b ON r.BOOKING_ID = b.BOOKING_ID
           JOIN [USER] u ON b.USER_ID = u.USER_ID
           LEFT JOIN USER_PROFILE p ON u.USER_ID = p.USER_ID
           WHERE b.CAR_ID = ?
       """);


        if (ratingFilter != null) {
            sql.append(" AND r.RATING = ?");
        }

        sql.append(" ORDER BY r.CREATED_AT DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");


        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {


            int idx = 1;
            ps.setInt(idx++, carId);
            if (ratingFilter != null) ps.setInt(idx++, ratingFilter);
            ps.setInt(idx++, offset);
            ps.setInt(idx++, limit);


            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Review rv = new Review();
                rv.setReviewId(rs.getInt("REVIEW_ID"));
                rv.setRating(rs.getInt("RATING"));
                rv.setComment(rs.getString("COMMENT"));
                Timestamp ts = rs.getTimestamp("CREATED_AT");
                if (ts != null) rv.setCreatedAt(ts.toLocalDateTime());


                Map<String, Object> map = new HashMap<>();
                map.put("review", rv);
                map.put("fullName", rs.getString("FULL_NAME"));
                map.put("profileImage", rs.getString("PROFILE_IMAGE"));
                list.add(map);
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }


    public int countReviewsByCarId(int carId, Integer ratingFilter) {
        StringBuilder sql = new StringBuilder("""
           SELECT COUNT(*)
           FROM REVIEW r
           JOIN BOOKING b ON r.BOOKING_ID = b.BOOKING_ID
           WHERE b.CAR_ID = ?
       """);


        if (ratingFilter != null) {
            sql.append(" AND r.RATING = ?");
        }


        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {


            ps.setInt(1, carId);
            if (ratingFilter != null) ps.setInt(2, ratingFilter);


            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);


        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }






    public List<Map<String, Object>> getReviewsByOwner(int ownerId, Integer ratingFilter, String carFilter, int offset, int limit) {
        List<Map<String, Object>> list = new ArrayList<>();


        StringBuilder sql = new StringBuilder(
                "SELECT r.REVIEW_ID, r.RATING, r.COMMENT, r.CREATED_AT, " +
                        "c.MODEL AS CAR_MODEL, p.FULL_NAME, p.profileImage AS PROFILE_IMAGE " +
                        "FROM REVIEW r " +
                        "JOIN BOOKING b ON r.BOOKING_ID = b.BOOKING_ID " +
                        "JOIN CAR c ON b.CAR_ID = c.CAR_ID " +
                        "JOIN [USER] u ON b.USER_ID = u.USER_ID " +
                        "LEFT JOIN USER_PROFILE p ON u.USER_ID = p.USER_ID " +
                        "WHERE c.USER_ID = ?"
        );




        if (ratingFilter != null) sql.append(" AND r.RATING = ?");
        if (carFilter != null && !carFilter.isEmpty()) sql.append(" AND c.MODEL LIKE ?");


        sql.append(" ORDER BY r.CREATED_AT DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");


        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {


            int idx = 1;
            ps.setInt(idx++, ownerId);
            if (ratingFilter != null) ps.setInt(idx++, ratingFilter);
            if (carFilter != null && !carFilter.isEmpty()) ps.setString(idx++, "%" + carFilter + "%");
            ps.setInt(idx++, offset);
            ps.setInt(idx++, limit);


            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Review rv = new Review();
                rv.setReviewId(rs.getInt("REVIEW_ID"));
                rv.setRating(rs.getInt("RATING"));
                rv.setComment(rs.getString("COMMENT"));
                rv.setCreatedAt(rs.getTimestamp("CREATED_AT").toLocalDateTime());


                Map<String, Object> map = new HashMap<>();
                map.put("review", rv);
                map.put("carModel", rs.getString("CAR_MODEL"));
                map.put("fullName", rs.getString("FULL_NAME"));
                map.put("profileImage", rs.getString("PROFILE_IMAGE"));
                list.add(map);
                System.out.println("✅ Found " + list.size() + " reviews for ownerId=" + ownerId);


            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }


    public int countReviewsByOwner(int ownerId, Integer ratingFilter, String carFilter) {
        StringBuilder sql = new StringBuilder(
                "SELECT COUNT(*) FROM REVIEW r " +
                        "JOIN BOOKING b ON r.BOOKING_ID = b.BOOKING_ID " +
                        "JOIN CAR c ON b.CAR_ID = c.CAR_ID " +
                        "WHERE c.USER_ID = ?"
        );


        if (ratingFilter != null) sql.append(" AND r.RATING = ?");
        if (carFilter != null && !carFilter.isEmpty()) sql.append(" AND c.MODEL LIKE ?");


        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {


            int idx = 1;
            ps.setInt(idx++, ownerId);
            if (ratingFilter != null) ps.setInt(idx++, ratingFilter);
            if (carFilter != null && !carFilter.isEmpty()) ps.setString(idx++, "%" + carFilter + "%");


            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);


        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    }






