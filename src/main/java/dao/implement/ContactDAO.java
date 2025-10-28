package dao.implement;

import model.Contact;
import dao.DBContext;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ContactDAO extends DBContext {

    // Câu lệnh SQL insert
    private static final String INSERT_CONTACT_SQL =
            "INSERT INTO SUPPORT_TICKET_REQUIREMENT (USER_ID, MESSAGE, PHONE_NUMBER, EMAIL, NAME) VALUES (?, ?, ?, ?, ?)";

    public boolean insertContact(Contact contact) {
        String sql = "INSERT INTO SUPPORT_TICKET_REQUIREMENT (USER_ID, MESSAGE, PHONE_NUMBER, EMAIL, NAME) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = this.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (contact.getUserId() == null) {
                ps.setNull(1, java.sql.Types.INTEGER);
            } else {
                ps.setInt(1, contact.getUserId());
            }

            ps.setString(2, contact.getMessage());
            ps.setString(3, contact.getPhoneNumber());
            ps.setString(4, contact.getEmail());
            ps.setString(5, contact.getName());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("SQL Error in insertContact: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

}
