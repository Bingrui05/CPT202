# Consultation Booking System

A Spring Boot web application for managing consultation bookings between customers and specialists. Built for CPT202 Assignment 2, First Release.

## Technology Stack

- **Java 21**
- **Spring Boot 3.3.0**
- **Maven**
- **Spring Data JPA / Hibernate**
- **MySQL 8.0**
- **Jakarta Bean Validation**
- **Lombok**

## Project Structure

```
com.cpt202.consultationbooking
├── config          # Configuration classes and data initializer
├── controller      # REST controllers
├── dto/request     # Request DTOs
├── dto/response    # Response DTOs
├── entity          # JPA entities
├── enums           # Enumerations (Role, BookingStatus, SlotStatus)
├── exception       # Global exception handling
├── repository      # JPA repositories
└── service         # Business logic
```

Frontend static files are located at `src/main/resources/static/` and served directly by Spring Boot — no separate web server is required.

## Prerequisites

- Java 21
- Maven 3.6+
- MySQL 8.0+

## Database Setup

Create a MySQL database before running the application:

```sql
CREATE DATABASE consultation_booking CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

Then update `src/main/resources/application.properties` with your credentials:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/consultation_booking?useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
server.port=8080
```

## Running Locally

```bash
# Clone the repository and switch to the release branch
git clone https://github.com/Bingrui05/CPT202.git
cd CPT202
git checkout rebuild-release1

# Build and run
mvn clean package -DskipTests
java -jar target/consultation-booking-1.0.0.jar
```

The application will start at `http://localhost:8080`.

## Demo Accounts

The application seeds the following accounts on first run:

| Role       | Username      | Password     |
|------------|---------------|--------------|
| Manager    | manager1      | password123  |
| Customer   | customer1     | password123  |
| Customer   | customer2     | password123  |
| Specialist | specialist1   | password123  |
| Specialist | specialist2   | password123  |

## Core Booking Workflow

1. **Customer** searches for specialists by category, level, or availability
2. **Customer** selects an available slot and creates a booking (status: `PENDING`)
3. **Manager** reviews and confirms or cancels the booking
4. **Specialist** marks the confirmed appointment as completed (status: `COMPLETED`)
5. Cancelling or completing a booking releases the slot back to `AVAILABLE`

## API Endpoints

### Authentication
- `POST /api/auth/register` — Register a new user
- `POST /api/auth/login` — Login

### Specialists
- `GET /api/specialists` — List all specialists
- `GET /api/specialists/search` — Search by category, level, or keyword
- `GET /api/specialists/{id}` — Get specialist by ID

### Availability Slots
- `POST /api/slots` — Create a slot (Specialist/Manager)
- `GET /api/slots/specialist/{specialistId}` — Get all slots for a specialist
- `GET /api/slots/specialist/{specialistId}/available` — Get available slots only

### Bookings
- `POST /api/bookings` — Create a booking (Customer)
- `GET /api/bookings/customer/{customerId}` — Get bookings by customer
- `GET /api/bookings/specialist/{specialistId}` — Get bookings by specialist
- `PUT /api/bookings/{bookingId}/confirm` — Confirm a booking (Manager)
- `PUT /api/bookings/{bookingId}/cancel` — Cancel a booking
- `PUT /api/bookings/{bookingId}/complete` — Complete a booking (Specialist)

## Business Rules

- A slot cannot be booked by more than one customer at a time
- Only `PENDING` bookings can be confirmed or cancelled
- Only `CONFIRMED` bookings can be completed
- Cancelling or completing a booking returns the slot to `AVAILABLE`
- Booking price is copied from the specialist's fee at the time of booking
- Slot overlap validation prevents a specialist from being double-booked

## Production Deployment

The first release is deployed on Alibaba Cloud ECS at:

```
http://121.43.177.5:8080
```

Deployed branch: `rebuild-release1` (commit `c0838b4`)

The JAR is started in the background using:

```bash
nohup java -jar /root/consultation-booking-1.0.0.jar \
  --spring.config.location=file:/root/application.properties \
  > /root/app.log 2>&1 &
```

See `docs/deployment/` for full deployment documentation.

## Known Limitations (First Release)

- Passwords are stored and compared in plain text — BCrypt hashing will be added in a future release
- No JWT authentication — all endpoints are currently unprotected
- Role assignment is not server-side validated during registration
- Application and database are co-located on a single ECS instance
- No HTTPS configured

## Sample Data

On first run, the `DataInitializer` automatically seeds the following data:

- 2 expertise categories (e.g. Finance, Legal)
- 2 levels (e.g. Junior, Senior)
- 1 manager, 2 customers, 2 specialists
- 5 availability slots across the two specialists

This is sufficient to demonstrate the full booking workflow without any manual setup.

## Running Tests

```bash
mvn clean test
```

Test documentation is maintained under `docs/testing/`.
