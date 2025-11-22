package model;

import java.sql.Timestamp;

public class ChatMessage {
    private int messageId;
    private int conversationId;
    private String role; // "user" or "model"
    private String content;
    private Timestamp createdAt;
    private String metadata;
    private Integer tokensUsed;
    private Integer responseTimeMs;

    public ChatMessage() {
    }

    public ChatMessage(int messageId, int conversationId, String role, String content,
                      Timestamp createdAt, String metadata, Integer tokensUsed, Integer responseTimeMs) {
        this.messageId = messageId;
        this.conversationId = conversationId;
        this.role = role;
        this.content = content;
        this.createdAt = createdAt;
        this.metadata = metadata;
        this.tokensUsed = tokensUsed;
        this.responseTimeMs = responseTimeMs;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public int getConversationId() {
        return conversationId;
    }

    public void setConversationId(int conversationId) {
        this.conversationId = conversationId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public Integer getTokensUsed() {
        return tokensUsed;
    }

    public void setTokensUsed(Integer tokensUsed) {
        this.tokensUsed = tokensUsed;
    }

    public Integer getResponseTimeMs() {
        return responseTimeMs;
    }

    public void setResponseTimeMs(Integer responseTimeMs) {
        this.responseTimeMs = responseTimeMs;
    }

    @Override
    public String toString() {
        return "ChatMessage{" +
                "messageId=" + messageId +
                ", conversationId=" + conversationId +
                ", role='" + role + '\'' +
                ", content='" + content + '\'' +
                ", createdAt=" + createdAt +
                ", tokensUsed=" + tokensUsed +
                ", responseTimeMs=" + responseTimeMs +
                '}';
    }
}
