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
    public List<Promotion> getAllPromotions() {
        List<Promotion> list = new ArrayList<>();

        String sql = """
                SELECT PROMO_ID, CODE, DESCRIPTION, DISCOUNT_RATE,
                       DISCOUNT_TYPE, START_DATE, END_DATE, ACTIVE
                FROM PROMOTION
                ORDER BY PROMO_ID DESC
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Promotion p = new Promotion();
                p.setPromoId(rs.getInt("PROMO_ID"));
                p.setCode(rs.getString("CODE"));
                p.setDescription(rs.getString("DESCRIPTION"));
                p.setDiscountRate(rs.getDouble("DISCOUNT_RATE"));
                p.setDiscountType(rs.getString("DISCOUNT_TYPE"));
                p.setStartDate(rs.getDate("START_DATE"));
                p.setEndDate(rs.getDate("END_DATE"));
                p.setActive(rs.getBoolean("ACTIVE"));
                list.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean updateActiveStatus(int promoId, boolean status) {
        String sql = "UPDATE PROMOTION SET ACTIVE = ? WHERE PROMO_ID = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setBoolean(1, status);
            ps.setInt(2, promoId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean deletePromotion(int promoId) {
        String sql = "DELETE FROM PROMOTION WHERE PROMO_ID = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, promoId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public List<Promotion> filterPromotions(Boolean active, Integer rateFilter,
                                            Date startFrom, Date endTo) {

        List<Promotion> list = new ArrayList<>();

        StringBuilder sql = new StringBuilder("""
        SELECT PROMO_ID, CODE, DESCRIPTION, DISCOUNT_RATE, DISCOUNT_TYPE,
               START_DATE, END_DATE, ACTIVE
        FROM PROMOTION
        WHERE 1=1
    """);

        // ACTIVE
        if (active != null) {
            sql.append(" AND ACTIVE = ").append(active ? 1 : 0);
        }

        // DISCOUNT RATE RANGE (0=0-15, 1=15-30, 2=30+)
        if (rateFilter != null) {
            switch (rateFilter) {
                case 0 -> sql.append(" AND DISCOUNT_RATE BETWEEN 0 AND 15 ");
                case 1 -> sql.append(" AND DISCOUNT_RATE BETWEEN 15 AND 30 ");
                case 2 -> sql.append(" AND DISCOUNT_RATE > 30 ");
            }
        }

        // START DATE
        if (startFrom != null) {
            sql.append(" AND START_DATE >= '").append(startFrom).append("' ");
        }

        // END DATE
        if (endTo != null) {
            sql.append(" AND END_DATE <= '").append(endTo).append("' ");
        }

        sql.append(" ORDER BY PROMO_ID DESC ");

        try (PreparedStatement ps = connection.prepareStatement(sql.toString());
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Promotion p = new Promotion();
                p.setPromoId(rs.getInt("PROMO_ID"));
                p.setCode(rs.getString("CODE"));
                p.setDescription(rs.getString("DESCRIPTION"));
                p.setDiscountRate(rs.getDouble("DISCOUNT_RATE"));
                p.setDiscountType(rs.getString("DISCOUNT_TYPE"));
                p.setStartDate(rs.getDate("START_DATE"));
                p.setEndDate(rs.getDate("END_DATE"));
                p.setActive(rs.getBoolean("ACTIVE"));
                list.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean createPromotion(Promotion p) {
        String sql = """
        INSERT INTO PROMOTION (CODE, DESCRIPTION, DISCOUNT_RATE, DISCOUNT_TYPE, START_DATE, END_DATE, ACTIVE)
        VALUES (?, ?, ?, ?, ?, ?, ?)
    """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, p.getCode());
            ps.setString(2, p.getDescription());
            ps.setDouble(3, p.getDiscountRate());
            ps.setString(4, p.getDiscountType());
            ps.setDate(5, p.getStartDate());
            ps.setDate(6, p.getEndDate());
            ps.setBoolean(7, p.isActive());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


}


