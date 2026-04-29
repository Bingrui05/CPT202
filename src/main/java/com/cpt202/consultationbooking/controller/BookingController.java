package com.cpt202.consultationbooking.controller;

import com.cpt202.consultationbooking.dto.request.CreateBookingRequest;
import com.cpt202.consultationbooking.dto.response.ApiResponse;
import com.cpt202.consultationbooking.dto.response.BookingResponse;
import com.cpt202.consultationbooking.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<BookingResponse>> createBooking(
            @Valid @RequestBody CreateBookingRequest request) {
        BookingResponse response = bookingService.createBooking(request);
        return ResponseEntity.ok(ApiResponse.success("Booking created successfully", response));
    }

    @PutMapping("/{bookingId}/confirm")
    public ResponseEntity<ApiResponse<BookingResponse>> confirmBooking(@PathVariable Long bookingId) {
        BookingResponse response = bookingService.confirmBooking(bookingId);
        return ResponseEntity.ok(ApiResponse.success("Booking confirmed successfully", response));
    }

    @PutMapping("/{bookingId}/cancel")
    public ResponseEntity<ApiResponse<BookingResponse>> cancelBooking(@PathVariable Long bookingId) {
        BookingResponse response = bookingService.cancelBooking(bookingId);
        return ResponseEntity.ok(ApiResponse.success("Booking cancelled successfully", response));
    }

    @PutMapping("/{bookingId}/complete")
    public ResponseEntity<ApiResponse<BookingResponse>> completeBooking(@PathVariable Long bookingId) {
        BookingResponse response = bookingService.completeBooking(bookingId);
        return ResponseEntity.ok(ApiResponse.success("Booking completed successfully", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<BookingResponse>>> getAllBookings() {
        List<BookingResponse> bookings = bookingService.getAllBookings();
        return ResponseEntity.ok(ApiResponse.success("Bookings retrieved successfully", bookings));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<ApiResponse<List<BookingResponse>>> getBookingsByCustomer(
            @PathVariable Long customerId) {
        List<BookingResponse> bookings = bookingService.getBookingsByCustomer(customerId);
        return ResponseEntity.ok(ApiResponse.success("Customer bookings retrieved successfully", bookings));
    }

    @GetMapping("/specialist/{specialistId}")
    public ResponseEntity<ApiResponse<List<BookingResponse>>> getBookingsBySpecialist(
            @PathVariable Long specialistId) {
        List<BookingResponse> bookings = bookingService.getBookingsBySpecialist(specialistId);
        return ResponseEntity.ok(ApiResponse.success("Specialist bookings retrieved successfully", bookings));
    }
}
