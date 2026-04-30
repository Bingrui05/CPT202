# Environment Configuration

## Project: Consultation Booking System
## Version: 1.0 (First Release)

---

## Overview

This document outlines the environment configurations for different stages of the project lifecycle.

---

## 1. Development Environment

### Hardware Requirements

| Component | Minimum | Recommended |
|-----------|---------|-------------|
| RAM | 4 GB | 8 GB |
| Disk Space | 2 GB | 5 GB |
| CPU | Dual-core | Quad-core |

### Software Requirements

| Software | Version | Purpose |
|----------|---------|---------|
| Java | 17+ | Runtime |
| Maven | 3.6+ | Build tool |
| MySQL | 8.0+ | Database |
| IDE | - | IntelliJ IDEA / VS Code |
| Git | 2.0+ | Version control |

### Local Development Setup

```
┌─────────────────────────────────────────────┐
│           Developer Workstation              │
├─────────────────────────────────────────────┤
│  OS: macOS / Windows / Linux                │
│  Java 17+                                   │
│  Maven 3.6+                                 │
│  MySQL 8.0+                                 │
│  IDE: IntelliJ IDEA / VS Code                │
└─────────────────────────────────────────────┘
           │
           │ localhost:3306
           ▼
┌─────────────────────────────────────────────┐
│           MySQL Server                       │
│  Database: consultation_booking              │
│  Port: 3306                                 │
└─────────────────────────────────────────────┘
```

---

## 2. Database Environment

### MySQL Configuration

**Connection Settings:**

| Setting | Development Value | Production Value |
|---------|------------------|------------------|
| Host | localhost | [PRODUCTION_HOST] |
| Port | 3306 | 3306 |
| Database | consultation_booking | consultation_booking |
| Username | root | [DB_USER] |
| Password | [LOCAL_PWD] | [SECURE_PWD] |

**Database Schema:**

```
Tables:
├── users
│   ├── user_id (PK)
│   ├── username
│   ├── email
│   ├── password_hash
│   └── role
├── customers
│   ├── customer_id (PK)
│   ├── user_id (FK)
│   └── ...
├── specialists
│   ├── specialist_id (PK)
│   ├── user_id (FK)
│   ├── fee
│   ├── level_id (FK)
│   └── ...
├── expertise_categories
│   ├── category_id (PK)
│   └── ...
├── levels
│   ├── level_id (PK)
│   └── ...
├── availability_slots
│   ├── slot_id (PK)
│   ├── specialist_id (FK)
│   └── ...
├── bookings
│   ├── booking_id (PK)
│   ├── customer_id (FK)
│   ├── specialist_id (FK)
│   ├── slot_id (FK)
│   └── ...
└── operation_managers
    ├── manager_id (PK)
    └── ...
```

---

## 3. Backend Runtime Environment

### Application Properties

**File Location:** `src/main/resources/application.properties`

```properties
# Application Name
spring.application.name=consultation-booking

# Server Configuration
server.port=8080
server.servlet.context-path=/

# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/consultation_booking
spring.datasource.username=root
spring.datasource.password=${DB_PASSWORD}
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA / Hibernate Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect

# Flyway Configuration
spring.flyway.enabled=true
spring.flyway.baseline-on-migrate=true
spring.flyway.locations=classpath:db/migration

# Logging Configuration
logging.level.org.springframework.web=INFO
logging.level.com.cpt202=DEBUG
logging.level.org.hibernate.SQL=DEBUG
```

### Environment Variables

| Variable | Description | Required |
|----------|-------------|----------|
| `DB_PASSWORD` | MySQL password | Yes |
| `DB_HOST` | MySQL host | No (default: localhost) |
| `DB_PORT` | MySQL port | No (default: 3306) |
| `SERVER_PORT` | Application port | No (default: 8080) |

### Runtime Profiles

**Development (default):**
```bash
mvn spring-boot:run
```

**Production:**
```bash
java -jar -Dspring.profiles.active=production target/consultation-booking-1.0.0.jar
```

**application-prod.properties:**
```properties
spring.datasource.url=jdbc:mysql://${DB_HOST}:${DB_PORT}/consultation_booking
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false
```

---

## 4. Production/Demo Environment (Placeholder)

### Production Environment Configuration

| Item | Placeholder |
|------|-------------|
| Server URL | https://consultation-system.example.com |
| Database Host | [TO_BE_CONFIGURED] |
| Database Port | 3306 |
| Application Port | 8080 |
| SSL Enabled | Yes |
| Backup Enabled | Yes |

### Demo Environment for Presentation

| Item | Value |
|------|-------|
| Server URL | [ALLOCATED_DEMO_URL] |
| Database Host | localhost |
| Application Port | 8080 |
| Demo Data | Pre-loaded |

---

## 5. Security Configuration

### Password Management

**CRITICAL: Never commit real passwords to version control.**

#### Method 1: Environment Variables (Recommended)

```bash
# Linux/macOS
export DB_PASSWORD="your_secure_password"

# Windows (Command Prompt)
set DB_PASSWORD=your_secure_password

# Windows (PowerShell)
$env:DB_PASSWORD = "your_secure_password"
```

#### Method 2: External Configuration File

Create `application-local.properties`:

```properties
# This file is NOT committed to git
spring.datasource.password=my_real_password
```

Add to `application.properties`:

```properties
spring.config.import=optional:file:./application-local.properties
```

#### Method 3: Maven Profile

```bash
mvn spring-boot:run -Ddb.password=your_password
```

### What NOT to Commit

```
# .gitignore
application-local.properties
*.local.properties
*.secret.properties
credentials.json
.env
*.env
```

### What CAN be Committed

- `application.properties` with placeholder values
- Database schema (Flyway migrations)
- Configuration templates

---

## 6. Configuration Summary Table

| Config Item | Default | Dev | Test | Production |
|-------------|---------|-----|------|------------|
| Port | 8080 | 8080 | 8080 | 8080 |
| DDL Auto | update | update | none | validate |
| Show SQL | true | true | false | false |
| Flyway | true | true | false | true |
| Log Level | INFO | DEBUG | WARN | INFO |

---

## 7. Troubleshooting

### Database Connection Issues

```bash
# Test MySQL connection
mysql -u root -p -h localhost -P 3306

# Check MySQL is running
mysqladmin -u root -p ping

# Check port availability
lsof -i :3306
```

### Application Won't Start

```bash
# Check Java version
java -version

# Check Maven
mvn -version

# Clean and rebuild
mvn clean package
```

---

*Document Version: 1.0*
*Last Updated: April 2026*
