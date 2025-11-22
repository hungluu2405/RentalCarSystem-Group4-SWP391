package model;

import java.sql.Timestamp;

public class ChatConversation {
    private int conversationId;
    private Integer userId; // Nullable for guest users
    private String sessionId;
    private Timestamp createdAt;
    private Timestamp lastMessageAt;
    private boolean isActive;

    public ChatConversation() {
    }

    public ChatConversation(int conversationId, Integer userId, String sessionId,
                           Timestamp createdAt, Timestamp lastMessageAt, boolean isActive) {
        this.conversationId = conversationId;
        this.userId = userId;
        this.sessionId = sessionId;
        this.createdAt = createdAt;
        this.lastMessageAt = lastMessageAt;
        this.isActive = isActive;
    }

    public int getConversationId() {
        return conversationId;
    }

    public void setConversationId(int conversationId) {
        this.conversationId = conversationId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getLastMessageAt() {
        return lastMessageAt;
    }

    public void setLastMessageAt(Timestamp lastMessageAt) {
        this.lastMessageAt = lastMessageAt;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public String toString() {
        return "ChatConversation{" +
                "conversationId=" + conversationId +
                ", userId=" + userId +
                ", sessionId='" + sessionId + '\'' +
                ", createdAt=" + createdAt +
                ", lastMessageAt=" + lastMessageAt +
                ", isActive=" + isActive +
                '}';
    }
}
