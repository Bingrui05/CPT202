package cpt2021.test.service;

import cpt2021.test.dto.BookingResponse;
import cpt2021.test.dto.CreateBookingRequest;
import cpt2021.test.entity.Booking;
import cpt2021.test.entity.BookingStatus;
import cpt2021.test.repository.BookingRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;

    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public BookingResponse createBooking(CreateBookingRequest request) {
        if (request.getCustomerId() == null ||
            request.getSpecialistId() == null ||
            request.getSlotId() == null ||
            request.getPricingId() == null) {
            throw new RuntimeException("Missing required fields");
        }

        Booking booking = new Booking();
        booking.setCustomerId(request.getCustomerId());
        booking.setSpecialistId(request.getSpecialistId());
        booking.setSlotId(request.getSlotId());
        booking.setPricingId(request.getPricingId());
        booking.setStatus(BookingStatus.PENDING);
        booking.setCreatedAt(LocalDateTime.now());

        Booking savedBooking = bookingRepository.save(booking);

        return new BookingResponse(
                savedBooking.getId(),
                savedBooking.getCustomerId(),
                savedBooking.getSpecialistId(),
                savedBooking.getSlotId(),
                savedBooking.getPricingId(),
                savedBooking.getStatus().name()
        );
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public List<Booking> getBookingsByCustomerId(Long customerId) {
        return bookingRepository.findByCustomerId(customerId);
    }

    public BookingResponse confirmBooking(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new RuntimeException("Only pending bookings can be confirmed");
        }

        booking.setStatus(BookingStatus.CONFIRMED);
        Booking updatedBooking = bookingRepository.save(booking);

        return new BookingResponse(
                updatedBooking.getId(),
                updatedBooking.getCustomerId(),
                updatedBooking.getSpecialistId(),
                updatedBooking.getSlotId(),
                updatedBooking.getPricingId(),
                updatedBooking.getStatus().name()
        );
    }

    public BookingResponse cancelBooking(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (booking.getStatus() == BookingStatus.COMPLETED) {
            throw new RuntimeException("Completed booking cannot be cancelled");
        }

        booking.setStatus(BookingStatus.CANCELLED);
        Booking updatedBooking = bookingRepository.save(booking);

        return new BookingResponse(
                updatedBooking.getId(),
                updatedBooking.getCustomerId(),
                updatedBooking.getSpecialistId(),
                updatedBooking.getSlotId(),
                updatedBooking.getPricingId(),
                updatedBooking.getStatus().name()
        );
    }

    public BookingResponse completeBooking(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (booking.getStatus() != BookingStatus.CONFIRMED) {
            throw new RuntimeException("Only confirmed bookings can be completed");
        }

        booking.setStatus(BookingStatus.COMPLETED);
        Booking updatedBooking = bookingRepository.save(booking);

        return new BookingResponse(
                updatedBooking.getId(),
                updatedBooking.getCustomerId(),
                updatedBooking.getSpecialistId(),
                updatedBooking.getSlotId(),
                updatedBooking.getPricingId(),
                updatedBooking.getStatus().name()
        );
    }
}