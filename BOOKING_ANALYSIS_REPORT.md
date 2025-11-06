# RENTAL CAR SYSTEM - BOOKING FLOW ANALYSIS REPORT

## 1. COMPLETE BOOKING FLOW DIAGRAM

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                          COMPLETE BOOKING FLOW                              │
└─────────────────────────────────────────────────────────────────────────────┘

┌─────────────────┐
│  User Browsing  │
│   Car Listing   │
└────────┬────────┘
         │
         ▼
   ┌─────────────────┐
   │ User selects    │
   │ car & dates     │
   │ (car-single.jsp)│
   └────────┬────────┘
            │
            ▼
   ┌──────────────────────────────────┐
   │ BookingController (POST /booking)│  ◄── USER INPUTS BOOKING DETAILS
   │ - Validate dates/times           │
   │ - Check login status             │
   │ - Parse parameters               │
   └────────┬─────────────────────────┘
            │
            ▼
   ┌──────────────────────────────────┐
   │  BookingService.createBooking()  │  ◄── CORE BUSINESS LOGIC
   │  - Check driver license exists   │
   │  - Validate pickup date > today  │
   │  - Validate minimum 24hrs rental │
   │  - Check car exists              │
   │  - Prevent self-booking          │
   │  - Check availability            │
   │  - Calculate price               │
   │  - Apply promo discount          │
   │  - Set status = "Pending"        │
   │  - Insert booking in DB          │
   │  - Store booking in session      │
   └────────┬─────────────────────────┘
            │
            ├─── SUCCESS ───┐
            │               │
            ▼               ▼
   ┌────────────────────────────────────┐
   │ Redirect to /booking-confirmation  │  (BookingConfirmationServlet)
   │ Display booking details            │
   │ Show price & discount              │
   └────────┬──────────────────────────┘
            │
            ▼
   ┌────────────────────────────────┐
   │ User Clicks "Proceed to Payment"│
   └────────┬─────────────────────────┘
            │
            ▼
   ┌──────────────────────────────────────┐
   │ CreatePaymentServlet                 │
   │ /customer/create-payment?bookingId=X │
   │ - Create PayPal order                │
   │ - Redirect to PayPal approval URL    │
   └────────┬──────────────────────────────┘
            │
            ▼
   ┌────────────────────────────────┐
   │   User Approves at PayPal      │
   └────────┬─────────────────────────┘
            │
            ▼
   ┌───────────────────────────────────────┐
   │ PaymentExecuteServlet                 │
   │ /customer/execute-payment             │
   │ - PayPalService.executeAndRecord()    │
   │ - BookingService.markAsPaid()         │
   │ - Set status = "Paid"                 │
   │ - Insert payment record               │
   │ - Send notification                   │
   └────────┬──────────────────────────────┘
            │
            ▼
   ┌────────────────────────────────┐
   │ Redirect to customerOrder page │
   │ Status = "Paid"                │
   └────────┬─────────────────────────┘
            │
            ├─────────────────────────────────────┐
            │ OWNER APPROVAL FLOW BEGINS          │
            │ (Owner reviews booking)              │
            │                                     │
            ├──────────────┬──────────────┐       │
            │              │              │       │
            ▼              ▼              ▼       │
   ┌──────────────┐ ┌──────────────┐ ┌────────┐  │
   │   APPROVE    │ │    REJECT    │ │CANCEL  │  │
   │ (accept)     │ (reject)       │ (cancel)│  │
   └──────┬───────┘ └────────┬─────┘ └────┬───┘  │
          │                  │            │      │
          ▼                  ▼            ▼      │
   ┌────────────────────────────────────────┐   │
   │ BookingService methods:                │   │
   │ - approveBooking() -> "Approved"       │   │
   │ - rejectBooking() -> "Rejected"        │   │
   │ - cancelBooking() -> "Cancelled"       │   │
   │ - Send notifications                  │   │
   └────────┬──────────────────────────────┘   │
            │                                   │
            ▼                                   │
   ┌────────────────────────────────────────┐  │
   │   If Approved: Customer proceeds to    │  │
   │   payment (flow above)                 │  │
   │                                        │  │
   │   If Rejected/Cancelled: Ends here     │  │
   └────────┬──────────────────────────────┘  │
            │                                   │
            ▼                                   │
   ┌──────────────────────────────────────┐    │
   │ RENTAL & RETURN FLOW                 │    │
   │ - Customer picks up car              │    │
   │ - Status remains "Approved"/"Paid"   │    │
   │                                      │    │
   │ ReturnCarServlet (/customer/returnCar)   │
   │ - Mark booking as "Completed"        │    │
   └──────────┬───────────────────────────┘    │
              │                                 │
              ▼                                 │
   ┌─────────────────────────────────────┐     │
   │ Booking Complete                    │     │
   │ Final Status = "Completed"          │     │
   └─────────────────────────────────────┘     │
                                               │
└──────────────────────────────────────────────┘
```

## 2. BOOKING STATUS FLOW

```
PENDING ─────────────────────────────────► APPROVED ──────► PAID ──────► COMPLETED
                                               │                          ▲
                                               │                          │
                                               └──────► REJECTED          │
                                                                          │
                                    ◄─── CAN CANCEL FROM ANY STATUS ─────┘
                                    │
                                    ▼
                                 CANCELLED
```

---

## 3. LOGICAL ISSUES FOUND

### CRITICAL ISSUES (Must Fix Immediately)

#### Issue 1: RACE CONDITION - Double Booking Prevention Not Atomic
**File:** `/home/user/RentalCarSystem-Group4-SWP391/src/main/java/dao/implement/BookingDAO.java` (Lines 79-122)
**File:** `/home/user/RentalCarSystem-Group4-SWP391/src/main/java/service/booking/BookingService.java` (Lines 44-50)

**Problem:**
The availability check (isCarAvailable) is NOT atomic with the booking insertion. Two users can simultaneously:
1. Check availability at same time → both get "available"
2. Both insert bookings for same car/time → both succeed, creating conflict

**Code Flow:**
```java
// BookingService.createBooking() Line 44-50
boolean available = bookingDAO.isCarAvailable(...);  // Check
if (!available) return "❌ This car is already booked...";

// ... later ...
boolean bookingSuccess = bookingDAO.insert(booking);  // Insert
```

The gap between lines 44 and 105 allows another user to book the same car.

**Severity:** CRITICAL
**Impact:** Overbooking of cars, revenue loss, customer disputes
**Recommendation:** 
- Use database-level locking (SELECT FOR UPDATE)
- Wrap check + insert in single transaction
- Implement optimistic locking with version field

---

#### Issue 2: Missing End Date Validation
**File:** `/home/user/RentalCarSystem-Group4-SWP391/src/main/java/service/booking/BookingService.java` (Lines 23-40)

**Problem:**
No validation that END_DATE > START_DATE. User can book with endDate = startDate or endDate < startDate.

**Current Validations:**
- Line 28: `if (booking.getStartDate().isBefore(today))` ✓
- Line 38: `if (totalHours < 24)` ← Only checks total hours, not date order

**Issue:** If endDate < startDate:
```java
LocalDateTime pickupDateTime = LocalDateTime.of(booking.getStartDate(), pickupTime);
LocalDateTime returnDateTime = LocalDateTime.of(booking.getEndDate(), dropoffTime);
long totalHours = ChronoUnit.HOURS.between(pickupDateTime, returnDateTime);
// Returns NEGATIVE value, which is < 24, but error message is misleading
```

**Severity:** HIGH
**Impact:** Invalid bookings created, incorrect pricing, data corruption
**Recommendation:** Add explicit validation:
```java
if (booking.getEndDate().isBefore(booking.getStartDate()) || 
    booking.getEndDate().equals(booking.getStartDate())) {
    return "❌ End date must be after start date!";
}
```

---

#### Issue 3: No Authorization Checks in Cancellation Endpoints
**File:** `/home/user/RentalCarSystem-Group4-SWP391/src/main/java/controller/booking/CancelBookingServlet.java` (Lines 14-39)
**File:** `/home/user/RentalCarSystem-Group4-SWP391/src/main/java/controller/booking/OwnerCancelBookingServlet.java` (Lines 14-38)

**Problem:**
CancelBookingServlet doesn't verify booking belongs to current user before canceling.
OwnerCancelBookingServlet doesn't verify car belongs to current owner.

**Code Flow:**
```java
// CancelBookingServlet
String bookingIdStr = request.getParameter("bookingId");
// ... NO CHECK IF booking.getUserId() == current user ...
boolean success = bookingService.cancelBooking(bookingId);  // Anyone can cancel anyone's booking!
```

**Severity:** CRITICAL
**Impact:** Users can cancel other users' bookings, security breach
**Recommendation:**
- Get current user from session
- Fetch booking from DB
- Verify ownership before proceeding
- Add similar check in ReturnCarServlet

---

#### Issue 4: No Refund Logic on Cancellation
**File:** `/home/user/RentalCarSystem-Group4-SWP391/src/main/java/service/booking/BookingService.java` (Lines 155-166)
**File:** `/home/user/RentalCarSystem-Group4-SWP391/src/main/java/dao/implement/BookingDAO.java` (No refund tracking)

**Problem:**
When booking is cancelled, no refund is processed or tracked.

**Code:**
```java
public boolean cancelBooking(int bookingId) {
    boolean success = bookingDAO.updateStatus(bookingId, "Cancelled");
    if (success) {
        try {
            notificationService.notifyBookingCancelled(bookingId);
        } catch (Exception e) { ... }
    }
    return success;
}
```

Missing:
- No refund amount calculation
- No refund status (pending/processed)
- No refund tracking in Payment table
- No partial refunds based on cancellation timing

**Severity:** CRITICAL
**Impact:** Financial loss, customer disputes, accounting issues
**Recommendation:**
- Add REFUND_AMOUNT field to BOOKING table
- Add refund_status to PAYMENT table
- Calculate refund based on cancellation time
- Log refund transactions

---

#### Issue 5: Invalid Status Transitions Allowed
**File:** `/home/user/RentalCarSystem-Group4-SWP391/src/main/java/service/booking/BookingService.java` (Lines 129-192)

**Problem:**
Any booking can transition to any status without validation.

**Invalid Transitions Currently Allowed:**
- Completed → Approved (should not be possible)
- Rejected → Approved (re-approve rejected booking)
- Rejected → Paid (pay for rejected booking)
- Cancelled → Paid (pay for cancelled booking)
- etc.

**Current Code:**
```java
public boolean approveBooking(int bookingId) {
    boolean success = bookingDAO.updateStatus(bookingId, "Approved");
    // ← No check on current status
}
```

**Severity:** HIGH
**Impact:** Data inconsistency, invalid state transitions, difficult to track booking lifecycle
**Recommendation:**
- Implement state machine validation
- Whitelist allowed transitions
- Example:
```java
private static final Map<String, Set<String>> ALLOWED_TRANSITIONS = Map.of(
    "Pending", Set.of("Approved", "Rejected", "Cancelled"),
    "Approved", Set.of("Paid", "Rejected", "Cancelled"),
    "Paid", Set.of("Completed", "Cancelled"),
    // etc.
);
```

---

#### Issue 6: Car Deleted While Booking Exists
**File:** `/home/user/RentalCarSystem-Group4-SWP391/src/main/java/service/booking/BookingService.java` (Lines 40-41)
**File:** No CASCADE DELETE or foreign key constraint checking visible

**Problem:**
If a car is deleted from database while a booking exists:
- Booking queries will return NULL for car info
- Availability checks will fail
- Owner browsing bookings will get NULL car details
- Payment processing might fail

**Current Code:**
```java
Car car = carDAO.findById(booking.getCarId());
if (car == null) return "❌ The selected car does not exist!";
```

This only prevents NEW bookings, but doesn't protect EXISTING ones.

**Severity:** HIGH
**Impact:** System crashes, data inconsistency, poor user experience
**Recommendation:**
- Add foreign key constraint with CASCADE DELETE or SET NULL
- Prevent car deletion if active bookings exist
- Implement soft delete (mark as deleted, don't remove)

---

#### Issue 7: No Driver License Expiry Validation
**File:** `/home/user/RentalCarSystem-Group4-SWP391/src/main/java/service/booking/BookingService.java` (Lines 24-27)

**Problem:**
System only checks if driver license exists, not if it's expired.

**Code:**
```java
Driver_License license = licenseDAO.getLicenseByUserId(booking.getUserId());
if (license == null) return "❌ You must upload your driver license before booking!";
// ← NO CHECK: if (license.getExpiry_date().isBefore(today))
```

**Severity:** HIGH
**Impact:** Users can book with expired licenses, legal liability
**Recommendation:**
```java
if (license.getExpiry_date().isBefore(today)) {
    return "❌ Your driver license has expired!";
}
```

---

### HIGH SEVERITY ISSUES

#### Issue 8: No Age Verification at Booking Time
**File:** `/home/user/RentalCarSystem-Group4-SWP391/src/main/java/service/booking/BookingService.java`
**File:** `/home/user/RentalCarSystem-Group4-SWP391/src/main/java/model/Driver_License.java` (has dob field)

**Problem:**
Driver's age is not verified against booking at creation time. While age validation exists in CustomerProfileController, it's not enforced at booking time.

**Model has DOB:**
```java
// Driver_License.java
private Date dob;
```

**But BookingService never checks it:**
```java
Driver_License license = licenseDAO.getLicenseByUserId(booking.getUserId());
if (license == null) return "...";
// ← NO AGE CHECK
```

**Severity:** HIGH
**Impact:** Underage drivers can book cars, legal/insurance issues
**Recommendation:**
```java
LocalDate birthDate = license.getDob().toLocalDate();
int age = LocalDate.now().getYear() - birthDate.getYear();
if (age < 18) {
    return "❌ Must be at least 18 years old to rent!";
}
```

---

#### Issue 9: Payment Only Possible After Owner Approval
**File:** `/home/user/RentalCarSystem-Group4-SWP391/src/main/java/service/booking/PayPalService.java` (Lines 28-29)

**Problem:**
Payment requires booking status = "Approved". This creates problematic flow:

1. Customer creates booking (status = "Pending")
2. Customer wants to pay, but can't (needs "Approved")
3. Owner must approve first
4. Then customer can pay
5. But what if owner rejects after customer already tried to pay?

**Current Code:**
```java
if (!"Approved".equalsIgnoreCase(booking.getStatus())) {
    throw new Exception("Booking ID " + bookingId + " is not approved yet.");
}
```

**Severity:** HIGH
**Impact:** Unclear booking flow, potential payment issues
**Recommendation:**
- Allow payment from "Pending" status
- Or allow immediate approval for instant payment bookings
- Better: Change flow to Pending → Paid → Approved (owner reviews later)

---

#### Issue 10: No Return Car Authorization Check
**File:** `/home/user/RentalCarSystem-Group4-SWP391/src/main/java/controller/booking/ReturnCarServlet.java` (Lines 22-54)

**Problem:**
ReturnCarServlet doesn't verify:
- User returning car is the one who booked it
- Booking is in valid state (Paid/Approved) not already Completed
- Booking date is actually due today

**Code:**
```java
int bookingId = Integer.parseInt(bookingIdParam);
boolean updated = bookingService.completeBooking(bookingId);  // Anyone can mark any booking as completed!
```

**Severity:** HIGH
**Impact:** Wrong users can mark bookings as returned, incorrect car return tracking
**Recommendation:**
```java
// Get booking, verify owner, check status, verify date
Booking booking = bookingDAO.getBookingById(bookingId);
if (booking == null) return error;
if (booking.getUserId() != currentUserId) return error;
if (!booking.getStatus().equals("Approved") && !booking.getStatus().equals("Paid")) {
    return error; // Already completed or cancelled
}
```

---

#### Issue 11: No Timezone Handling
**File:** Multiple files using LocalDate/LocalDateTime without timezone awareness

**Problem:**
System uses LocalDate and LocalTime but doesn't handle timezones. A booking for "2025-12-20" means different things in different timezones.

**Affected Areas:**
- Availability calculation (BookingDAO.isCarAvailable)
- Date comparisons (BookingService)
- Pickup/dropoff time calculations

**Example Issue:**
```java
// In UTC: 2025-12-20 22:00:00
// In EST: 2025-12-20 17:00:00
// In JST: 2025-12-21 07:00:00
// Same instant, different "dates"
```

**Severity:** MEDIUM
**Impact:** Incorrect availability for international operations
**Recommendation:**
- Store all times in UTC in database
- Convert to user's timezone for display
- Use ZonedDateTime instead of LocalDateTime

---

### MEDIUM SEVERITY ISSUES

#### Issue 12: No Concurrent Modification Lock
**File:** `/home/user/RentalCarSystem-Group4-SWP391/src/main/java/service/booking/BookingService.java`

**Problem:**
Multiple updates to same booking are not locked.

**Scenarios:**
1. Owner approves while customer is paying
2. Customer cancels while owner is approving
3. Multiple cancellation requests simultaneously

**Severity:** MEDIUM
**Impact:** Inconsistent state, lost updates
**Recommendation:**
- Add optimistic locking (version field)
- Use database row-level locking

---

#### Issue 13: Booking Location vs Car Location Confusion
**File:** `/home/user/RentalCarSystem-Group4-SWP391/src/main/java/model/Booking.java` (Line 16)
**File:** `/home/user/RentalCarSystem-Group4-SWP391/src/main/java/dao/implement/BookingDAO.java` (Lines 289, 311)

**Problem:**
System has both:
- Booking.location (pickup location specified by customer)
- Car.location (fixed car location)

But uses Car.LOCATION for booking details instead of Booking.LOCATION in some queries.

**Code:**
```java
// BookingDAO line 289 - getBookingDetailsByUserId
c.LOCATION  // ← Uses car location, not booking location!
```

**Severity:** MEDIUM
**Impact:** Wrong pickup location displayed to users
**Recommendation:**
- Use BOOKING.LOCATION consistently
- Or clarify if booking location overrides car location

---

#### Issue 14: No Booking Confirmation Timeout
**File:** `/home/user/RentalCarSystem-Group4-SWP391/src/main/java/controller/booking/BookingConfirmationServlet.java` (Lines 58-61)

**Problem:**
Booking confirmation session data cleared immediately after display, but no expiration check.

**Severity:** MEDIUM
**Impact:** Hard to retry if user navigates away
**Recommendation:**
- Add session timeout
- Store booking ID in URL as backup
- Allow resending confirmation email

---

#### Issue 15: Promo Code Validation Issues
**File:** `/home/user/RentalCarSystem-Group4-SWP391/src/main/java/service/booking/BookingService.java` (Lines 78-98)

**Problem:**
- No max uses per customer validation
- No minimum booking amount validation
- No specific car/category restrictions enforced
- No usage frequency limits (once per X days)

**Severity:** MEDIUM
**Impact:** Abuse of promo codes, unintended discounts
**Recommendation:**
- Add use_count and max_uses fields
- Add minimum_price_threshold
- Add usage_limit_per_customer
- Check usage history before applying

---

### LOW SEVERITY ISSUES

#### Issue 16: Booking ID Consistency
**File:** `/home/user/RentalCarSystem-Group4-SWP391/src/main/java/controller/booking/BookingController.java` (Lines 129-135)

**Problem:**
Code checks if bookingId == 0 after insertion, but Java int defaults to 0, creating ambiguity.

**Code:**
```java
if (booking.getBookingId() == 0) {
    System.err.println("❌ Booking ID is 0!");
    // ... error handling ...
    return;
}
```

Better to check after insertion for null/failure.

**Severity:** LOW
**Impact:** Rare edge case, minimal impact
**Recommendation:**
- Use Integer wrapper instead of int
- Check result of insert() method instead

---

#### Issue 17: No Audit Trail for Booking Changes
**File:** All service methods

**Problem:**
Status changes (approve, reject, cancel, complete) don't log who made the change or when.

**Severity:** LOW
**Impact:** Cannot track booking change history, difficult for support
**Recommendation:**
- Add BOOKING_HISTORY or AUDIT_LOG table
- Log timestamp, user, old_status, new_status

---

#### Issue 18: Error Messages Exposed to User
**File:** Multiple files, e.g., `/home/user/RentalCarSystem-Group4-SWP391/src/main/java/service/booking/BookingService.java`

**Problem:**
Detailed error messages returned directly to users (e.g., "❌ You cannot book your own car!").

While helpful for debugging, could expose business logic.

**Severity:** LOW
**Impact:** Potential information disclosure
**Recommendation:**
- Log detailed errors server-side
- Return generic messages to frontend
- Use error codes for specific handling

---

#### Issue 19: No Maximum Booking Duration Limit
**File:** `/home/user/RentalCarSystem-Group4-SWP391/src/main/java/service/booking/BookingService.java`

**Problem:**
No limit on how long a customer can book a car (could be 1 year+).

**Severity:** LOW
**Impact:** Business policy enforcement, planning
**Recommendation:**
- Add max_booking_days configuration
- Validate: `if (totalDays > MAX_BOOKING_DAYS) return error;`

---

#### Issue 20: Missing Null Checks in DAO Methods
**File:** `/home/user/RentalCarSystem-Group4-SWP391/src/main/java/dao/implement/BookingDAO.java`

**Problem:**
Some methods don't null-check return values before using.

**Example:** Line 148-156 in getRecentBookingDetails

**Severity:** LOW
**Impact:** Potential NullPointerException in edge cases
**Recommendation:**
- Add null checks in loops
- Use Optional for single value returns

---

## 4. AVAILABILITY LOGIC ANALYSIS

### How Availability Check Works (Lines 79-122 in BookingDAO)

```java
public boolean isCarAvailable(int carId, LocalDate startDate, LocalTime startTime,
                              LocalDate endDate, LocalTime endTime) {
    
    // Convert to LocalDateTime
    LocalDateTime requestStart = LocalDateTime.of(startDate, startTime);
    LocalDateTime requestEnd = LocalDateTime.of(endDate, endTime);

    String sql = """
        SELECT COUNT(*) FROM BOOKING
        WHERE CAR_ID = ? 
          AND STATUS IN ('Pending', 'Approved', 'Paid')
          AND (
              -- Overlap check
              CONCAT(START_DATE, ' ', PICKUP_TIME) < ?
              AND CONCAT(END_DATE, ' ', DROPOFF_TIME) > ?
          )
    """;
}
```

### Overlap Logic Visualization

```
EXISTING BOOKING A:     |----A----|
REQUEST (New Booking):           |----REQ----|
CONFLICT:                         YES (overlaps)

EXISTING BOOKING B:     |----B----|
REQUEST (After B):                      |----REQ----|
NO CONFLICT:                            YES (available)

EXISTING BOOKING C:     |----C----|
REQUEST (Before C):     |----REQ----|
NO CONFLICT:            YES (available)
```

### Statuses Checked for Conflict

Current: `STATUS IN ('Pending', 'Approved', 'Paid')`

- ✓ Pending: Blocks availability (user might still confirm)
- ✓ Approved: Blocks availability (likely to proceed)
- ✓ Paid: Blocks availability (confirmed)
- ✗ Rejected: Doesn't block (good)
- ✗ Cancelled: Doesn't block (good)
- ✗ Completed: Doesn't block (good)

**Issue:** Should "Pending" bookings block availability? (Unconfirmed bookings from deadbeats)

---

## 5. SUMMARY TABLE OF ALL ISSUES

| # | Issue | File | Lines | Severity | Type | Fix Difficulty |
|---|-------|------|-------|----------|------|-----------------|
| 1 | Race condition in booking | BookingDAO, BookingService | 44-50, 79-122 | CRITICAL | Concurrency | Hard |
| 2 | Missing end date validation | BookingService | 23-40 | CRITICAL | Validation | Easy |
| 3 | No authorization in cancel | CancelBookingServlet | 14-39 | CRITICAL | Security | Easy |
| 4 | No refund on cancellation | BookingService, BookingDAO | 155-166 | CRITICAL | Business Logic | Medium |
| 5 | No status transition validation | BookingService | 129-192 | HIGH | Business Logic | Medium |
| 6 | Car deletion with active booking | BookingService | 40-41 | HIGH | Data Integrity | Medium |
| 7 | No license expiry check | BookingService | 24-27 | HIGH | Validation | Easy |
| 8 | No age verification | BookingService | N/A | HIGH | Validation | Easy |
| 9 | Payment requires pre-approval | PayPalService | 28-29 | HIGH | Business Logic | Medium |
| 10 | No return car authorization | ReturnCarServlet | 22-54 | HIGH | Security | Easy |
| 11 | No timezone handling | Multiple | Multiple | MEDIUM | Data Integrity | Hard |
| 12 | No concurrent mod lock | BookingService | Multiple | MEDIUM | Concurrency | Medium |
| 13 | Location field confusion | BookingDAO | 289, 311 | MEDIUM | Data Integrity | Easy |
| 14 | No confirmation timeout | BookingConfirmationServlet | 58-61 | MEDIUM | UX | Easy |
| 15 | Promo code abuse possible | BookingService | 78-98 | MEDIUM | Business Logic | Medium |
| 16 | Booking ID consistency | BookingController | 129-135 | LOW | Code Quality | Easy |
| 17 | No audit trail | All | Multiple | LOW | Operations | Medium |
| 18 | Error info disclosure | Multiple | Multiple | LOW | Security | Easy |
| 19 | No max booking duration | BookingService | N/A | LOW | Business Logic | Easy |
| 20 | Missing null checks | BookingDAO | 148-156 | LOW | Code Quality | Easy |

---

## 6. RECOMMENDED FIX PRIORITY

### Phase 1 (Critical - Implement Immediately)
1. Add authorization checks to CancelBookingServlet, OwnerCancelBookingServlet, ReturnCarServlet
2. Add end date > start date validation
3. Implement transactional locking for availability check + booking insert
4. Add refund logic on cancellation

### Phase 2 (High - Implement This Sprint)
5. Add status transition validation
6. Add driver license expiry check
7. Add age verification
8. Prevent car deletion if active bookings exist

### Phase 3 (Medium - Plan for Next Sprint)
9. Fix payment/approval flow
10. Add timezone support
11. Implement concurrent modification locking
12. Fix booking location vs car location
13. Add promo code restrictions

### Phase 4 (Low - Plan for Future)
14. Add confirmation timeout
15. Add audit trail
16. Clean up error messages
17. Add max booking duration
18. Improve null checks

---

## 7. CODE EXAMPLES FOR FIXES

### Fix #1: Add Authorization to CancelBookingServlet

```java
protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws IOException {
    
    String bookingIdStr = request.getParameter("bookingId");
    HttpSession session = request.getSession();
    User currentUser = (User) session.getAttribute("user");
    
    if (currentUser == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
    
    if (bookingIdStr != null && !bookingIdStr.isEmpty()) {
        try {
            int bookingId = Integer.parseInt(bookingIdStr);
            
            // ← NEW: Verify ownership
            BookingDAO bookingDAO = new BookingDAO();
            Booking booking = bookingDAO.getBookingById(bookingId);
            
            if (booking == null) {
                request.getSession().setAttribute("error", "Booking not found.");
                response.sendRedirect(request.getContextPath() + "/customer/customerOrder");
                return;
            }
            
            if (booking.getUserId() != currentUser.getUserId()) {
                request.getSession().setAttribute("error", "Unauthorized access.");
                response.sendRedirect(request.getContextPath() + "/customer/customerOrder");
                return;
            }
            
            boolean success = bookingService.cancelBooking(bookingId);
            if (success) {
                request.getSession().setAttribute("message", 
                    "Booking #" + bookingId + " has been canceled successfully!");
            } else {
                request.getSession().setAttribute("error", 
                    "Error: Could not cancel booking #" + bookingId);
            }
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("error", "Invalid booking ID.");
        }
    }
    
    response.sendRedirect(request.getContextPath() + "/customer/customerOrder");
}
```

### Fix #2: Add End Date Validation

```java
// In BookingService.createBooking() - add after line 27
if (booking.getEndDate().isBefore(booking.getStartDate()) || 
    booking.getEndDate().equals(booking.getStartDate())) {
    return "❌ End date must be after start date!";
}
```

### Fix #3: Add License Expiry Check

```java
// In BookingService.createBooking() - replace lines 24-27
Driver_License license = licenseDAO.getLicenseByUserId(booking.getUserId());
if (license == null) {
    return "❌ You must upload your driver license before booking!";
}

// ← NEW: Check expiry
LocalDate today = LocalDate.now();
if (license.getExpiry_date().toLocalDate().isBefore(today)) {
    return "❌ Your driver license has expired!";
}
```

### Fix #4: Add Age Verification

```java
// In BookingService.createBooking() - after license expiry check
LocalDate birthDate = license.getDob().toLocalDate();
int age = today.getYear() - birthDate.getYear();
if (birthDate.plusYears(age).isAfter(today)) {
    age--;
}
if (age < 18) {
    return "❌ You must be at least 18 years old to rent a car!";
}
```

### Fix #5: Add Status Transition Validation

```java
// Add to BookingService class
private static final Map<String, Set<String>> ALLOWED_TRANSITIONS = Map.of(
    "Pending", Set.of("Approved", "Rejected", "Cancelled"),
    "Approved", Set.of("Paid", "Rejected", "Cancelled"),
    "Paid", Set.of("Completed", "Cancelled"),
    "Completed", Set.of() // No transitions from completed
);

public boolean approveBooking(int bookingId) {
    Booking booking = bookingDAO.getBookingById(bookingId);
    if (booking == null) return false;
    
    if (!isValidTransition(booking.getStatus(), "Approved")) {
        System.err.println("❌ Invalid transition: " + booking.getStatus() + " → Approved");
        return false;
    }
    
    return bookingDAO.updateStatus(bookingId, "Approved");
}

private boolean isValidTransition(String from, String to) {
    Set<String> allowed = ALLOWED_TRANSITIONS.getOrDefault(from, Set.of());
    return allowed.contains(to);
}
```

### Fix #6: Atomic Availability Check + Insert

```java
// In BookingDAO - wrap in transaction
public boolean insertWithAvailabilityCheck(Booking booking) {
    // BEGIN TRANSACTION
    try (Connection conn = getConnection()) {
        conn.setAutoCommit(false);
        
        // Check availability WITH LOCK
        String checkSql = """
            SELECT COUNT(*) FROM BOOKING WITH (UPDLOCK, READCOMMITTED)
            WHERE CAR_ID = ? 
              AND STATUS IN ('Pending', 'Approved', 'Paid')
              AND (...)
        """;
        
        try (PreparedStatement ps = conn.prepareStatement(checkSql)) {
            // ... check logic ...
            if (count > 0) {
                conn.rollback();
                return false;
            }
        }
        
        // Insert
        String insertSql = "INSERT INTO BOOKING ...";
        try (PreparedStatement ps = conn.prepareStatement(insertSql, 
            Statement.RETURN_GENERATED_KEYS)) {
            // ... insert logic ...
            if (ps.executeUpdate() > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    booking.setBookingId(rs.getInt(1));
                    conn.commit();
                    return true;
                }
            }
        }
        
        conn.rollback();
        return false;
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}
```

---

## 8. TESTING RECOMMENDATIONS

### Unit Tests Needed
1. Test end date validation (invalid dates)
2. Test authorization checks (wrong user access)
3. Test status transitions (invalid paths)
4. Test license expiry (expired vs valid)
5. Test age verification (underage, adult)
6. Test refund calculation (various cancellation times)

### Integration Tests Needed
1. Concurrent booking test (race condition)
2. Car deletion with active booking
3. Complete booking flow (from browse to completion)
4. Payment flow (cancelled, failed, successful)
5. Promotion code validation and application
6. Availability check across time zones

### System Tests Needed
1. Database constraint validation
2. Transaction rollback scenarios
3. Payment gateway integration
4. Notification system integration
5. Performance under load

