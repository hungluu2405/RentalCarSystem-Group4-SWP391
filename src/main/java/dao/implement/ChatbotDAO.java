package dao.implement;

import dao.DBContext;
import model.ChatConversation;
import model.ChatMessage;
import model.ChatbotConfig;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChatbotDAO extends DBContext {

    // ==================== CONVERSATION METHODS ====================

    public ChatConversation createConversation(Integer userId, String sessionId) {
        String sql = "INSERT INTO CHAT_CONVERSATION (user_id, session_id, created_at, last_message_at, is_active) " +
                    "VALUES (?, ?, GETDATE(), GETDATE(), 1)";
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            if (userId != null) {
                ps.setInt(1, userId);
            } else {
                ps.setNull(1, Types.INTEGER);
            }
            ps.setString(2, sessionId);
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

    public ChatConversation getConversationById(int conversationId) {
        String sql = "SELECT * FROM CHAT_CONVERSATION WHERE conversation_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
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

    public ChatConversation getActiveConversationBySessionId(String sessionId) {
        String sql = "SELECT * FROM CHAT_CONVERSATION WHERE session_id = ? AND is_active = 1 " +
                    "ORDER BY created_at DESC";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, sessionId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapConversation(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error getting active conversation: " + e.getMessage());
        }
        return null;
    }

    public void updateConversationLastMessage(int conversationId) {
        String sql = "UPDATE CHAT_CONVERSATION SET last_message_at = GETDATE() WHERE conversation_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, conversationId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating conversation: " + e.getMessage());
        }
    }

    // ==================== MESSAGE METHODS ====================

    public ChatMessage createMessage(int conversationId, String role, String content,
                                    Integer tokensUsed, Integer responseTimeMs) {
        String sql = "INSERT INTO CHAT_MESSAGE (conversation_id, role, content, created_at, tokens_used, response_time_ms) " +
                    "VALUES (?, ?, ?, GETDATE(), ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, conversationId);
            ps.setString(2, role);
            ps.setString(3, content);

            if (tokensUsed != null) {
                ps.setInt(4, tokensUsed);
            } else {
                ps.setNull(4, Types.INTEGER);
            }

            if (responseTimeMs != null) {
                ps.setInt(5, responseTimeMs);
            } else {
                ps.setNull(5, Types.INTEGER);
            }

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int messageId = rs.getInt(1);
                updateConversationLastMessage(conversationId);
                return getMessageById(messageId);
            }
        } catch (SQLException e) {
            System.err.println("Error creating message: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public ChatMessage getMessageById(int messageId) {
        String sql = "SELECT * FROM CHAT_MESSAGE WHERE message_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, messageId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapMessage(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error getting message: " + e.getMessage());
        }
        return null;
    }

    public List<ChatMessage> getMessagesByConversationId(int conversationId) {
        List<ChatMessage> messages = new ArrayList<>();
        String sql = "SELECT * FROM CHAT_MESSAGE WHERE conversation_id = ? ORDER BY created_at ASC";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, conversationId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                messages.add(mapMessage(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting messages: " + e.getMessage());
        }
        return messages;
    }

    // ==================== CONFIG METHODS ====================

    public String getConfigValue(String configKey) {
        String sql = "SELECT config_value FROM CHATBOT_CONFIG WHERE config_key = ? AND is_active = 1";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, configKey);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("config_value");
            }
        } catch (SQLException e) {
            System.err.println("Error getting config: " + e.getMessage());
        }
        return null;
    }

    public ChatbotConfig getConfig(String configKey) {
        String sql = "SELECT * FROM CHATBOT_CONFIG WHERE config_key = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, configKey);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapConfig(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error getting config: " + e.getMessage());
        }
        return null;
    }

    // ==================== MAPPING METHODS ====================

    private ChatConversation mapConversation(ResultSet rs) throws SQLException {
        ChatConversation conv = new ChatConversation();
        conv.setConversationId(rs.getInt("conversation_id"));

        int userId = rs.getInt("user_id");
        conv.setUserId(rs.wasNull() ? null : userId);

        conv.setSessionId(rs.getString("session_id"));
        conv.setCreatedAt(rs.getTimestamp("created_at"));
        conv.setLastMessageAt(rs.getTimestamp("last_message_at"));
        conv.setActive(rs.getBoolean("is_active"));
        return conv;
    }

    private ChatMessage mapMessage(ResultSet rs) throws SQLException {
        ChatMessage msg = new ChatMessage();
        msg.setMessageId(rs.getInt("message_id"));
        msg.setConversationId(rs.getInt("conversation_id"));
        msg.setRole(rs.getString("role"));
        msg.setContent(rs.getString("content"));
        msg.setCreatedAt(rs.getTimestamp("created_at"));
        msg.setMetadata(rs.getString("metadata"));

        int tokensUsed = rs.getInt("tokens_used");
        msg.setTokensUsed(rs.wasNull() ? null : tokensUsed);

        int responseTime = rs.getInt("response_time_ms");
        msg.setResponseTimeMs(rs.wasNull() ? null : responseTime);

        return msg;
    }

    private ChatbotConfig mapConfig(ResultSet rs) throws SQLException {
        ChatbotConfig config = new ChatbotConfig();
        config.setConfigId(rs.getInt("config_id"));
        config.setConfigKey(rs.getString("config_key"));
        config.setConfigValue(rs.getString("config_value"));
        config.setDescription(rs.getString("description"));
        config.setActive(rs.getBoolean("is_active"));
        config.setCreatedAt(rs.getTimestamp("created_at"));
        config.setUpdatedAt(rs.getTimestamp("updated_at"));
        return config;
    }
}
