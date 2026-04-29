package com.cpt202.consultationbooking.service;

import com.cpt202.consultationbooking.dto.request.CreateBookingRequest;
import com.cpt202.consultationbooking.dto.response.BookingResponse;
import com.cpt202.consultationbooking.entity.AvailabilitySlot;
import com.cpt202.consultationbooking.entity.Booking;
import com.cpt202.consultationbooking.entity.Customer;
import com.cpt202.consultationbooking.entity.Specialist;
import com.cpt202.consultationbooking.enums.BookingStatus;
import com.cpt202.consultationbooking.enums.SlotStatus;
import com.cpt202.consultationbooking.exception.BusinessException;
import com.cpt202.consultationbooking.exception.ResourceNotFoundException;
import com.cpt202.consultationbooking.repository.AvailabilitySlotRepository;
import com.cpt202.consultationbooking.repository.BookingRepository;
import com.cpt202.consultationbooking.repository.CustomerRepository;
import com.cpt202.consultationbooking.repository.SpecialistRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final CustomerRepository customerRepository;
    private final SpecialistRepository specialistRepository;
    private final AvailabilitySlotRepository slotRepository;

    public BookingService(BookingRepository bookingRepository,
                          CustomerRepository customerRepository,
                          SpecialistRepository specialistRepository,
                          AvailabilitySlotRepository slotRepository) {
        this.bookingRepository = bookingRepository;
        this.customerRepository = customerRepository;
        this.specialistRepository = specialistRepository;
        this.slotRepository = slotRepository;
    }

    @Transactional
    public BookingResponse createBooking(CreateBookingRequest request) {
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        Specialist specialist = specialistRepository.findById(request.getSpecialistId())
                .orElseThrow(() -> new ResourceNotFoundException("Specialist not found"));

        AvailabilitySlot slot = slotRepository.findById(request.getSlotId())
                .orElseThrow(() -> new ResourceNotFoundException("Slot not found"));

        if (!slot.getSpecialist().getSpecialistId().equals(specialist.getSpecialistId())) {
            throw new BusinessException("Slot does not belong to the selected specialist");
        }

        if (slot.getStatus() != SlotStatus.AVAILABLE) {
            throw new BusinessException("Slot is not available");
        }

        if (bookingRepository.existsBySlot_SlotId(slot.getSlotId())) {
            throw new BusinessException("Slot has already been booked");
        }

        Booking booking = Booking.builder()
                .customer(customer)
                .specialist(specialist)
                .slot(slot)
                .topic(request.getTopic())
                .notes(request.getNotes())
                .status(BookingStatus.PENDING)
                .price(specialist.getFee())
                .createdAt(LocalDateTime.now())
                .build();

        slot.setStatus(SlotStatus.BOOKED);
        slotRepository.save(slot);

        Booking saved = bookingRepository.save(booking);
        return toResponse(saved);
    }

    @Transactional
    public BookingResponse confirmBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new BusinessException("Only PENDING booking can be confirmed");
        }

        booking.setStatus(BookingStatus.CONFIRMED);
        booking.setUpdatedAt(LocalDateTime.now());
        Booking saved = bookingRepository.save(booking);
        return toResponse(saved);
    }

    @Transactional
    public BookingResponse cancelBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        if (booking.getStatus() == BookingStatus.COMPLETED) {
            throw new BusinessException("COMPLETED booking cannot be cancelled");
        }

        AvailabilitySlot slot = booking.getSlot();
        slot.setStatus(SlotStatus.AVAILABLE);
        slotRepository.save(slot);

        booking.setStatus(BookingStatus.CANCELLED);
        booking.setUpdatedAt(LocalDateTime.now());
        Booking saved = bookingRepository.save(booking);
        return toResponse(saved);
    }

    @Transactional
    public BookingResponse completeBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        if (booking.getStatus() != BookingStatus.CONFIRMED) {
            throw new BusinessException("Only CONFIRMED booking can be completed");
        }

        booking.setStatus(BookingStatus.COMPLETED);
        booking.setUpdatedAt(LocalDateTime.now());
        Booking saved = bookingRepository.save(booking);
        return toResponse(saved);
    }

    public List<BookingResponse> getBookingsByCustomer(Long customerId) {
        return bookingRepository.findByCustomer_CustomerId(customerId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<BookingResponse> getBookingsBySpecialist(Long specialistId) {
        return bookingRepository.findBySpecialist_SpecialistId(specialistId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<BookingResponse> getAllBookings() {
        return bookingRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private BookingResponse toResponse(Booking booking) {
        return BookingResponse.builder()
                .bookingId(booking.getBookingId())
                .customerId(booking.getCustomer().getCustomerId())
                .customerName(booking.getCustomer().getUser().getUsername())
                .specialistId(booking.getSpecialist().getSpecialistId())
                .specialistName(booking.getSpecialist().getUser().getUsername())
                .slotId(booking.getSlot().getSlotId())
                .slotDateTime(booking.getSlot().getDate().atTime(booking.getSlot().getStartTime()))
                .topic(booking.getTopic())
                .notes(booking.getNotes())
                .status(booking.getStatus())
                .price(booking.getPrice())
                .createdAt(booking.getCreatedAt())
                .updatedAt(booking.getUpdatedAt())
                .build();
    }
}
