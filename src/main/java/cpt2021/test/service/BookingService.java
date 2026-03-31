package cpt2021.test.service;

import cpt2021.test.dto.CreateBookingRequest;
import cpt2021.test.entity.AvailabilitySlot;
import cpt2021.test.entity.Booking;
import cpt2021.test.repository.AvailabilitySlotRepository;
import cpt2021.test.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private AvailabilitySlotRepository availabilitySlotRepository;

    public Booking getBookingById(Long bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
    }

    @Transactional
    public Booking createBooking(CreateBookingRequest request) {
        AvailabilitySlot slot = availabilitySlotRepository.findById(request.getSlotId())
                .orElseThrow(() -> new RuntimeException("Slot not found"));

        if (!slot.isAvailable()) {
            throw new RuntimeException("Selected slot is not available");
        }

        Booking booking = new Booking();
        booking.setStatus("CONFIRMED");
        booking.setAvailabilitySlot(slot);

        slot.setAvailable(false);
        availabilitySlotRepository.save(slot);

        return bookingRepository.save(booking);
    }
    
    @Transactional
    public void rescheduleBooking(Long bookingId, Long newSlotId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        AvailabilitySlot newSlot = availabilitySlotRepository.findById(newSlotId)
                .orElseThrow(() -> new RuntimeException("Slot not found"));

        AvailabilitySlot oldSlot = booking.getAvailabilitySlot();

        if (oldSlot == null) {
            throw new RuntimeException("Current booking has no assigned slot");
        }

        if (!"CONFIRMED".equals(booking.getStatus())) {
            throw new RuntimeException("Only confirmed bookings can be rescheduled");
        }

        if (oldSlot.getId().equals(newSlot.getId())) {
            throw new RuntimeException("New slot must be different from current slot");
        }

        if (!newSlot.isAvailable()) {
            throw new RuntimeException("Selected slot is not available");
        }

        oldSlot.setAvailable(true);
        newSlot.setAvailable(false);
        booking.setAvailabilitySlot(newSlot);

        availabilitySlotRepository.save(oldSlot);
        availabilitySlotRepository.save(newSlot);
        bookingRepository.save(booking);
    }
    @Transactional
    public void cancelBooking(Long bookingId) {
    Booking booking = bookingRepository.findById(bookingId)
            .orElseThrow(() -> new RuntimeException("Booking not found"));

    AvailabilitySlot slot = booking.getAvailabilitySlot();

    if (slot != null) {
        slot.setAvailable(true);
        availabilitySlotRepository.save(slot);
    }

    booking.setStatus("CANCELLED");
    bookingRepository.save(booking);
}
}