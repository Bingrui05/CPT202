# Specialist Update Request Submission Test

## Purpose

This test document verifies the new **PBI 10.1 Specialist Update Request Submission** feature.

The feature does not replace the existing Specialist CRUD or Availability Slot CRUD functions. Instead, it adds a system-based request and tracking layer. Specialists can submit profile or availability update requests, and the manager can view and process these requests. The official specialist information and availability slots are still updated through the existing manager-side functions.

## Scope

Included:

- Specialist submits an update request.
- The system stores the request with `PENDING` status.
- Manager views all requests.
- Manager views only pending requests.
- Manager marks a pending request as `REVIEWED`.
- Manager rejects a pending request as `REJECTED`.
- Specialist views their own request history.

Not included:

- Specialist directly updates official profile information.
- Specialist directly updates official availability slots.
- The system automatically changes the `specialists` or `availability_slots` tables after review.

## API Endpoints

| Method | Endpoint | Purpose |
|---|---|---|
| POST | `/api/specialist-update-requests` | Submit a specialist update request |
| GET | `/api/specialist-update-requests` | Manager views all update requests |
| GET | `/api/specialist-update-requests/pending` | Manager views pending update requests |
| GET | `/api/specialist-update-requests/{requestId}` | View one request by id |
| GET | `/api/specialist-update-requests/specialist/{specialistId}` | Specialist views own request history |
| PUT | `/api/specialist-update-requests/{requestId}/review` | Manager marks request as reviewed |
| PUT | `/api/specialist-update-requests/{requestId}/reject` | Manager rejects request |

## Test Case 1: Submit Profile Update Request

### Request

```powershell
$body = @{
    specialistId = 1
    requestType = "PROFILE"
    fieldName = "information"
    oldValue = "Current specialist introduction"
    newValue = "Updated specialist introduction with new experience"
    reason = "I want customers to see my updated professional experience."
} | ConvertTo-Json

Invoke-RestMethod `
    -Uri "http://localhost:8080/api/specialist-update-requests" `
    -Method Post `
    -ContentType "application/json" `
    -Body $body
```

### Expected Result

- `success` is `true`.
- Response message is `Specialist update request submitted successfully`.
- Response data contains a new `requestId`.
- Request status is `PENDING`.
- Official specialist information is not changed automatically.

## Test Case 2: Submit Availability Update Request

### Request

```powershell
$body = @{
    specialistId = 1
    requestType = "AVAILABILITY"
    fieldName = "slot"
    oldValue = "2026-05-20 14:00-15:00"
    newValue = "2026-05-21 10:00-11:00"
    reason = "I am unavailable at the original time."
} | ConvertTo-Json

Invoke-RestMethod `
    -Uri "http://localhost:8080/api/specialist-update-requests" `
    -Method Post `
    -ContentType "application/json" `
    -Body $body
```

### Expected Result

- Request is created successfully.
- Request type is `AVAILABILITY`.
- Status is `PENDING`.
- Official availability slot is not changed automatically.

## Test Case 3: Manager Views Pending Requests

### Request

```powershell
Invoke-RestMethod `
    -Uri "http://localhost:8080/api/specialist-update-requests/pending" `
    -Method Get
```

### Expected Result

- Response contains pending requests.
- Recently submitted requests appear in the list.

## Test Case 4: Specialist Views Own Request History

### Request

```powershell
Invoke-RestMethod `
    -Uri "http://localhost:8080/api/specialist-update-requests/specialist/1" `
    -Method Get
```

### Expected Result

- Response contains requests submitted by specialist `1`.
- Request status and manager comment are visible.

## Test Case 5: Manager Marks Request as Reviewed

Replace `{requestId}` with a pending request id.

### Request

```powershell
$body = @{
    managerComment = "Reviewed. Manager will update the official record using the existing management function."
} | ConvertTo-Json

Invoke-RestMethod `
    -Uri "http://localhost:8080/api/specialist-update-requests/{requestId}/review" `
    -Method Put `
    -ContentType "application/json" `
    -Body $body
```

### Expected Result

- Request status changes from `PENDING` to `REVIEWED`.
- `reviewedAt` is recorded.
- Manager comment is saved.

## Test Case 6: Manager Rejects Request

Replace `{requestId}` with a pending request id.

### Request

```powershell
$body = @{
    managerComment = "Rejected because the requested value needs more evidence."
} | ConvertTo-Json

Invoke-RestMethod `
    -Uri "http://localhost:8080/api/specialist-update-requests/{requestId}/reject" `
    -Method Put `
    -ContentType "application/json" `
    -Body $body
```

### Expected Result

- Request status changes from `PENDING` to `REJECTED`.
- `reviewedAt` is recorded.
- Manager comment is saved.
- Original specialist profile or availability data remains unchanged.

## Test Case 7: Invalid Specialist ID

### Request

```powershell
$body = @{
    specialistId = 99999
    requestType = "PROFILE"
    fieldName = "information"
    oldValue = "Old value"
    newValue = "New value"
    reason = "Testing invalid specialist id."
} | ConvertTo-Json

Invoke-RestMethod `
    -Uri "http://localhost:8080/api/specialist-update-requests" `
    -Method Post `
    -ContentType "application/json" `
    -Body $body
```

### Expected Result

- System returns an error.
- Error message indicates that the specialist was not found.

## Design Note

This feature keeps the original manager-controlled design. Specialists do not directly modify public profile data or availability slots. They only submit structured requests, replacing offline phone or message communication with a traceable system workflow.
