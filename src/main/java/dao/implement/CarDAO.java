package dao.implement;

import dao.DBContext;
import java.sql.*;
import java.util.*;
import java.math.BigDecimal;
import model.Car;
import model.CarViewModel;
import model.CarImage;
import model.CarType;

public class CarDAO extends DBContext {

    // Lấy danh sách xe có filter và phân trang
    public List<CarViewModel> findCars(String name, String brand, String typeIdStr,
                                       String capacity, String fuel, String price,
                                       int page, int pageSize) {
        List<CarViewModel> list = new ArrayList<>();
        String sql = "SELECT c.CAR_ID, c.BRAND, c.MODEL, c.PRICE_PER_DAY, c.CAPACITY, "
                + "c.TRANSMISSION, c.FUEL_TYPE, c.LOCATION, t.NAME AS CAR_TYPE_NAME, "
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
                car.setLocation(rs.getString("LOCATION")); // ✅ thêm location

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

    public List<String> getAllTypes() {
        List<String> types = new ArrayList<>();
        String sql = "SELECT TYPE_ID, NAME FROM CAR_TYPE ORDER BY NAME";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                types.add(rs.getInt("TYPE_ID") + ":" + rs.getString("NAME"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return types;
    }

    // Lấy danh sách tất cả loại xe
    public List<CarType> getAllCarTypes() {
        List<CarType> list = new ArrayList<>();
        String sql = "SELECT TYPE_ID, NAME, DESCRIPTION FROM CAR_TYPE";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                CarType ct = new CarType();
                ct.setTypeId(rs.getInt("TYPE_ID"));
                ct.setName(rs.getString("NAME"));
                ct.setDescription(rs.getString("DESCRIPTION"));
                list.add(ct);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // Lấy danh sách truyền động (Transmission)
    public List<String> getAllTransmissions() {
        List<String> list = new ArrayList<>();
        String sql = "SELECT DISTINCT TRANSMISSION FROM CAR WHERE TRANSMISSION IS NOT NULL";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(rs.getString("TRANSMISSION"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // Lấy danh sách loại nhiên liệu (Fuel type)
    public List<String> getAllFuelTypess() {
        List<String> list = new ArrayList<>();
        String sql = "SELECT DISTINCT FUEL_TYPE FROM CAR WHERE FUEL_TYPE IS NOT NULL";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(rs.getString("FUEL_TYPE"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }




    // Lấy chi tiết xe theo ID, có cả description và ảnh
    // Trong lớp CarDAO.java

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
                car.setLocation(rs.getString("LOCATION"));
                car.setLicensePlate(rs.getString("LICENSE_PLATE"));

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

        String sql = "SELECT TOP " + limit + " "
                + "c.CAR_ID, c.BRAND, c.MODEL, c.CAPACITY, c.TRANSMISSION, c.FUEL_TYPE, c.LOCATION, c.PRICE_PER_DAY, "
                + "ct.NAME AS CarTypeName, "
                + "(SELECT TOP 1 ci.IMAGE_URL FROM CAR_IMAGE ci WHERE ci.CAR_ID = c.CAR_ID ORDER BY ci.IMAGE_ID) AS ImageUrl, "
                + "COUNT(b.BOOKING_ID) AS booking_count "
                + "FROM CAR c "
                + "JOIN CAR_TYPE ct ON c.TYPE_ID = ct.TYPE_ID "
                + "LEFT JOIN BOOKING b ON c.CAR_ID = b.CAR_ID "
                + "WHERE c.AVAILABILITY = 1 "
                + "GROUP BY c.CAR_ID, c.BRAND, c.MODEL, c.CAPACITY, c.TRANSMISSION, c.FUEL_TYPE, c.PRICE_PER_DAY, c.LOCATION, ct.NAME "
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
                car.setLocation(rs.getString("LOCATION")); // ✅ thêm dòng này
                car.setImageUrl(rs.getString("ImageUrl"));
                cars.add(car);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return cars;
    }
    // ==========================
    // ĐẾM SỐ LƯỢNG BOOKING THEO CHỦ XE THEO SIDEBAROWNER
    // ==========================

    public int countCarsByOwner(int ownerId) {
        String sql = "SELECT COUNT(*) FROM CAR WHERE USER_ID = ?";
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

 //Hàm đếm tổng số lượng tất cả các booking (đặt xe)

    public int countTotalBookingsByOwner(int ownerId) {
        String sql = "SELECT COUNT(*) " +
                "FROM BOOKING b " +
                "JOIN CAR c ON b.CAR_ID = c.CAR_ID " +
                " JOIN USER_PROFILE up ON b.USER_ID = up.USER_ID "+
                "WHERE c.USER_ID = ?";
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
//Hàm đếm số lượng booking đã được chấp nhận (Approved)
    public int countApprovedBookingsByOwner(int ownerId) {
        String sql = "SELECT COUNT(*) " +
                "FROM BOOKING b " +
                "JOIN CAR c ON b.CAR_ID = c.CAR_ID " +
                "WHERE c.USER_ID = ? AND b.STATUS = 'Approved'";
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
    //Hàm đếm số lượng booking đã bị từ chối (Rejected)
    public int countRejectedBookingsByOwner(int ownerId) {
        String sql = "SELECT COUNT(*) " +
                "FROM BOOKING b " +
                "JOIN CAR c ON b.CAR_ID = c.CAR_ID " +
                "WHERE c.USER_ID = ? AND b.STATUS = 'Rejected'";
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


    public List<CarViewModel> getCarsByOwner(int ownerId) {
        List<CarViewModel> list = new ArrayList<>();
        String sql = "SELECT c.CAR_ID, c.BRAND, c.MODEL, c.LOCATION, c.PRICE_PER_DAY, c.CAPACITY, " +
                "c.TRANSMISSION, c.FUEL_TYPE, c.YEAR, t.NAME AS CAR_TYPE_NAME, i.IMAGE_URL " +
                "FROM CAR c " +
                "JOIN CAR_TYPE t ON c.TYPE_ID = t.TYPE_ID " +
                "LEFT JOIN CAR_IMAGE i ON c.CAR_ID = i.CAR_ID " +
                "WHERE c.USER_ID = ?";

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
                car.setLocation(rs.getString("LOCATION"));
                car.setYear(rs.getInt("YEAR"));
                car.setImageUrl(
                        rs.getString("IMAGE_URL") != null
                                ? rs.getString("IMAGE_URL")
                                : "images/default.jpg"
                );

                list.add(car);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public CarViewModel getCarSingleById(int carId) {

        String sql = "SELECT c.CAR_ID, c.TYPE_ID, c.BRAND, c.MODEL, c.LOCATION, c.PRICE_PER_DAY, c.CAPACITY, " +
                "c.TRANSMISSION, c.FUEL_TYPE, c.YEAR, t.NAME AS CAR_TYPE_NAME, i.IMAGE_URL, " +
                "c.DESCRIPTION, c.LICENSE_PLATE " +
                "FROM CAR c " +
                "JOIN CAR_TYPE t ON c.TYPE_ID = t.TYPE_ID " +
                "LEFT JOIN CAR_IMAGE i ON c.CAR_ID = i.CAR_ID " +
                "WHERE c.CAR_ID = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
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
                car.setCarTypeName(rs.getString("CAR_TYPE_NAME"));
                car.setLocation(rs.getString("LOCATION"));
                car.setYear(rs.getInt("YEAR"));
                car.setLicensePlate(rs.getString("LICENSE_PLATE"));
                car.setDescription(rs.getString("DESCRIPTION"));
                car.setImageUrl(
                        rs.getString("IMAGE_URL") != null
                                ? rs.getString("IMAGE_URL")
                                : "images/default.jpg"
                );
                return car;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateCar(CarViewModel car) {
        String sql = "UPDATE CAR " +
                "SET BRAND = ?, MODEL = ?, PRICE_PER_DAY = ?, CAPACITY = ?, TRANSMISSION = ?, " +
                "FUEL_TYPE = ?, DESCRIPTION = ?, LOCATION = ?, LICENSE_PLATE = ?, YEAR = ?, TYPE_ID = ? " +
                "WHERE CAR_ID = ?";

        String sqlImage = "UPDATE CAR_IMAGE SET IMAGE_URL = ? WHERE CAR_ID = ?";

        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);

            // --- Cập nhật thông tin cơ bản của xe ---
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, car.getBrand());
                ps.setString(2, car.getModel());
                ps.setBigDecimal(3, car.getPricePerDay());
                ps.setInt(4, car.getCapacity());
                ps.setString(5, car.getTransmission());
                ps.setString(6, car.getFuelType());
                ps.setString(7, car.getDescription());
                ps.setString(8, car.getLocation());
                ps.setString(9, car.getLicensePlate());
                ps.setInt(10, car.getYear());
                ps.setInt(11, car.getTypeId());
                ps.setInt(12, car.getCarId());
                ps.executeUpdate();
            }

            // --- Nếu có ảnh mới thì cập nhật ---
            if (car.getImageUrl() != null && !car.getImageUrl().isEmpty()) {
                try (PreparedStatement psImg = conn.prepareStatement(sqlImage)) {
                    psImg.setString(1, car.getImageUrl().replace("images/car/", "")); // lưu tên file thuần
                    psImg.setInt(2, car.getCarId());
                    int rows = psImg.executeUpdate();

                    // Nếu chưa có ảnh trong bảng CAR_IMAGE thì thêm mới
                    if (rows == 0) {
                        String insertImg = "INSERT INTO CAR_IMAGE (CAR_ID, IMAGE_URL) VALUES (?, ?)";
                        try (PreparedStatement psInsert = conn.prepareStatement(insertImg)) {
                            psInsert.setInt(1, car.getCarId());
                            psInsert.setString(2, car.getImageUrl().replace("images/car/", ""));
                            psInsert.executeUpdate();
                        }
                    }
                }
            }
            conn.commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }



    public boolean deleteCar(int carId) {
        String sqlImage = "DELETE FROM CAR_IMAGE WHERE CAR_ID = ?";
        String sqlCar = "DELETE FROM CAR WHERE CAR_ID = ?";
        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false); // Bắt đầu transaction

            // 1. Xóa ảnh xe trước (nếu có)
            try (PreparedStatement psImg = conn.prepareStatement(sqlImage)) {
                psImg.setInt(1, carId);
                psImg.executeUpdate();
            }

            // 2. Xóa xe
            int rowsDeleted;
            try (PreparedStatement psCar = conn.prepareStatement(sqlCar)) {
                psCar.setInt(1, carId);
                rowsDeleted = psCar.executeUpdate();
            }

            conn.commit(); // Hoàn tất transaction
            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 🔍 Lấy thông tin xe theo ID
    public Car findById(int carId) {
        String sql = "SELECT * FROM CAR WHERE CAR_ID = ?";
        try (Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql) ) {
            ps.setInt(1, carId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Car car = new Car();
                car.setCarId(rs.getInt("CAR_ID"));
                car.setOwnerId(rs.getInt("USER_ID"));
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
                return car;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public double getCarPrice(int carId) {
        String sql = "SELECT PRICE_PER_DAY FROM CAR WHERE CAR_ID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, carId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble("PRICE_PER_DAY");
            }
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

    //HÀM THÊM CAR
    public int addCarAndReturnId(Car car) {
        String sql = "INSERT INTO CAR (USER_ID, TYPE_ID, MODEL, BRAND, YEAR, LICENSE_PLATE, CAPACITY, TRANSMISSION, FUEL_TYPE, PRICE_PER_DAY, DESCRIPTION, AVAILABILITY, LOCATION) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, car.getOwnerId());
            ps.setInt(2, car.getTypeId());
            ps.setString(3, car.getModel());
            ps.setString(4, car.getBrand());
            ps.setInt(5, car.getYear());
            ps.setString(6, car.getLicensePlate());
            ps.setInt(7, car.getCapacity());
            ps.setString(8, car.getTransmission());
            ps.setString(9, car.getFuelType());
            ps.setBigDecimal(10, car.getPricePerDay());
            ps.setString(11, car.getDescription());
            ps.setBoolean(12, car.isAvailability());
            ps.setString(13, car.getLocation());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
    //HÀM UP ẢNH XE
    public void addCarImage(int carId, String imageUrl) {
        String sql = "INSERT INTO CAR_IMAGE (CAR_ID, IMAGE_URL) VALUES (?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, carId);
            ps.setString(2, imageUrl);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public int countAllCars() {
        String sql = "SELECT COUNT(*) AS total FROM [CAR]";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }




}
