package com.cpt202.consultationbooking.service;

import com.cpt202.consultationbooking.dto.request.CreateSlotRequest;
import com.cpt202.consultationbooking.dto.request.UpdateSlotRequest;
import com.cpt202.consultationbooking.dto.response.SlotResponse;
import com.cpt202.consultationbooking.entity.AvailabilitySlot;
import com.cpt202.consultationbooking.entity.Specialist;
import com.cpt202.consultationbooking.enums.BookingStatus;
import com.cpt202.consultationbooking.enums.SlotStatus;
import com.cpt202.consultationbooking.exception.BusinessException;
import com.cpt202.consultationbooking.exception.ResourceNotFoundException;
import com.cpt202.consultationbooking.repository.AvailabilitySlotRepository;
import com.cpt202.consultationbooking.repository.BookingRepository;
import com.cpt202.consultationbooking.repository.SpecialistRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AvailabilitySlotService {

    private static final List<BookingStatus> ACTIVE_BOOKING_STATUSES = Arrays.asList(
            BookingStatus.PENDING, 
            BookingStatus.CONFIRMED
    );

    private final AvailabilitySlotRepository slotRepository;
    private final SpecialistRepository specialistRepository;
    private final BookingRepository bookingRepository;

    public AvailabilitySlotService(AvailabilitySlotRepository slotRepository,
                                   SpecialistRepository specialistRepository,
                                   BookingRepository bookingRepository) {
        this.slotRepository = slotRepository;
        this.specialistRepository = specialistRepository;
        this.bookingRepository = bookingRepository;
    }

    @Transactional
    public SlotResponse createSlot(CreateSlotRequest request) {
        Specialist specialist = specialistRepository.findById(request.getSpecialistId())
                .orElseThrow(() -> new ResourceNotFoundException("Specialist not found"));

        // Check if specialist is active
        if (!specialist.isActive()) {
            throw new BusinessException("Specialist is not active");
        }

        validateTimeRange(request.getStartTime(), request.getEndTime());

        // Check for overlapping slots
        List<AvailabilitySlot> overlapping = slotRepository.findOverlappingSlotsForCreate(
                specialist.getSpecialistId(), 
                request.getDate(), 
                request.getStartTime(), 
                request.getEndTime());
        
        if (!overlapping.isEmpty()) {
            throw new BusinessException("Slot time overlaps with an existing slot for this specialist");
        }

        SlotStatus status = request.getStatus() != null ? request.getStatus() : SlotStatus.AVAILABLE;

        AvailabilitySlot slot = AvailabilitySlot.builder()
                .specialist(specialist)
                .date(request.getDate())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .status(status)
                .build();

        AvailabilitySlot saved = slotRepository.save(slot);
        return toResponse(saved);
    }

    public SlotResponse getSlotById(Long slotId) {
        AvailabilitySlot slot = slotRepository.findById(slotId)
                .orElseThrow(() -> new ResourceNotFoundException("Slot not found"));
        return toResponse(slot);
    }

    @Transactional
    public SlotResponse updateSlot(Long slotId, UpdateSlotRequest request) {
        AvailabilitySlot slot = slotRepository.findById(slotId)
                .orElseThrow(() -> new ResourceNotFoundException("Slot not found"));

        if (slot.getStatus() == SlotStatus.BOOKED) {
            if (request.getDate() != null || request.getStartTime() != null || request.getEndTime() != null) {
                throw new BusinessException("Cannot modify date or time for a booked slot");
            }
        }

        if (request.getSpecialistId() != null) {
            Specialist specialist = specialistRepository.findById(request.getSpecialistId())
                    .orElseThrow(() -> new ResourceNotFoundException("Specialist not found"));
            slot.setSpecialist(specialist);
        }

        LocalDate date = request.getDate() != null ? request.getDate() : slot.getDate();
        LocalTime startTime = request.getStartTime() != null ? request.getStartTime() : slot.getStartTime();
        LocalTime endTime = request.getEndTime() != null ? request.getEndTime() : slot.getEndTime();

        if (request.getStartTime() != null || request.getEndTime() != null || request.getDate() != null) {
            validateTimeRange(startTime, endTime);
            
            // Check for overlapping slots (excluding current slot)
            List<AvailabilitySlot> overlapping = slotRepository.findOverlappingSlots(
                    slot.getSpecialist().getSpecialistId(),
                    date,
                    startTime,
                    endTime,
                    slotId);
            
            if (!overlapping.isEmpty()) {
                throw new BusinessException("Slot time overlaps with an existing slot for this specialist");
            }
            
            slot.setDate(date);
            slot.setStartTime(startTime);
            slot.setEndTime(endTime);
        }

        if (request.getStatus() != null) {
            slot.setStatus(request.getStatus());
        }

        AvailabilitySlot saved = slotRepository.save(slot);
        return toResponse(saved);
    }

    @Transactional
    public void deleteSlot(Long slotId) {
        AvailabilitySlot slot = slotRepository.findById(slotId)
                .orElseThrow(() -> new ResourceNotFoundException("Slot not found"));

        // Cannot delete a booked slot
        if (slot.getStatus() == SlotStatus.BOOKED) {
            throw new BusinessException("Booked slot cannot be deactivated");
        }

        // Check if slot has active bookings
        boolean hasActiveBookings = bookingRepository.existsBySlot_SlotIdAndStatusIn(
                slotId, ACTIVE_BOOKING_STATUSES);

        if (hasActiveBookings) {
            throw new BusinessException("Slot is occupied by an active booking and cannot be deactivated");
        }

        slot.setStatus(SlotStatus.UNAVAILABLE);
        slotRepository.save(slot);
    }

    public List<SlotResponse> getAvailableSlotsBySpecialist(Long specialistId) {
        return slotRepository.findBySpecialist_SpecialistIdAndStatus(specialistId, SlotStatus.AVAILABLE)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<SlotResponse> getSlotsBySpecialist(Long specialistId) {
        return slotRepository.findBySpecialist_SpecialistId(specialistId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private void validateTimeRange(LocalTime startTime, LocalTime endTime) {
        if (startTime != null && endTime != null && !startTime.isBefore(endTime)) {
            throw new BusinessException("Start time must be before end time");
        }
    }

    private SlotResponse toResponse(AvailabilitySlot slot) {
        return SlotResponse.builder()
                .slotId(slot.getSlotId())
                .specialistId(slot.getSpecialist().getSpecialistId())
                .specialistName(slot.getSpecialist().getUser().getUsername())
                .date(slot.getDate())
                .startTime(slot.getStartTime())
                .endTime(slot.getEndTime())
                .status(slot.getStatus())
                .build();
    }
}
