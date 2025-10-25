package dao.implement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import dao.DBContext;
import model.Contact;

public class ContactDao extends DBContext {

    public void insertContact(Contact contact) throws SQLException {
        String sql = "INSERT INTO SUPPORT_TICKET_REQUIREMENT "
                   + "(USER_ID, NAME, EMAIL, PHONE_NUMBER, MESSAGE, CREATED_AT) "
                   + "VALUES (?, ?, ?, ?, ?, CURRENT_TIMESTAMP)";
        
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, contact.getUserId());
        ps.setString(2, contact.getName());
        ps.setString(3, contact.getEmail());
        ps.setString(4, contact.getPhoneNumber());
        ps.setString(5, contact.getMessage());
        ps.executeUpdate();
        
        ps.close();
    }
}
