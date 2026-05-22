# Testing Plan

## Project: Consultation Booking System
## Module: Testing Evidence
## Version: 1.0 (First Release)

---

## 1. Testing Objectives

The primary objectives of testing for this consultation booking system are:

1. **Functional Correctness**: Verify that all system components function as specified in the requirements
2. **Data Integrity**: Ensure that all database operations maintain data consistency
3. **API Compliance**: Validate that all REST API endpoints return correct responses
4. **Error Handling**: Confirm appropriate error messages are returned for invalid inputs
5. **Security**: Ensure sensitive data (passwords) are never exposed
6. **Workflow Validation**: Test end-to-end booking workflows

---

## 2. Testing Scope

### In Scope
- All REST API endpoints
- Service layer business logic
- Repository layer data access
- Booking state transitions
- User validation (customer, specialist, manager)
- Resource existence validation

### Out of Scope
- Frontend UI testing
- Performance/load testing
- Security penetration testing
- Database backup/recovery
- Third-party integration

---

## 3. Unit Testing

### 3.1 Service Layer Tests

| Test Class | Test Method | Description |
|------------|-------------|-------------|
| BookingServiceTest | createBooking_Success | Valid booking creation |
| BookingServiceTest | createBooking_CustomerNotFound | Customer validation |
| BookingServiceTest | createBooking_SpecialistNotFound | Specialist validation |
| BookingServiceTest | createBooking_SlotNotAvailable | Slot availability check |
| BookingServiceTest | confirmBooking_Success | Booking confirmation |
| BookingServiceTest | cancelBooking_Success | Booking cancellation |
| BookingServiceTest | completeBooking_Success | Booking completion |

### 3.2 Repository Tests

| Test Class | Test Method | Description |
|------------|-------------|-------------|
| BookingRepositoryTest | findByCustomerId | Customer booking query |
| BookingRepositoryTest | findBySpecialistId | Specialist booking query |
| BookingRepositoryTest | existsBySlot | Duplicate booking check |

### 3.3 Validation Rules

| Rule | Test Scenario | Expected Result |
|------|---------------|-----------------|
| Customer must exist | Create booking with invalid customer ID | ResourceNotFoundException |
| Specialist must exist | Create booking with invalid specialist ID | ResourceNotFoundException |
| Slot must be available | Book an already booked slot | BusinessException |
| Only PENDING can be confirmed | Confirm an already confirmed booking | BusinessException |
| Only CONFIRMED can be completed | Complete a PENDING booking | BusinessException |
| Cannot cancel COMPLETED | Cancel a completed booking | BusinessException |

---

## 4. Integration Testing

### 4.1 API Integration Tests

| Module | Endpoint | Test Scenario |
|--------|----------|---------------|
| Auth | POST /api/auth/register | User registration |
| Auth | POST /api/auth/login | User login |
| Specialist | GET /api/specialists | List all specialists |
| Availability | POST /api/availability | Create availability slot |
| Booking | POST /api/bookings | Create booking |
| Booking | PUT /api/bookings/{id}/confirm | Confirm booking |
| Booking | PUT /api/bookings/{id}/cancel | Cancel booking |
| Customer Views | GET /api/bookings/customer/{id} | Customer booking history |
| Specialist Views | GET /api/bookings/specialist/{id} | Specialist schedule |
| Admin | GET /api/admin/bookings | Admin booking overview |

### 4.2 Database Integration

- Verify data persists correctly after CRUD operations
- Validate foreign key relationships
- Test transaction rollback on failure

---

## 5. Acceptance Testing

### 5.1 User Story Acceptance Criteria

| User Story | Acceptance Criteria |
|------------|-------------------|
| Customer Registration | User can register with valid data |
| Specialist Management | Manager can add/view specialists |
| Availability Setup | Specialist can create time slots |
| Booking Creation | Customer can book available slot |
| Booking Confirmation | Manager can confirm pending bookings |
| Booking Cancellation | Customer can cancel non-completed bookings |
| Booking Completion | Specialist can mark booking as completed |
| View Booking History | Customers see their bookings |
| View Schedule | Specialists see their appointments |
| Admin Overview | Admin can view all bookings |

### 5.2 End-to-End Scenarios

See: `acceptance-test-scenarios.md`

---

## 6. Test Environment

### 6.1 Development Environment

| Component | Version |
|-----------|---------|
| Java | 23 |
| Spring Boot | 3.3.0 |
| Maven | 3.x |
| MySQL | 8.x |
| Tomcat | Embedded |

### 6.2 Test Database

- Separate test database instance
- Flyway migration scripts for test data
- Pre-populated with sample customers, specialists, and slots

### 6.3 Test Configuration

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/consultation_booking_test
spring.jpa.hibernate.ddl-auto=validate
```

---

## 7. Test Data Strategy

### 7.1 Test Data Categories

1. **Valid Data**: Correctly formatted, existing references
2. **Invalid Data**: Wrong format, non-existing IDs
3. **Boundary Data**: Edge cases (empty lists, single items)
4. **State Data**: Different booking statuses (PENDING, CONFIRMED, CANCELLED, COMPLETED)

### 7.2 Sample Test Data

| Entity | Field | Test Values |
|--------|-------|-------------|
| Customer | customerId | 1, 2, 9999 (non-existing) |
| Specialist | specialistId | 1, 2, 9999 (non-existing) |
| Slot | slotId | 1, 2, 3 |
| Booking | status | PENDING, CONFIRMED, CANCELLED, COMPLETED |

---

## 8. Defect Tracking Process

### 8.1 Bug Workflow

```
NEW → CONFIRMED → IN PROGRESS → RESOLVED → VERIFIED → CLOSED
```

### 8.2 Severity Levels

| Level | Description | Example |
|-------|-------------|---------|
| Critical | System unusable | Cannot start application |
| High | Major function broken | Cannot create bookings |
| Medium | Function impaired | Validation messages unclear |
| Low | Minor issue | UI formatting |

### 8.3 Bug Log

See: `bug-log.md`

---

## 9. How Testing Supports the First Release

### 9.1 Quality Assurance

- **Code Coverage**: All service methods have unit tests
- **API Coverage**: All endpoints are tested
- **Workflow Coverage**: All booking state transitions validated

### 9.2 Risk Mitigation

| Risk | Mitigation |
|------|-----------|
| Data loss | Transaction rollback tests |
| Invalid bookings | Validation tests |
| Security breach | Password exposure checks |
| State inconsistency | Workflow tests |

### 9.3 Release Criteria

- [ ] All unit tests pass
- [ ] All integration tests pass
- [ ] No critical/high severity bugs open
- [ ] API documentation complete
- [ ] Test evidence documented

---

## 10. Test Execution Schedule

| Phase | Activities | Timeline |
|-------|------------|----------|
| Unit Testing | Service layer tests | Week 1-2 |
| Integration Testing | API endpoint tests | Week 2-3 |
| System Testing | End-to-end workflows | Week 3-4 |
| Acceptance Testing | User story validation | Week 4 |
| Documentation | Evidence collection | Week 4-5 |

---

## 11. Roles and Responsibilities

| Role | Responsibilities |
|------|------------------|
| Student A (Auth) | Auth module testing |
| Student B (Specialist) | Specialist management testing |
| Student C (Availability) | Availability slots testing |
| Student D (Booking Core) | Booking CRUD testing |
| Student E (Customer/Specialist Views) | View endpoints testing |
| Student F (Admin) | Admin module testing |

---

*Document Version: 1.0*
*Last Updated: April 2026*
