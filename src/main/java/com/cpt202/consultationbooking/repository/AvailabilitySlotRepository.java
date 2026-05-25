package com.cpt202.consultationbooking.repository;

import com.cpt202.consultationbooking.entity.AvailabilitySlot;
import com.cpt202.consultationbooking.enums.SlotStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AvailabilitySlotRepository extends JpaRepository<AvailabilitySlot, Long> {

    List<AvailabilitySlot> findBySpecialist_SpecialistIdAndStatus(Long specialistId, SlotStatus status);

    List<AvailabilitySlot> findBySpecialist_SpecialistId(Long specialistId);

    List<AvailabilitySlot> findBySpecialist_SpecialistIdAndDayOfWeek(Long specialistId, DayOfWeek dayOfWeek);

    boolean existsBySpecialist_SpecialistIdAndDayOfWeekAndStartTimeLessThanAndEndTimeGreaterThan(
            Long specialistId, DayOfWeek dayOfWeek, LocalTime endTime, LocalTime startTime);

    /**
     * Pessimistic write lock query for concurrency control.
     * Use this when creating a booking to prevent double booking.
     * The lock is held until the transaction commits.
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM AvailabilitySlot s WHERE s.slotId = :slotId")
    Optional<AvailabilitySlot> findByIdForUpdate(@Param("slotId") Long slotId);
}
