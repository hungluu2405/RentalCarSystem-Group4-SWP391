package model;

import java.sql.Timestamp;

public class UserConversation {
    private int conversationId;
    private int bookingId;
    private int customerId;
    private int ownerId;
    private Timestamp createdAt;
    private Timestamp lastMessageAt;
    private boolean isActive;

    // For display purposes (not from DB directly)
    private String lastMessageContent;
    private int unreadCount;
    private String otherUserName;  // Name of the person they're chatting with
    private String carName;  // Name of the car for this booking

    public UserConversation() {
    }

    public UserConversation(int conversationId, int bookingId, int customerId, int ownerId,
                           Timestamp createdAt, Timestamp lastMessageAt, boolean isActive) {
        this.conversationId = conversationId;
        this.bookingId = bookingId;
        this.customerId = customerId;
        this.ownerId = ownerId;
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

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
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

    public String getLastMessageContent() {
        return lastMessageContent;
    }

    public void setLastMessageContent(String lastMessageContent) {
        this.lastMessageContent = lastMessageContent;
    }

    public int getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }

    public String getOtherUserName() {
        return otherUserName;
    }

    public void setOtherUserName(String otherUserName) {
        this.otherUserName = otherUserName;
    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    @Override
    public String toString() {
        return "UserConversation{" +
                "conversationId=" + conversationId +
                ", bookingId=" + bookingId +
                ", customerId=" + customerId +
                ", ownerId=" + ownerId +
                ", createdAt=" + createdAt +
                ", lastMessageAt=" + lastMessageAt +
                ", isActive=" + isActive +
                '}';
    }
}
