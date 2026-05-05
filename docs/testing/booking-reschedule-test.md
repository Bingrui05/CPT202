# Booking Reschedule Test Record

## Module
Booking reschedule

## Owner
Student F

## Branch
feature/booking-reschedule

## Related Issue
Add rescheduling support to the existing booking workflow

## Test Environment
- Java 23
- Spring Boot 3.3.0
- Maven
- MySQL
- Localhost port: 8080

---

## API Endpoint

| Method | Endpoint | Description |
|--------|----------|-------------|
| PUT | /api/bookings/{bookingId}/reschedule | Reschedule a booking to a new slot |

**Request body:**
```json
{
  "newSlotId": 2
}
```

---

## Test Case 1: Reschedule PENDING booking successfully

**Endpoint:** `PUT /api/bookings/{bookingId}/reschedule`

**Precondition:** A PENDING booking exists with at least two available slots belonging to the same specialist.

**Test steps:**
1. Create a booking (status = PENDING) linked to slot A.
2. Verify another available slot B exists for the same specialist.
3. Send PUT request with `newSlotId = B.id`.

**Expected result:**
- Returns HTTP 200.
- Booking status becomes `PENDING`.
- `slotId` becomes the new slot's ID.
- `updatedAt` is updated to current time.
- `price` remains unchanged.

**Actual result:**
- **PLACEHOLDER** - Fill after testing

**Screenshot:**
- docs/screenshots/booking-reschedule/reschedule-pending-success.png

---

## Test Case 2: Reschedule CONFIRMED booking successfully and status becomes PENDING

**Endpoint:** `PUT /api/bookings/{bookingId}/reschedule`

**Precondition:** A CONFIRMED booking exists with at least two available slots belonging to the same specialist.

**Test steps:**
1. Create a booking (status = PENDING), confirm it (status = CONFIRMED).
2. Verify another available slot B exists for the same specialist.
3. Send PUT request with `newSlotId = B.id`.

**Expected result:**
- Returns HTTP 200.
- Booking status becomes `PENDING` (resets to PENDING after rescheduling).
- `slotId` becomes the new slot's ID.
- `updatedAt` is updated.
- `price` remains unchanged.

**Actual result:**
- **PLACEHOLDER** - Fill after testing

**Screenshot:**
- docs/screenshots/booking-reschedule/reschedule-confirmed-to-pending.png

---

## Test Case 3: Old slot becomes AVAILABLE after rescheduling

**Endpoint:** `PUT /api/bookings/{bookingId}/reschedule`

**Precondition:** A booking is linked to slot A.

**Test steps:**
1. Reschedule the booking to slot B (as in Test Case 1).
2. After rescheduling, query slot A via `GET /api/slots/specialist/{id}` or `GET /api/slots/specialist/{id}/available`.

**Expected result:**
- Slot A's `status` is `AVAILABLE`.
- Slot B's `status` is `BOOKED`.

**Actual result:**
- **PLACEHOLDER** - Fill after testing

**Screenshot:**
- docs/screenshots/booking-reschedule/old-slot-available.png

---

## Test Case 4: New slot becomes BOOKED after rescheduling

**Endpoint:** `PUT /api/bookings/{bookingId}/reschedule`

**Precondition:** Slot B is `AVAILABLE` and belongs to the same specialist as the booking.

**Test steps:**
1. Reschedule the booking to slot B.
2. After rescheduling, query slot B's status.

**Expected result:**
- Slot B's `status` is `BOOKED`.

**Actual result:**
- **PLACEHOLDER** - Fill after testing

**Screenshot:**
- docs/screenshots/booking-reschedule/new-slot-booked.png

---

## Test Case 5: Reject rescheduling COMPLETED booking

**Endpoint:** `PUT /api/bookings/{bookingId}/reschedule`

**Precondition:** A COMPLETED booking exists.

**Test steps:**
1. Send PUT request with a valid `newSlotId` for a COMPLETED booking.

**Expected result:**
- Returns HTTP 400 Bad Request.
- Response body: `{"success": false, "message": "COMPLETED booking cannot be rescheduled", "data": null}`

**Actual result:**
- **PLACEHOLDER** - Fill after testing

**Screenshot:**
- docs/screenshots/booking-reschedule/reject-reschedule-completed.png

---

## Test Case 6: Reject rescheduling CANCELLED booking

**Endpoint:** `PUT /api/bookings/{bookingId}/reschedule`

**Precondition:** A CANCELLED booking exists.

**Test steps:**
1. Send PUT request with a valid `newSlotId` for a CANCELLED booking.

**Expected result:**
- Returns HTTP 400 Bad Request.
- Response body: `{"success": false, "message": "CANCELLED booking cannot be rescheduled", "data": null}`

**Actual result:**
- **PLACEHOLDER** - Fill after testing

**Screenshot:**
- docs/screenshots/booking-reschedule/reject-reschedule-cancelled.png

---

## Test Case 7: Reject rescheduling non-existing booking

**Endpoint:** `PUT /api/bookings/9999/reschedule`

**Test steps:**
1. Send PUT request with a non-existent `bookingId`.

**Expected result:**
- Returns HTTP 404 Not Found.
- Response body: `{"success": false, "message": "Booking not found", "data": null}`

**Actual result:**
- **PLACEHOLDER** - Fill after testing

**Screenshot:**
- docs/screenshots/booking-reschedule/reject-non-existing-booking.png

---

## Test Case 8: Reject non-existing new slot

**Endpoint:** `PUT /api/bookings/{bookingId}/reschedule`

**Precondition:** A PENDING booking exists.

**Test steps:**
1. Send PUT request with a non-existent `newSlotId` (e.g., 9999).

**Expected result:**
- Returns HTTP 404 Not Found.
- Response body: `{"success": false, "message": "Slot not found", "data": null}`

**Actual result:**
- **PLACEHOLDER** - Fill after testing

**Screenshot:**
- docs/screenshots/booking-reschedule/reject-non-existing-slot.png

---

## Test Case 9: Reject new slot that belongs to another specialist

**Endpoint:** `PUT /api/bookings/{bookingId}/reschedule`

**Precondition:** A booking exists for specialist A. An available slot exists for specialist B.

**Test steps:**
1. Send PUT request with `newSlotId` pointing to specialist B's slot.

**Expected result:**
- Returns HTTP 400 Bad Request.
- Response body: `{"success": false, "message": "New slot does not belong to the same specialist as the original booking", "data": null}`

**Actual result:**
- **PLACEHOLDER** - Fill after testing

**Screenshot:**
- docs/screenshots/booking-reschedule/reject-wrong-specialist.png

---

## Test Case 10: Reject new slot that is not AVAILABLE

**Endpoint:** `PUT /api/bookings/{bookingId}/reschedule`

**Precondition:** A booking exists. Another slot for the same specialist exists but its status is `BOOKED`.

**Test steps:**
1. Send PUT request with `newSlotId` pointing to a BOOKED slot.

**Expected result:**
- Returns HTTP 400 Bad Request.
- Response body: `{"success": false, "message": "New slot is not available", "data": null}`

**Actual result:**
- **PLACEHOLDER** - Fill after testing

**Screenshot:**
- docs/screenshots/booking-reschedule/reject-slot-not-available.png

---

## Test Case 11: Verify price remains unchanged after rescheduling

**Endpoint:** `PUT /api/bookings/{bookingId}/reschedule`

**Precondition:** A booking exists with a known `price`.

**Test steps:**
1. Record the booking's `price` before rescheduling.
2. Reschedule the booking to a different slot.
3. Verify the returned `price` equals the original `price`.

**Expected result:**
- `price` in the response equals the original `price`.
- The specialist is the same, so the price was already set at creation time.

**Actual result:**
- **PLACEHOLDER** - Fill after testing

**Screenshot:**
- docs/screenshots/booking-reschedule/price-unchanged.png

---

## Test Case 12: Reject new slot already booked by another booking

**Endpoint:** `PUT /api/bookings/{bookingId}/reschedule`

**Precondition:** Two different bookings exist, each using a different slot. Both slots belong to the same specialist.

**Test steps:**
1. Attempt to reschedule booking A using booking B's slot (which is already BOOKED).

**Expected result:**
- Returns HTTP 400 Bad Request.
- Response body: `{"success": false, "message": "New slot has already been booked by another booking", "data": null}`

**Actual result:**
- **PLACEHOLDER** - Fill after testing

**Screenshot:**
- docs/screenshots/booking-reschedule/reject-slot-already-booked.png

---

## Test Case 13: Reschedule with missing newSlotId (validation error)

**Endpoint:** `PUT /api/bookings/{bookingId}/reschedule`

**Test steps:**
1. Send PUT request with an empty or null body (no `newSlotId`).

**Expected result:**
- Returns HTTP 400 Bad Request.
- Response body contains validation error: `"New slot ID is required"`.

**Actual result:**
- **PLACEHOLDER** - Fill after testing

**Screenshot:**
- docs/screenshots/booking-reschedule/validation-missing-slot-id.png

---

## Notes for Group Report

The booking reschedule feature allows customers or admins to change the time slot of an existing booking. The feature follows the existing booking workflow architecture.

**Implementation summary:**

1. **New endpoint:** `PUT /api/bookings/{bookingId}/reschedule` with request body `{ "newSlotId": <Long> }`.

2. **Business rules enforced:**
   - Booking must exist (404 if not found).
   - New slot must exist (404 if not found).
   - New slot must belong to the same specialist as the original booking.
   - New slot status must be `AVAILABLE`.
   - New slot must not already be booked by another booking.
   - `COMPLETED` bookings cannot be rescheduled.
   - `CANCELLED` bookings cannot be rescheduled.

3. **On successful reschedule:**
   - Old slot status → `AVAILABLE`.
   - New slot status → `BOOKED`.
   - Booking's `slot` → new slot.
   - Booking status → `PENDING` (regardless of previous status).
   - `updatedAt` → current time.
   - `price` → unchanged.

4. **Price rationale:** Since the specialist remains the same, the booking price calculated at creation time is preserved.

**Security Notes:**
- No authentication/authorization implemented in this module.
- Role-based access control should be added in future releases.
