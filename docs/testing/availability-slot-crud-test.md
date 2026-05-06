# Availability Slot CRUD Test Record

## Module
Availability Slot CRUD Management

## Branch
feature/availability-slot-crud

## Updated Endpoint
CRUD operations for availability slots

## Supported Endpoints
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /api/slots | Create a new availability slot |
| GET | /api/slots/{slotId} | Get slot by ID |
| PUT | /api/slots/{slotId} | Update an existing slot |
| DELETE | /api/slots/{slotId} | Deactivate a slot (soft delete) |
| GET | /api/slots/specialist/{specialistId} | Get all slots for a specialist |
| GET | /api/slots/specialist/{specialistId}/available | Get available slots for a specialist |

## Request Fields
- specialistId
- date
- startTime
- endTime
- status (AVAILABLE, BOOKED, UNAVAILABLE)

---

## Test Case 1: Get slot by ID

Example:
GET /api/slots/{slotId}

Expected result:
- Returns the slot details with slotId, specialistId, specialistName, date, startTime, endTime, and status.

Actual result:
- To be completed during testing.

Screenshot:
- docs/testing/screenshots/slot-get-by-id.png

---

## Test Case 2: Update available slot time successfully

Example:
PUT /api/slots/{slotId}
Request Body:
```json
{
    "date": "2026-05-10",
    "startTime": "09:00",
    "endTime": "10:00"
}
```

Expected result:
- Slot is updated with new date and time.
- Status remains AVAILABLE.

Actual result:
- To be completed during testing.

Screenshot:
- docs/testing/screenshots/slot-update-time-success.png

---

## Test Case 3: Update slot status successfully

Example:
PUT /api/slots/{slotId}
Request Body:
```json
{
    "status": "UNAVAILABLE"
}
```

Expected result:
- Slot status is updated to UNAVAILABLE.
- Other fields remain unchanged.

Actual result:
- To be completed during testing.

Screenshot:
- docs/testing/screenshots/slot-update-status-success.png

---

## Test Case 4: Reject startTime after or equal to endTime

Example:
PUT /api/slots/{slotId}
Request Body:
```json
{
    "startTime": "14:00",
    "endTime": "10:00"
}
```

Expected result:
- System returns error: "Start time must be before end time"
- HTTP 400 Bad Request

Actual result:
- To be completed during testing.

Screenshot:
- docs/testing/screenshots/slot-update-time-error.png

---

## Test Case 5: Reject updating non-existing slot

Example:
PUT /api/slots/99999
Request Body:
```json
{
    "date": "2026-05-10"
}
```

Expected result:
- System returns error: "Slot not found"
- HTTP 404 Not Found

Actual result:
- To be completed during testing.

Screenshot:
- docs/testing/screenshots/slot-update-not-found.png

---

## Test Case 6: Reject updating booked slot time

Example:
PUT /api/slots/{slotId} (where slot has status=BOOKED)
Request Body:
```json
{
    "date": "2026-05-15",
    "startTime": "11:00",
    "endTime": "12:00"
}
```

Expected result:
- System returns error: "Cannot modify date or time for a booked slot"
- HTTP 400 Bad Request

Actual result:
- To be completed during testing.

Screenshot:
- docs/testing/screenshots/slot-update-booked-error.png

---

## Test Case 7: Deactivate available slot successfully

Example:
DELETE /api/slots/{slotId} (where slot has status=AVAILABLE)

Expected result:
- Slot status is changed to UNAVAILABLE.
- Slot is not hard deleted from database.

Actual result:
- To be completed during testing.

Screenshot:
- docs/testing/screenshots/slot-delete-success.png

---

## Test Case 8: Reject deleting booked slot

Example:
DELETE /api/slots/{slotId} (where slot has status=BOOKED)

Expected result:
- System returns error: "Cannot delete a booked slot"
- HTTP 400 Bad Request
- Slot remains with status BOOKED.

Actual result:
- To be completed during testing.

Screenshot:
- docs/testing/screenshots/slot-delete-booked-error.png

---

## Test Case 9: Verify deactivated slot status is UNAVAILABLE

Example:
1. Create a slot with status AVAILABLE
2. DELETE /api/slots/{slotId}
3. GET /api/slots/{slotId}

Expected result:
- Slot status is UNAVAILABLE.
- Slot data is preserved (date, time, specialist info).

Actual result:
- To be completed during testing.

Screenshot:
- docs/testing/screenshots/slot-deactivated-verify.png

---

## Test Case 10: Create slot with all fields

Example:
POST /api/slots
Request Body:
```json
{
    "specialistId": 1,
    "date": "2026-05-10",
    "startTime": "09:00",
    "endTime": "10:00",
    "status": "AVAILABLE"
}
```

Expected result:
- Slot is created successfully.
- Returns slot details with generated slotId.

Actual result:
- To be completed during testing.

Screenshot:
- docs/testing/screenshots/slot-create-success.png

---

## Test Case 11: Get all slots by specialist

Example:
GET /api/slots/specialist/{specialistId}

Expected result:
- Returns all slots for the specialist (including AVAILABLE, BOOKED, UNAVAILABLE).

Actual result:
- To be completed during testing.

Screenshot:
- docs/testing/screenshots/slot-get-by-specialist.png

---

## Test Case 12: Get available slots by specialist

Example:
GET /api/slots/specialist/{specialistId}/available

Expected result:
- Returns only slots with status=AVAILABLE.

Actual result:
- To be completed during testing.

Screenshot:
- docs/testing/screenshots/slot-get-available-by-specialist.png

---

## Business Rules Summary

1. Slot must exist before update/delete.
2. Specialist must exist.
3. startTime must be before endTime.
4. status can be AVAILABLE, BOOKED, or UNAVAILABLE.
5. BOOKED slot cannot have date/startTime/endTime changed.
6. BOOKED slot cannot be deleted/deactivated directly.
7. DELETE should set status to UNAVAILABLE for non-booked slots (soft delete).
8. Available slot query should only return AVAILABLE slots.
9. Do not expose password or sensitive user data in responses.

---

## Notes for Group Report

The availability slot CRUD module allows staff to manage consultation slots for specialists. Staff can create new slots, view existing slots, update slot information (subject to business rules), and deactivate slots when needed. Booked slots are protected from modification and deletion to maintain booking integrity.
