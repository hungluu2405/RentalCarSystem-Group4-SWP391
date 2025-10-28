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
        try (Connection conn = this.connection;
             PreparedStatement ps = conn.prepareStatement(INSERT_CONTACT_SQL)) {


            if (contact.getUserId() == null) {
                ps.setNull(1, java.sql.Types.INTEGER);
            } else {
                ps.setInt(1, contact.getUserId());
            }

            ps.setString(2, contact.getMessage());
            ps.setString(3, contact.getPhoneNumber());
            ps.setString(4, contact.getEmail());
            ps.setString(5, contact.getName());

            // Thực thi
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("❌ SQL Error in insertContact: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
