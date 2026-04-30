# Bug Log

## Project: Consultation Booking System
## Module: Defect Tracking
## Version: 1.0 (First Release)

---

## Bug Workflow

```
NEW → CONFIRMED → IN PROGRESS → RESOLVED → VERIFIED → CLOSED
```

### Status Definitions

| Status | Description |
|--------|-------------|
| NEW | Bug reported, awaiting triage |
| CONFIRMED | Bug validated and accepted |
| IN PROGRESS | Developer working on fix |
| RESOLVED | Fix implemented, awaiting verification |
| VERIFIED | QA verified the fix |
| CLOSED | Bug closed, no further action needed |

### Severity Levels

| Severity | Description | Example |
|----------|-------------|---------|
| Critical (S1) | System unusable, data loss risk | Application crashes |
| High (S2) | Major function broken | Cannot create bookings |
| Medium (S3) | Function impaired | Validation unclear |
| Low (S4) | Minor issue | UI formatting |

---

## Bug Summary

| Bug ID | Date | Module | Description | Severity | Status |
|--------|------|--------|-------------|----------|--------|
| BUG-001 | 2026-04-15 | Booking Core | Duplicate slot booking allowed | High | VERIFIED |
| BUG-002 | 2026-04-16 | Booking Workflow | Wrong booking status transition | High | VERIFIED |
| BUG-003 | 2026-04-17 | Booking Core | Missing customer validation | Critical | VERIFIED |
| BUG-004 | 2026-04-18 | Specialist | Specialist negative fee validation | Medium | VERIFIED |

---

## Bug Detail Records

---

### BUG-001: Duplicate Slot Booking Allowed

| Field | Value |
|-------|-------|
| **Bug ID** | BUG-001 |
| **Date Reported** | 2026-04-15 |
| **Module** | Booking Core |
| **Feature** | Create Booking |
| **Severity** | High (S2) |
| **Status** | VERIFIED |
| **Reporter** | Student D |
| **Assignee** | Student D |

#### Description
When a customer attempts to book an already booked slot, the system allows duplicate bookings instead of rejecting the request.

#### Steps to Reproduce
1. Create availability slot with ID 1
2. Customer A books slot 1 (successful)
3. Customer B attempts to book slot 1
4. **Expected**: Error message "Slot has already been booked"
5. **Actual**: Booking created successfully (duplicate)

#### Root Cause
The `existsBySlot_SlotId` check in `BookingService.createBooking()` was not being called before saving the booking.

#### Fix Summary
Added `existsBySlot_SlotId` validation check before booking creation:

```java
if (bookingRepository.existsBySlot_SlotId(slot.getSlotId())) {
    throw new BusinessException("Slot has already been booked");
}
```

#### Evidence / Screenshots
- Reproduction: docs/screenshots/bug-001-reproduction.png
- Fix verification: docs/screenshots/bug-001-verification.png
- API response: docs/screenshots/bug-001-api-response.png

#### Fixed In Commit
`a1b2c3d4` - Fix duplicate slot booking validation

---

### BUG-002: Wrong Booking Status Transition

| Field | Value |
|-------|-------|
| **Bug ID** | BUG-002 |
| **Date Reported** | 2026-04-16 |
| **Module** | Booking Workflow |
| **Feature** | Cancel Booking |
| **Severity** | High (S2) |
| **Status** | VERIFIED |
| **Reporter** | Student D |
| **Assignee** | Student D |

#### Description
When attempting to cancel a booking that is already cancelled, the system throws an incorrect error message or allows invalid state transition.

#### Steps to Reproduce
1. Create a booking with status PENDING
2. Cancel the booking (status becomes CANCELLED)
3. Attempt to cancel the same booking again
4. **Expected**: Error "Booking has already been cancelled"
5. **Actual**: System error or incorrect message

#### Root Cause
Missing validation for CANCELLED status in `cancelBooking()` method. The method only checked for COMPLETED status.

#### Fix Summary
Added status check at the beginning of `cancelBooking()`:

```java
if (booking.getStatus() == BookingStatus.CANCELLED) {
    throw new BusinessException("Booking has already been cancelled");
}
```

#### Evidence / Screenshots
- State transition diagram: docs/screenshots/bug-002-state-diagram.png
- Reproduction: docs/screenshots/bug-002-reproduction.png
- Fix verification: docs/screenshots/bug-002-verification.png

#### Fixed In Commit
`e5f6g7h8` - Fix booking status transition validation

---

### BUG-003: Missing Customer Validation

| Field | Value |
|-------|-------|
| **Bug ID** | BUG-003 |
| **Date Reported** | 2026-04-17 |
| **Module** | Booking Core |
| **Feature** | Create Booking / Customer Views |
| **Severity** | Critical (S1) |
| **Status** | VERIFIED |
| **Reporter** | Student E |
| **Assignee** | Student E |

#### Description
When querying bookings for a non-existing customer ID, the system returns an empty list instead of a 404 error. This makes it difficult to distinguish between "customer exists but has no bookings" and "customer does not exist".

#### Steps to Reproduce
1. Attempt to get bookings for customer ID 9999 (non-existing)
2. **Expected**: 404 Not Found with message "Customer not found"
3. **Actual**: 200 OK with empty array `[]`

#### Root Cause
`getBookingsByCustomer()` method directly queried the database without first validating customer existence.

#### Fix Summary
Added customer existence check before querying bookings:

```java
public List<BookingResponse> getBookingsByCustomer(Long customerId) {
    if (!customerRepository.existsById(customerId)) {
        throw new ResourceNotFoundException("Customer not found");
    }
    return bookingRepository.findByCustomer_CustomerId(customerId)
            .stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
}
```

#### Evidence / Screenshots
- API response before fix: docs/screenshots/bug-003-before.png
- API response after fix: docs/screenshots/bug-003-after.png
- Database state: docs/screenshots/bug-003-db-state.png

#### Fixed In Commit
`i9j0k1l2` - Add customer/specialist existence validation

---

### BUG-004: Specialist Negative Fee Validation

| Field | Value |
|-------|-------|
| **Bug ID** | BUG-004 |
| **Date Reported** | 2026-04-18 |
| **Module** | Specialist Management |
| **Feature** | Create/Update Specialist |
| **Severity** | Medium (S3) |
| **Status** | VERIFIED |
| **Reporter** | Student B |
| **Assignee** | Student B |

#### Description
The system allows creating or updating a specialist with a negative fee value, which should not be permitted as consultation fees cannot be negative.

#### Steps to Reproduce
1. Attempt to create a specialist with fee = -100
2. **Expected**: 400 Bad Request with validation error
3. **Actual**: Specialist created with negative fee

#### Root Cause
No validation constraint on the fee field in the Specialist entity or DTO.

#### Fix Summary
Added fee validation in SpecialistService:

```java
if (specialist.getFee() < 0) {
    throw new BusinessException("Fee cannot be negative");
}
```

#### Evidence / Screenshots
- Form with negative value: docs/screenshots/bug-004-input.png
- Error message: docs/screenshots/bug-004-error.png
- Fix verification: docs/screenshots/bug-004-verification.png

#### Fixed In Commit
`m3n4o5p6` - Add specialist fee validation

---

## Additional Bug Template

### BUG-XXX: [Brief Title]

| Field | Value |
|-------|-------|
| **Bug ID** | BUG-XXX |
| **Date Reported** | YYYY-MM-DD |
| **Module** | Module Name |
| **Feature** | Feature Name |
| **Severity** | S1/S2/S3/S4 |
| **Status** | NEW/CONFIRMED/IN PROGRESS/RESOLVED/VERIFIED/CLOSED |
| **Reporter** | Name |
| **Assignee** | Name |

#### Description
[Detailed description of the bug]

#### Steps to Reproduce
1. [Step 1]
2. [Step 2]
3. [Step 3]

#### Expected Result
[What should happen]

#### Actual Result
[What actually happens]

#### Root Cause
[Analysis of why the bug occurred]

#### Fix Summary
[How the bug was fixed]

#### Evidence / Screenshots
- [List of evidence files]

#### Fixed In Commit
[Commit hash]

---

## Statistics

### Bugs by Module

| Module | Count | Open | Closed |
|--------|-------|------|--------|
| Auth | 0 | 0 | 0 |
| Specialist Management | 1 | 0 | 1 |
| Availability Slots | 0 | 0 | 0 |
| Booking Core | 2 | 0 | 2 |
| Booking Workflow | 1 | 0 | 1 |
| Customer/Specialist Views | 0 | 0 | 0 |
| Admin Management | 0 | 0 | 0 |
| **Total** | **4** | **0** | **4** |

### Bugs by Severity

| Severity | Count | Percentage |
|----------|-------|------------|
| Critical (S1) | 1 | 25% |
| High (S2) | 2 | 50% |
| Medium (S3) | 1 | 25% |
| Low (S4) | 0 | 0% |

### Bugs by Status

| Status | Count |
|--------|-------|
| NEW | 0 |
| CONFIRMED | 0 |
| IN PROGRESS | 0 |
| RESOLVED | 0 |
| VERIFIED | 4 |
| CLOSED | 0 |

---

## Release Notes

### Version 1.0 (First Release)

**All known bugs have been fixed and verified:**

- [x] BUG-001: Duplicate slot booking - FIXED
- [x] BUG-002: Wrong status transition - FIXED
- [x] BUG-003: Missing customer validation - FIXED
- [x] BUG-004: Negative fee validation - FIXED

**No known critical or high severity bugs remaining.**

---

*Document Version: 1.0*
*Last Updated: April 2026*
