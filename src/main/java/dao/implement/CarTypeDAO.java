package dao.implement;

import dao.DBContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import model.CarType;

public class CarTypeDAO extends DBContext {

    public List<CarType> getAllCarTypes() {
        List<CarType> typeList = new ArrayList<>();
        String sql = "SELECT * FROM CAR_TYPE";

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                CarType type = new CarType();
                type.setTypeId(rs.getInt("TYPE_ID"));
                type.setName(rs.getString("NAME"));
                type.setDescription(rs.getString("DESCRIPTION"));
                typeList.add(type);
            }
        } catch (Exception e) { e.printStackTrace(); }

        return typeList;
    }
}
