# Hướng Dẫn Test User Chat

## ✅ Đã Include Widget vào Footer

User chat widget đã được thêm vào `_footer_scripts.jsp`. Giờ bạn sẽ thấy:

**2 Chatbox:**
1. **AI Chatbot** (bottom: 25px) - Icon robot - Luôn hiện
2. **User Chat** (bottom: 100px) - Icon chat - Chỉ hiện khi có booking

## 🧪 Cách Test Ngay

### Option 1: Thêm vào Booking Detail Page (Recommended)

Tìm file JSP hiển thị chi tiết booking (ví dụ: `booking-details.jsp` hoặc `booking-info.jsp`), thêm đoạn này vào cuối trang:

```jsp
<script>
    // Thay 123 bằng bookingId thực tế từ database
    window.initUserChat(${booking.bookingId});

    // Hoặc nếu bookingId là parameter:
    // window.initUserChat(${param.bookingId});
</script>
```

### Option 2: Test với URL Parameter

Mở bất kỳ trang nào với parameter `bookingId`:
```
http://localhost:8080/home?bookingId=1
```

User chat button sẽ tự động hiện ra!

### Option 3: Test Từ Console (Quick Test)

1. Mở bất kỳ trang nào
2. Bấm F12 → Console
3. Gõ lệnh:
```javascript
window.initUserChat(1);  // Thay 1 bằng bookingId thực tế
```

Button chat sẽ hiện ngay lập tức!

## 📝 Tạo Booking Test

Nếu chưa có booking trong DB, tạo một booking test:

```sql
-- 1. Kiểm tra có booking nào chưa
SELECT TOP 5 * FROM BOOKING;

-- 2. Nếu chưa có, tạo booking mẫu
INSERT INTO BOOKING (CUSTOMER_ID, CAR_ID, START_DATE, END_DATE, TOTAL_PRICE, STATUS)
VALUES (
    1,  -- Customer ID (phải tồn tại trong bảng USER)
    1,  -- Car ID (phải tồn tại trong bảng CAR)
    GETDATE(),
    DATEADD(DAY, 3, GETDATE()),
    1500000,
    'Confirmed'
);

-- 3. Lấy booking_id vừa tạo
SELECT TOP 1 BOOKING_ID FROM BOOKING ORDER BY BOOKING_ID DESC;
```

## 🎯 Test Scenarios

### Test 1: Chat từ Customer
1. Login với user có role = Customer
2. Vào trang có `bookingId` (hoặc dùng console)
3. Click button chat (icon 💬)
4. Gửi tin nhắn: "Xin chào, tôi muốn hỏi về xe"
5. Check database:
```sql
SELECT * FROM USER_CONVERSATION WHERE booking_id = 1;
SELECT * FROM USER_MESSAGE ORDER BY created_at DESC;
```

### Test 2: Chat từ Owner
1. Login với user là owner của xe trong booking
2. Vào cùng `bookingId`
3. Click button chat
4. Sẽ thấy tin nhắn từ customer
5. Reply lại

### Test 3: Upload Hình
1. Mở chat
2. Click icon 📎 (paperclip)
3. Chọn hình ảnh (JPG, PNG)
4. Gửi
5. Kiểm tra thư mục: `webapp/uploads/chat/`

### Test 4: Typing Indicator
1. Mở 2 browser khác nhau (hoặc incognito)
2. Login customer ở browser 1
3. Login owner ở browser 2
4. Cả 2 mở cùng booking
5. Gõ tin nhắn ở browser 1
6. Browser 2 sẽ thấy "đang gõ..."

### Test 5: Unread Badge
1. Login customer → gửi tin nhắn → logout
2. Login owner → mở trang (chưa mở chat)
3. Sẽ thấy badge đỏ với số tin nhắn chưa đọc
4. Click chat → badge biến mất

## 🔍 Troubleshooting

### "Button chat không hiện"

**Check 1: Có bookingId chưa?**
```javascript
// Mở console (F12), gõ:
const urlParams = new URLSearchParams(window.location.search);
console.log('BookingId:', urlParams.get('bookingId'));
```

**Check 2: Init thủ công**
```javascript
window.initUserChat(1);  // Thay 1 bằng bookingId
```

**Check 3: Check console errors**
```javascript
// F12 → Console tab, xem có error không
```

### "Gửi tin nhắn lỗi"

**Check database connection:**
```sql
-- Verify conversation exists
SELECT * FROM USER_CONVERSATION WHERE booking_id = 1;

-- Verify user có quyền không
SELECT * FROM BOOKING WHERE BOOKING_ID = 1;
```

**Check Tomcat logs:**
```
tail -f logs/catalina.out
# Hoặc xem trong Tomcat console
```

### "Upload file lỗi"

**Check upload directory:**
```bash
ls -la src/main/webapp/uploads/chat/
# Hoặc trong deployed folder:
ls -la [TOMCAT]/webapps/CarRentalSystem/uploads/chat/
```

**Create directory nếu chưa có:**
```bash
mkdir -p src/main/webapp/uploads/chat
# Hoặc:
mkdir -p [TOMCAT]/webapps/CarRentalSystem/uploads/chat
```

## 🎨 Visual Check

Sau khi init thành công, bạn sẽ thấy:

```
┌─────────────────┐
│  AI Chatbot ☰   │ ← Bottom: 25px (green gradient)
└─────────────────┘

┌─────────────────┐
│  User Chat 💬   │ ← Bottom: 100px (teal gradient, white border)
│       [3]       │ ← Red badge nếu có unread
└─────────────────┘
```

## 📊 Database Queries để Debug

### Check conversations
```sql
SELECT
    c.*,
    b.BOOKING_ID,
    cu.FULL_NAME as customer_name,
    o.FULL_NAME as owner_name,
    car.TITLE as car_name
FROM USER_CONVERSATION c
JOIN BOOKING b ON c.booking_id = b.BOOKING_ID
JOIN [USER] cu ON c.customer_id = cu.user_id
JOIN [USER] o ON c.owner_id = o.user_id
JOIN CAR car ON b.CAR_ID = car.CAR_ID;
```

### Check messages
```sql
SELECT
    m.*,
    u.FULL_NAME as sender_name,
    CASE WHEN m.sender_id = c.customer_id THEN 'Customer' ELSE 'Owner' END as role
FROM USER_MESSAGE m
JOIN USER_CONVERSATION c ON m.conversation_id = c.conversation_id
JOIN [USER] u ON m.sender_id = u.user_id
ORDER BY m.created_at DESC;
```

### Check unread count
```sql
SELECT
    conversation_id,
    COUNT(*) as unread_count
FROM USER_MESSAGE
WHERE is_read = 0
GROUP BY conversation_id;
```

## ✨ Quick Start Script

Copy-paste vào browser console để test nhanh:

```javascript
// Auto-init với bookingId = 1
setTimeout(() => {
    window.initUserChat(1);
    console.log('✅ Chat initialized with bookingId=1');

    // Auto-open chatbox sau 1 giây
    setTimeout(() => {
        document.getElementById('user-chat-button').click();
        console.log('✅ Chatbox opened');
    }, 1000);
}, 500);
```

## 🚀 Production Checklist

Trước khi deploy production:

- [ ] Chạy `user-chat-schema.sql` trên production DB
- [ ] Tạo thư mục `uploads/chat` với quyền write
- [ ] Verify permissions cho file upload
- [ ] Test với real booking data
- [ ] Test trên mobile devices
- [ ] Check performance với nhiều users
- [ ] Backup database trước khi deploy

## 📞 Need Help?

Nếu vẫn gặp vấn đề:

1. Check Tomcat logs (catalina.out)
2. Check browser console (F12)
3. Verify database schema
4. Test API endpoints với Postman:
   - GET `/api/init-chat?bookingId=1`
   - POST `/api/user-chat?action=send`

---

**Tóm tắt:** User chat đã sẵn sàng! Chỉ cần có `bookingId` và gọi `window.initUserChat(bookingId)` là button sẽ hiện ngay! 🎉
