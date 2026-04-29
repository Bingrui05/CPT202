package com.cpt202.consultationbooking.repository;

import com.cpt202.consultationbooking.entity.AvailabilitySlot;
import com.cpt202.consultationbooking.enums.SlotStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AvailabilitySlotRepository extends JpaRepository<AvailabilitySlot, Long> {
    
    List<AvailabilitySlot> findBySpecialist_SpecialistIdAndStatus(Long specialistId, SlotStatus status);
    
    List<AvailabilitySlot> findBySpecialist_SpecialistId(Long specialistId);
}
