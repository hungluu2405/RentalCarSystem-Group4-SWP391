# Debug Guide: Chat Issues

## Vấn đề 1: User Chat Widget Không Hiện

### Kiểm tra bước 1: Xem console log

1. Bấm **F12** để mở Developer Tools
2. Chuyển sang tab **Console**
3. Reload trang
4. Tìm các thông báo lỗi màu đỏ

### Kiểm tra bước 2: Test thủ công

Mở console (F12), gõ lệnh sau:

```javascript
// Kiểm tra có bookingId không
const urlParams = new URLSearchParams(window.location.search);
console.log('BookingId từ URL:', urlParams.get('bookingId'));

// Init thủ công với bookingId test (thay 1 bằng bookingId thực)
window.initUserChat(1);
```

**Kết quả mong đợi:** Button chat sẽ hiện ở góc phải bên dưới (bottom: 100px)

### Kiểm tra bước 3: Check API response

1. Bấm F12 → Tab **Network**
2. Gõ lệnh: `window.initUserChat(1)` trong console
3. Tìm request tên **init-chat?bookingId=1**
4. Click vào → Tab **Response**

**Nếu thành công:**
```json
{
  "success": true,
  "conversationId": 1,
  "bookingId": 1,
  "otherUserId": 5,
  "otherUserName": "Nguyễn Văn A",
  "carName": "Honda City"
}
```

**Nếu lỗi 401 (Unauthorized):**
- User chưa login
- Giải pháp: Login trước

**Nếu lỗi 403 (Forbidden):**
- User không phải customer hoặc owner của booking
- Giải pháp: Login với account đúng

**Nếu lỗi 404 (Not Found):**
- Booking không tồn tại
- Giải pháp: Kiểm tra lại bookingId

**Nếu lỗi 500:**
- Database error hoặc servlet lỗi
- Xem Tomcat logs

### Kiểm tra bước 4: Verify database

```sql
-- Kiểm tra booking có tồn tại không
SELECT
    b.BOOKING_ID,
    b.CUSTOMER_ID,
    c.OWNER_ID,
    cu.FULL_NAME as customer_name,
    o.FULL_NAME as owner_name,
    car.TITLE as car_title
FROM BOOKING b
JOIN CAR c ON b.CAR_ID = c.CAR_ID
JOIN [USER] cu ON b.CUSTOMER_ID = cu.user_id
JOIN [USER] o ON c.OWNER_ID = o.user_id
WHERE b.BOOKING_ID = 1;  -- Thay 1 bằng bookingId test
```

**Nếu không trả về kết quả:**
- Booking không tồn tại → Tạo booking mới
- Foreign key không đúng → Fix data

### Giải pháp nhanh:

**Option 1: Thêm vào JSP page có bookingId**
```jsp
<script>
    // Ví dụ: trong booking-details.jsp
    window.initUserChat(${booking.bookingId});
</script>
```

**Option 2: Test với URL parameter**
```
http://localhost:8080/any-page?bookingId=1
```

---

## Vấn đề 2: AI Chatbot Không Load Chat History

### Kiểm tra bước 1: Console errors

1. Mở trang có chatbot
2. F12 → Console tab
3. Tìm error: `Error loading chat history`

### Kiểm tra bước 2: Network tab

1. F12 → Network tab
2. Reload trang
3. Tìm request: **chatbot?action=history**
4. Click → Tab Response

**Nếu thành công:**
```json
{
  "success": true,
  "messages": [
    {"role": "user", "content": "Hello"},
    {"role": "assistant", "content": "Hi there!"}
  ],
  "conversationId": 1
}
```

**Nếu lỗi:**
```json
{
  "success": false,
  "error": "Error message here"
}
```

### Kiểm tra bước 3: Database

```sql
-- Kiểm tra conversation
SELECT * FROM CHAT_CONVERSATION
WHERE session_id = 'YOUR_SESSION_ID'
AND is_active = 1;

-- Kiểm tra messages
SELECT * FROM CHAT_MESSAGE
WHERE conversation_id = 1
ORDER BY created_at DESC;
```

### Kiểm tra bước 4: Tomcat logs

Xem logs để tìm errors:

```bash
# Linux/Mac
tail -f $CATALINA_HOME/logs/catalina.out

# Windows - Xem trong Tomcat console
```

Tìm các dòng:
- `Error in chatbot servlet`
- `Error getting chat history`
- SQL errors
- `NullPointerException`

### Giải pháp:

**Nếu conversation null:**
- Chưa có conversation → Gửi tin nhắn đầu tiên để tạo conversation

**Nếu database connection error:**
- Check context.xml có cấu hình đúng không
- Verify SQL Server đang chạy
- Test connection string

**Nếu API key chưa cấu hình:**
```sql
-- Insert API key vào database
INSERT INTO CHATBOT_CONFIG (config_key, config_value, description, is_active)
VALUES ('GEMINI_API_KEY', 'YOUR_API_KEY_HERE', 'Google Gemini API Key', 1);
```

---

## Quick Debug Script

Copy-paste vào browser console để test toàn bộ:

```javascript
(async function debugChat() {
    console.log('=== CHAT DEBUG TOOL ===');

    // 1. Check AI Chatbot
    console.log('\n1. Testing AI Chatbot History API...');
    try {
        const res1 = await fetch('/api/chatbot?action=history');
        const data1 = await res1.json();
        console.log('✅ AI Chatbot API:', data1);
    } catch (e) {
        console.error('❌ AI Chatbot API Error:', e);
    }

    // 2. Check User Chat Init (với bookingId=1)
    console.log('\n2. Testing User Chat Init API...');
    try {
        const res2 = await fetch('/api/init-chat?bookingId=1');
        const data2 = await res2.json();
        console.log('✅ User Chat Init API:', data2);

        if (data2.success) {
            console.log('🎉 User chat can be initialized!');
            console.log('Call: window.initUserChat(1) to show button');
        }
    } catch (e) {
        console.error('❌ User Chat Init Error:', e);
    }

    // 3. Check current bookingId in URL
    console.log('\n3. Checking URL parameters...');
    const urlParams = new URLSearchParams(window.location.search);
    const bookingId = urlParams.get('bookingId');
    console.log('BookingId in URL:', bookingId || 'NOT FOUND');

    // 4. Check if widgets are loaded
    console.log('\n4. Checking widget elements...');
    console.log('AI Chatbot button:', document.getElementById('chatbot-button') ? '✅ Found' : '❌ Not found');
    console.log('User Chat button:', document.getElementById('user-chat-button') ? '✅ Found' : '❌ Not found');

    const userChatBtn = document.getElementById('user-chat-button');
    if (userChatBtn) {
        console.log('User Chat button visibility:',
            window.getComputedStyle(userChatBtn).display !== 'none' ? '✅ Visible' : '❌ Hidden (display: none)');
    }

    console.log('\n=== END DEBUG ===');
})();
```

---

## Checklist Tổng Hợp

### Setup ban đầu:

- [ ] Database schema đã chạy (DB60.sql và user-chat-schema.sql)
- [ ] Gemini API key đã insert vào CHATBOT_CONFIG
- [ ] Cả 2 widgets đã include trong _footer_scripts.jsp
- [ ] Tomcat đã restart sau khi deploy

### Test AI Chatbot:

- [ ] Button chatbot hiện ở góc phải (bottom: 25px)
- [ ] Click button → chatbox mở ra
- [ ] Gửi tin nhắn test: "Hello"
- [ ] Nhận được response từ Gemini
- [ ] Reload trang → Chat history được load lại

### Test User Chat:

- [ ] Đã login với user có booking
- [ ] Gọi `window.initUserChat(bookingId)` hoặc có ?bookingId trong URL
- [ ] Button user chat hiện ở góc phải (bottom: 100px)
- [ ] Click button → chatbox mở ra
- [ ] Gửi tin nhắn test
- [ ] Login user khác (owner/customer) → nhận được tin nhắn
- [ ] Upload hình ảnh → hiển thị preview
- [ ] Upload file → hiển thị link download

---

## Cần Hỗ Trợ?

Nếu vẫn gặp vấn đề sau khi làm theo guide này:

1. **Copy toàn bộ console errors** (F12 → Console)
2. **Copy API response** (F12 → Network → Click request → Response tab)
3. **Copy Tomcat logs** (phần có error)
4. **Screenshot** màn hình hiện tại

Gửi cho developer để debug chi tiết hơn.
