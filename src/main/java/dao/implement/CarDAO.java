package dao.implement;

import dao.DBContext;

import java.sql.*;
import java.util.*;
import java.math.BigDecimal;
import model.Car;
import model.CarViewModel;
import model.CarImage;

public class CarDAO extends DBContext {

    // Lấy danh sách xe có filter và phân trang
    public List<CarViewModel> findCars(String name, String brand, String typeIdStr,
            String capacity, String fuel, String price,
            int page, int pageSize) {
        List<CarViewModel> list = new ArrayList<>();
        String sql = "SELECT c.CAR_ID, c.BRAND, c.MODEL, c.PRICE_PER_DAY, c.CAPACITY, "
                + "c.TRANSMISSION, c.FUEL_TYPE, t.NAME AS CAR_TYPE_NAME, "
                + "(SELECT TOP 1 IMAGE_URL FROM CAR_IMAGE WHERE CAR_ID = c.CAR_ID) AS IMAGE_URL "
                + "FROM CAR c "
                + "JOIN CAR_TYPE t ON c.TYPE_ID = t.TYPE_ID WHERE 1=1";

        if (name != null && !name.isEmpty()) {
            sql += " AND (c.BRAND LIKE ? OR c.MODEL LIKE ?)";
        }
        if (brand != null && !brand.isEmpty()) {
            sql += " AND c.BRAND = ?";
        }
        if (typeIdStr != null && !typeIdStr.isEmpty()) {
            sql += " AND c.TYPE_ID = ?";
        }
        if (capacity != null && !capacity.isEmpty()) {
            sql += " AND c.CAPACITY = ?";
        }
        if (fuel != null && !fuel.isEmpty()) {
            sql += " AND c.FUEL_TYPE = ?";
        }
        if (price != null && !price.isEmpty()) {
            sql += " AND c.PRICE_PER_DAY <= ?";
        }

        sql += " ORDER BY c.CAR_ID OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            int idx = 1;
            if (name != null && !name.isEmpty()) {
                ps.setString(idx++, "%" + name + "%");
                ps.setString(idx++, "%" + name + "%");
            }
            if (brand != null && !brand.isEmpty()) {
                ps.setString(idx++, brand);
            }
            if (typeIdStr != null && !typeIdStr.isEmpty()) {
                ps.setInt(idx++, Integer.parseInt(typeIdStr));
            }
            if (capacity != null && !capacity.isEmpty()) {
                ps.setInt(idx++, Integer.parseInt(capacity));
            }
            if (fuel != null && !fuel.isEmpty()) {
                ps.setString(idx++, fuel);
            }
            if (price != null && !price.isEmpty()) {
                ps.setBigDecimal(idx++, new BigDecimal(price));
            }

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

                List<CarImage> images = new ArrayList<>();
                String imageUrl = rs.getString("IMAGE_URL");
                if (imageUrl != null) {
                    CarImage img = new CarImage();
                    img.setCarId(rs.getInt("CAR_ID"));
                    img.setImageUrl(imageUrl);
                    images.add(img);
                }
                car.setImages(images);

                list.add(car);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // countCars version
    public int countCars(String name, String brand, String typeIdStr,
            String capacity, String fuel, String price) {
        String sql = "SELECT COUNT(*) FROM CAR c "
                + "JOIN CAR_TYPE t ON c.TYPE_ID = t.TYPE_ID WHERE 1=1";

        if (name != null && !name.isEmpty()) {
            sql += " AND (c.BRAND LIKE ? OR c.MODEL LIKE ?)";
        }
        if (brand != null && !brand.isEmpty()) {
            sql += " AND c.BRAND = ?";
        }
        if (typeIdStr != null && !typeIdStr.isEmpty()) {
            sql += " AND c.TYPE_ID = ?";
        }
        if (capacity != null && !capacity.isEmpty()) {
            sql += " AND c.CAPACITY = ?";
        }
        if (fuel != null && !fuel.isEmpty()) {
            sql += " AND c.FUEL_TYPE = ?";
        }
        if (price != null && !price.isEmpty()) {
            sql += " AND c.PRICE_PER_DAY <= ?";
        }

        int total = 0;
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            int idx = 1;
            if (name != null && !name.isEmpty()) {
                ps.setString(idx++, "%" + name + "%");
                ps.setString(idx++, "%" + name + "%");
            }
            if (brand != null && !brand.isEmpty()) {
                ps.setString(idx++, brand);
            }
            if (typeIdStr != null && !typeIdStr.isEmpty()) {
                ps.setInt(idx++, Integer.parseInt(typeIdStr));
            }
            if (capacity != null && !capacity.isEmpty()) {
                ps.setInt(idx++, Integer.parseInt(capacity));
            }
            if (fuel != null && !fuel.isEmpty()) {
                ps.setString(idx++, fuel);
            }
            if (price != null && !price.isEmpty()) {
                ps.setBigDecimal(idx++, new BigDecimal(price));
            }

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                total = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }

    public List<String> getAllBrands() {
        List<String> list = new ArrayList<>();
        String sql = "SELECT DISTINCT BRAND FROM CAR ORDER BY BRAND";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(rs.getString("BRAND"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<String> getAllModels() {
        List<String> list = new ArrayList<>();
        String sql = "SELECT DISTINCT MODEL FROM CAR ORDER BY MODEL";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(rs.getString("MODEL"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Integer> getAllCapacities() {
        List<Integer> list = new ArrayList<>();
        String sql = "SELECT DISTINCT CAPACITY FROM CAR ORDER BY CAPACITY";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(rs.getInt("CAPACITY"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<String> getAllFuelTypes() {
        List<String> list = new ArrayList<>();
        String sql = "SELECT DISTINCT FUEL_TYPE FROM CAR ORDER BY FUEL_TYPE";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(rs.getString("FUEL_TYPE"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }


    public double getCarPrice(int carId) throws SQLException {
        String sql = "SELECT PRICE_PER_DAY FROM CAR WHERE CAR_ID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, carId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble("PRICE_PER_DAY");
            }
        }
        return 0;
    }
    public List<String> getAllTypes() {
        List<String> types = new ArrayList<>();
        String sql = "SELECT TYPE_ID, NAME FROM CAR_TYPE ORDER BY NAME";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                types.add(rs.getInt("TYPE_ID") + ":" + rs.getString("NAME")); // có thể dùng format "id:name" để frontend dễ lấy
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return types;
    }

    // Lấy chi tiết xe theo ID, có cả description và ảnh
    public CarViewModel getCarById(int carId) {
        String sql = "SELECT c.*, t.NAME AS TYPE_NAME "
                + "FROM CAR c JOIN CAR_TYPE t ON c.TYPE_ID = t.TYPE_ID "
                + "WHERE c.CAR_ID = ?";
        try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, carId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                CarViewModel car = new CarViewModel();
                car.setCarId(rs.getInt("CAR_ID"));
                car.setBrand(rs.getString("BRAND"));
                car.setModel(rs.getString("MODEL"));
                car.setPricePerDay(rs.getBigDecimal("PRICE_PER_DAY"));
                car.setCapacity(rs.getInt("CAPACITY"));
                car.setTransmission(rs.getString("TRANSMISSION"));
                car.setFuelType(rs.getString("FUEL_TYPE"));
                car.setCarTypeName(rs.getString("TYPE_NAME"));
                car.setDescription(rs.getString("DESCRIPTION"));

                // Lấy danh sách ảnh
                List<CarImage> images = new ArrayList<>();
                String sqlImg = "SELECT * FROM CAR_IMAGE WHERE CAR_ID = ?";
                try (PreparedStatement psImg = con.prepareStatement(sqlImg)) {
                    psImg.setInt(1, carId);
                    ResultSet rsImg = psImg.executeQuery();
                    while (rsImg.next()) {
                        CarImage img = new CarImage();
                        img.setImageId(rsImg.getInt("IMAGE_ID"));
                        img.setCarId(carId);
                        img.setImageUrl(rsImg.getString("IMAGE_URL"));
                        images.add(img);
                    }
                }
                car.setImages(images);
                return car;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<CarViewModel> findTopBookedCars(int limit) {
        List<CarViewModel> cars = new ArrayList<>();

        // Ghép trực tiếp biến limit vào TOP (SQL Server không hỗ trợ TOP ?)
        String sql = "SELECT TOP " + limit + " "
                + "c.CAR_ID, c.BRAND, c.MODEL, c.CAPACITY, c.TRANSMISSION, c.FUEL_TYPE, c.PRICE_PER_DAY, "
                + "ct.NAME AS CarTypeName, "
                + "(SELECT TOP 1 ci.IMAGE_URL FROM CAR_IMAGE ci WHERE ci.CAR_ID = c.CAR_ID ORDER BY ci.IMAGE_ID) AS ImageUrl, "
                + "COUNT(b.BOOKING_ID) AS booking_count "
                + "FROM CAR c "
                + "JOIN CAR_TYPE ct ON c.TYPE_ID = ct.TYPE_ID "
                + "LEFT JOIN BOOKING b ON c.CAR_ID = b.CAR_ID "
                + "WHERE c.AVAILABILITY = 1 "
                + "GROUP BY c.CAR_ID, c.BRAND, c.MODEL, c.CAPACITY, c.TRANSMISSION, c.FUEL_TYPE, c.PRICE_PER_DAY, ct.NAME "
                + "ORDER BY booking_count DESC";

        try (Connection conn = getConnection(); PreparedStatement st = conn.prepareStatement(sql); ResultSet rs = st.executeQuery()) {

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
                car.setImageUrl(rs.getString("ImageUrl"));
                cars.add(car);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return cars;
    }

    /**
     * Đếm tổng số xe mà chủ xe đang sở hữu.
     */
    public int countCarsByOwner(int ownerId) {
        String sql = "SELECT COUNT(*) FROM CAR WHERE OWNER_ID = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, ownerId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    
    public List<Car> getAllCarsForAdmin() {
    List<Car> cars = new ArrayList<>();

    String sql = """
        SELECT 
            c.CAR_ID,
            u.FULL_NAME AS CAR_OWNER_NAME,
            t.NAME AS TYPE_NAME,        
            c.MODEL,
            c.BRAND,
            c.YEAR,
            c.LICENSE_PLATE,
            c.CAPACITY,
            c.FUEL_TYPE,
            c.PRICE_PER_DAY,
            c.AVAILABILITY
        FROM [CarRentalDB].[dbo].[CAR] AS c
        JOIN [CarRentalDB].[dbo].[CAR_TYPE] AS t
            ON c.TYPE_ID = t.TYPE_ID
        JOIN [CarRentalDB].[dbo].[USER_PROFILE] AS u
            ON c.USER_ID = u.USER_ID
        ORDER BY c.CAR_ID ASC
    """;

    try (Connection conn = getConnection();
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            Car car = new Car();
            car.setCarId(rs.getInt("CAR_ID"));
            car.setModel(rs.getString("MODEL"));
            car.setBrand(rs.getString("BRAND"));
            car.setYear(rs.getInt("YEAR"));
            car.setLicensePlate(rs.getString("LICENSE_PLATE"));
            car.setCapacity(rs.getInt("CAPACITY"));
            car.setFuelType(rs.getString("FUEL_TYPE"));
            car.setPricePerDay(rs.getBigDecimal("PRICE_PER_DAY"));
            car.setAvailability(rs.getBoolean("AVAILABILITY"));

            // Thông tin join thêm
            car.setTypeName(rs.getString("TYPE_NAME"));
            car.setCarOwnerName(rs.getString("CAR_OWNER_NAME"));

            cars.add(car);
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return cars;
}


    /**
     * Lấy danh sách xe mà chủ xe đang sở hữu.
     */
    public List<CarViewModel> getCarsByOwner(int ownerId) {
        List<CarViewModel> list = new ArrayList<>();
        String sql = "SELECT c.CAR_ID, c.BRAND, c.MODEL, c.PRICE_PER_DAY, c.CAPACITY, " +
                "c.TRANSMISSION, c.FUEL_TYPE, t.NAME AS CAR_TYPE_NAME, i.IMAGE_URL " +
                "FROM CAR c " +
                "JOIN CAR_TYPE t ON c.TYPE_ID = t.TYPE_ID " +
                "LEFT JOIN CAR_IMAGE i ON c.CAR_ID = i.CAR_ID " +
                "WHERE c.OWNER_ID = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, ownerId);
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





}
