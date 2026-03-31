package cpt2021.test.controller;

import cpt2021.test.dto.CreateBookingRequest;
import cpt2021.test.dto.RescheduleRequest;
import cpt2021.test.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getBooking(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(bookingService.getBookingById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createBooking(@RequestBody CreateBookingRequest request) {
        try {
            return ResponseEntity.ok(bookingService.createBooking(request));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}/reschedule")
    public ResponseEntity<?> rescheduleBooking(
            @PathVariable Long id,
            @RequestBody RescheduleRequest request) {
        try {
            bookingService.rescheduleBooking(id, request.getNewSlotId());
            return ResponseEntity.ok("Booking rescheduled successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PutMapping("/{id}/cancel")
public ResponseEntity<?> cancelBooking(@PathVariable Long id) {
    try {
        bookingService.cancelBooking(id);
        return ResponseEntity.ok("Booking cancelled successfully");
    } catch (RuntimeException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
}