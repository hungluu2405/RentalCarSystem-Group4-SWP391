package model;

import java.time.LocalDateTime;

public class Notification {
    private int notificationId;
    private int userId;
    private String type;
    private String title;
    private boolean isRead;
    private String content;
    private String linkUrl;
    private LocalDateTime createdAt;

    public Notification() {
    }

    // ✅ 1. CONSTRUCTOR TIỆN LỢI (Dùng khi INSERT trong Service)
    public Notification(int userId, String type, String title, String content, String linkUrl) {
        this.userId = userId;
        this.type = type;
        this.title = title;
        this.content = content;
        this.linkUrl = linkUrl;
        this.isRead = false; // <-- THÊM: Khởi tạo isRead là false
    }

    // ✅ 2. FULL CONSTRUCTOR (Dùng khi SELECT/Ánh xạ trong DAO)
    public Notification(int notificationId, int userId, String type, String title, boolean isRead, String content, String linkUrl, LocalDateTime createdAt) {
        this.notificationId = notificationId;
        this.userId = userId;
        this.type = type;
        this.title = title;
        this.isRead = isRead; // <-- NHẬN GIÁ TRỊ IS_READ TỪ DB
        this.content = content;
        this.linkUrl = linkUrl;
        this.createdAt = createdAt;
    }


    // --- GETTERS AND SETTERS ---

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    // ... (Các Getter/Setter khác giữ nguyên) ...
    // ... (Tôi đã lược bỏ các hàm Getter/Setter còn lại ở đây vì chúng không thay đổi) ...
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }
}

