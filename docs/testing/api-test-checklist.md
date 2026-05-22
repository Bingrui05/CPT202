# API Test Checklist

## Project: Consultation Booking System
## Module: Comprehensive API Testing
## Version: 1.0 (First Release)

---

## 1. Authentication Module (Auth)

### POST /api/auth/register

| Test Scenario | Expected Result | Actual Result | Pass/Fail | Screenshot |
|---------------|-----------------|---------------|-----------|------------|
| Register new customer with valid data | 201 Created, user registered | | | |
| Register with existing email | 400 Bad Request | | | |
| Register with missing required fields | 400 Bad Request | | | |
| Register with invalid email format | 400 Bad Request | | | |

### POST /api/auth/login

| Test Scenario | Expected Result | Actual Result | Pass/Fail | Screenshot |
|---------------|-----------------|---------------|-----------|------------|
| Login with correct credentials | 200 OK, JWT token returned | | | |
| Login with wrong password | 401 Unauthorized | | | |
| Login with non-existing email | 401 Unauthorized | | | |
| Login with missing credentials | 400 Bad Request | | | |

### GET /api/auth/me (Protected)

| Test Scenario | Expected Result | Actual Result | Pass/Fail | Screenshot |
|---------------|-----------------|---------------|-----------|------------|
| Access with valid token | 200 OK, user info | | | |
| Access without token | 401 Unauthorized | | | |
| Access with invalid token | 401 Unauthorized | | | |

---

## 2. Specialist Management Module

### GET /api/specialists

| Test Scenario | Expected Result | Actual Result | Pass/Fail | Screenshot |
|---------------|-----------------|---------------|-----------|------------|
| Get all specialists | 200 OK, list of specialists | | | |
| Get specialists with valid filter | 200 OK, filtered list | | | |
| Get specialists with invalid filter | 200 OK, empty list | | | |

### GET /api/specialists/{id}

| Test Scenario | Expected Result | Actual Result | Pass/Fail | Screenshot |
|---------------|-----------------|---------------|-----------|------------|
| Get existing specialist | 200 OK, specialist details | | | |
| Get non-existing specialist | 404 Not Found | | | |

### POST /api/specialists

| Test Scenario | Expected Result | Actual Result | Pass/Fail | Screenshot |
|---------------|-----------------|---------------|-----------|------------|
| Create specialist with valid data | 201 Created | | | |
| Create specialist with duplicate userId | 400 Bad Request | | | |
| Create specialist with invalid category | 400 Bad Request | | | |
| Create specialist with negative fee | 400 Bad Request | | | |

### PUT /api/specialists/{id}

| Test Scenario | Expected Result | Actual Result | Pass/Fail | Screenshot |
|---------------|-----------------|---------------|-----------|------------|
| Update specialist with valid data | 200 OK, updated specialist | | | |
| Update non-existing specialist | 404 Not Found | | | |
| Update with invalid fee | 400 Bad Request | | | |

### DELETE /api/specialists/{id}

| Test Scenario | Expected Result | Actual Result | Pass/Fail | Screenshot |
|---------------|-----------------|---------------|-----------|------------|
| Delete existing specialist | 200 OK | | | |
| Delete non-existing specialist | 404 Not Found | | | |

---

## 3. Availability Slots Module

### GET /api/availability

| Test Scenario | Expected Result | Actual Result | Pass/Fail | Screenshot |
|---------------|-----------------|---------------|-----------|------------|
| Get all slots | 200 OK, list of slots | | | |
| Get slots by specialistId | 200 OK, specialist's slots | | | |
| Get slots by date | 200 OK, slots on date | | | |

### GET /api/availability/{id}

| Test Scenario | Expected Result | Actual Result | Pass/Fail | Screenshot |
|---------------|-----------------|---------------|-----------|------------|
| Get existing slot | 200 OK, slot details | | | |
| Get non-existing slot | 404 Not Found | | | |

### POST /api/availability

| Test Scenario | Expected Result | Actual Result | Pass/Fail | Screenshot |
|---------------|-----------------|---------------|-----------|------------|
| Create slot with valid data | 201 Created | | | |
| Create slot for non-existing specialist | 404 Not Found | | | |
| Create overlapping slot | 400 Bad Request | | | |
| Create slot with past date | 400 Bad Request | | | |

### PUT /api/availability/{id}

| Test Scenario | Expected Result | Actual Result | Pass/Fail | Screenshot |
|---------------|-----------------|---------------|-----------|------------|
| Update slot with valid data | 200 OK | | | |
| Update non-existing slot | 404 Not Found | | | |
| Update already booked slot | 400 Bad Request | | | |

### DELETE /api/availability/{id}

| Test Scenario | Expected Result | Actual Result | Pass/Fail | Screenshot |
|---------------|-----------------|---------------|-----------|------------|
| Delete available slot | 200 OK | | | |
| Delete booked slot | 400 Bad Request | | | |
| Delete non-existing slot | 404 Not Found | | | |

---

## 4. Booking Core Module

### GET /api/bookings

| Test Scenario | Expected Result | Actual Result | Pass/Fail | Screenshot |
|---------------|-----------------|---------------|-----------|------------|
| Get all bookings | 200 OK, all bookings | | | |
| Get bookings with filter | 200 OK, filtered list | | | |

### GET /api/bookings/{id}

| Test Scenario | Expected Result | Actual Result | Pass/Fail | Screenshot |
|---------------|-----------------|---------------|-----------|------------|
| Get existing booking | 200 OK, booking details | | | |
| Get non-existing booking | 404 Not Found | | | |

### POST /api/bookings

| Test Scenario | Expected Result | Actual Result | Pass/Fail | Screenshot |
|---------------|-----------------|---------------|-----------|------------|
| Create booking with valid data | 201 Created | | | |
| Create booking for non-existing customer | 404 Not Found | | | |
| Create booking for non-existing specialist | 404 Not Found | | | |
| Create booking for non-existing slot | 404 Not Found | | | |
| Create booking for unavailable slot | 400 Bad Request | | | |
| Create duplicate booking for same slot | 400 Bad Request | | | |
| Create booking with missing fields | 400 Bad Request | | | |

### PUT /api/bookings/{id}/confirm

| Test Scenario | Expected Result | Actual Result | Pass/Fail | Screenshot |
|---------------|-----------------|---------------|-----------|------------|
| Confirm PENDING booking | 200 OK, CONFIRMED | | | |
| Confirm non-existing booking | 404 Not Found | | | |
| Confirm already CONFIRMED booking | 400 Bad Request | | | |
| Confirm CANCELLED booking | 400 Bad Request | | | |

### PUT /api/bookings/{id}/complete

| Test Scenario | Expected Result | Actual Result | Pass/Fail | Screenshot |
|---------------|-----------------|---------------|-----------|------------|
| Complete CONFIRMED booking | 200 OK, COMPLETED | | | |
| Complete non-existing booking | 404 Not Found | | | |
| Complete PENDING booking | 400 Bad Request | | | |
| Complete already COMPLETED booking | 400 Bad Request | | | |

---

## 5. Booking Workflow Module

### Full Booking Lifecycle Tests

| Test Scenario | Expected Result | Actual Result | Pass/Fail | Screenshot |
|---------------|-----------------|---------------|-----------|------------|
| Complete workflow: create → confirm → complete | All statuses correct | | | |
| Complete workflow: create → cancel | Status is CANCELLED | | | |
| Attempt to confirm cancelled booking | 400 Bad Request | | | |
| Attempt to complete cancelled booking | 400 Bad Request | | | |
| Attempt to cancel completed booking | 400 Bad Request | | | |
| Create multiple bookings for same slot | Second booking rejected | | | |

### Edge Cases

| Test Scenario | Expected Result | Actual Result | Pass/Fail | Screenshot |
|---------------|-----------------|---------------|-----------|------------|
| Create booking with past slot | 400 Bad Request | | | |
| Create booking with missing topic | 201 Created (topic optional) | | | |
| Create booking with empty notes | 201 Created | | | |
| Rapid duplicate submissions | Only one booking created | | | |

---

## 6. Customer/Specialist Views Module

### GET /api/bookings/customer/{customerId}

| Test Scenario | Expected Result | Actual Result | Pass/Fail | Screenshot |
|---------------|-----------------|---------------|-----------|------------|
| Get bookings for existing customer | 200 OK, booking list | | | |
| Get bookings for customer with no bookings | 200 OK, empty list | | | |
| Get bookings for non-existing customer | 404 Not Found | | | |
| Response contains required fields | status, price, topic, notes, slotId | | | |
| Password not exposed in response | No password field | | | |

### GET /api/bookings/specialist/{specialistId}

| Test Scenario | Expected Result | Actual Result | Pass/Fail | Screenshot |
|---------------|-----------------|---------------|-----------|------------|
| Get schedule for existing specialist | 200 OK, booking list | | | |
| Get schedule for specialist with no bookings | 200 OK, empty list | | | |
| Get schedule for non-existing specialist | 404 Not Found | | | |
| Response contains required fields | status, price, topic, notes, slotId | | | |
| Password not exposed in response | No password field | | | |

---

## 7. Admin Management Module

### GET /api/admin/expertise-categories

| Test Scenario | Expected Result | Actual Result | Pass/Fail | Screenshot |
|---------------|-----------------|---------------|-----------|------------|
| Get all expertise categories | 200 OK, category list | | | |
| Empty categories list | 200 OK, empty list | | | |
| Response contains categoryId, name, status | All fields present | | | |

### GET /api/admin/levels

| Test Scenario | Expected Result | Actual Result | Pass/Fail | Screenshot |
|---------------|-----------------|---------------|-----------|------------|
| Get all levels | 200 OK, level list | | | |
| Empty levels list | 200 OK, empty list | | | |
| Response contains levelId, name | All fields present | | | |

### GET /api/admin/bookings

| Test Scenario | Expected Result | Actual Result | Pass/Fail | Screenshot |
|---------------|-----------------|---------------|-----------|------------|
| Get all bookings as admin | 200 OK, booking list | | | |
| Empty bookings list | 200 OK, empty list | | | |
| Booking details complete | All fields present | | | |
| No sensitive data exposed | No password fields | | | |

---

## 8. Common API Tests

### Response Format

| Test Scenario | Expected Result | Actual Result | Pass/Fail | Screenshot |
|---------------|-----------------|---------------|-----------|------------|
| Success response has success=true | true | | | |
| Error response has success=false | false | | | |
| Error response has message field | Message present | | | |
| Paginated response has correct structure | Has data array | | | |

### Security

| Test Scenario | Expected Result | Actual Result | Pass/Fail | Screenshot |
|---------------|-----------------|---------------|-----------|------------|
| Password not in any response | Password field absent | | | |
| Invalid token rejected | 401 Unauthorized | | | |
| Expired token rejected | 401 Unauthorized | | | |

---

## Test Summary

| Module | Total Tests | Passed | Failed | Blocked |
|--------|-------------|--------|--------|---------|
| Auth | 10 | | | |
| Specialist Management | 12 | | | |
| Availability Slots | 12 | | | |
| Booking Core | 14 | | | |
| Booking Workflow | 6 | | | |
| Customer/Specialist Views | 10 | | | |
| Admin Management | 9 | | | |
| Common API Tests | 7 | | | |
| **Total** | **80** | | | |

---

## Test Execution Sign-Off

| Role | Name | Date | Signature |
|------|------|------|-----------|
| Auth Module | Student A | | |
| Specialist Module | Student B | | |
| Availability Module | Student C | | |
| Booking Core Module | Student D | | |
| Customer/Specialist Views | Student E | | |
| Admin Module | Student F | | |

---

*Document Version: 1.0*
*Last Updated: April 2026*
