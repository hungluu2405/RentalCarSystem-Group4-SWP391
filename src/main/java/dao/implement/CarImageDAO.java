package dao.implement;

import dao.DBContext;
import java.sql.*;
import java.util.*;
import model.CarImage;

public class CarImageDAO extends DBContext {
    public List<CarImage> getImagesByCarId(int carId) {
        List<CarImage> list = new ArrayList<>();
        String sql = "SELECT * FROM CAR_IMAGE WHERE CAR_ID = ?";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, carId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                CarImage img = new CarImage();
                img.setImageId(rs.getInt("IMAGE_ID"));
                img.setCarId(rs.getInt("CAR_ID"));
                img.setImageUrl(rs.getString("IMAGE_URL"));
                list.add(img);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
