# Deployment Log Template

## Project: Consultation Booking System
## Version: 1.0 (First Release)

---

## Purpose

This log documents all deployment activities, issues encountered, and resolutions. Use this template for each deployment or significant system change.

---

## Log Structure

| Field | Description |
|-------|-------------|
| **Date/Time** | When the deployment occurred |
| **Operator** | Who performed the deployment |
| **Branch/Commit** | Source code version |
| **Environment** | Target environment (dev/demo/prod) |
| **Action** | What was done |
| **Result** | Success/failure status |
| **Issue Found** | Any problems encountered |
| **Resolution** | How issues were resolved |
| **Evidence** | Screenshots or log file paths |

---

## Deployment Log

| Date/Time | Operator | Branch/Commit | Environment | Action | Result | Issue Found | Resolution | Evidence |
|-----------|----------|---------------|-------------|--------|--------|-------------|------------|----------|
| 2026-04-15 10:00 | Student A | rebuild-release1/a1b2c3d | Development | Initial setup | SUCCESS | None | N/A | logs/init-2026-04-15.png |
| 2026-04-16 14:30 | Student A | rebuild-release1/e5f6g7h8 | Development | Auth module deployment | SUCCESS | None | N/A | logs/auth-deploy-2026-04-16.png |
| 2026-04-17 09:00 | Student B | rebuild-release1/i9j0k1l2 | Development | Specialist module deployment | SUCCESS | DB connection timeout | Increased connection pool | logs/specialist-deploy-2026-04-17.png |
| 2026-04-18 11:00 | Student C | rebuild-release1/m3n4o5p6 | Development | Availability module deployment | SUCCESS | None | N/A | logs/availability-deploy-2026-04-18.png |
| 2026-04-19 15:00 | Student D | rebuild-release1/q7r8s9t0 | Development | Booking core deployment | SUCCESS | Duplicate booking bug | BUG-001 fixed | logs/booking-deploy-2026-04-19.png |
| 2026-04-20 10:00 | Student D | rebuild-release1/u1v2w3x4 | Development | Booking workflow deployment | SUCCESS | Status transition bug | BUG-002 fixed | logs/workflow-deploy-2026-04-20.png |
| 2026-04-21 14:00 | Student E | rebuild-release1/y5z6a7b8 | Development | Customer/Specialist views deployment | SUCCESS | Missing validation | BUG-003 fixed | logs/views-deploy-2026-04-21.png |
| 2026-04-22 16:00 | Student F | rebuild-release1/c9d0e1f2 | Development | Admin module deployment | SUCCESS | None | N/A | logs/admin-deploy-2026-04-22.png |
| 2026-04-25 09:00 | All Team | rebuild-release1/g3h4i5j6 | Demo | Pre-presentation deployment | SUCCESS | Port conflict | Changed to 8081 | logs/demo-prep-2026-04-25.png |
| 2026-04-30 08:00 | Student A | rebuild-release1/k7l8m9n0 | Demo | Final presentation deployment | SUCCESS | None | N/A | logs/final-demo-2026-04-30.png |

---

## Issue Details

### Issue 1: Database Connection Timeout

| Field | Value |
|-------|-------|
| **Date** | 2026-04-17 |
| **Module** | Specialist Module |
| **Error** | `CommunicationsException: Communications link failure` |
| **Root Cause** | MySQL connection timeout too short |
| **Resolution** | Updated HikariCP settings in application.properties |
| **Changes Made** | `spring.datasource.hikari.connection-timeout=30000` |

### Issue 2: Port Conflict

| Field | Value |
|-------|-------|
| **Date** | 2026-04-25 |
| **Module** | System |
| **Error** | `Address already in use (Bind failed)` |
| **Root Cause** | Another application using port 8080 |
| **Resolution** | Changed application port to 8081 |
| **Changes Made** | `server.port=8081` |

---

## Template for New Entries

Copy this template for new deployment log entries:

```
| YYYY-MM-DD HH:MM | Name | branch/commit | Environment | Action description | SUCCESS/FAILURE | Issue description (if any) | Resolution (if applicable) | screenshot-path.png |
```

### Example Entry:

```
| 2026-04-30 10:00 | Student A | rebuild-release1/abc123 | Demo | Final release deployment | SUCCESS | None | N/A | logs/demo-final.png |
```

---

## Environment Definitions

| Environment | Purpose | Access |
|-------------|---------|--------|
| Development | Local development | Developer only |
| Testing | Integration testing | All team members |
| Demo | Presentation | Public (allocated URL) |
| Production | Live system | Admin only |

---

## Deployment Commands Reference

| Action | Command |
|--------|---------|
| Clean build | `mvn clean` |
| Run tests | `mvn test` |
| Package | `mvn package -DskipTests` |
| Start application | `mvn spring-boot:run` |
| Check logs | `tail -f logs/spring.log` |
| Stop application | `pkill -f spring-boot` |

---

## Verification Checklist

After each deployment, verify:

- [ ] Application starts successfully
- [ ] Health endpoint responds
- [ ] Database connection works
- [ ] Core APIs return expected responses
- [ ] No errors in logs
- [ ] Screenshots captured

---

## Sign-Off

| Deployment | Operator | Date | Reviewer | Status |
|------------|----------|------|----------|--------|
| Initial Setup | Student A | 2026-04-15 | Team Lead | APPROVED |
| Auth Module | Student A | 2026-04-16 | Team Lead | APPROVED |
| Specialist Module | Student B | 2026-04-17 | Team Lead | APPROVED |
| Availability Module | Student C | 2026-04-18 | Team Lead | APPROVED |
| Booking Core | Student D | 2026-04-19 | Team Lead | APPROVED |
| Booking Workflow | Student D | 2026-04-20 | Team Lead | APPROVED |
| Views Module | Student E | 2026-04-21 | Team Lead | APPROVED |
| Admin Module | Student F | 2026-04-22 | Team Lead | APPROVED |
| Pre-Presentation | All Team | 2026-04-25 | Team Lead | APPROVED |
| Final Presentation | Student A | 2026-04-30 | Team Lead | APPROVED |

---

## Notes

_Use this space for additional notes about deployments._

---

*Document Version: 1.0*
*Last Updated: April 2026*
