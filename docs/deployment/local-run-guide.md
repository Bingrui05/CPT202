# Local Run Guide

## Project: Consultation Booking System
## Version: 1.0 (First Release)

---

## Prerequisites

Before running the application locally, ensure you have the following installed:

| Requirement | Version | Command to Check |
|-------------|---------|------------------|
| Java | 17 or higher | `java -version` |
| Maven | 3.6 or higher | `mvn -version` |
| MySQL | 8.0 or higher | `mysql --version` |

---

## Step 1: Clone the Repository

```bash
git clone <repository-url>
cd CPT202-rebuild
```

---

## Step 2: Switch to Release Branch

```bash
git checkout rebuild-release1
git pull origin rebuild-release1
```

Verify you are on the correct branch:

```bash
git branch -a
# Should show * rebuild-release1
```

---

## Step 3: Create MySQL Database

1. Log in to MySQL:

```bash
mysql -u root -p
```

2. Create the database:

```sql
CREATE DATABASE consultation_booking;
CREATE DATABASE consultation_booking_test;
```

3. Grant privileges (if needed):

```sql
GRANT ALL PRIVILEGES ON consultation_booking.* TO 'root'@'localhost';
FLUSH PRIVILEGES;
EXIT;
```

---

## Step 4: Configure application.properties

The application configuration is located at:

```
src/main/resources/application.properties
```

Default configuration:

```properties
# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/consultation_booking
spring.datasource.username=root
spring.datasource.password=your_password_here
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect

# Server Configuration
server.port=8080

# Flyway (Database Migration)
spring.flyway.enabled=true
spring.flyway.baseline-on-migrate=true
```

### Important Security Note

**Never commit real passwords to version control.**

Use one of these methods:

1. **Environment Variables (Recommended):**

```properties
spring.datasource.password=${DB_PASSWORD}
```

2. **Separate config file:**

```properties
# application.properties (committed)
spring.datasource.password=

# application-local.properties (not committed)
spring.datasource.password=my_real_password
```

3. **.gitignore the sensitive file:**

```bash
# .gitignore
application-local.properties
```

---

## Step 5: Run Tests

```bash
mvn clean test
```

Expected output:

```
[INFO] Tests run: XX, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

---

## Step 6: Run the Application

### Option A: Development Mode

```bash
mvn spring-boot:run
```

### Option B: Build and Run JAR

```bash
mvn clean package -DskipTests
java -jar target/consultation-booking-1.0.0.jar
```

### Expected Output

```
:: Spring Boot ::                (v3.3.0)

2026-04-30T12:00:00.000+08:00  INFO 12345 --- [main] c.c.c.ConsultationBookingApplication     : Started ConsultationBookingApplication in X.XXX seconds
```

---

## Step 7: Verify Application is Running

### Health Check Endpoint

```bash
curl http://localhost:8080/api/admin/expertise-categories
```

Expected response:

```json
{
  "success": true,
  "message": "Expertise categories retrieved successfully",
  "data": [...]
}
```

### Other Quick Tests

```bash
# Test levels endpoint
curl http://localhost:8080/api/admin/levels

# Test bookings endpoint
curl http://localhost:8080/api/admin/bookings
```

---

## Common Local Run Problems and Solutions

### Problem 1: Port 8080 Already in Use

**Error:**
```
java.net.BindException: Address already in use (Bind failed)
```

**Solution:**
```bash
# Find process using port 8080
lsof -i :8080

# Kill the process
kill -9 <PID>

# Or use a different port in application.properties
server.port=8081
```

---

### Problem 2: Database Connection Failed

**Error:**
```
com.mysql.cj.jdbc.exceptions.CommunicationsException: Communications link failure
```

**Solutions:**
1. Verify MySQL is running:
   ```bash
   mysql -u root -p -e "SELECT 1"
   ```

2. Check credentials in application.properties

3. Verify database exists:
   ```sql
   SHOW DATABASES;
   ```

---

### Problem 3: Maven Build Fails - Java Version Mismatch

**Error:**
```
[ERROR] Source option XX is not supported
```

**Solution:**
```bash
# Check Java version
java -version

# If using Java 8/11, upgrade to Java 17+
# Or update pom.xml to use compatible version
```

---

### Problem 4: Tests Fail - Database Lock

**Error:**
```
Caused by: java.sql.SQLNonTransientConnectionException: Could not connect
```

**Solution:**
```bash
# Ensure no other instance is connected to test database
mysql -u root -p -e "SHOW PROCESSLIST;"
```

---

### Problem 5: Flyway Migration Error

**Error:**
```
org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'flywayInitializer'
```

**Solution:**
```bash
# Clean database and restart
mysql -u root -p -e "DROP DATABASE consultation_booking;"
mysql -u root -p -e "CREATE DATABASE consultation_booking;"

# Run with clean build
mvn clean package
```

---

### Problem 6: Out of Memory

**Error:**
```
java.lang.OutOfMemoryError: Java heap space
```

**Solution:**
```bash
# Increase heap size
export MAVEN_OPTS="-Xmx1024m"
mvn spring-boot:run
```

---

## Development Workflow

```
1. Clone repo
   └─> git clone <url>

2. Create feature branch
   └─> git checkout -b local/feature-name

3. Make changes
   └─> Edit source files

4. Test locally
   └─> mvn clean test

5. Commit changes
   └─> git add . && git commit -m "description"

6. Run full verification
   └─> mvn clean test
   └─> mvn spring-boot:run
   └─> Test endpoints

7. Push to remote
   └─> git push origin local/feature-name
```

---

## Useful Commands

| Command | Description |
|---------|-------------|
| `mvn clean` | Clean build artifacts |
| `mvn test` | Run unit tests |
| `mvn clean test` | Clean and test |
| `mvn package -DskipTests` | Build JAR without tests |
| `mvn spring-boot:run` | Run application |
| `mvn dependency:tree` | View dependencies |
| `mvn help:effective-pom` | View resolved POM |

---

*Document Version: 1.0*
*Last Updated: April 2026*
