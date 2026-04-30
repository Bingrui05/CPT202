# Release Checklist

## Project: Consultation Booking System
## Version: 1.0 (First Release)

---

## Purpose

This checklist ensures all components are verified before release and presentation.

---

## Pre-Release Checklist

### 1. Code Compilation

- [ ] All source code compiles without errors
- [ ] No compilation warnings
- [ ] Run: `mvn clean compile`

```
Expected: BUILD SUCCESS
```

---

### 2. Tests Pass

- [ ] All unit tests pass
- [ ] All integration tests pass
- [ ] Run: `mvn clean test`

```
Expected: Tests run: XX, Failures: 0, Errors: 0, Skipped: 0
```

---

### 3. Database Preparation

- [ ] MySQL is running
- [ ] Database `consultation_booking` exists
- [ ] Database `consultation_booking_test` exists
- [ ] Flyway migrations applied successfully
- [ ] Test data loaded

```bash
mysql -u root -p -e "SHOW DATABASES;"
```

---

### 4. Application Starts Successfully

- [ ] Application starts on port 8080
- [ ] No startup errors
- [ ] Run: `mvn spring-boot:run`

```
Expected: Started ConsultationBookingApplication in X.XXX seconds
```

---

### 5. Health Endpoint Verification

- [ ] GET /api/admin/expertise-categories returns 200
- [ ] GET /api/admin/levels returns 200
- [ ] GET /api/admin/bookings returns 200

```bash
curl http://localhost:8080/api/admin/expertise-categories
curl http://localhost:8080/api/admin/levels
curl http://localhost:8080/api/admin/bookings
```

---

### 6. Core APIs Verified

| Module | Endpoint | Method | Expected |
|--------|----------|--------|----------|
| Auth | /api/auth/login | POST | 200 OK |
| Specialist | /api/specialists | GET | 200 OK |
| Specialist | /api/specialists | POST | 201 Created |
| Availability | /api/availability | GET | 200 OK |
| Availability | /api/availability | POST | 201 Created |
| Booking | /api/bookings | POST | 201 Created |
| Booking | /api/bookings/{id}/confirm | PUT | 200 OK |
| Booking | /api/bookings/{id}/cancel | PUT | 200 OK |
| Booking | /api/bookings/{id}/complete | PUT | 200 OK |
| Customer View | /api/bookings/customer/{id} | GET | 200 OK |
| Specialist View | /api/bookings/specialist/{id} | GET | 200 OK |
| Admin | /api/admin/expertise-categories | GET | 200 OK |
| Admin | /api/admin/levels | GET | 200 OK |
| Admin | /api/admin/bookings | GET | 200 OK |

- [ ] All endpoints return correct status codes
- [ ] Response format is consistent (ApiResponse)
- [ ] No password or sensitive data exposed

---

### 7. Demo Data Prepared

- [ ] At least 2 expertise categories exist
- [ ] At least 2 levels exist
- [ ] At least 2 specialists exist
- [ ] At least 2 customers exist
- [ ] At least 5 availability slots exist
- [ ] At least 3 bookings in various states exist

**Sample Data:**

```sql
-- Verify data exists
SELECT COUNT(*) FROM expertise_categories;
SELECT COUNT(*) FROM levels;
SELECT COUNT(*) FROM users WHERE role='SPECIALIST';
SELECT COUNT(*) FROM users WHERE role='CUSTOMER';
SELECT COUNT(*) FROM availability_slots;
SELECT COUNT(*) FROM bookings;
```

---

### 8. Screenshots Collected

#### Customer Module
- [ ] Login success screenshot
- [ ] Customer dashboard screenshot

#### Specialist Module
- [ ] Specialist list screenshot
- [ ] Specialist profile screenshot

#### Availability Module
- [ ] Slot creation screenshot
- [ ] Available slots screenshot

#### Booking Module
- [ ] Booking creation screenshot
- [ ] Booking confirmation screenshot
- [ ] Booking cancellation screenshot
- [ ] Booking completion screenshot

#### Error Handling
- [ ] Duplicate booking rejection screenshot
- [ ] Invalid status transition screenshot

#### Admin Module
- [ ] Admin categories screenshot
- [ ] Admin levels screenshot
- [ ] Admin bookings screenshot

---

### 9. Documentation Complete

- [ ] README.md updated
- [ ] Testing documentation complete
- [ ] Deployment documentation complete
- [ ] API documentation available
- [ ] User guide available

---

### 10. Source Code Package Prepared

**Step 1: Clean Build Artifacts**

```bash
mvn clean
```

**Step 2: Verify .gitignore is Correct**

```gitignore
# Build output
target/

# IDE files
.idea/
*.iml
.vscode/

# Logs
*.log
logs/

# Local config (contains passwords)
application-local.properties
*.local.properties
.env

# OS files
.DS_Store
Thumbs.db

# Other
*.bak
*.swp
```

**Step 3: Create Release Package**

```bash
# Create zip without target folder
git archive -o consultation-booking-v1.0.zip rebuild-release1 --prefix=consultation-booking/ -- . ':!target/'

# Or manually:
# 1. Copy project to new folder
# 2. Delete target/ folder
# 3. Delete .git folder
# 4. Zip the clean folder
```

**Step 4: Verify Package Contents**

```
consultation-booking/
├── src/
│   ├── main/
│   └── test/
├── docs/
├── pom.xml
├── README.md
└── .gitignore
```

- [ ] No compiled binaries (.class, .jar)
- [ ] No target/ folder
- [ ] No local config files with passwords
- [ ] Source code is clean

---

## Presentation Day Checklist

### Before Presentation

- [ ] Laptop fully charged
- [ ] Presentation slides ready
- [ ] All screenshots loaded
- [ ] Live demo environment tested
- [ ] Backup slides/screenshots ready
- [ ] Timer ready

### During Presentation

- [ ] Show allocated URL early
- [ ] Follow demo script
- [ ] Stay within time limit
- [ ] Explain validation/error cases
- [ ] Show database evidence
- [ ] Prepare for Q&A

### After Presentation

- [ ] Document any issues in deployment log
- [ ] Update final screenshots
- [ ] Complete presentation feedback

---

## Sign-Off

| Checkpoint | Name | Date | Signature |
|------------|------|------|-----------|
| Code Review | | | |
| Testing Complete | | | |
| Documentation Review | | | |
| Demo Ready | | | |
| Final Approval | | | |

---

## Notes

_Use this space for any additional notes or issues encountered._

---

*Document Version: 1.0*
*Last Updated: April 2026*
