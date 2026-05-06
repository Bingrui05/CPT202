package com.cpt202.consultationbooking.repository;

import com.cpt202.consultationbooking.entity.AvailabilitySlot;
import com.cpt202.consultationbooking.enums.SlotStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface AvailabilitySlotRepository extends JpaRepository<AvailabilitySlot, Long> {
    
    List<AvailabilitySlot> findBySpecialist_SpecialistIdAndStatus(Long specialistId, SlotStatus status);
    
    List<AvailabilitySlot> findBySpecialist_SpecialistId(Long specialistId);
    
    @Query("SELECT s FROM AvailabilitySlot s WHERE s.specialist.specialistId = :specialistId " +
           "AND s.date = :date AND s.slotId != :slotId " +
           "AND ((s.startTime < :endTime AND s.endTime > :startTime))")
    List<AvailabilitySlot> findOverlappingSlots(
            @Param("specialistId") Long specialistId,
            @Param("date") LocalDate date,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime,
            @Param("slotId") Long slotId);
    
    @Query("SELECT s FROM AvailabilitySlot s WHERE s.specialist.specialistId = :specialistId " +
           "AND s.date = :date " +
           "AND ((s.startTime < :endTime AND s.endTime > :startTime))")
    List<AvailabilitySlot> findOverlappingSlotsForCreate(
            @Param("specialistId") Long specialistId,
            @Param("date") LocalDate date,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime);
}
