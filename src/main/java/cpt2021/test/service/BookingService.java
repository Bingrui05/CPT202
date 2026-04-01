package cpt2021.test.service;

import cpt2021.test.dto.BookingResponse;
import cpt2021.test.dto.CreateBookingRequest;
import cpt2021.test.entity.AvailabilitySlot;
import cpt2021.test.entity.Booking;
import cpt2021.test.entity.BookingStatus;
import cpt2021.test.repository.AvailabilitySlotRepository;
import cpt2021.test.repository.BookingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final AvailabilitySlotRepository availabilitySlotRepository;

    public BookingService(BookingRepository bookingRepository,
                          AvailabilitySlotRepository availabilitySlotRepository) {
        this.bookingRepository = bookingRepository;
        this.availabilitySlotRepository = availabilitySlotRepository;
    }

    // ✅ 创建预约（结合 slot 可用性）
    @Transactional
    public BookingResponse createBooking(CreateBookingRequest request) {

        AvailabilitySlot slot = availabilitySlotRepository.findById(request.getSlotId())
                .orElseThrow(() -> new RuntimeException("Slot not found"));

        if (!slot.isAvailable()) {
            throw new RuntimeException("Selected slot is not available");
        }

        Booking booking = new Booking();
        booking.setCustomerId(request.getCustomerId());
        booking.setSpecialistId(request.getSpecialistId());
        booking.setPricingId(request.getPricingId());
        booking.setAvailabilitySlot(slot);
        booking.setStatus(BookingStatus.PENDING);
        booking.setCreatedAt(LocalDateTime.now());

        slot.setAvailable(false);
        availabilitySlotRepository.save(slot);

        Booking saved = bookingRepository.save(booking);

        return mapToResponse(saved);
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public List<Booking> getBookingsByCustomerId(Long customerId) {
        return bookingRepository.findByCustomerId(customerId);
    }

    // ✅ 确认预约
    public BookingResponse confirmBooking(Long id) {
        Booking booking = getBookingOrThrow(id);

        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new RuntimeException("Only pending bookings can be confirmed");
        }

        booking.setStatus(BookingStatus.CONFIRMED);
        return mapToResponse(bookingRepository.save(booking));
    }

    // ✅ 取消预约（释放 slot）
    @Transactional
    public BookingResponse cancelBooking(Long id) {
        Booking booking = getBookingOrThrow(id);

        if (booking.getStatus() == BookingStatus.COMPLETED) {
            throw new RuntimeException("Completed booking cannot be cancelled");
        }

        AvailabilitySlot slot = booking.getAvailabilitySlot();
        if (slot != null) {
            slot.setAvailable(true);
            availabilitySlotRepository.save(slot);
        }

        booking.setStatus(BookingStatus.CANCELLED);
        return mapToResponse(bookingRepository.save(booking));
    }

    // ✅ 完成预约
    public BookingResponse completeBooking(Long id) {
        Booking booking = getBookingOrThrow(id);

        if (booking.getStatus() != BookingStatus.CONFIRMED) {
            throw new RuntimeException("Only confirmed bookings can be completed");
        }

        booking.setStatus(BookingStatus.COMPLETED);
        return mapToResponse(bookingRepository.save(booking));
    }

    // ✅ 改期（核心功能）
    @Transactional
    public void rescheduleBooking(Long bookingId, Long newSlotId) {

        Booking booking = getBookingOrThrow(bookingId);

        if (booking.getStatus() != BookingStatus.CONFIRMED) {
            throw new RuntimeException("Only confirmed bookings can be rescheduled");
        }

        AvailabilitySlot newSlot = availabilitySlotRepository.findById(newSlotId)
                .orElseThrow(() -> new RuntimeException("Slot not found"));

        AvailabilitySlot oldSlot = booking.getAvailabilitySlot();

        if (oldSlot.getId().equals(newSlot.getId())) {
            throw new RuntimeException("New slot must be different");
        }

        if (!newSlot.isAvailable()) {
            throw new RuntimeException("New slot not available");
        }

        // 释放旧 slot
        oldSlot.setAvailable(true);

        // 占用新 slot
        newSlot.setAvailable(false);
        booking.setAvailabilitySlot(newSlot);

        availabilitySlotRepository.save(oldSlot);
        availabilitySlotRepository.save(newSlot);
        bookingRepository.save(booking);
    }

    // 🔧 工具方法
    private Booking getBookingOrThrow(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
    }

    private BookingResponse mapToResponse(Booking booking) {
        return new BookingResponse(
                booking.getId(),
                booking.getCustomerId(),
                booking.getSpecialistId(),
                booking.getAvailabilitySlot() != null ? booking.getAvailabilitySlot().getId() : null,
                booking.getPricingId(),
                booking.getStatus().name()
        );
    }
}