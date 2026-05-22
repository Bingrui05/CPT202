# Consultation Booking System

A Spring Boot backend application for managing consultation bookings between customers and specialists.

## Technology Stack

- **Java 23** (compatible with Java 17+)
- **Spring Boot 3.3.0**
- **Spring Boot 3.2.0**
- **Maven**
- **Spring Data JPA**
- **MySQL Driver**
- **Validation (Jakarta Bean Validation)**
- **Lombok**

## Project Structure

```
com.cpt202.consultationbooking
├── config          # Configuration classes
├── controller      # REST controllers
├── dto.request     # Request DTOs
├── dto.response    # Response DTOs
├── entity          # JPA entities
├── enums           # Enumerations
├── exception       # Exception handling
├── repository      # JPA repositories
└── service         # Business logic services
```

## Database Setup

### 1. Create MySQL Database

Before running the application, create a MySQL database named `consultation_booking`:

```sql
CREATE DATABASE consultation_booking CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 2. Configure Database Connection

Edit `src/main/resources/application.properties` with your MySQL credentials:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/consultation_booking?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=YOUR_USERNAME
spring.datasource.password=YOUR_PASSWORD
```

Or copy the example file and modify it:

```bash
cp src/main/resources/application-example.properties src/main/resources/application.properties
```

## Running the Application

### Prerequisites

- Java 17 or higher
- Maven 3.6+
- MySQL 8.0+

### Build and Run

```bash
# Navigate to project directory
cd consultation-booking-system

# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## API Endpoints

### Health Check
- `GET /api/health` - Check if backend is running

### Authentication
- `POST /api/auth/register` - Register a new user
- `POST /api/auth/login` - Login and get user info

### Specialists
- `POST /api/specialists` - Create a new specialist
- `GET /api/specialists` - Get all specialists
- `GET /api/specialists/{id}` - Get specialist by ID
- `GET /api/specialists/search` - Search specialists by category, level, or status

### Availability Slots
- `POST /api/slots` - Create a new slot
- `GET /api/slots/specialist/{specialistId}` - Get all slots for a specialist
- `GET /api/slots/specialist/{specialistId}/available` - Get available slots for a specialist

### Bookings
- `POST /api/bookings` - Create a new booking
- `GET /api/bookings` - Get all bookings
- `GET /api/bookings/customer/{customerId}` - Get bookings by customer
- `GET /api/bookings/specialist/{specialistId}` - Get bookings by specialist
- `PUT /api/bookings/{bookingId}/confirm` - Confirm a booking
- `PUT /api/bookings/{bookingId}/cancel` - Cancel a booking
- `PUT /api/bookings/{bookingId}/complete` - Complete a booking

## First-Release Workflow

### User Roles
1. **CUSTOMER** - Can browse specialists, view slots, and create bookings
2. **SPECIALIST** - Can view their schedule and mark appointments as completed
3. **MANAGER** - Can confirm or cancel bookings

### Booking Workflow

1. **Customer browses specialists**
   - `GET /api/specialists` or `GET /api/specialists/search`

2. **Customer views available slots**
   - `GET /api/slots/specialist/{specialistId}/available`

3. **Customer creates a booking**
   - `POST /api/bookings`
   - Booking status starts as PENDING
   - Price is automatically set from specialist's fee
   - Slot status changes to BOOKED

4. **Manager confirms or cancels the booking**
   - `PUT /api/bookings/{bookingId}/confirm`
   - `PUT /api/bookings/{bookingId}/cancel`
   - Only PENDING bookings can be confirmed
   - COMPLETED bookings cannot be cancelled
   - Cancelled bookings release the slot (status becomes AVAILABLE)

5. **Specialist views their schedule**
   - `GET /api/bookings/specialist/{specialistId}`

6. **Specialist marks appointment as COMPLETED**
   - `PUT /api/bookings/{bookingId}/complete`
   - Only CONFIRMED bookings can be completed

## Business Rules

1. A slot cannot be booked by more than one customer
2. Only CONFIRMED bookings are valid appointments
3. COMPLETED bookings cannot be modified by customers
4. Pricing is calculated consistently from specialist's fee
5. When a booking is created, its price is copied from the specialist's fee
6. When a slot is booked, its status becomes BOOKED

## Sample Data

The application includes a `DataInitializer` that creates sample data on first run:

- 1 manager user (username: `manager1`, password: `password123`)
- 2 customer users (username: `customer1`/`customer2`, password: `password123`)
- 2 specialist users (username: `specialist1`/`specialist2`, password: `password123`)
- 2 expertise categories
- 2 levels
- 5 available slots across specialists

## Testing

Run the test suite:

```bash
mvn clean test
```

## Development Notes

- **Password Storage**: Currently uses plain text comparison. JWT authentication can be added in future releases.
- **Validation**: All request DTOs are validated using Jakarta Bean Validation annotations.
- **DTO Responses**: Controllers return DTOs instead of entities to avoid circular JSON serialization issues.
- **Business Logic**: Service layer handles all business logic; controllers remain thin.
