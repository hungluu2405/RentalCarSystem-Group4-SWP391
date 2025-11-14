package dao.implement;


import dao.DBContext;
import model.Car;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/**
 * DAO cho bảng FAVOURITE_CAR
 */

public class FavouriteCarDAO extends DBContext {


    /**
     * Thêm xe vào danh sách yêu thích
     */

    public boolean addFavourite(int userId, int carId) {

        String sql = "INSERT INTO FAVOURITE_CAR (USER_ID, CAR_ID) VALUES (?, ?)";


        try (Connection conn = getConnection();

             PreparedStatement ps = conn.prepareStatement(sql)) {


            ps.setInt(1, userId);

            ps.setInt(2, carId);


            return ps.executeUpdate() > 0;


        } catch (SQLException e) {

            System.err.println("Error adding favourite: " + e.getMessage());

            return false;

        }

    }


    /**
     * Xóa xe khỏi danh sách yêu thích
     */

    public boolean removeFavourite(int userId, int carId) {

        String sql = "DELETE FROM FAVOURITE_CAR WHERE USER_ID = ? AND CAR_ID = ?";


        try (Connection conn = getConnection();

             PreparedStatement ps = conn.prepareStatement(sql)) {


            ps.setInt(1, userId);

            ps.setInt(2, carId);


            return ps.executeUpdate() > 0;


        } catch (SQLException e) {

            System.err.println("Error removing favourite: " + e.getMessage());

            return false;

        }

    }


    /**
     * Kiểm tra xe đã được yêu thích chưa
     */

    public boolean isFavourite(int userId, int carId) {

        String sql = "SELECT COUNT(*) FROM FAVOURITE_CAR WHERE USER_ID = ? AND CAR_ID = ?";


        try (Connection conn = getConnection();

             PreparedStatement ps = conn.prepareStatement(sql)) {


            ps.setInt(1, userId);

            ps.setInt(2, carId);


            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                return rs.getInt(1) > 0;

            }


        } catch (SQLException e) {

            System.err.println("Error checking favourite: " + e.getMessage());

        }


        return false;

    }


    /**
     * Lấy danh sách ID các xe yêu thích của user
     */

    public List<Integer> getFavouriteCarIdsByUser(int userId) {

        List<Integer> carIds = new ArrayList<>();

        String sql = "SELECT CAR_ID FROM FAVOURITE_CAR WHERE USER_ID = ? ORDER BY CREATED_AT DESC";


        try (Connection conn = getConnection();

             PreparedStatement ps = conn.prepareStatement(sql)) {


            ps.setInt(1, userId);


            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                carIds.add(rs.getInt("CAR_ID"));

            }


        } catch (SQLException e) {

            System.err.println("Error getting favourite car IDs: " + e.getMessage());

        }


        return carIds;

    }


    /**
     * Lấy danh sách xe yêu thích của user (có thông tin đầy đủ)
     */

    public List<Car> getFavouriteCarsByUser(int userId) {

        List<Car> cars = new ArrayList<>();

        String sql = "SELECT c.CAR_ID, c.USER_ID AS OWNER_ID, c.TYPE_ID, c.MODEL, c.BRAND, " +

                "c.YEAR, c.LICENSE_PLATE, c.CAPACITY, c.TRANSMISSION, c.FUEL_TYPE, " +

                "c.PRICE_PER_DAY, c.DESCRIPTION, c.AVAILABILITY, c.LOCATION, " +

                "t.NAME AS TYPE_NAME, up.FULL_NAME AS CAR_OWNER_NAME, " +

                "(SELECT TOP 1 IMAGE_URL FROM CAR_IMAGE WHERE CAR_ID = c.CAR_ID) AS IMAGE_URL " +

                "FROM FAVOURITE_CAR fc " +

                "INNER JOIN CAR c ON fc.CAR_ID = c.CAR_ID " +

                "LEFT JOIN CAR_TYPE t ON c.TYPE_ID = t.TYPE_ID " +

                "LEFT JOIN USER_PROFILE up ON c.USER_ID = up.USER_ID " +

                "WHERE fc.USER_ID = ? " +

                "ORDER BY fc.CREATED_AT DESC";


        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {

                Car car = new Car();
                car.setCarId(rs.getInt("CAR_ID"));
                car.setOwnerId(rs.getInt("OWNER_ID"));
                car.setTypeId(rs.getInt("TYPE_ID"));
                car.setModel(rs.getString("MODEL"));
                car.setBrand(rs.getString("BRAND"));
                car.setYear(rs.getInt("YEAR"));
                car.setLicensePlate(rs.getString("LICENSE_PLATE"));
                car.setCapacity(rs.getInt("CAPACITY"));
                car.setTransmission(rs.getString("TRANSMISSION"));
                car.setFuelType(rs.getString("FUEL_TYPE"));
                car.setPricePerDay(rs.getBigDecimal("PRICE_PER_DAY"));
                car.setDescription(rs.getString("DESCRIPTION"));
                car.setAvailability(rs.getBoolean("AVAILABILITY"));
                car.setLocation(rs.getString("LOCATION"));
                car.setTypeName(rs.getString("TYPE_NAME"));
                car.setCarOwnerName(rs.getString("CAR_OWNER_NAME"));
                cars.add(car);
            }
        } catch (SQLException e) {
            System.err.println("Error getting favourite cars: " + e.getMessage());
        }
        return cars;
    }


    public int countFavouritesByUser(int userId) {
        String sql = "SELECT COUNT(*) FROM FAVOURITE_CAR WHERE USER_ID = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error counting favourites: " + e.getMessage());
        }
        return 0;
    }

    public boolean removeAllFavouritesByUser(int userId) {
        String sql = "DELETE FROM FAVOURITE_CAR WHERE USER_ID = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error removing all favourites by user: " + e.getMessage());
            return false;
        }
    }


    public boolean removeAllFavouritesByCar(int carId) {
        String sql = "DELETE FROM FAVOURITE_CAR WHERE CAR_ID = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, carId);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error removing all favourites by car: " + e.getMessage());
            return false;
        }
    }
}