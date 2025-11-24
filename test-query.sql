-- Test query để check cấu trúc bảng
-- Chạy từng query này để xem tên cột đúng là gì

-- Query 1: Xem cấu trúc bảng USER
SELECT TOP 1 * FROM [USER];

-- Query 2: Xem cấu trúc bảng CAR
SELECT TOP 1 * FROM CAR;

-- Query 3: Xem cấu trúc bảng BOOKING
SELECT TOP 1 * FROM BOOKING;

-- Query 4: Test query đơn giản (FIX LỖI ALIAS)
SELECT
    b.BOOKING_ID,
    b.USER_ID as customer_id,
    car.USER_ID as owner_id  -- SỬA: c.USER_ID → car.USER_ID
FROM BOOKING b
JOIN CAR car ON b.CAR_ID = car.CAR_ID
WHERE b.BOOKING_ID = 41;
