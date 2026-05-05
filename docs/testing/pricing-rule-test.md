# Pricing Rule Test Record

## Module
Pricing rule / automatic charge calculation

## Related Requirement
- Automatic charge calculation using simple pricing rules
- Pricing must follow defined rules consistently

## Pricing Rule Used in First Release

The first release uses a simple fixed-fee pricing rule:

- Each specialist has a consultation fee stored as `Specialist.fee`.
- When a customer creates a booking, the system automatically copies the selected specialist's fee into `Booking.price`.
- The calculated price is stored in the booking record.
- The booking response returns the price through `BookingResponse.price`.

This ensures that each booking has a clear and consistent charge.

---

## Test Case 1: Booking price is copied from specialist fee

### Preconditions
- Specialist 1 exists.
- Specialist 1 has fee = 150.00.
- Customer 1 exists.
- Slot 1 belongs to Specialist 1 and is AVAILABLE.

### Endpoint
POST /api/bookings

### Request Body
```json
{
  "customerId": 1,
  "specialistId": 1,
  "slotId": 1,
  "topic": "Career consultation",
  "notes": "Need advice about internship applications."
}
```

### Expected Result
- Booking is created successfully.
- Booking status is PENDING.
- Booking price equals Specialist 1's fee.
- BookingResponse includes price = 150.00.

### Actual Result
To be completed by module owner.

### Screenshot
docs/testing/screenshots/pricing-booking-price-150.png

---

## Test Case 2: Different specialist fee produces different booking price

### Preconditions
- Specialist 2 exists.
- Specialist 2 has fee = 100.00.
- Customer 1 exists.
- Slot 2 belongs to Specialist 2 and is AVAILABLE.

### Endpoint
POST /api/bookings

### Expected Result
- Booking is created successfully.
- Booking price equals Specialist 2's fee.
- BookingResponse includes price = 100.00.

### Actual Result
To be completed by module owner.

### Screenshot
docs/testing/screenshots/pricing-booking-price-100.png

---

## Test Case 3: BookingResponse contains price field

### Endpoint
GET /api/bookings

### Expected Result
- Each booking response contains a price field.
- The price value matches the price stored in the booking record.

### Actual Result
To be completed by module owner.

### Screenshot
docs/testing/screenshots/pricing-booking-response-price.png

---

## Evidence in Code

The pricing rule is implemented through the following fields and logic:

- `Specialist.fee`
- `Booking.price`
- `BookingResponse.price`
- `BookingService.createBooking()` sets booking price using the selected specialist's fee.

Relevant logic:

```java
booking.price = specialist.fee
```

## Notes for Group Report

For the first release, the system adopts a simple fixed-fee pricing rule. Each specialist has a consultation fee. When a customer creates a booking, the system automatically calculates the booking charge by copying the selected specialist's fee into the booking record. This stored booking price ensures consistent charge calculation for each appointment.
