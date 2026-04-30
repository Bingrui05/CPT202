# Acceptance Test Scenarios

## Project: Consultation Booking System
## Module: End-to-End Testing
## Version: 1.0 (First Release)

---

## Overview

Acceptance testing validates that the system meets the business requirements from an end-user perspective. These scenarios cover the complete booking workflow from registration to completion.

---

## Scenario 1: Customer Books a Consultation Successfully

| Field | Value |
|-------|-------|
| **Scenario ID** | ACC-001 |
| **Title** | Customer books a consultation successfully |
| **Priority** | High |
| **Module** | Booking Core |

### Actor
Customer (registered user)

### Preconditions
1. Customer is registered in the system (customerId = 1)
2. Specialist is registered with available slots (specialistId = 1)
3. Availability slot exists and is AVAILABLE (slotId = 1)
4. Customer is logged in

### Steps

| Step | Action | Expected Result |
|------|--------|-----------------|
| 1 | Customer views available specialists | List of specialists displayed |
| 2 | Customer selects specialist (specialistId = 1) | Specialist details shown |
| 3 | Customer views specialist's available slots | Available slots listed |
| 4 | Customer selects slot (slotId = 1) | Slot selected |
| 5 | Customer submits booking request with topic and notes | Booking created |
| 6 | System returns booking confirmation | Booking ID received |

### Request Data

```json
{
  "customerId": 1,
  "specialistId": 1,
  "slotId": 1,
  "topic": "Career Guidance",
  "notes": "Looking for advice on software engineering career path"
}
```

### Expected Result
- Booking created with status PENDING
- Slot status changed to BOOKED
- Response contains bookingId, status, price, customerId, specialistId
- No sensitive data exposed

### Evidence Placeholder
- Screenshot: `docs/screenshots/acc-001-booking-created.png`
- API Response: `docs/screenshots/acc-001-response.png`

---

## Scenario 2: Manager Confirms Booking

| Field | Value |
|-------|-------|
| **Scenario ID** | ACC-002 |
| **Title** | Manager confirms booking |
| **Priority** | High |
| **Module** | Booking Workflow |

### Actor
Manager (admin user)

### Preconditions
1. Booking exists with status PENDING (bookingId = 1)
2. Manager is logged in

### Steps

| Step | Action | Expected Result |
|------|--------|-----------------|
| 1 | Manager views all pending bookings | Pending bookings listed |
| 2 | Manager selects booking (bookingId = 1) | Booking details shown |
| 3 | Manager confirms booking | Booking status updated |
| 4 | System returns updated booking | Status = CONFIRMED |

### Expected Result
- Booking status changed from PENDING to CONFIRMED
- createdAt and updatedAt timestamps correct
- Response confirms successful update

### Evidence Placeholder
- Screenshot: `docs/screenshots/acc-002-confirmed.png`
- API Response: `docs/screenshots/acc-002-response.png`

---

## Scenario 3: Specialist Completes Appointment

| Field | Value |
|-------|-------|
| **Scenario ID** | ACC-003 |
| **Title** | Specialist completes appointment |
| **Priority** | High |
| **Module** | Booking Workflow |

### Actor
Specialist (service provider)

### Preconditions
1. Booking exists with status CONFIRMED (bookingId = 1)
2. Specialist is logged in
3. Appointment time has passed

### Steps

| Step | Action | Expected Result |
|------|--------|-----------------|
| 1 | Specialist views their schedule | All confirmed bookings shown |
| 2 | Specialist selects booking (bookingId = 1) | Booking details shown |
| 3 | Specialist marks booking as completed | Booking status updated |
| 4 | System returns updated booking | Status = COMPLETED |

### Expected Result
- Booking status changed from CONFIRMED to COMPLETED
- Cannot be cancelled or modified after completion
- Specialist can view completed appointments in history

### Evidence Placeholder
- Screenshot: `docs/screenshots/acc-003-completed.png`
- API Response: `docs/screenshots/acc-003-response.png`

---

## Scenario 4: Customer Cancels Booking Before Completion

| Field | Value |
|-------|-------|
| **Scenario ID** | ACC-004 |
| **Title** | Customer cancels booking before completion |
| **Priority** | High |
| **Module** | Booking Workflow |

### Actor
Customer (registered user)

### Preconditions
1. Booking exists with status PENDING or CONFIRMED (bookingId = 1)
2. Customer is logged in
3. Appointment has not been completed

### Steps

| Step | Action | Expected Result |
|------|--------|-----------------|
| 1 | Customer views their booking history | All bookings shown |
| 2 | Customer selects booking (bookingId = 1) | Booking details shown |
| 3 | Customer cancels booking | Booking cancelled |
| 4 | System returns updated booking | Status = CANCELLED |
| 5 | System makes slot available again | Slot status = AVAILABLE |

### Expected Result
- Booking status changed to CANCELLED
- Slot status changed back to AVAILABLE
- Other customers can now book this slot
- Cannot cancel a COMPLETED booking

### Evidence Placeholder
- Screenshot: `docs/screenshots/acc-004-cancelled.png`
- API Response: `docs/screenshots/acc-004-response.png`

---

## Scenario 5: Duplicate Booking Rejected

| Field | Value |
|-------|-------|
| **Scenario ID** | ACC-005 |
| **Title** | Duplicate booking is rejected |
| **Priority** | High |
| **Module** | Booking Core |

### Actor
Customer (registered user)

### Preconditions
1. Customer A has booked slot 1 (status PENDING)
2. Customer B is logged in and wants to book slot 1

### Steps

| Step | Action | Expected Result |
|------|--------|-----------------|
| 1 | Customer B views available slots | Slot 1 not shown as available |
| 2 | (Alternative) Customer B knows slot ID = 1 | |
| 3 | Customer B attempts to book slot 1 | Error returned |
| 4 | System returns error message | "Slot has already been booked" |

### Expected Result
- Error response with status 400
- Message: "Slot has already been booked"
- No duplicate booking created
- Existing booking remains intact

### Evidence Placeholder
- Screenshot: `docs/screenshots/acc-005-rejected.png`
- API Response: `docs/screenshots/acc-005-response.png`

---

## Scenario 6: Admin Checks Master Data and Bookings

| Field | Value |
|-------|-------|
| **Scenario ID** | ACC-006 |
| **Title** | Admin checks master data and bookings |
| **Priority** | Medium |
| **Module** | Admin Management |

### Actor
Administrator

### Preconditions
1. Admin is logged in
2. System has expertise categories, levels, and bookings

### Steps

| Step | Action | Expected Result |
|------|--------|-----------------|
| 1 | Admin views all expertise categories | Categories listed |
| 2 | Admin views all levels | Levels listed |
| 3 | Admin views all bookings | Bookings listed |
| 4 | Admin verifies data integrity | All data accessible |

### Expected Result
- Expertise categories returned with categoryId, name, status
- Levels returned with levelId, name
- All bookings returned with complete details
- No sensitive data (passwords) exposed

### Evidence Placeholder
- Screenshot: `docs/screenshots/acc-006-categories.png`
- Screenshot: `docs/screenshots/acc-006-levels.png`
- Screenshot: `docs/screenshots/acc-006-bookings.png`

---

## Scenario 7: Customer Views Booking History

| Field | Value |
|-------|-------|
| **Scenario ID** | ACC-007 |
| **Title** | Customer views own booking history |
| **Priority** | High |
| **Module** | Customer/Specialist Views |

### Actor
Customer (registered user)

### Preconditions
1. Customer is registered (customerId = 1)
2. Customer has existing bookings

### Steps

| Step | Action | Expected Result |
|------|--------|-----------------|
| 1 | Customer requests booking history | GET /api/bookings/customer/1 |
| 2 | System validates customer exists | Customer ID verified |
| 3 | System returns customer's bookings | List of bookings |

### Expected Result
- Only bookings belonging to this customer returned
- Each booking contains: bookingId, status, price, topic, notes, slotId, specialistId
- Customer name displayed (not password)
- Empty list if no bookings

### Evidence Placeholder
- Screenshot: `docs/screenshots/acc-007-history.png`
- API Response: `docs/screenshots/acc-007-response.png`

---

## Scenario 8: Specialist Views Schedule

| Field | Value |
|-------|-------|
| **Scenario ID** | ACC-008 |
| **Title** | Specialist views own schedule |
| **Priority** | High |
| **Module** | Customer/Specialist Views |

### Actor
Specialist (service provider)

### Preconditions
1. Specialist is registered (specialistId = 1)
2. Specialist has existing bookings

### Steps

| Step | Action | Expected Result |
|------|--------|-----------------|
| 1 | Specialist requests schedule | GET /api/bookings/specialist/1 |
| 2 | System validates specialist exists | Specialist ID verified |
| 3 | System returns specialist's bookings | List of bookings |

### Expected Result
- Only bookings for this specialist returned
- Each booking contains: bookingId, status, price, topic, notes, slotId, customerId
- Customer name displayed (not password)
- Empty list if no bookings

### Evidence Placeholder
- Screenshot: `docs/screenshots/acc-008-schedule.png`
- API Response: `docs/screenshots/acc-008-response.png`

---

## Scenario 9: Invalid Status Transition Rejected

| Field | Value |
|-------|-------|
| **Scenario ID** | ACC-009 |
| **Title** | Invalid booking status transition is rejected |
| **Priority** | Medium |
| **Module** | Booking Workflow |

### Actor
System (automatic validation)

### Preconditions
1. Booking exists with various statuses

### Test Cases

| Booking Status | Action | Expected Result |
|---------------|--------|-----------------|
| CANCELLED | Attempt to confirm | Error: "Booking has already been cancelled" |
| CANCELLED | Attempt to complete | Error: "Booking has already been cancelled" |
| COMPLETED | Attempt to cancel | Error: "COMPLETED booking cannot be cancelled" |
| COMPLETED | Attempt to confirm | Error: "Only PENDING booking can be confirmed" |
| CONFIRMED | Attempt to confirm | Error: "Only PENDING booking can be confirmed" |

### Expected Result
- Appropriate error message returned
- Booking status unchanged
- No invalid state transitions allowed

### Evidence Placeholder
- Screenshot: `docs/screenshots/acc-009-invalid-transition.png`

---

## Scenario 10: Non-Existing User Returns Error

| Field | Value |
|-------|-------|
| **Scenario ID** | ACC-010 |
| **Title** | Query for non-existing user returns proper error |
| **Priority** | Medium |
| **Module** | Customer/Specialist Views |

### Actor
System (automatic validation)

### Preconditions
1. Customer ID 9999 does not exist
2. Specialist ID 9999 does not exist

### Test Cases

| Query | Expected Result |
|-------|-----------------|
| GET /api/bookings/customer/9999 | 404 "Customer not found" |
| GET /api/bookings/specialist/9999 | 404 "Specialist not found" |

### Expected Result
- HTTP 200 with success=false
- Message: "Customer/Specialist not found"
- No empty list returned for non-existing users

### Evidence Placeholder
- Screenshot: `docs/screenshots/acc-010-customer-not-found.png`
- Screenshot: `docs/screenshots/acc-010-specialist-not-found.png`

---

## Acceptance Criteria Summary

| Scenario | Criteria | Status |
|----------|----------|--------|
| ACC-001 | Customer can book available slot | |
| ACC-002 | Manager can confirm pending bookings | |
| ACC-003 | Specialist can complete confirmed booking | |
| ACC-004 | Customer can cancel non-completed booking | |
| ACC-005 | Duplicate booking is prevented | |
| ACC-006 | Admin can view all system data | |
| ACC-007 | Customer sees only their bookings | |
| ACC-008 | Specialist sees only their bookings | |
| ACC-009 | Invalid transitions are rejected | |
| ACC-010 | Non-existing users return error | |

---

## Test Sign-Off

| Role | Name | Date | Result |
|------|------|------|--------|
| Customer Booking | Student D | | PASS/FAIL |
| Booking Workflow | Student D | | PASS/FAIL |
| Customer Views | Student E | | PASS/FAIL |
| Specialist Views | Student E | | PASS/FAIL |
| Admin Views | Student F | | PASS/FAIL |

---

*Document Version: 1.0*
*Last Updated: April 2026*
