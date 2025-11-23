# User-to-User Chat System Setup Guide

## Overview
Hệ thống chat cho phép **Customer** và **Car Owner** nhắn tin trực tiếp với nhau sau khi có booking.

## Features
- ✅ Chat 1-1 giữa Customer ↔ Car Owner
- ✅ Gửi tin nhắn văn bản
- ✅ Gửi hình ảnh (JPG, PNG, GIF, WebP)
- ✅ Gửi file đính kèm (PDF, DOC, XLS, TXT)
- ✅ Typing indicator (hiển thị "đang gõ...")
- ✅ Đánh dấu đã đọc/chưa đọc
- ✅ Thông báo tin nhắn mới (badge)
- ✅ Real-time updates (polling mỗi 2 giây)
- ✅ Chatbox nổi (giống AI chatbot)

## Database Setup

### Bước 1: Chạy SQL Script
```bash
# Chạy file user-chat-schema.sql vào database
```

Hoặc trực tiếp SQL Server Management Studio:
```sql
USE CarRentalDB;
GO
-- Copy và paste nội dung từ user-chat-schema.sql
```

### Bước 2: Verify Tables
Kiểm tra các bảng đã được tạo:
- **USER_CONVERSATION** - Lưu conversations
- **USER_MESSAGE** - Lưu tin nhắn
- **TYPING_STATUS** - Trạng thái typing

```sql
SELECT * FROM USER_CONVERSATION;
SELECT * FROM USER_MESSAGE;
SELECT * FROM TYPING_STATUS;
```

## Files Created

### Backend (Java)
```
src/main/java/
├── model/
│   ├── UserConversation.java        # Model cho conversation
│   └── UserMessage.java              # Model cho message
├── dao/implement/
│   └── UserChatDAO.java              # Database operations
└── controller/userchat/
    ├── UserChatServlet.java          # Main chat API
    ├── ChatFileUploadServlet.java    # File upload handler
    └── InitChatServlet.java          # Initialize chat from booking
```

### Frontend (JSP)
```
src/main/webapp/view/common/userchat/
└── user-chatbox.jsp                  # Chat UI widget
```

## How to Use

### 1. Include Chat Widget in JSP Pages

Thêm vào **booking detail pages** (nơi customer/owner xem chi tiết booking):

```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Booking Details</title>
    <!-- Your CSS -->
</head>
<body>
    <!-- Your booking content -->

    <!-- Include User Chat Widget -->
    <jsp:include page="/view/common/userchat/user-chatbox.jsp"/>

    <script>
        // Initialize chat with bookingId
        // Chat widget will auto-detect bookingId from URL (?bookingId=123)
        // Or manually init:
        window.initUserChat(${booking.bookingId});
    </script>
</body>
</html>
```

### 2. Passing Booking ID

**Option A: Via URL Parameter** (Recommended)
```
http://localhost:8080/booking-details?bookingId=123
```
Chat widget sẽ tự động detect và init.

**Option B: Manual JavaScript Call**
```javascript
<script>
    window.initUserChat(${booking.bookingId});
</script>
```

### 3. Button Integration

Thêm button "Chat" trong booking detail:
```html
<button onclick="document.getElementById('user-chat-button').click()">
    <i class="fa fa-comments"></i> Chat với chủ xe
</button>
```

## API Endpoints

### 1. Initialize Chat
```
GET /api/init-chat?bookingId={id}
```
**Response:**
```json
{
  "success": true,
  "conversationId": 1,
  "bookingId": 123,
  "otherUserId": 5,
  "otherUserName": "Nguyễn Văn A",
  "carName": "Honda City 2023"
}
```

### 2. Send Message
```
POST /api/user-chat?action=send
Content-Type: application/json

{
  "conversationId": 1,
  "content": "Xin chào!",
  "attachmentUrl": null,
  "attachmentType": null
}
```

### 3. Get Messages
```
GET /api/user-chat?action=messages&conversationId=1&limit=50&offset=0
```

### 4. Poll New Messages
```
GET /api/user-chat?action=poll&conversationId=1&lastMessageId=10
```

### 5. Upload File
```
POST /api/chat-upload
Content-Type: multipart/form-data

file: [binary data]
```

### 6. Typing Status
```
POST /api/user-chat?action=typing
Content-Type: application/json

{
  "conversationId": 1,
  "isTyping": true
}
```

### 7. Mark as Read
```
POST /api/user-chat?action=markRead
Content-Type: application/json

{
  "conversationId": 1
}
```

## Security

### Access Control
- ✅ Chỉ Customer và Owner của booking mới có thể chat
- ✅ Verify user authentication (must be logged in)
- ✅ Check conversation ownership trước mỗi action

### File Upload Security
- ✅ Max file size: 10MB
- ✅ Allowed image types: JPG, PNG, GIF, WebP
- ✅ Allowed document types: PDF, DOC, DOCX, XLS, XLSX, TXT
- ✅ Unique filename generation (UUID + timestamp)
- ✅ Stored in: `uploads/chat/` directory

## File Upload Directory

Tạo thư mục upload:
```bash
mkdir -p src/main/webapp/uploads/chat
```

Hoặc Tomcat sẽ tự tạo khi có file upload đầu tiên.

## UI Differences from AI Chatbot

| Feature | AI Chatbot | User Chat |
|---------|-----------|-----------|
| Position | bottom: 25px | bottom: 100px |
| Gradient | #28a745 → #4DC0B5 | #4DC0B5 → #28a745 (reversed) |
| Icon | 🤖 robot | 💬 comments |
| Border | No border | 2px white border |
| Visibility | Always visible | Only when booking exists |

## Customization

### Change Colors
Edit `user-chatbox.jsp`:
```css
background: linear-gradient(135deg, #YOUR_COLOR_1, #YOUR_COLOR_2);
```

### Change Polling Interval
```javascript
pollInterval = setInterval(() => {
    pollNewMessages();
    checkTypingStatus();
}, 3000); // Change to 3 seconds
```

### Change Max File Size
Edit `ChatFileUploadServlet.java`:
```java
private static final long MAX_FILE_SIZE = 20 * 1024 * 1024; // 20MB
```

## Testing

### 1. Create a Booking
```sql
-- Ensure you have a test booking
SELECT * FROM BOOKING WHERE BOOKING_ID = 1;
```

### 2. Test as Customer
1. Login as customer
2. Go to booking details: `/booking-details?bookingId=1`
3. Click chat button
4. Send message
5. Upload image/file

### 3. Test as Owner
1. Login as car owner
2. Go to same booking: `/booking-details?bookingId=1`
3. Chat button should appear
4. Receive customer's messages
5. Reply

### 4. Test Features
- ✅ Send text message
- ✅ Send image
- ✅ Send document
- ✅ Typing indicator
- ✅ Message read status
- ✅ Real-time updates
- ✅ Unread badge

## Troubleshooting

### Chat button không hiện
- Kiểm tra `bookingId` có đúng không
- Check console log: F12 → Console
- Verify user có quyền access booking không

### Không gửi được tin nhắn
- Check user đã login chưa
- Verify conversation exists
- Check network tab (F12) xem API response

### Upload file lỗi
- File size > 10MB?
- File type không được phép?
- Thư mục `uploads/chat` có quyền write không?

### Typing indicator không hoạt động
- Check polling interval (mặc định 2s)
- Verify TYPING_STATUS table exists
- Check database connection

## Database Queries for Debugging

### Check Conversations
```sql
SELECT * FROM USER_CONVERSATION WHERE booking_id = 1;
```

### Check Messages
```sql
SELECT m.*, u.FULL_NAME as sender_name
FROM USER_MESSAGE m
JOIN [USER] u ON m.sender_id = u.user_id
WHERE m.conversation_id = 1
ORDER BY m.created_at DESC;
```

### Check Unread Messages
```sql
SELECT COUNT(*) as unread
FROM USER_MESSAGE
WHERE conversation_id = 1
  AND sender_id != 5  -- Replace with current user ID
  AND is_read = 0;
```

### Check Typing Status
```sql
SELECT * FROM TYPING_STATUS
WHERE conversation_id = 1
  AND DATEDIFF(SECOND, last_updated, GETDATE()) < 5;
```

## Performance Tips

### Polling Optimization
- Default: Poll every 2 seconds
- Tăng interval nếu traffic cao: `setInterval(..., 5000)`
- Consider WebSocket cho real-time (advanced)

### Database Indexes
Indexes đã được tạo trong schema:
```sql
CREATE INDEX [IX_UserMsg_ConversationId] ON [USER_MESSAGE]([conversation_id]);
CREATE INDEX [IX_UserMsg_IsRead] ON [USER_MESSAGE]([is_read]);
```

### File Upload Optimization
- Compress images before upload (client-side)
- Use CDN cho file storage (production)
- Clean up old files periodically

## Future Enhancements

Potential improvements:
- [ ] WebSocket cho real-time thay vì polling
- [ ] Voice messages
- [ ] Video calls
- [ ] Message reactions (like, love, etc.)
- [ ] Delete/edit messages
- [ ] Search in conversation
- [ ] Export chat history
- [ ] Block/report user
- [ ] Admin monitoring dashboard

## Support

Nếu gặp vấn đề:
1. Check Tomcat logs
2. Check browser console (F12)
3. Verify database tables exist
4. Test API endpoints với Postman
5. Contact: tungender1508@gmail.com

---

**Created by:** hungluu2405
**Date:** November 23, 2025
**Version:** 1.0
