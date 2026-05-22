# Specialist CRUD Test Documentation

## Overview
This document outlines the test scenarios for the Specialist CRUD functionality in the consultation booking system.

## Test Accounts

| Role | Username | Password |
|------|----------|----------|
| Operation Manager | manager1 | password123 |
| Specialist | specialist1 | password123 |
| Specialist | specialist2 | password123 |
| Customer | customer1 | password123 |

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/specialists` | Create a new specialist |
| GET | `/api/specialists` | Get all specialists |
| GET | `/api/specialists/{id}` | Get specialist by ID |
| GET | `/api/specialists/search` | Search specialists |
| PUT | `/api/specialists/{id}` | Update specialist |
| DELETE | `/api/specialists/{id}` | Deactivate specialist |

---

## Test Scenarios

### 1. Create Specialist Successfully

**Precondition:** A user with SPECIALIST role exists.

**Request:**
```http
POST /api/specialists
Content-Type: application/json

{
    "userId": 4,
    "categoryId": 1,
    "levelId": 2,
    "fee": 150.00,
    "status": "ACTIVE",
    "information": "Experienced software architect specializing in cloud solutions"
}
```

**Expected Response:**
```json
{
    "success": true,
    "message": "Specialist created successfully",
    "data": {
        "specialistId": 6,
        "userId": 4,
        "username": "specialist_new",
        "email": "specialist_new@example.com",
        "categoryId": 1,
        "categoryName": "Software Engineering",
        "levelId": 2,
        "levelName": "Senior",
        "status": "ACTIVE",
        "fee": 150.00,
        "information": "Experienced software architect specializing in cloud solutions"
    }
}
```

---

### 2. Get Specialist by ID

**Request:**
```http
GET /api/specialists/1
```

**Expected Response:**
```json
{
    "success": true,
    "message": "Specialist retrieved successfully",
    "data": {
        "specialistId": 1,
        "userId": 3,
        "username": "specialist1",
        "email": "specialist1@example.com",
        "categoryId": 1,
        "categoryName": "Software Engineering",
        "levelId": 2,
        "levelName": "Senior",
        "status": "ACTIVE",
        "fee": 150.00,
        "information": "Senior software architect with 10+ years of experience..."
    }
}
```

---

### 3. Update Specialist Category Successfully

**Request:**
```http
PUT /api/specialists/1
Content-Type: application/json

{
    "categoryId": 3
}
```

**Expected Response:**
```json
{
    "success": true,
    "message": "Specialist updated successfully",
    "data": {
        "specialistId": 1,
        "userId": 3,
        "username": "specialist1",
        "email": "specialist1@example.com",
        "categoryId": 3,
        "categoryName": "Career Consulting",
        "levelId": 2,
        "levelName": "Senior",
        "status": "ACTIVE",
        "fee": 150.00,
        "information": "Senior software architect with 10+ years of experience..."
    }
}
```

---

### 4. Update Specialist Level Successfully

**Request:**
```http
PUT /api/specialists/1
Content-Type: application/json

{
    "levelId": 3
}
```

**Expected Response:**
```json
{
    "success": true,
    "message": "Specialist updated successfully",
    "data": {
        "specialistId": 1,
        "categoryId": 1,
        "categoryName": "Software Engineering",
        "levelId": 3,
        "levelName": "Expert",
        ...
    }
}
```

---

### 5. Update Specialist Fee Successfully

**Request:**
```http
PUT /api/specialists/1
Content-Type: application/json

{
    "fee": 200.00
}
```

**Expected Response:**
```json
{
    "success": true,
    "message": "Specialist updated successfully",
    "data": {
        "specialistId": 1,
        "fee": 200.00,
        ...
    }
}
```

---

### 6. Update Specialist Information/Profile Successfully

**Request:**
```http
PUT /api/specialists/1
Content-Type: application/json

{
    "information": "Updated: Expert in distributed systems, Kubernetes, and cloud-native architectures. 15+ years of experience leading engineering teams at Fortune 500 companies."
}
```

**Expected Response:**
```json
{
    "success": true,
    "message": "Specialist updated successfully",
    "data": {
        "specialistId": 1,
        "information": "Updated: Expert in distributed systems, Kubernetes, and cloud-native architectures...",
        ...
    }
}
```

---

### 7. Reject Negative Fee

**Request:**
```http
PUT /api/specialists/1
Content-Type: application/json

{
    "fee": -50.00
}
```

**Expected Response:**
```json
{
    "success": false,
    "message": "Fee cannot be negative"
}
```

---

### 8. Reject Non-existing Category

**Request:**
```http
PUT /api/specialists/1
Content-Type: application/json

{
    "categoryId": 999
}
```

**Expected Response:**
```json
{
    "success": false,
    "message": "Category not found"
}
```

---

### 9. Reject Non-existing Level

**Request:**
```http
PUT /api/specialists/1
Content-Type: application/json

{
    "levelId": 999
}
```

**Expected Response:**
```json
{
    "success": false,
    "message": "Level not found"
}
```

---

### 10. Deactivate Specialist Successfully

**Request:**
```http
DELETE /api/specialists/1
```

**Expected Response:**
```json
{
    "success": true,
    "message": "Specialist deactivated successfully"
}
```

---

### 11. Verify Deactivated Specialist Status is INACTIVE

**Request:**
```http
GET /api/specialists/1
```

**Expected Response:**
```json
{
    "success": true,
    "message": "Specialist retrieved successfully",
    "data": {
        "specialistId": 1,
        "status": "INACTIVE",
        ...
    }
}
```

---

### 12. Verify Enriched Demo Specialists Can Be Browsed

**Request:**
```http
GET /api/specialists
```

**Expected Response:** List of all specialists including the 5 enriched demo specialists:
- specialist1 (Software Engineering, Senior)
- specialist2 (Data Science, Junior)
- career_specialist (Career Consulting, Senior)
- finance_specialist (Finance Consulting, Expert)
- academic_specialist (Academic Planning, Senior)

---

### 13. Verify Specialist Profile Information

**Request:**
```http
GET /api/specialists/1
```

**Expected Response:** Response includes rich `information` field with:
- Professional background
- Consulting area
- Experience details
- Suitable customer needs

---

### 14. Search Specialists by Keyword

**Request:**
```http
GET /api/specialists/search?keyword=software
```

**Expected Response:** Returns specialists matching "software" in username, email, category, level, or information.

---

### 15. Search Specialists by Category

**Request:**
```http
GET /api/specialists/search?categoryId=1
```

**Expected Response:** Returns all specialists in Software Engineering category.

---

### 16. Search Specialists with Availability

**Request:**
```http
GET /api/specialists/search?onlyAvailable=true
```

**Expected Response:** Returns only specialists with at least one AVAILABLE slot.

---

## Actual Results

| Test Case | Result | Notes |
|-----------|--------|-------|
| Create specialist successfully | TBD | |
| Get specialist by ID | TBD | |
| Update specialist category | TBD | |
| Update specialist level | TBD | |
| Update specialist fee | TBD | |
| Update specialist information | TBD | |
| Reject negative fee | TBD | |
| Reject non-existing category | TBD | |
| Reject non-existing level | TBD | |
| Deactivate specialist | TBD | |
| Verify deactivated status | TBD | |
| Browse enriched demo specialists | TBD | |
| Search by keyword | TBD | |
| Search by category | TBD | |
| Search with availability | TBD | |

---

## Screenshots

### Create Specialist
![Create Specialist Screenshot](./screenshots/create-specialist.png)

### Get Specialist by ID
![Get Specialist Screenshot](./screenshots/get-specialist.png)

### Update Specialist
![Update Specialist Screenshot](./screenshots/update-specialist.png)

### Deactivate Specialist
![Deactivate Specialist Screenshot](./screenshots/deactivate-specialist.png)

### Browse All Specialists
![Browse Specialists Screenshot](./screenshots/browse-specialists.png)

### Search Specialists
![Search Specialists Screenshot](./screenshots/search-specialists.png)
