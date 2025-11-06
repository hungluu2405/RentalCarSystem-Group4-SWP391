# RENTAL CAR SYSTEM - BOOKING FLOW ISSUES SUMMARY

## Quick Overview

**Total Issues Found: 20**
- **CRITICAL: 5** issues (Must fix immediately)
- **HIGH: 6** issues (Fix this sprint)
- **MEDIUM: 5** issues (Plan next sprint)
- **LOW: 4** issues (Plan for future)

---

## CRITICAL ISSUES (MUST FIX NOW)

### 1. Race Condition - Double Booking (HARDEST)
**File:** `BookingDAO.java:79-122`, `BookingService.java:44-50`
- Two users can simultaneously book same car for overlapping periods
- Availability check not atomic with booking insertion
- **Fix:** Use database transactions with SELECT FOR UPDATE lock

### 2. Missing End Date Validation (EASY)
**File:** `BookingService.java:23-40`
- User can book with endDate ≤ startDate
- **Fix:** Add: `if (endDate.isBefore(startDate)) return error;`

### 3. No Authorization in Cancellation (EASY)
**File:** `CancelBookingServlet.java:14-39`, `OwnerCancelBookingServlet.java:14-38`
- Any logged-in user can cancel ANY booking
- Any owner can cancel ANY booking
- **Fix:** Verify booking belongs to current user before canceling

### 4. No Refund Logic (MEDIUM)
**File:** `BookingService.java:155-166`
- Bookings cancelled with NO refund tracking or processing
- Financial loss, no refund audit trail
- **Fix:** Add refund amount calculation and tracking

### 5. No Status Transition Validation (MEDIUM)
**File:** `BookingService.java:129-192`
- Any booking can transition to ANY status
- Can go: Completed → Approved, Rejected → Paid, etc.
- **Fix:** Implement state machine with whitelisted transitions

---

## HIGH SEVERITY ISSUES

### 6. No Driver License Expiry Check (EASY)
**File:** `BookingService.java:24-27`
- System doesn't check if license is expired
- Users can book with expired licenses
- **Fix:** Add: `if (license.getExpiry_date().isBefore(today)) return error;`

### 7. No Age Verification (EASY)
**File:** `BookingService.java` (missing code)
- System has DOB in Driver_License model but never checks it
- Underage drivers can book cars
- **Fix:** Calculate age from DOB and verify ≥ 18

### 8. No Return Car Authorization (EASY)
**File:** `ReturnCarServlet.java:22-54`
- Any user can mark ANY booking as "Completed"/returned
- **Fix:** Verify booking belongs to current user

### 9. Payment Requires Owner Pre-Approval (MEDIUM)
**File:** `PayPalService.java:28-29`
- Customer can't pay until owner approves booking
- Awkward flow for instant bookings
- **Fix:** Allow payment from Pending status or change flow

### 10. Car Deletion Risk (MEDIUM)
**File:** `BookingService.java:40-41`
- No protection if car is deleted while booking exists
- System fails when fetching car details for active bookings
- **Fix:** Add foreign key constraint or prevent deletion

### 11. No Timezone Handling (HARDEST)
**File:** Multiple files
- System uses LocalDate/LocalTime without timezone awareness
- Availability calculated incorrectly across timezones
- **Fix:** Store times in UTC, convert to user timezone

---

## MEDIUM SEVERITY ISSUES

### 12. No Concurrent Modification Locking
**File:** `BookingService.java`
- Multiple simultaneous updates to same booking not locked
- Owner approves while customer paying = data inconsistency
- **Fix:** Add optimistic locking with version field

### 13. Location Field Confusion
**File:** `BookingDAO.java:289, 311`
- Uses CAR.LOCATION instead of BOOKING.LOCATION
- Wrong pickup location displayed to users
- **Fix:** Use BOOKING.LOCATION consistently

### 14. No Booking Confirmation Timeout
**File:** `BookingConfirmationServlet.java:58-61`
- Session data cleared immediately after display
- Hard to retry if user navigates away
- **Fix:** Add session timeout, store ID in URL backup

### 15. Promo Code Abuse Possible
**File:** `BookingService.java:78-98`
- No max uses per customer
- No minimum booking amount requirement
- No frequency limits
- **Fix:** Add usage restrictions and validation

### 16. No Concurrent Booking Lock
**File:** `BookingDAO.java` (availability check)
- Missing database transaction locks
- **Fix:** Use SELECT FOR UPDATE in atomic transaction

---

## LOW SEVERITY ISSUES (4)

17. **Booking ID Consistency** - Use Integer wrapper
18. **No Audit Trail** - Add booking history table
19. **Exposed Error Messages** - Log server-side, generic response
20. **No Max Booking Duration** - Add duration limit config

---

## BOOKING STATUS FLOW (CURRENT)

```
PENDING ──► APPROVED ──► PAID ──► COMPLETED
  │           │ ◄────────────────────┘
  │           │
  └────────► REJECTED
  
CANCELLED ◄─── (from any status)
```

**PROBLEM:** Invalid transitions allowed (Rejected→Approved, Completed→Approved, etc.)

---

## FILE LOCATIONS

| Component | File | Issues |
|-----------|------|--------|
| Booking Model | `model/Booking.java` | - |
| Booking Detail Model | `model/BookingDetail.java` | - |
| Car Model | `model/Car.java` | - |
| Driver License Model | `model/Driver_License.java` | Age not checked |
| **Booking Controller** | `controller/booking/BookingController.java` | End date validation |
| **Booking Confirmation** | `controller/booking/BookingConfirmationServlet.java` | No timeout |
| **Cancel Booking** | `controller/booking/CancelBookingServlet.java` | **CRITICAL: No auth check** |
| **Owner Cancel** | `controller/booking/OwnerCancelBookingServlet.java` | **CRITICAL: No auth check** |
| **Return Car** | `controller/booking/ReturnCarServlet.java` | **HIGH: No auth check** |
| **Booking Service** | `service/booking/BookingService.java` | Multiple critical issues |
| **PayPal Service** | `service/booking/PayPalService.java` | Payment approval required |
| **Booking DAO** | `dao/implement/BookingDAO.java` | **CRITICAL: Race condition** |
| **Car DAO** | `dao/implement/CarDAO.java` | - |
| Owner Booking | `controller/carOwner/OwnerBooking.java` | - |

---

## RISK ASSESSMENT

### Business Risks
- **Revenue Loss:** Race condition allows overbooking
- **Legal Liability:** Underage drivers, expired licenses
- **Financial Loss:** No refund tracking
- **Reputation:** Customer data integrity issues

### Security Risks
- **Authorization Bypass:** Cancel/return anyone's booking
- **Data Manipulation:** Invalid status transitions
- **Information Disclosure:** Detailed error messages

### Technical Risks
- **Data Integrity:** Car deletion breaks existing bookings
- **Concurrency Issues:** Race conditions, lost updates
- **System Crashes:** Missing null checks, timezone issues

---

## IMPLEMENTATION ROADMAP

### Sprint 1 (CRITICAL - 1 week)
```
✓ Add authorization checks (CancelBooking, ReturnCar) - 3 hours
✓ Add end date validation - 1 hour
✓ Add license expiry check - 1 hour
✓ Add age verification - 2 hours
✓ Add refund logic skeleton - 4 hours
✓ Testing - 4 hours
Total: ~15 hours
```

### Sprint 2 (HIGH - 1 week)
```
✓ Add status transition validation - 4 hours
✓ Add return car status check - 2 hours
✓ Prevent car deletion with bookings - 3 hours
✓ Atomic booking + availability check - 6 hours (hardest)
✓ Testing - 5 hours
Total: ~20 hours
```

### Sprint 3 (MEDIUM - 2 weeks)
```
✓ Payment flow redesign - 6 hours
✓ Concurrent modification locking - 5 hours
✓ Promo code restrictions - 4 hours
✓ Location field fixes - 2 hours
✓ Confirmation timeout - 3 hours
✓ Testing - 5 hours
Total: ~25 hours
```

### Sprint 4 (LOW - As needed)
```
✓ Audit trail implementation - 6 hours
✓ Timezone support - 8 hours (hardest)
✓ Error message sanitization - 2 hours
✓ Null check improvements - 2 hours
✓ Max booking duration - 1 hour
```

---

## TESTING CHECKLIST

### Unit Tests (Priority Order)
- [ ] End date validation
- [ ] Age verification  
- [ ] License expiry check
- [ ] Authorization checks
- [ ] Status transition validation
- [ ] Refund calculation

### Integration Tests
- [ ] Race condition prevention (concurrent bookings)
- [ ] Complete booking flow
- [ ] Payment processing
- [ ] Cancellation with refund
- [ ] Car deletion protection
- [ ] Authorization enforcement

### System Tests
- [ ] Multi-user concurrent bookings
- [ ] Payment gateway integration
- [ ] Notification delivery
- [ ] Database constraints
- [ ] Performance under load

---

## ESTIMATED EFFORT

- **Easy Issues (5):** 2 points each = 10 points
- **Medium Issues (7):** 5 points each = 35 points
- **Hard Issues (3):** 13 points each = 39 points
- **Hardest Issues (2):** 21 points each = 42 points

**TOTAL: ~126 story points**

**Recommended:** 2-4 sprints (3 weeks - 2 months) to fully address all issues

---

## NEXT STEPS

1. **Review** this document with your team
2. **Prioritize** based on business impact
3. **Create Jira/GitHub Issues** for each item
4. **Assign** to developers with priority tags
5. **Test** each fix thoroughly
6. **Deploy** with rollback plan
7. **Monitor** for regressions

---

## FILES TO REVIEW

Full detailed analysis with code examples in:
`/home/user/RentalCarSystem-Group4-SWP391/BOOKING_ANALYSIS_REPORT.md`

