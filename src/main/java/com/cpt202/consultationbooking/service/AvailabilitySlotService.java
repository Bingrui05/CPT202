package com.cpt202.consultationbooking.service;

import com.cpt202.consultationbooking.dto.request.CreateSlotRequest;
import com.cpt202.consultationbooking.dto.response.SlotResponse;
import com.cpt202.consultationbooking.entity.AvailabilitySlot;
import com.cpt202.consultationbooking.entity.Specialist;
import com.cpt202.consultationbooking.enums.SlotStatus;
import com.cpt202.consultationbooking.exception.ResourceNotFoundException;
import com.cpt202.consultationbooking.repository.AvailabilitySlotRepository;
import com.cpt202.consultationbooking.repository.SpecialistRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AvailabilitySlotService {

    private final AvailabilitySlotRepository slotRepository;
    private final SpecialistRepository specialistRepository;

    public AvailabilitySlotService(AvailabilitySlotRepository slotRepository,
                                   SpecialistRepository specialistRepository) {
        this.slotRepository = slotRepository;
        this.specialistRepository = specialistRepository;
    }

    @Transactional
    public SlotResponse createSlot(CreateSlotRequest request) {
        Specialist specialist = specialistRepository.findById(request.getSpecialistId())
                .orElseThrow(() -> new ResourceNotFoundException("Specialist not found"));

        AvailabilitySlot slot = AvailabilitySlot.builder()
                .specialist(specialist)
                .date(request.getDate())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .status(SlotStatus.AVAILABLE)
                .build();

        AvailabilitySlot saved = slotRepository.save(slot);
        return toResponse(saved);
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
