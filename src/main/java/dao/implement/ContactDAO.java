package dao.implement;

import model.Contact;
import dao.DBContext;
import java.sql.*;
import java.util.*;
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
            conn.setAutoCommit(true);

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
    public List<Contact> getAllContacts() {
        List<Contact> list = new ArrayList<>();
        String sql = "SELECT TICKET_ID, NAME, PHONE_NUMBER, EMAIL, MESSAGE, CREATED_AT, STATUS "
                + "FROM SUPPORT_TICKET_REQUIREMENT ORDER BY CREATED_AT DESC";

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Contact c = new Contact();
                c.setTicketId(rs.getInt("TICKET_ID"));
                c.setName(rs.getString("NAME"));
                c.setPhoneNumber(rs.getString("PHONE_NUMBER"));
                c.setEmail(rs.getString("EMAIL"));
                c.setMessage(rs.getString("MESSAGE"));

                Timestamp ts = rs.getTimestamp("CREATED_AT");
                if (ts != null) {
                    c.setCreatedAt(ts.toLocalDateTime());
                }

                c.setStatus(rs.getBoolean("STATUS"));
                list.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Cập nhật trạng thái xử lý (bit)
    public void updateStatus(int ticketId, boolean status) {
        String sql = "UPDATE SUPPORT_TICKET_REQUIREMENT SET STATUS = ? WHERE TICKET_ID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setBoolean(1, status);
            ps.setInt(2, ticketId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int countUnresolvedContacts() {
        String sql = "SELECT COUNT(*) FROM SUPPORT_TICKET_REQUIREMENT WHERE STATUS = 0";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<Contact> getContactsFiltered(String statusFilter) {
        List<Contact> list = new ArrayList<>();

        StringBuilder sql = new StringBuilder("""
        SELECT TICKET_ID, NAME, PHONE_NUMBER, EMAIL, MESSAGE, CREATED_AT, STATUS
        FROM SUPPORT_TICKET_REQUIREMENT
        WHERE 1=1
    """);

        // Nếu có chọn trạng thái thì thêm điều kiện
        if (statusFilter != null && !statusFilter.isEmpty()) {
            if (statusFilter.equalsIgnoreCase("todo")) {
                sql.append(" AND STATUS = 0");  // Giả sử 0 = chưa xử lý
            } else if (statusFilter.equalsIgnoreCase("done")) {
                sql.append(" AND STATUS = 1");  // Giả sử 1 = đã xử lý
            }
        }

        sql.append(" ORDER BY CREATED_AT DESC");

        try (PreparedStatement ps = connection.prepareStatement(sql.toString());
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Contact c = new Contact();
                c.setTicketId(rs.getInt("TICKET_ID"));
                c.setName(rs.getString("NAME"));
                c.setPhoneNumber(rs.getString("PHONE_NUMBER"));
                c.setEmail(rs.getString("EMAIL"));
                c.setMessage(rs.getString("MESSAGE"));

                Timestamp ts = rs.getTimestamp("CREATED_AT");
                if (ts != null) {
                    c.setCreatedAt(ts.toLocalDateTime());
                }

                c.setStatus(rs.getBoolean("STATUS"));
                list.add(c);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }




}
