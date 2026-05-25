# Concurrency Booking Test

## Purpose

Verify that the pessimistic locking mechanism correctly prevents double-booking when multiple customers try to book the same availability slot simultaneously.

## Expected Behavior

With pessimistic locking implemented:

- **One request** should successfully create a booking (HTTP 200, `success: true`)
- **Other concurrent requests** should fail with clear error: "This appointment time is no longer available."
- **Only one active booking** (PENDING or CONFIRMED) should exist for the selected slot and date

## Setup Steps

### 1. Start the Spring Boot Application

```bash
cd CPT202
mvn spring-boot:run
```

Wait for the application to start (usually takes 10-30 seconds).

### 2. Find or Create an Available Slot

**Option A: Use existing slot**

```bash
# List all slots for a specialist
curl http://localhost:8080/api/slots/specialist/1
```

Look for a slot with `"status": "AVAILABLE"` and note its `slotId`.

**Option B: Create a new slot via Manager dashboard**

1. Login as Manager
2. Go to Slots tab
3. Create a new slot with today's or tomorrow's day of week

### 3. Determine Appointment Date

The appointment date must match the slot's `dayOfWeek`.

For example, if the slot is on `MONDAY`, use a future Monday date:

```bash
# Calculate next Monday
date -v+1d -v+Tuesday -v+Wednesday -v+Thursday -v+Friday -v+Saturday -v+Sunday +%Y-%m-%d
```

Or simply: `2026-05-25` (if that's a Monday)

### 4. Configure the Test Script

Edit `book_once.sh`:

```bash
# Line 22-23
SLOT_ID=1           # Your available slot ID
APPOINTMENT_DATE="2026-05-25"  # Matching date
```

## Running the Test

### Step 1: Make Scripts Executable

```bash
chmod +x book_once.sh run_concurrency_test.sh
```

### Step 2: Single Request Test (Optional)

First, verify basic booking works:

```bash
./book_once.sh
```

Expected: HTTP 200, `success: true`

### Step 3: Concurrency Test

```bash
./run_concurrency_test.sh
```

This will:
1. Launch 20 concurrent booking requests
2. Wait for all to complete
3. Display a summary

## Interpreting Results

### Expected Output

```
================================================
  SUMMARY
================================================

  Successful bookings:  1
  Slot unavailable:    19
  Other errors:       0

  Total requests: 20

✓ TEST PASSED: Exactly 1 booking succeeded (expected behavior)
```

### Failure Indicators

| Symptom | Likely Cause |
|---------|--------------|
| Multiple successes (e.g., 5-20) | Pessimistic locking not working |
| All requests fail | Slot already booked or configuration issue |
| No requests succeed, all fail with different errors | Check SLOT_ID, APPOINTMENT_DATE, or API endpoint |

## Verifying in MySQL

After the test, verify the database state:

### Check Bookings for This Test

```sql
SELECT booking_id, customer_id, specialist_id, slot_id,
       appointment_date, topic, status, created_at
FROM consultation_booking.bookings
WHERE topic = 'concurrency_test'
ORDER BY created_at;
```

**Expected**: 1 row with status `PENDING`

### Check Slot Has Only One Active Booking

```sql
SELECT s.slot_id, s.day_of_week, s.start_time, s.end_time,
       b.booking_id AS active_booking_id, b.status, b.appointment_date
FROM consultation_booking.availability_slots s
LEFT JOIN consultation_booking.bookings b
    ON s.slot_id = b.slot_id
    AND b.status IN ('PENDING', 'CONFIRMED')
WHERE s.slot_id = YOUR_SLOT_ID;
```

**Expected**: Only 1 active booking for the test slot

### Verify No Race Condition

Check that booking IDs are sequential (no gaps indicating lost transactions):

```sql
SELECT booking_id, created_at, status
FROM consultation_booking.bookings
WHERE topic = 'concurrency_test'
ORDER BY booking_id;
```

## Technical Details

### Implementation

The concurrency control uses:

- **Repository**: `findByIdForUpdate()` with `@Lock(LockModeType.PESSIMISTIC_WRITE)`
- **Service**: `@Transactional` on `createBooking()` and `rescheduleBooking()`
- **Database**: MySQL InnoDB `SELECT ... FOR UPDATE`

### How It Works

1. First request acquires row-level lock via `SELECT ... FOR UPDATE`
2. Other requests wait (blocked) for the lock
3. First request creates booking and commits
4. Subsequent requests see the booking and receive "no longer available" error

## Cleanup

After testing, clean up test data:

```sql
DELETE FROM consultation_booking.bookings WHERE topic = 'concurrency_test';
```

Or via the application UI.

## Troubleshooting

### "Connection refused" error

Spring Boot not running. Start it first.

### "Slot not found" error

Check `SLOT_ID` in `book_once.sh` matches an actual slot.

### "Appointment date must be on XDAY" error

`APPOINTMENT_DATE` doesn't match slot's `dayOfWeek`. Fix the date.

### All requests succeed (TEST FAILED)

Pessimistic locking not working. Check:
- `AvailabilitySlotRepository.findByIdForUpdate()` exists
- `@Lock(LockModeType.PESSIMISTIC_WRITE)` annotation present
- MySQL InnoDB engine is used (not MyISAM)
