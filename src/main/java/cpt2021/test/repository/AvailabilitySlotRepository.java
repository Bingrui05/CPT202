package cpt2021.test.repository;

import cpt2021.test.entity.AvailabilitySlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AvailabilitySlotRepository extends JpaRepository<AvailabilitySlot, Long> {

    @Query("SELECT a FROM AvailabilitySlot a WHERE a.specialistId = :specId " +
           "AND a.startTime < :end AND a.endTime > :start")
    List<AvailabilitySlot> findOverlappingSlots(
            @Param("specId") Long specialistId,
            @Param("start") LocalDateTime startTime,
            @Param("end") LocalDateTime endTime);
}