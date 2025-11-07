package service;

import dao.implement.ReviewDAO;
import model.Review;

public class ReviewService {

    private final ReviewDAO reviewDAO = new ReviewDAO();

    // ✅ Kiểm tra xem booking này đã được review chưa
    public boolean hasUserReviewed(int bookingId) {
        try {
            return reviewDAO.hasUserReviewed(bookingId);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // ✅ Lưu review mới
    public boolean addReview(int bookingId, int rating, String feedback) {
        try {
            Review review = new Review(bookingId, rating, feedback);
            return reviewDAO.insertReview(review);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
