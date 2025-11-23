package model;

import java.sql.Timestamp;

public class UserMessage {
    private int messageId;
    private int conversationId;
    private int senderId;
    private String content;
    private String attachmentUrl;
    private String attachmentType;  // 'image', 'file', null
    private Timestamp createdAt;
    private boolean isRead;
    private Timestamp readAt;

    // For display purposes
    private String senderName;

    public UserMessage() {
    }

    public UserMessage(int messageId, int conversationId, int senderId, String content,
                      String attachmentUrl, String attachmentType, Timestamp createdAt,
                      boolean isRead, Timestamp readAt) {
        this.messageId = messageId;
        this.conversationId = conversationId;
        this.senderId = senderId;
        this.content = content;
        this.attachmentUrl = attachmentUrl;
        this.attachmentType = attachmentType;
        this.createdAt = createdAt;
        this.isRead = isRead;
        this.readAt = readAt;
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

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAttachmentUrl() {
        return attachmentUrl;
    }

    public void setAttachmentUrl(String attachmentUrl) {
        this.attachmentUrl = attachmentUrl;
    }

    public String getAttachmentType() {
        return attachmentType;
    }

    public void setAttachmentType(String attachmentType) {
        this.attachmentType = attachmentType;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public Timestamp getReadAt() {
        return readAt;
    }

    public void setReadAt(Timestamp readAt) {
        this.readAt = readAt;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    @Override
    public String toString() {
        return "UserMessage{" +
                "messageId=" + messageId +
                ", conversationId=" + conversationId +
                ", senderId=" + senderId +
                ", content='" + content + '\'' +
                ", attachmentUrl='" + attachmentUrl + '\'' +
                ", attachmentType='" + attachmentType + '\'' +
                ", createdAt=" + createdAt +
                ", isRead=" + isRead +
                ", readAt=" + readAt +
                '}';
    }
}
