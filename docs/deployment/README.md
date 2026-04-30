# Deployment Documentation

## Project: Consultation Booking System
## Folder: docs/deployment/
## Version: 1.0 (First Release)

---

## Purpose of This Folder

This folder (`docs/deployment/`) contains all deployment-related documentation for the Consultation Booking System. It provides guidance for local development, environment setup, release preparation, and presentation demonstrations.

---

## Document List

| Document | Description |
|---------|-------------|
| `README.md` | This file - overview and guide |
| `local-run-guide.md` | Step-by-step guide for running the application locally |
| `environment.md` | Environment configuration and setup details |
| `release-checklist.md` | Pre-release verification checklist |
| `demo-script.md` | 10-minute presentation demo script |
| `deployment-log-template.md` | Template for documenting deployment activities |

---

## How These Documents Support the Group Report

### Chapter 6: Deployment and Operations

```
6.1 Development Environment Setup
    - Reference: local-run-guide.md, environment.md

6.2 Database Configuration
    - Reference: local-run-guide.md (database section)
    - Reference: environment.md (database environment)

6.3 Build and Deployment Process
    - Reference: local-run-guide.md (build commands)
    - Reference: release-checklist.md

6.4 Demo Preparation
    - Reference: demo-script.md

6.5 Deployment Log
    - Reference: deployment-log-template.md
```

---

## How These Documents Support the Presentation

| Presentation Section | Supporting Document |
|---------------------|-------------------|
| Demo Setup | local-run-guide.md |
| Environment Config | environment.md |
| Pre-Demo Checklist | release-checklist.md |
| Live Demo Script | demo-script.md |
| Deployment Evidence | deployment-log-template.md |

---

## Quick Start for Presenters

1. **Before Presentation:**
   - Complete all items in `release-checklist.md`
   - Verify application starts successfully
   - Test all demo endpoints
   - Prepare screenshots for backup

2. **During Presentation:**
   - Follow `demo-script.md` for the live demo
   - Keep `local-run-guide.md` open for reference
   - Document any issues in `deployment-log-template.md`

3. **After Presentation:**
   - Update deployment log with any issues encountered
   - Collect final screenshots
   - Prepare release package

---

## Branch Information

| Branch | Purpose |
|--------|---------|
| `main` | Production/release branch |
| `rebuild-release1` | Current development branch |
| `local/*` | Individual feature branches |

---

*Document Version: 1.0*
*Last Updated: April 2026*
