# Conflict and Error Handling Test Documentation

This document lists all conflict test cases for the Consultation Booking System, organized by module.

## Testing Setup

1. Start the application: `mvn spring-boot:run`
2. Access the UI: http://localhost:8080/index.html
3. Open browser developer console for additional debugging
4. Monitor the Debug Panel at the bottom of the page for request/response details

---

## 1. Auth Module (Login/Register)

### 1.1 Login Tests

| Test ID | Scenario | Request | Expected Error |
|---------|----------|---------|---------------|
| AUTH-001 | Login with blank username | POST /api/auth/login with `username: ""` | "Username is required" |
| AUTH-002 | Login with blank password | POST /api/auth/login with `password: ""` | "Password is required" |
| AUTH-003 | Login with wrong password | POST /api/auth/login with correct username, wrong password | "Invalid username or password" |
| AUTH-004 | Login with non-existent user | POST /api/auth/login with unknown username | "Invalid username or password" |
| AUTH-005 | Login with inactive account | Login as inactive user | "Account is inactive" |

### 1.2 Registration Tests

| Test ID | Scenario | Request | Expected Error |
|---------|----------|---------|---------------|
| REG-001 | Register with blank username | Submit registration with empty username | "Username is required" |
| REG-002 | Register with short username | Submit with username < 3 chars | "Username must be at least 3 characters long" |
| REG-003 | Register with blank password | Submit with empty password | "Password is required" |
| REG-004 | Register with short password | Submit with password < 6 chars | "Password must be at least 6 characters long" |
| REG-005 | Register with blank email | Submit with empty email | "Email is required" |
| REG-006 | Register with invalid email | Submit with email lacking @ or . | "Invalid email format" |
| REG-007 | Register duplicate username | Submit with existing username | "Username already exists" |
| REG-008 | Register duplicate email | Submit with existing email | "Email already exists" |

### 1.3 Register Profile Creation

| Test ID | Scenario | Expected Behavior |
|---------|----------|------------------|
| REG-009 | Register CUSTOMER | Auto-creates linked Customer profile |
| REG-010 | Register MANAGER | Auto-creates linked OperationManager profile |
| REG-011 | Register SPECIALIST | Only User account created, no Specialist profile auto-created |

---

## 2. Specialist Search

| Test ID | Scenario | Expected Result |
|---------|----------|-----------------|
| SPC-001 | Search with no results | "Found 0 specialist(s)" with empty table message |
| SPC-002 | Search with onlyAvailable | "Found 0 specialist(s)" |

---

## 3. Specialist CRUD

| Test ID | Scenario | Expected Error |
|---------|----------|---------------|
| SPC-CRUD-001 | Create specialist with non-existent user | "User not found" |
| SPC-CRUD-002 | Create specialist with non-SPECIALIST user | "User must have SPECIALIST role" |
| SPC-CRUD-003 | Create specialist with non-existent category | "Category not found" |
| SPC-CRUD-004 | Create specialist with inactive category | "Cannot assign inactive category to specialist" |
| SPC-CRUD-005 | Create specialist with non-existent level | "Level not found" |
| SPC-CRUD-006 | Create specialist with negative fee | "Fee must be positive" (if validated) |
| SPC-CRUD-007 | Update non-existent specialist | "Specialist not found" |
| SPC-CRUD-008 | Deactivate non-existent specialist | "Specialist not found" |
| SPC-CRUD-009 | Deactivate specialist with active bookings | "Specialist has active bookings and cannot be deactivated" |
| SPC-CRUD-010 | Update specialist with inactive category | "Cannot assign inactive category to specialist" |

---

## 4. Expertise Category CRUD

| Test ID | Scenario | Expected Error |
|---------|----------|---------------|
| CAT-001 | Create with blank name | "Name is required" |
| CAT-002 | Create with duplicate name | "Category name already exists" |
| CAT-003 | Update non-existent category | "Category not found" |
| CAT-004 | Deactivate non-existent category | "Category not found" |
| CAT-005 | Deactivate already inactive category | "Category is already inactive" |

---

## 5. Availability Slot CRUD

| Test ID | Scenario | Expected Error |
|---------|----------|---------------|
| SLOT-001 | Create with non-existent specialist | "Specialist not found" |
| SLOT-002 | Create with inactive specialist | "Specialist is not active" |
| SLOT-003 | Create with blank date | "Date is required" |
| SLOT-004 | Create with startTime >= endTime | "Start time must be before end time" |
| SLOT-005 | Create duplicate/overlapping slot | "Slot time overlaps with an existing slot for this specialist" |
| SLOT-006 | Update non-existent slot | "Slot not found" |
| SLOT-007 | Update BOOKED slot time | "Cannot modify date or time for a booked slot" |
| SLOT-008 | Update slot to overlapping time | "Slot time overlaps with an existing slot for this specialist" |
| SLOT-009 | Delete BOOKED slot | "Booked slot cannot be deactivated" |
| SLOT-010 | Delete slot with active booking | "Slot is occupied by an active booking and cannot be deactivated" |
| SLOT-011 | Delete non-existent slot | "Slot not found" |

---

## 6. Booking Create

| Test ID | Scenario | Expected Error |
|---------|----------|---------------|
| BOOK-001 | No customerId | "This customer account does not have a linked Customer profile" |
| BOOK-002 | No specialist selected | "Please select a specialist first" |
| BOOK-003 | No slot selected | "Please select a slot first" |
| BOOK-004 | Blank topic | "Topic is required" |
| BOOK-005 | Customer not found | "Customer not found" |
| BOOK-006 | Specialist not found | "Specialist not found" |
| BOOK-007 | Slot not found | "Slot not found" |
| BOOK-008 | Slot wrong specialist | "Slot does not belong to the selected specialist" |
| BOOK-009 | Slot not available | "Slot is not available" |
| BOOK-010 | Slot occupied by active booking | "Slot is already occupied by an active booking" |

### 6.1 Slot Reusable Template Rule

| Test ID | Scenario | Expected Behavior |
|---------|----------|------------------|
| BOOK-011 | Book slot with COMPLETED booking | Should SUCCEED (slot released after completion) |
| BOOK-012 | Book slot with CANCELLED booking | Should SUCCEED (slot released after cancellation) |
| BOOK-013 | Book slot with PENDING booking | Should FAIL with "Slot is already occupied" |
| BOOK-014 | Book slot with CONFIRMED booking | Should FAIL with "Slot is already occupied" |

---

## 7. Booking Workflow

### 7.1 Confirm Booking (Manager)

| Test ID | Scenario | Expected Error |
|---------|----------|---------------|
| CONF-001 | Confirm non-PENDING booking | "Only PENDING booking can be confirmed" |
| CONF-002 | Confirm non-existent booking | "Booking not found" |
| CONF-003 | Confirm already confirmed | "Only PENDING booking can be confirmed" |

### 7.2 Cancel Booking (Customer/Manager)

| Test ID | Scenario | Expected Error |
|---------|----------|---------------|
| CANC-001 | Cancel COMPLETED booking | "COMPLETED booking cannot be cancelled" |
| CANC-002 | Cancel already CANCELLED booking | "Booking has already been cancelled" |
| CANC-003 | Cancel non-existent booking | "Booking not found" |
| CANC-004 | Cancel PENDING/CONFIRMED booking | Success - slot becomes AVAILABLE |

### 7.3 Complete Booking (Specialist)

| Test ID | Scenario | Expected Error |
|---------|----------|---------------|
| COMP-001 | Complete non-CONFIRMED booking | "Only CONFIRMED booking can be completed" |
| COMP-002 | Complete non-existent booking | "Booking not found" |
| COMP-003 | Complete CONFIRMED booking | Success - slot becomes AVAILABLE |

---

## 8. Booking Reschedule

| Test ID | Scenario | Expected Error |
|---------|----------|---------------|
| RES-001 | Reschedule without selecting booking | "Please select a booking to reschedule" |
| RES-002 | Reschedule without selecting new slot | "Please select a new slot" |
| RES-003 | Reschedule COMPLETED booking | "COMPLETED booking cannot be rescheduled" |
| RES-004 | Reschedule CANCELLED booking | "CANCELLED booking cannot be rescheduled" |
| RES-005 | New slot not found | "Slot not found" |
| RES-006 | New slot different specialist | "New slot does not belong to the same specialist" |
| RES-007 | New slot not AVAILABLE | "New slot is not available" |
| RES-008 | New slot occupied by active booking | "New slot is already occupied by an active booking" |

### 8.1 Reschedule Slot Management

| Test ID | Scenario | Expected Behavior |
|---------|----------|------------------|
| RES-009 | Reschedule to available slot | Success - old slot AVAILABLE, new slot BOOKED |
| RES-010 | Reschedule with PENDING booking | Success - booking status becomes PENDING |

---

## 9. Frontend Auto-Refresh Tests

| Test ID | Scenario | Expected Behavior |
|---------|----------|-------------------|
| REFRESH-001 | Login | Clear previous data, show new role's sections |
| REFRESH-002 | Logout | Clear all selections and tables |
| REFRESH-003 | Create booking success | Refresh My Bookings, refresh available slots |
| REFRESH-004 | Cancel booking success | Refresh My Bookings, refresh available slots |
| REFRESH-005 | Complete booking success | Refresh Specialist schedule |
| REFRESH-006 | Create category success | Refresh category list |
| REFRESH-007 | Deactivate category success | Refresh category list |
| REFRESH-008 | Confirm booking success | Refresh All Bookings list |
| REFRESH-009 | Deactivate specialist success | Refresh specialist list |

---

## 10. Frontend Stale Data After Role Switch

| Test ID | Scenario | Expected Behavior |
|---------|----------|-------------------|
| STALE-001 | Login as Customer, then logout | Clear customer tables, show login |
| STALE-002 | Login as Customer, then login as Manager | Clear customer data, show manager sections |
| STALE-003 | Login as Specialist, then login as Customer | Clear specialist data, show customer sections |

---

## Test Execution Log

| Date | Tester | Tests Run | Passed | Failed | Notes |
|------|--------|-----------|--------|--------|-------|
| | | | | | |

---

## Business Rules Summary

### Slot Reusable Template Rule
- Slots are treated as reusable fixed consultation time templates
- PENDING and CONFIRMED bookings occupy a slot
- CANCELLED and COMPLETED bookings are historical records (do not block slot)
- When booking is completed or cancelled, slot becomes AVAILABLE

### Register Profile Creation
- CUSTOMER: Auto-creates linked Customer profile
- MANAGER: Auto-creates linked OperationManager profile
- SPECIALIST: Only User account (specialist profile created by manager later)

### Category Deactivate Rule
- Deactivating category sets status to INACTIVE
- Existing specialists using that category are NOT modified
- Creating/updating specialist with INACTIVE category is REJECTED

### Specialist Deactivate Rule
- Soft delete: sets status to INACTIVE
- REJECTED if specialist has PENDING or CONFIRMED bookings
- ALLOWED if only CANCELLED or COMPLETED bookings exist

### Slot Deactivate Rule
- Soft delete: sets status to UNAVAILABLE
- BOOKED slots CANNOT be deactivated
- Slots with active bookings (PENDING/CONFIRMED) CANNOT be deactivated

### Slot Time Conflict Rule
- Same specialist + same date + overlapping time = REJECTED
- Applies to both create and update operations
- Update excludes current slot from conflict check

---

## Known Issues / Limitations

- Add any known issues discovered during testing

---

## Screenshots

| Test ID | Screenshot |
|---------|-----------|
| | |
