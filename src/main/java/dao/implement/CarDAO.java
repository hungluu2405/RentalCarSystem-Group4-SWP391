package dao.implement;

import dao.DBContext;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import model.CarViewModel;

public class CarDAO extends DBContext {

    public List<CarViewModel> findCars(Integer typeId, String brand, String model, Integer capacity, BigDecimal minPrice, BigDecimal maxPrice) {
        List<CarViewModel> cars = new ArrayList<>();
        List<Object> params = new ArrayList<>();

        // Query lấy xe + ảnh đầu tiên, LEFT JOIN để tránh lỗi khi CAR_IMAGE trống
        StringBuilder sql = new StringBuilder(
            "SELECT c.CAR_ID, c.BRAND, c.MODEL, c.CAPACITY, c.TRANSMISSION, c.FUEL_TYPE, c.PRICE_PER_DAY, " +
            "       ct.NAME as CarTypeName, ci.IMAGE_URL as ImageUrl " +
            "FROM CAR c " +
            "JOIN CAR_TYPE ct ON c.TYPE_ID = ct.TYPE_ID " +
            "LEFT JOIN ( " +
            "    SELECT CAR_ID, IMAGE_URL " +
            "    FROM CAR_IMAGE ci1 " +
            "    WHERE IMAGE_ID = (SELECT MIN(IMAGE_ID) FROM CAR_IMAGE ci2 WHERE ci2.CAR_ID = ci1.CAR_ID) " +
            ") ci ON c.CAR_ID = ci.CAR_ID " +
            "WHERE c.AVAILABILITY = 1");

        // Thêm điều kiện filter
        if (typeId != null && typeId > 0) { sql.append(" AND c.TYPE_ID = ?"); params.add(typeId); }
        if (brand != null && !brand.trim().isEmpty()) { sql.append(" AND c.BRAND LIKE ?"); params.add("%" + brand.trim() + "%"); }
        if (model != null && !model.trim().isEmpty()) { sql.append(" AND c.MODEL LIKE ?"); params.add("%" + model.trim() + "%"); }
        if (capacity != null && capacity > 0) { sql.append(" AND c.CAPACITY >= ?"); params.add(capacity); }
        if (minPrice != null) { sql.append(" AND c.PRICE_PER_DAY >= ?"); params.add(minPrice); }
        if (maxPrice != null) { sql.append(" AND c.PRICE_PER_DAY <= ?"); params.add(maxPrice); }

        try (Connection conn = getConnection();
             PreparedStatement st = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                st.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    CarViewModel car = new CarViewModel();
                    car.setCarId(rs.getInt("CAR_ID"));
                    car.setBrand(rs.getString("BRAND"));
                    car.setModel(rs.getString("MODEL"));
                    car.setCapacity(rs.getInt("CAPACITY"));
                    car.setTransmission(rs.getString("TRANSMISSION"));
                    car.setFuelType(rs.getString("FUEL_TYPE"));
                    car.setPricePerDay(rs.getBigDecimal("PRICE_PER_DAY"));
                    car.setCarTypeName(rs.getString("CarTypeName"));
                    car.setImageUrl(rs.getString("ImageUrl")); // null nếu chưa có ảnh
                    cars.add(car);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return cars;
    }

    public List<CarViewModel> getAllCars() {
        return findCars(null, null, null, null, null, null);
    }
}
