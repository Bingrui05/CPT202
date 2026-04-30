# Demo Script

## Project: Consultation Booking System
## Duration: 10 Minutes
## Version: 1.0 (First Release)

---

## Overview

This script provides a structured walkthrough for the live demo presentation. Each section includes timing guidelines, key points, and commands to execute.

---

## Demo Timeline

| Section | Duration | Start Time |
|---------|---------|------------|
| 1. Introduction | 1 min | 0:00 |
| 2. System Overview | 1 min | 1:00 |
| 3. Customer Registration & Login | 1 min | 2:00 |
| 4. Browse Specialists | 0.5 min | 3:00 |
| 5. View Available Slots | 0.5 min | 3:30 |
| 6. Create Booking | 1 min | 4:00 |
| 7. Manager Confirms Booking | 1 min | 5:00 |
| 8. Customer Checks Status | 0.5 min | 6:00 |
| 9. Specialist Views Schedule | 0.5 min | 6:30 |
| 10. Specialist Completes Booking | 0.5 min | 7:00 |
| 11. Error Handling Demo | 1 min | 7:30 |
| 12. Admin Overview | 1 min | 8:30 |
| 13. Closing Summary | 1 min | 9:30 |

---

## Section 1: Introduction (0:00 - 1:00)

### Key Points:
- "Good morning/afternoon, we are Team CPT202"
- "Today we will demonstrate our Consultation Booking System"
- **State the allocated URL clearly:**

> **Demo URL: http://[ALLOCATED_URL]:8080**

### Commands:
```bash
# Verify server is running
curl http://localhost:8080/api/admin/expertise-categories
```

---

## Section 2: System Overview (1:00 - 2:00)

### Key Points:
- "Our system allows customers to book consultations with specialists"
- "Three user roles: Customer, Specialist, Manager"
- "Complete booking lifecycle: Create → Confirm → Complete/Cancel"
- Show system architecture diagram (slide)

### Mention:
- Spring Boot backend
- MySQL database
- RESTful API
- Clean architecture

---

## Section 3: Customer Registration & Login (2:00 - 3:00)

### Step 1: Register New Customer

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testcustomer",
    "email": "test@example.com",
    "password": "password123",
    "role": "CUSTOMER"
  }'
```

### Step 2: Login

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "password123"
  }'
```

### Key Points:
- JWT token returned for authentication
- Password is hashed (never stored in plain text)

---

## Section 4: Browse Specialists (3:00 - 3:30)

### Command:
```bash
curl http://localhost:8080/api/specialists
```

### Expected Response:
```json
{
  "success": true,
  "data": [
    {
      "specialistId": 1,
      "name": "Dr. Smith",
      "category": "Software Engineering",
      "fee": 100.00
    }
  ]
}
```

### Key Points:
- Specialists have expertise categories
- Fee information visible
- No sensitive data exposed

---

## Section 5: View Available Slots (3:30 - 4:00)

### Command:
```bash
curl "http://localhost:8080/api/availability?specialistId=1"
```

### Key Points:
- Filter by specialist
- Shows date, start time, end time
- Status: AVAILABLE / BOOKED

---

## Section 6: Create Booking (4:00 - 5:00)

### Command:
```bash
curl -X POST http://localhost:8080/api/bookings \
  -H "Content-Type: application/json" \
  -d '{
    "customerId": 1,
    "specialistId": 1,
    "slotId": 1,
    "topic": "Career Guidance",
    "notes": "Need advice on software engineering path"
  }'
```

### Expected Response:
```json
{
  "success": true,
  "message": "Booking created successfully",
  "data": {
    "bookingId": 5,
    "status": "PENDING",
    "price": 100.00
  }
}
```

### Key Points:
- Booking created with PENDING status
- Slot status changed to BOOKED
- Cannot book same slot twice

---

## Section 7: Manager Confirms Booking (5:00 - 6:00)

### Command:
```bash
curl -X PUT http://localhost:8080/api/bookings/5/confirm
```

### Expected Response:
```json
{
  "success": true,
  "message": "Booking confirmed",
  "data": {
    "bookingId": 5,
    "status": "CONFIRMED"
  }
}
```

### Key Points:
- Status changed from PENDING to CONFIRMED
- This step requires manager authorization (shown in real system)

---

## Section 8: Customer Checks Booking Status (6:00 - 6:30)

### Command:
```bash
curl http://localhost:8080/api/bookings/customer/1
```

### Key Points:
- Customer sees all their bookings
- Shows complete booking history
- No other customer's data visible

---

## Section 9: Specialist Views Schedule (6:30 - 7:00)

### Command:
```bash
curl http://localhost:8080/api/bookings/specialist/1
```

### Key Points:
- Specialist sees all their bookings
- Includes customer information
- Shows appointment details

---

## Section 10: Specialist Completes Booking (7:00 - 7:30)

### Command:
```bash
curl -X PUT http://localhost:8080/api/bookings/5/complete
```

### Expected Response:
```json
{
  "success": true,
  "message": "Booking completed",
  "data": {
    "bookingId": 5,
    "status": "COMPLETED"
  }
}
```

### Key Points:
- Booking marked as COMPLETED
- Cannot be cancelled after completion
- Full lifecycle demonstrated

---

## Section 11: Error Handling Demo (7:30 - 8:30)

### Demo 1: Duplicate Booking Rejection

```bash
# First booking - succeeds
curl -X POST http://localhost:8080/api/bookings \
  -H "Content-Type: application/json" \
  -d '{"customerId": 2, "specialistId": 1, "slotId": 2, "topic": "Test"}'

# Second booking same slot - fails
curl -X POST http://localhost:8080/api/bookings \
  -H "Content-Type: application/json" \
  -d '{"customerId": 3, "specialistId": 1, "slotId": 2, "topic": "Test2"}'
```

### Expected Error:
```json
{
  "success": false,
  "message": "Slot has already been booked"
}
```

### Demo 2: Invalid Status Transition

```bash
# Try to confirm already confirmed booking
curl -X PUT http://localhost:8080/api/bookings/5/confirm
```

### Expected Error:
```json
{
  "success": false,
  "message": "Only PENDING booking can be confirmed"
}
```

### Key Points:
- System prevents invalid operations
- Clear error messages returned
- Data integrity maintained

---

## Section 12: Admin Overview (8:30 - 9:30)

### Commands:

```bash
# View expertise categories
curl http://localhost:8080/api/admin/expertise-categories

# View levels
curl http://localhost:8080/api/admin/levels

# View all bookings
curl http://localhost:8080/api/admin/bookings
```

### Key Points:
- Admin has read-only access to all data
- Master data management
- Booking overview
- No sensitive data exposed

### Database Evidence:

```bash
mysql -u root -p consultation_booking -e "SELECT * FROM bookings;"
```

### Key Points:
- Show actual database records
- Demonstrate data persistence
- Verify all fields stored correctly

---

## Section 13: Closing Summary (9:30 - 10:00)

### Key Points:
- "We have demonstrated the complete booking workflow"
- "All CRUD operations working correctly"
- "Proper validation and error handling"
- "Clean architecture with separation of concerns"
- "Future enhancements: frontend, notifications, payments"

### Thank You:
> "Thank you for watching. We welcome any questions."

---

## Backup Commands

If anything fails during the demo:

```bash
# Restart application
mvn spring-boot:run

# Check database
mysql -u root -p consultation_booking -e "SELECT * FROM bookings;"

# Verify data
mysql -u root -p consultation_booking -e "SELECT * FROM availability_slots;"
```

---

## Demo Data Reference

| ID | Entity | Data |
|----|--------|------|
| 1 | Customer | John Doe |
| 2 | Customer | Jane Smith |
| 1 | Specialist | Dr. Smith |
| 2 | Specialist | Prof. Johnson |
| 1 | Slot | 2026-05-01 10:00 |
| 2 | Slot | 2026-05-01 11:00 |
| 1 | Category | Software Engineering |
| 2 | Category | Data Science |
| 1 | Level | Junior |
| 2 | Level | Senior |

---

## Notes for Presenters

1. **URL**: State at the beginning and write on whiteboard
2. **Timing**: Practice to stay within 10 minutes
3. **Commands**: Have commands ready to copy-paste
4. **Backup**: Keep screenshots as fallback
5. **Confidence**: Explain what you're doing as you demo

---

*Document Version: 1.0*
*Last Updated: April 2026*
