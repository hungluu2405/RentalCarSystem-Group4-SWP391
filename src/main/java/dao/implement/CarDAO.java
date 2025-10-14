package dao.implement;

import dao.DBContext;

import java.sql.*;
import java.util.*;
import java.math.BigDecimal;
import model.Car;

import model.CarViewModel;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CarDAO extends DBContext {

    public List<CarViewModel> findCars(String name, String brand, String modelParam,
                                      String capacity, String fuel, String price,
                                      int page, int pageSize) {
        List<CarViewModel> list = new ArrayList<>();
        String sql = "SELECT c.CAR_ID, c.BRAND, c.MODEL, c.PRICE_PER_DAY, c.CAPACITY, " +
                     "c.TRANSMISSION, c.FUEL_TYPE, t.NAME AS CAR_TYPE_NAME, i.IMAGE_URL " +
                     "FROM CAR c " +
                     "JOIN CAR_TYPE t ON c.TYPE_ID = t.TYPE_ID " +
                     "LEFT JOIN CAR_IMAGE i ON c.CAR_ID = i.CAR_ID " +
                     "WHERE 1=1";

        if (name != null && !name.isEmpty()) sql += " AND (c.BRAND LIKE ? OR c.MODEL LIKE ?)";
        if (brand != null && !brand.isEmpty()) sql += " AND c.BRAND = ?";
        if (modelParam != null && !modelParam.isEmpty()) sql += " AND c.MODEL = ?";
        if (capacity != null && !capacity.isEmpty()) sql += " AND c.CAPACITY = ?";
        if (fuel != null && !fuel.isEmpty()) sql += " AND c.FUEL_TYPE = ?";
        if (price != null && !price.isEmpty()) sql += " AND c.PRICE_PER_DAY <= ?";

        sql += " ORDER BY c.CAR_ID OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            int idx = 1;
            if (name != null && !name.isEmpty()) { 
                ps.setString(idx++, "%" + name + "%"); 
                ps.setString(idx++, "%" + name + "%"); 
            }
            if (brand != null && !brand.isEmpty()) ps.setString(idx++, brand);
            if (modelParam != null && !modelParam.isEmpty()) ps.setString(idx++, modelParam);
            if (capacity != null && !capacity.isEmpty()) ps.setInt(idx++, Integer.parseInt(capacity));
            if (fuel != null && !fuel.isEmpty()) ps.setString(idx++, fuel);
            if (price != null && !price.isEmpty()) ps.setBigDecimal(idx++, new BigDecimal(price));

            ps.setInt(idx++, (page - 1) * pageSize);
            ps.setInt(idx++, pageSize);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                CarViewModel car = new CarViewModel();
                car.setCarId(rs.getInt("CAR_ID"));
                car.setBrand(rs.getString("BRAND"));
                car.setModel(rs.getString("MODEL"));
                car.setPricePerDay(rs.getBigDecimal("PRICE_PER_DAY"));
                car.setCapacity(rs.getInt("CAPACITY"));
                car.setTransmission(rs.getString("TRANSMISSION"));
                car.setFuelType(rs.getString("FUEL_TYPE"));
                car.setCarTypeName(rs.getString("CAR_TYPE_NAME"));
                car.setImageUrl(rs.getString("IMAGE_URL") != null ? rs.getString("IMAGE_URL") : "default.jpg");
                list.add(car);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public int countCars(String name, String brand, String modelParam,
                         String capacity, String fuel, String price) {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM CAR c JOIN CAR_TYPE t ON c.TYPE_ID = t.TYPE_ID WHERE 1=1";
        if (name != null && !name.isEmpty()) sql += " AND (c.BRAND LIKE ? OR c.MODEL LIKE ?)";
        if (brand != null && !brand.isEmpty()) sql += " AND c.BRAND = ?";
        if (modelParam != null && !modelParam.isEmpty()) sql += " AND c.MODEL = ?";
        if (capacity != null && !capacity.isEmpty()) sql += " AND c.CAPACITY = ?";
        if (fuel != null && !fuel.isEmpty()) sql += " AND c.FUEL_TYPE = ?";
        if (price != null && !price.isEmpty()) sql += " AND c.PRICE_PER_DAY <= ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            int idx = 1;
            if (name != null && !name.isEmpty()) { 
                ps.setString(idx++, "%" + name + "%"); 
                ps.setString(idx++, "%" + name + "%"); 
            }
            if (brand != null && !brand.isEmpty()) ps.setString(idx++, brand);
            if (modelParam != null && !modelParam.isEmpty()) ps.setString(idx++, modelParam);
            if (capacity != null && !capacity.isEmpty()) ps.setInt(idx++, Integer.parseInt(capacity));
            if (fuel != null && !fuel.isEmpty()) ps.setString(idx++, fuel);
            if (price != null && !price.isEmpty()) ps.setBigDecimal(idx++, new BigDecimal(price));

            ResultSet rs = ps.executeQuery();
            if (rs.next()) count = rs.getInt(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    public List<String> getAllBrands() {
        List<String> list = new ArrayList<>();
        String sql = "SELECT DISTINCT BRAND FROM CAR ORDER BY BRAND";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(rs.getString("BRAND"));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public List<String> getAllModels() {
        List<String> list = new ArrayList<>();
        String sql = "SELECT DISTINCT MODEL FROM CAR ORDER BY MODEL";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(rs.getString("MODEL"));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public List<Integer> getAllCapacities() {
        List<Integer> list = new ArrayList<>();
        String sql = "SELECT DISTINCT CAPACITY FROM CAR ORDER BY CAPACITY";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(rs.getInt("CAPACITY"));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public List<String> getAllFuelTypes() {
        List<String> list = new ArrayList<>();
        String sql = "SELECT DISTINCT FUEL_TYPE FROM CAR ORDER BY FUEL_TYPE";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(rs.getString("FUEL_TYPE"));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }
}
