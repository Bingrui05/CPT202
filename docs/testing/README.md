# Testing Evidence Documentation

## Project: Consultation Booking System
## Folder: docs/testing/
## Version: 1.0 (First Release)

---

## Purpose of This Folder

This folder (`docs/testing/`) contains comprehensive testing documentation for the Consultation Booking System. It serves as the central repository for all testing evidence, bug records, and test artifacts.

### Contents

| File | Description |
|------|-------------|
| `testing-plan.md` | Overall testing strategy and approach |
| `api-test-checklist.md` | Detailed API endpoint test cases |
| `bug-log.md` | Defect tracking and bug history |
| `acceptance-test-scenarios.md` | End-to-end user scenarios |
| `README.md` | This file - guide to the testing documents |
| `screenshots/` | Folder for test evidence screenshots |

---

## How to Use These Documents

### For Developers

1. **Before implementing a feature:**
   - Review `testing-plan.md` for testing requirements
   - Check `api-test-checklist.md` for expected behaviors
   - Reference `acceptance-test-scenarios.md` for user stories

2. **During development:**
   - Use checklists to verify implementations
   - Document bugs in `bug-log.md`
   - Take screenshots for evidence

3. **After implementation:**
   - Run through the relevant test cases
   - Update actual results in checklists
   - Verify all acceptance criteria

### For Testers

1. **Execute tests according to:**
   - `api-test-checklist.md` - API endpoint testing
   - `acceptance-test-scenarios.md` - End-to-end scenarios

2. **Document findings:**
   - Fill in actual results in checklists
   - Record pass/fail status
   - Capture screenshots for evidence

3. **Track defects:**
   - Log bugs in `bug-log.md`
   - Update status as bugs are fixed
   - Verify fixes before closing

### For Documentation

1. **Group Report:**
   - Reference testing strategy from `testing-plan.md`
   - Include test case summary from checklists
   - Document bugs and fixes from `bug-log.md`
   - Present acceptance scenarios

2. **Presentation:**
   - Use screenshots from `screenshots/` folder
   - Highlight key test results
   - Demonstrate acceptance scenarios

---

## Screenshot Guidelines

### Naming Convention

Screenshots should be named following this pattern:

```
{type}-{number}-{description}.png
```

### Types

| Type | Description | Examples |
|------|-------------|----------|
| `api` | API response screenshots | `api-001-login-success.png` |
| `bug` | Bug reproduction/fix | `bug-001-reproduction.png` |
| `acc` | Acceptance test evidence | `acc-001-booking-created.png` |
| `admin` | Admin module screenshots | `admin-expertise-categories.png` |
| `customer` | Customer view screenshots | `customer-booking-history.png` |
| `specialist` | Specialist view screenshots | `specialist-schedule.png` |

### Screenshot Organization

```
docs/testing/
├── README.md
├── testing-plan.md
├── api-test-checklist.md
├── bug-log.md
├── acceptance-test-scenarios.md
└── screenshots/
    ├── api/
    │   ├── api-001-login-success.png
    │   └── api-002-login-failure.png
    ├── bug/
    │   ├── bug-001-reproduction.png
    │   └── bug-001-verification.png
    ├── acc/
    │   ├── acc-001-booking-created.png
    │   └── acc-002-confirmed.png
    └── ...
```

### Required Screenshots

Each module should have the following evidence:

| Module | Required Screenshots |
|--------|---------------------|
| Auth | Login success, login failure, register success |
| Specialist | Create specialist, list specialists, update, delete |
| Availability | Create slot, list slots, delete slot |
| Booking Core | Create booking, confirm, complete, cancel |
| Customer Views | View history, empty history, not found |
| Specialist Views | View schedule, empty schedule, not found |
| Admin | View categories, levels, all bookings |

---

## Supporting the Group Report

### Structure Recommendation

```
4. Testing
   4.1 Testing Strategy
       - Reference: testing-plan.md
   4.2 Unit Testing
       - Reference: testing-plan.md Section 3
   4.3 Integration Testing
       - Reference: testing-plan.md Section 4
   4.4 Acceptance Testing
       - Reference: acceptance-test-scenarios.md
   4.5 API Testing Results
       - Reference: api-test-checklist.md
   4.6 Defect Summary
       - Reference: bug-log.md

5. Test Evidence
   5.1 Test Cases
       - Include summary tables from checklists
   5.2 Bug Reports
       - Reference: bug-log.md
   5.3 Screenshots
       - Include key screenshots with captions
```

### What to Include in the Report

1. **Testing Strategy Overview:**
   - Extract from `testing-plan.md` Section 1-2

2. **Test Case Summary:**
   - Table from `api-test-checklist.md` Test Summary section
   - Pass/fail statistics

3. **Bug Summary:**
   - Extract statistics from `bug-log.md`
   - Highlight key bugs and fixes

4. **Acceptance Testing:**
   - Summarize key scenarios from `acceptance-test-scenarios.md`
   - Include diagrams of user workflows

5. **Evidence:**
   - Select representative screenshots
   - Include before/after for bug fixes

---

## Supporting the Presentation

### Slide Structure Recommendation

| Slide | Content | Source |
|-------|---------|--------|
| 1 | Title: Testing Overview | testing-plan.md |
| 2 | Testing Scope and Objectives | testing-plan.md |
| 3 | Test Architecture | testing-plan.md |
| 4 | API Test Results | api-test-checklist.md |
| 5 | Bug Summary | bug-log.md |
| 6 | Acceptance Test Scenarios | acceptance-test-scenarios.md |
| 7 | Demo Screenshots | screenshots/ folder |

### Demo Recommendations

1. **Live Demo 1:** Book a consultation
   - Show: Specialist availability → Create booking → View in history

2. **Live Demo 2:** Booking workflow
   - Show: Confirm → Complete → View completed

3. **Live Demo 3:** Error handling
   - Show: Invalid status transition, duplicate booking

---

## Maintenance

### Before Submission

1. [ ] All test cases have actual results filled in
2. [ ] All bugs have fix summaries
3. [ ] Screenshots are captured and named correctly
4. [ ] Acceptance criteria are checked
5. [ ] Document version numbers are updated

### Document Version History

| Version | Date | Changes | Author |
|---------|------|---------|--------|
| 1.0 | 2026-04-30 | Initial version | Team |

---

## Contact Information

| Module | Student | Responsibility |
|--------|---------|----------------|
| Auth | Student A | Auth module testing |
| Specialist | Student B | Specialist management testing |
| Availability | Student C | Availability slots testing |
| Booking Core | Student D | Booking CRUD and workflow testing |
| Customer/Specialist Views | Student E | View endpoints testing |
| Admin | Student F | Admin module testing |
| Documentation | All | Evidence collection |

---

## Quick Reference

### Run All Tests
```bash
mvn clean test
```

### Run Specific Test Class
```bash
mvn test -Dtest=BookingServiceTest
```

### Run with Coverage
```bash
mvn test jacoco:report
```

### Start Application
```bash
mvn spring-boot:run
```

### Test Endpoints (curl examples)
```bash
# Get all bookings
curl http://localhost:8080/api/bookings

# Get customer bookings
curl http://localhost:8080/api/bookings/customer/1

# Get specialist schedule
curl http://localhost:8080/api/bookings/specialist/1

# Get admin categories
curl http://localhost:8080/api/admin/expertise-categories
```

---

*Document Version: 1.0*
*Last Updated: April 2026*
