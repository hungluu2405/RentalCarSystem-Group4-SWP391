package dao.implement;

import dao.DBContext;
import model.Promotion;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
                p.setDiscountRate(rs.getDouble("DISCOUNT_RATE"));
                p.setDiscountType(rs.getString("DISCOUNT_TYPE"));
                p.setStartDate(rs.getDate("START_DATE"));
                p.setEndDate(rs.getDate("END_DATE"));
                p.setActive(rs.getBoolean("ACTIVE"));
                return p;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Promotion> getAvailablePromotions() {
        List<Promotion> list = new ArrayList<>();

        String sql = """
                    SELECT PROMO_ID, CODE, DESCRIPTION, DISCOUNT_RATE, DISCOUNT_TYPE, START_DATE, END_DATE,[ACTIVE] AS ACTIVE
                    FROM PROMOTION 
                    WHERE ACTIVE = 1 
                    AND START_DATE <= GETDATE() 
                    AND END_DATE >= GETDATE()
                    ORDER BY END_DATE ASC
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Promotion promo = new Promotion();
                promo.setPromoId(rs.getInt("PROMO_ID"));
                promo.setCode(rs.getString("CODE"));
                promo.setDescription(rs.getString("DESCRIPTION"));
                promo.setDiscountRate(rs.getDouble("DISCOUNT_RATE"));
                promo.setDiscountType(rs.getString("DISCOUNT_TYPE"));

                promo.setStartDate(rs.getDate("START_DATE"));
                promo.setEndDate(rs.getDate("END_DATE"));


                promo.setActive(rs.getBoolean("ACTIVE"));


                list.add(promo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}


