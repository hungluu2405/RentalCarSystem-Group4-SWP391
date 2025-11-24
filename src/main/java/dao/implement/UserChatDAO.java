package dao.implement;

import dao.DBContext;
import model.UserConversation;
import model.UserMessage;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserChatDAO extends DBContext {

    // ==================== CONVERSATION METHODS ====================

    public UserConversation getOrCreateConversation(int bookingId, int customerId, int ownerId) {
        // First, try to get existing conversation
        UserConversation existing = getConversationByBookingId(bookingId);
        if (existing != null) {
            return existing;
        }

        // Create new conversation
        String sql = "INSERT INTO USER_CONVERSATION (booking_id, customer_id, owner_id, created_at, last_message_at, is_active) " +
                    "VALUES (?, ?, ?, GETDATE(), GETDATE(), 1)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, bookingId);
            ps.setInt(2, customerId);
            ps.setInt(3, ownerId);
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int conversationId = rs.getInt(1);
                return getConversationById(conversationId);
            }
        } catch (SQLException e) {
            System.err.println("Error creating conversation: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public UserConversation getConversationByBookingId(int bookingId) {
        String sql = "SELECT * FROM USER_CONVERSATION WHERE booking_id = ? AND is_active = 1";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, bookingId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapConversation(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error getting conversation by booking: " + e.getMessage());
        }
        return null;
    }

    public UserConversation getConversationById(int conversationId) {
        String sql = "SELECT * FROM USER_CONVERSATION WHERE conversation_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, conversationId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapConversation(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error getting conversation: " + e.getMessage());
        }
        return null;
    }

    public List<UserConversation> getConversationsForUser(int userId) {
        List<UserConversation> conversations = new ArrayList<>();
        String sql = "SELECT c.*, " +
                    "CASE " +
                    "  WHEN c.customer_id = ? THEN (SELECT FULL_NAME FROM [USER] WHERE user_id = c.owner_id) " +
                    "  ELSE (SELECT FULL_NAME FROM [USER] WHERE user_id = c.customer_id) " +
                    "END AS other_user_name, " +
                    "(SELECT TOP 1 TITLE FROM CAR WHERE CAR_ID = (SELECT CAR_ID FROM BOOKING WHERE BOOKING_ID = c.booking_id)) AS car_name, " +
                    "(SELECT TOP 1 content FROM USER_MESSAGE WHERE conversation_id = c.conversation_id ORDER BY created_at DESC) AS last_message, " +
                    "(SELECT COUNT(*) FROM USER_MESSAGE WHERE conversation_id = c.conversation_id AND sender_id != ? AND is_read = 0) AS unread_count " +
                    "FROM USER_CONVERSATION c " +
                    "WHERE (c.customer_id = ? OR c.owner_id = ?) AND c.is_active = 1 " +
                    "ORDER BY c.last_message_at DESC";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, userId);
            ps.setInt(3, userId);
            ps.setInt(4, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                UserConversation conv = mapConversation(rs);
                conv.setOtherUserName(rs.getString("other_user_name"));
                conv.setCarName(rs.getString("car_name"));
                conv.setLastMessageContent(rs.getString("last_message"));
                conv.setUnreadCount(rs.getInt("unread_count"));
                conversations.add(conv);
            }
        } catch (SQLException e) {
            System.err.println("Error getting conversations for user: " + e.getMessage());
            e.printStackTrace();
        }
        return conversations;
    }

    public void updateConversationLastMessage(int conversationId) {
        String sql = "UPDATE USER_CONVERSATION SET last_message_at = GETDATE() WHERE conversation_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, conversationId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating conversation: " + e.getMessage());
        }
    }

    // ==================== MESSAGE METHODS ====================

    public UserMessage sendMessage(int conversationId, int senderId, String content,
                                   String attachmentUrl, String attachmentType) {
        String sql = "INSERT INTO USER_MESSAGE (conversation_id, sender_id, content, attachment_url, attachment_type, created_at, is_read) " +
                    "VALUES (?, ?, ?, ?, ?, GETDATE(), 0)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, conversationId);
            ps.setInt(2, senderId);

            if (content != null && !content.isEmpty()) {
                ps.setString(3, content);
            } else {
                ps.setNull(3, Types.NVARCHAR);
            }

            if (attachmentUrl != null && !attachmentUrl.isEmpty()) {
                ps.setString(4, attachmentUrl);
                ps.setString(5, attachmentType);
            } else {
                ps.setNull(4, Types.NVARCHAR);
                ps.setNull(5, Types.NVARCHAR);
            }

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int messageId = rs.getInt(1);
                updateConversationLastMessage(conversationId);
                return getMessageById(messageId);
            }
        } catch (SQLException e) {
            System.err.println("Error sending message: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public UserMessage getMessageById(int messageId) {
        String sql = "SELECT m.*, u.FULL_NAME as sender_name " +
                    "FROM USER_MESSAGE m " +
                    "LEFT JOIN [USER] u ON m.sender_id = u.user_id " +
                    "WHERE m.message_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, messageId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                UserMessage msg = mapMessage(rs);
                msg.setSenderName(rs.getString("sender_name"));
                return msg;
            }
        } catch (SQLException e) {
            System.err.println("Error getting message: " + e.getMessage());
        }
        return null;
    }

    public List<UserMessage> getMessages(int conversationId, int limit, int offset) {
        List<UserMessage> messages = new ArrayList<>();
        String sql = "SELECT m.*, u.FULL_NAME as sender_name " +
                    "FROM USER_MESSAGE m " +
                    "LEFT JOIN [USER] u ON m.sender_id = u.user_id " +
                    "WHERE m.conversation_id = ? " +
                    "ORDER BY m.created_at DESC " +
                    "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, conversationId);
            ps.setInt(2, offset);
            ps.setInt(3, limit);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                UserMessage msg = mapMessage(rs);
                msg.setSenderName(rs.getString("sender_name"));
                messages.add(msg);
            }
        } catch (SQLException e) {
            System.err.println("Error getting messages: " + e.getMessage());
            e.printStackTrace();
        }
        return messages;
    }

    public List<UserMessage> getNewMessages(int conversationId, int lastMessageId) {
        List<UserMessage> messages = new ArrayList<>();
        String sql = "SELECT m.*, u.FULL_NAME as sender_name " +
                    "FROM USER_MESSAGE m " +
                    "LEFT JOIN [USER] u ON m.sender_id = u.user_id " +
                    "WHERE m.conversation_id = ? AND m.message_id > ? " +
                    "ORDER BY m.created_at ASC";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, conversationId);
            ps.setInt(2, lastMessageId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                UserMessage msg = mapMessage(rs);
                msg.setSenderName(rs.getString("sender_name"));
                messages.add(msg);
            }
        } catch (SQLException e) {
            System.err.println("Error getting new messages: " + e.getMessage());
        }
        return messages;
    }

    public void markMessagesAsRead(int conversationId, int userId) {
        String sql = "UPDATE USER_MESSAGE " +
                    "SET is_read = 1, read_at = GETDATE() " +
                    "WHERE conversation_id = ? AND sender_id != ? AND is_read = 0";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, conversationId);
            ps.setInt(2, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error marking messages as read: " + e.getMessage());
        }
    }

    public int getUnreadCount(int conversationId, int userId) {
        String sql = "SELECT COUNT(*) FROM USER_MESSAGE " +
                    "WHERE conversation_id = ? AND sender_id != ? AND is_read = 0";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, conversationId);
            ps.setInt(2, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error getting unread count: " + e.getMessage());
        }
        return 0;
    }

    // ==================== TYPING STATUS METHODS ====================

    public void updateTypingStatus(int conversationId, int userId, boolean isTyping) {
        // First try to update existing status
        String updateSql = "UPDATE TYPING_STATUS " +
                          "SET is_typing = ?, last_updated = GETDATE() " +
                          "WHERE conversation_id = ? AND user_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(updateSql)) {
            ps.setBoolean(1, isTyping);
            ps.setInt(2, conversationId);
            ps.setInt(3, userId);
            int rowsAffected = ps.executeUpdate();

            // If no rows updated, insert new record
            if (rowsAffected == 0) {
                String insertSql = "INSERT INTO TYPING_STATUS (conversation_id, user_id, is_typing, last_updated) " +
                                  "VALUES (?, ?, ?, GETDATE())";
                try (PreparedStatement insertPs = conn.prepareStatement(insertSql)) {
                    insertPs.setInt(1, conversationId);
                    insertPs.setInt(2, userId);
                    insertPs.setBoolean(3, isTyping);
                    insertPs.executeUpdate();
                }
            }
        } catch (SQLException e) {
            System.err.println("Error updating typing status: " + e.getMessage());
        }
    }

    public boolean isUserTyping(int conversationId, int userId) {
        String sql = "SELECT is_typing FROM TYPING_STATUS " +
                    "WHERE conversation_id = ? AND user_id = ? " +
                    "AND DATEDIFF(SECOND, last_updated, GETDATE()) < 5";  // Consider typing if updated within last 5 seconds
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, conversationId);
            ps.setInt(2, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getBoolean("is_typing");
            }
        } catch (SQLException e) {
            System.err.println("Error checking typing status: " + e.getMessage());
        }
        return false;
    }

    // ==================== HELPER METHODS ====================

    public boolean canUserAccessConversation(int conversationId, int userId) {
        String sql = "SELECT COUNT(*) FROM USER_CONVERSATION " +
                    "WHERE conversation_id = ? AND (customer_id = ? OR owner_id = ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, conversationId);
            ps.setInt(2, userId);
            ps.setInt(3, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error checking conversation access: " + e.getMessage());
        }
        return false;
    }

    // ==================== MAPPING METHODS ====================

    private UserConversation mapConversation(ResultSet rs) throws SQLException {
        UserConversation conv = new UserConversation();
        conv.setConversationId(rs.getInt("conversation_id"));
        conv.setBookingId(rs.getInt("booking_id"));
        conv.setCustomerId(rs.getInt("customer_id"));
        conv.setOwnerId(rs.getInt("owner_id"));
        conv.setCreatedAt(rs.getTimestamp("created_at"));
        conv.setLastMessageAt(rs.getTimestamp("last_message_at"));
        conv.setActive(rs.getBoolean("is_active"));
        return conv;
    }

    private UserMessage mapMessage(ResultSet rs) throws SQLException {
        UserMessage msg = new UserMessage();
        msg.setMessageId(rs.getInt("message_id"));
        msg.setConversationId(rs.getInt("conversation_id"));
        msg.setSenderId(rs.getInt("sender_id"));
        msg.setContent(rs.getString("content"));
        msg.setAttachmentUrl(rs.getString("attachment_url"));
        msg.setAttachmentType(rs.getString("attachment_type"));
        msg.setCreatedAt(rs.getTimestamp("created_at"));
        msg.setRead(rs.getBoolean("is_read"));
        msg.setReadAt(rs.getTimestamp("read_at"));
        return msg;
    }
}
