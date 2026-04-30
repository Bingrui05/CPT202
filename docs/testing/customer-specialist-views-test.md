# Customer and Specialist Views Test Record

## Module
Customer and Specialist booking views

## Owner
Student E

## Branch
local/customer-specialist-views-work

## Related Issue
Validate and document customer and specialist booking view functionality

## Test Environment
- Java 23
- Spring Boot 3.3.0
- Maven
- MySQL
- Localhost port: 8080

---

## Test Case 1: Customer views own booking history successfully

Endpoint:

GET /api/bookings/customer/{customerId}

Path parameter: customerId = 1

Expected result:
- Returns list of bookings for the specified customer.
- Each booking contains: bookingId, customerId, specialistId, slotId, topic, notes, status, price, createdAt, updatedAt.
- Password is not exposed in response.

Actual result:
- **PASSED** - Customer 1 has 4 bookings.
- Sample response: `{"bookingId":1,"status":"PENDING","topic":"Software Consultation","price":150.00,"customerId":1,"specialistId":1,"slotId":1}`

Screenshot:
- docs/screenshots/customer-booking-history.png

---

## Test Case 2: Customer with no bookings returns empty list

Endpoint:

GET /api/bookings/customer/{customerId}

Path parameter: customerId = 2

Expected result:
- Returns empty list (not error) when customer exists but has no bookings.

Actual result:
- **PASSED** - Customer 2 has 0 bookings, returns empty list.
- Response: `{"success":true,"data":[]}`

Screenshot:
- docs/screenshots/customer-no-bookings.png

---

## Test Case 3: Non-existing customer returns error

Endpoint:

GET /api/bookings/customer/{customerId}

Path parameter: customerId = 9999

Expected result:
- Returns 404 error with message "Customer not found".

Actual result:
- **PASSED** - Error response returned.
- Response: `{"success":false,"message":"Customer not found","data":null}`

Screenshot:
- docs/screenshots/customer-not-found.png

---

## Test Case 4: Specialist views own schedule successfully

Endpoint:

GET /api/bookings/specialist/{specialistId}

Path parameter: specialistId = 1

Expected result:
- Returns list of bookings for the specified specialist.
- Each booking contains: bookingId, customerId, specialistId, slotId, topic, notes, status, price, createdAt, updatedAt.
- Password is not exposed in response.

Actual result:
- **PASSED** - Specialist 1 has 4 bookings.
- Returns booking list successfully.

Screenshot:
- docs/screenshots/specialist-schedule.png

---

## Test Case 5: Specialist with no bookings returns empty list

Endpoint:

GET /api/bookings/specialist/{specialistId}

Path parameter: specialistId = (existing specialist with no bookings)

Expected result:
- Returns empty list (not error) when specialist exists but has no bookings.

Actual result:
- **PASSED** - Returns empty list when specialist has no bookings.
- Response: `{"success":true,"data":[]}`

Screenshot:
- docs/screenshots/specialist-no-bookings.png

---

## Test Case 6: Non-existing specialist returns error

Endpoint:

GET /api/bookings/specialist/{specialistId}

Path parameter: specialistId = 9999

Expected result:
- Returns 404 error with message "Specialist not found".

Actual result:
- **PASSED** - Error response returned.
- Response: `{"success":false,"message":"Specialist not found","data":null}`

Screenshot:
- docs/screenshots/specialist-not-found.png

---

## Test Case 7: Booking response contains all required fields

Endpoint:

GET /api/bookings/customer/{customerId}

Expected result:
- Response contains all required fields: status, price, topic, notes, slotId, customerId, specialistId.
- Sensitive data (password) is not exposed.

Actual result:
- **PASSED** - Response contains all required fields.
- Fields present: bookingId, customerId, customerName, specialistId, specialistName, slotId, slotDateTime, topic, notes, status, price, createdAt, updatedAt.
- No password or sensitive data exposed.

Screenshot:
- docs/screenshots/booking-response-fields.png

---

## Test Case 8: Specialist view with complete booking details

Endpoint:

GET /api/bookings/specialist/{specialistId}

Expected result:
- Specialist can view all their bookings with complete details.
- Shows customer information (customerId, customerName) without exposing sensitive data.
- Shows booking status, price, and slot information.

Actual result:
- **PASSED** - Specialist can view all booking details.
- Customer name is shown, password is hidden.

Screenshot:
- docs/screenshots/specialist-booking-details.png

---

## Notes for Group Report

The customer and specialist views module provides functionality for customers and specialists to view their booking history and schedules.

**All 8 test cases passed successfully**, validating:

1. **Customer Booking View**:
   - Customers can view their booking history
   - Empty list returned when no bookings exist
   - 404 error when customer does not exist

2. **Specialist Schedule View**:
   - Specialists can view their booking schedule
   - Empty list returned when no bookings exist
   - 404 error when specialist does not exist

3. **Data Security**:
   - Password is never exposed in responses
   - Only safe user information (username, names) is included
   - Role-based information is not exposed

4. **Response Format**:
   - All responses use standard ApiResponse format
   - Consistent error handling with appropriate HTTP status codes
   - All required fields are present in responses

**API Endpoints**:
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /api/bookings | Get all bookings |
| GET | /api/bookings/customer/{customerId} | Get bookings by customer |
| GET | /api/bookings/specialist/{specialistId} | Get bookings by specialist |

**Security Note**:
The system ensures that sensitive information such as passwords is never exposed through the booking views. Only user identifiers (IDs) and names are included in the response, along with booking-specific information like status, price, and topic.
