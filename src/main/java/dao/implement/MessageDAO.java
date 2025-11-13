package dao.implement;

import model.Message;
import dao.DBContext;
import java.sql.*;
import java.util.*;

public class MessageDAO extends DBContext {

    public void insertMessage(Message msg) {
        String sql = "INSERT INTO MESSAGE (SENDER_ID, RECEIVER_ID, CONTENT) VALUES (?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, msg.getSenderId());
            ps.setInt(2, msg.getReceiverId());
            ps.setString(3, msg.getContent());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Message> getMessages(int user1, int user2) {
        List<Message> list = new ArrayList<>();
        String sql = """
            SELECT * FROM MESSAGE
            WHERE (SENDER_ID=? AND RECEIVER_ID=?) OR (SENDER_ID=? AND RECEIVER_ID=?)
            ORDER BY SENT_AT ASC
        """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, user1);
            ps.setInt(2, user2);
            ps.setInt(3, user2);
            ps.setInt(4, user1);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Message m = new Message();
                m.setMessageId(rs.getInt("MESSAGE_ID"));
                m.setSenderId(rs.getInt("SENDER_ID"));
                m.setReceiverId(rs.getInt("RECEIVER_ID"));
                m.setContent(rs.getString("CONTENT"));
                m.setSentAt(rs.getTimestamp("SENT_AT"));
                m.setRead(rs.getBoolean("IS_READ"));
                list.add(m);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Message> getNewMessages(int receiverId, int lastMsgId) {
        List<Message> list = new ArrayList<>();
        String sql = "SELECT * FROM MESSAGE WHERE RECEIVER_ID=? AND MESSAGE_ID>? ORDER BY SENT_AT ASC";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, receiverId);
            ps.setInt(2, lastMsgId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Message m = new Message();
                m.setMessageId(rs.getInt("MESSAGE_ID"));
                m.setSenderId(rs.getInt("SENDER_ID"));
                m.setReceiverId(rs.getInt("RECEIVER_ID"));
                m.setContent(rs.getString("CONTENT"));
                m.setSentAt(rs.getTimestamp("SENT_AT"));
                list.add(m);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void markAsRead(int senderId, int receiverId) {
        String sql = "UPDATE MESSAGE SET IS_READ=1 WHERE SENDER_ID=? AND RECEIVER_ID=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, senderId);
            ps.setInt(2, receiverId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Integer> getRecentChatPartners(int userId) {
        List<Integer> partners = new ArrayList<>();
        String sql = """
            SELECT DISTINCT CASE 
                WHEN SENDER_ID = ? THEN RECEIVER_ID 
                ELSE SENDER_ID 
            END AS PARTNER_ID
            FROM MESSAGE
            WHERE SENDER_ID = ? OR RECEIVER_ID = ?
        """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, userId);
            ps.setInt(3, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                partners.add(rs.getInt("PARTNER_ID"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return partners;
    }
    // Lấy tin nhắn mới giữa 2 user, sau lastMsgId
    public List<Message> getNewMessagesBetween(int user1, int user2, int lastMsgId) {
        List<Message> list = new ArrayList<>();
        String sql = """
        SELECT * FROM MESSAGE
        WHERE MESSAGE_ID > ?
          AND (
                (SENDER_ID = ? AND RECEIVER_ID = ?)
             OR (SENDER_ID = ? AND RECEIVER_ID = ?)
          )
        ORDER BY SENT_AT ASC
    """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, lastMsgId);
            ps.setInt(2, user1);
            ps.setInt(3, user2);
            ps.setInt(4, user2);
            ps.setInt(5, user1);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Message m = new Message();
                m.setMessageId(rs.getInt("MESSAGE_ID"));
                m.setSenderId(rs.getInt("SENDER_ID"));
                m.setReceiverId(rs.getInt("RECEIVER_ID"));
                m.setContent(rs.getString("CONTENT"));
                m.setSentAt(rs.getTimestamp("SENT_AT"));
                m.setRead(rs.getBoolean("IS_READ"));
                list.add(m);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

}
