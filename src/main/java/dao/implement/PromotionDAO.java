package dao.implement;

import dao.DBContext;
import model.Promotion;

import java.sql.*;
import java.time.LocalDate;

public class PromotionDAO extends DBContext {

    public Promotion findByCode(String code) {
        String sql = "SELECT * FROM PROMOTION WHERE CODE = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, code);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Promotion p = new Promotion();
                p.setPromoId(rs.getInt("PROMO_ID"));
                p.setCode(rs.getString("CODE"));
                p.setDescription(rs.getString("DESCRIPTION"));
                p.setDiscountRate(rs.getDouble("DISCOUNT_RATE")); // ✅ sửa ở đây
                p.setDiscountType(rs.getString("DISCOUNT_TYPE")); // ✅ thêm dòng này
                p.setStartDate(rs.getObject("START_DATE", LocalDate.class));
                p.setEndDate(rs.getObject("END_DATE", LocalDate.class));
                p.setActive(rs.getBoolean("ACTIVE")); // ✅ thêm dòng này
                return p;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
