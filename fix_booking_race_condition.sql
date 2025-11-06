-- =============================================
-- Fix Booking Race Condition
-- =============================================
-- File này chứa các SQL statements để fix vấn đề race condition
-- khi 2 người cùng lúc book 1 xe

USE CarRentalDB;
GO

-- Bước 1: Thêm PICKUP_TIME và DROPOFF_TIME nếu chưa có
IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID('BOOKING') AND name = 'PICKUP_TIME')
BEGIN
    ALTER TABLE BOOKING ADD PICKUP_TIME TIME NOT NULL DEFAULT '00:00:00';
    PRINT '✅ Added PICKUP_TIME column';
END

IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID('BOOKING') AND name = 'DROPOFF_TIME')
BEGIN
    ALTER TABLE BOOKING ADD DROPOFF_TIME TIME NOT NULL DEFAULT '00:00:00';
    PRINT '✅ Added DROPOFF_TIME column';
END
GO

-- Bước 2: Tạo function để check overlap
IF OBJECT_ID('dbo.fn_CheckBookingOverlap', 'FN') IS NOT NULL
    DROP FUNCTION dbo.fn_CheckBookingOverlap;
GO

CREATE FUNCTION dbo.fn_CheckBookingOverlap
(
    @CarId INT,
    @StartDate DATE,
    @StartTime TIME,
    @EndDate DATE,
    @EndTime TIME
)
RETURNS INT
AS
BEGIN
    DECLARE @OverlapCount INT;

    -- Đếm số booking trùng lặp
    SELECT @OverlapCount = COUNT(*)
    FROM BOOKING
    WHERE CAR_ID = @CarId
      AND STATUS IN ('Pending', 'Approved', 'Paid')
      AND (
          -- Kiểm tra overlap: new booking conflict với existing booking
          CAST(CONCAT(@StartDate, ' ', @StartTime) AS DATETIME) < CAST(CONCAT(END_DATE, ' ', DROPOFF_TIME) AS DATETIME)
          AND CAST(CONCAT(@EndDate, ' ', @EndTime) AS DATETIME) > CAST(CONCAT(START_DATE, ' ', PICKUP_TIME) AS DATETIME)
      );

    RETURN @OverlapCount;
END;
GO

PRINT '✅ Created function fn_CheckBookingOverlap';
GO

-- Bước 3: Tạo trigger để prevent duplicate bookings
IF OBJECT_ID('dbo.trg_PreventOverlappingBookings', 'TR') IS NOT NULL
    DROP TRIGGER dbo.trg_PreventOverlappingBookings;
GO

CREATE TRIGGER trg_PreventOverlappingBookings
ON BOOKING
INSTEAD OF INSERT
AS
BEGIN
    SET NOCOUNT ON;

    -- Kiểm tra xem có booking nào overlap không
    DECLARE @CarId INT, @StartDate DATE, @StartTime TIME, @EndDate DATE, @EndTime TIME;
    DECLARE @OverlapCount INT;

    SELECT
        @CarId = CAR_ID,
        @StartDate = START_DATE,
        @StartTime = PICKUP_TIME,
        @EndDate = END_DATE,
        @EndTime = DROPOFF_TIME
    FROM inserted;

    -- Sử dụng function để check
    SET @OverlapCount = dbo.fn_CheckBookingOverlap(@CarId, @StartDate, @StartTime, @EndDate, @EndTime);

    IF @OverlapCount > 0
    BEGIN
        -- Có conflict, không cho insert
        RAISERROR('❌ This car is already booked for the selected period!', 16, 1);
        ROLLBACK TRANSACTION;
        RETURN;
    END

    -- Không có conflict, cho phép insert
    INSERT INTO BOOKING (CAR_ID, USER_ID, START_DATE, END_DATE, PICKUP_TIME, DROPOFF_TIME, TOTAL_PRICE, STATUS, CREATED_AT, LOCATION)
    SELECT CAR_ID, USER_ID, START_DATE, END_DATE, PICKUP_TIME, DROPOFF_TIME, TOTAL_PRICE, STATUS, CREATED_AT, LOCATION
    FROM inserted;
END;
GO

PRINT '✅ Created trigger trg_PreventOverlappingBookings';
PRINT '====================================';
PRINT '✅ Migration completed successfully!';
PRINT '====================================';
GO
