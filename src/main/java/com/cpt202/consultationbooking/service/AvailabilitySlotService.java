package com.cpt202.consultationbooking.service;

import com.cpt202.consultationbooking.dto.request.CreateSlotRequest;
import com.cpt202.consultationbooking.dto.request.UpdateSlotRequest;
import com.cpt202.consultationbooking.dto.response.AvailableSlotResponse;
import com.cpt202.consultationbooking.dto.response.SlotResponse;
import com.cpt202.consultationbooking.entity.AvailabilitySlot;
import com.cpt202.consultationbooking.entity.Booking;
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

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AvailabilitySlotService {

    private static final List<BookingStatus> BLOCKING_STATUSES = Arrays.asList(
            BookingStatus.PENDING, BookingStatus.CONFIRMED, BookingStatus.COMPLETED);
    private static final int UPCOMING_DAYS = 14;

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

        if (!specialist.isActive()) {
            throw new BusinessException("Specialist is not active");
        }

        validateTimeRange(request.getStartTime(), request.getEndTime());

        if (hasOverlappingSlot(specialist.getSpecialistId(), request.getDayOfWeek(), request.getStartTime(), request.getEndTime(), null)) {
            throw new BusinessException("This specialist already has an overlapping slot on " + request.getDayOfWeek());
        }

        SlotStatus status = request.getStatus() != null ? request.getStatus() : SlotStatus.AVAILABLE;

        AvailabilitySlot slot = AvailabilitySlot.builder()
                .specialist(specialist)
                .dayOfWeek(request.getDayOfWeek())
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

        if (request.getSpecialistId() != null) {
            Specialist specialist = specialistRepository.findById(request.getSpecialistId())
                    .orElseThrow(() -> new ResourceNotFoundException("Specialist not found"));
            slot.setSpecialist(specialist);
        }

        if (request.getDayOfWeek() != null) {
            slot.setDayOfWeek(request.getDayOfWeek());
        }
        LocalTime startTime = request.getStartTime() != null ? request.getStartTime() : slot.getStartTime();
        LocalTime endTime = request.getEndTime() != null ? request.getEndTime() : slot.getEndTime();

        if (request.getStartTime() != null || request.getEndTime() != null || request.getDayOfWeek() != null) {
            validateTimeRange(startTime, endTime);
            if (hasOverlappingSlot(slot.getSpecialist().getSpecialistId(), slot.getDayOfWeek(), startTime, endTime, slotId)) {
                throw new BusinessException("This specialist already has an overlapping slot on " + slot.getDayOfWeek());
            }
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

        slot.setStatus(SlotStatus.UNAVAILABLE);
        slotRepository.save(slot);
    }

    public List<AvailableSlotResponse> getAvailableSlotsBySpecialist(Long specialistId) {
        LocalDate today = LocalDate.now();
        LocalDate endDate = today.plusDays(UPCOMING_DAYS);

        List<AvailabilitySlot> weeklySlots = slotRepository.findBySpecialist_SpecialistIdAndStatus(specialistId, SlotStatus.AVAILABLE);

        List<AvailableSlotResponse> slots = new ArrayList<>();

        for (AvailabilitySlot slot : weeklySlots) {
            LocalDate nextDate = today.with(TemporalAdjusters.nextOrSame(slot.getDayOfWeek()));
            while (!nextDate.isAfter(endDate)) {
                String occurrenceStatus = determineOccurrenceStatus(slot.getSlotId(), nextDate);
                boolean bookable = "AVAILABLE".equals(occurrenceStatus);
                Long bookingId = bookable ? null : findBookingIdForOccurrence(slot.getSlotId(), nextDate);

                String displayLabel = nextDate + " " + formatTime(slot.getStartTime()) + " - " + formatTime(slot.getEndTime());
                slots.add(AvailableSlotResponse.builder()
                        .slotId(slot.getSlotId())
                        .specialistId(specialistId)
                        .specialistName(slot.getSpecialist().getUser().getUsername())
                        .appointmentDate(nextDate)
                        .dayOfWeek(slot.getDayOfWeek())
                        .startTime(slot.getStartTime())
                        .endTime(slot.getEndTime())
                        .status(SlotStatus.AVAILABLE)
                        .displayLabel(displayLabel)
                        .occurrenceStatus(occurrenceStatus)
                        .bookingId(bookingId)
                        .bookable(bookable)
                        .build());
                nextDate = nextDate.plusWeeks(1);
            }
        }

        return slots.stream()
                .filter(s -> !s.getAppointmentDate().isBefore(today))
                .sorted((a, b) -> {
                    int dateCompare = a.getAppointmentDate().compareTo(b.getAppointmentDate());
                    if (dateCompare != 0) return dateCompare;
                    return a.getStartTime().compareTo(b.getStartTime());
                })
                .collect(Collectors.toList());
    }

    private String determineOccurrenceStatus(Long slotId, LocalDate appointmentDate) {
        Optional<Booking> booking = bookingRepository.findBySlot_SlotIdAndAppointmentDateAndStatusIn(
                slotId, appointmentDate, BLOCKING_STATUSES);
        return booking.map(b -> b.getStatus().name()).orElse("AVAILABLE");
    }

    private Long findBookingIdForOccurrence(Long slotId, LocalDate appointmentDate) {
        return bookingRepository.findBySlot_SlotIdAndAppointmentDateAndStatusIn(
                slotId, appointmentDate, BLOCKING_STATUSES)
                .map(Booking::getBookingId)
                .orElse(null);
    }

    public List<SlotResponse> getSlotsBySpecialist(Long specialistId) {
        return slotRepository.findBySpecialist_SpecialistId(specialistId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private boolean hasOverlappingSlot(Long specialistId, DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime, Long excludeSlotId) {
        List<AvailabilitySlot> existingSlots = slotRepository.findBySpecialist_SpecialistIdAndDayOfWeek(specialistId, dayOfWeek);
        
        for (AvailabilitySlot existing : existingSlots) {
            if (excludeSlotId != null && existing.getSlotId().equals(excludeSlotId)) {
                continue;
            }
            if (timesOverlap(existing.getStartTime(), existing.getEndTime(), startTime, endTime)) {
                return true;
            }
        }
        return false;
    }

    private boolean timesOverlap(LocalTime start1, LocalTime end1, LocalTime start2, LocalTime end2) {
        return start1.isBefore(end2) && start2.isBefore(end1);
    }

    private void validateTimeRange(LocalTime startTime, LocalTime endTime) {
        if (startTime != null && endTime != null && !startTime.isBefore(endTime)) {
            throw new BusinessException("Start time must be before end time");
        }
    }

    private String formatTime(LocalTime time) {
        if (time == null) return "";
        return time.toString();
    }

    private SlotResponse toResponse(AvailabilitySlot slot) {
        return SlotResponse.builder()
                .slotId(slot.getSlotId())
                .specialistId(slot.getSpecialist().getSpecialistId())
                .specialistName(slot.getSpecialist().getUser().getUsername())
                .dayOfWeek(slot.getDayOfWeek())
                .startTime(slot.getStartTime())
                .endTime(slot.getEndTime())
                .status(slot.getStatus())
                .build();
    }
}
