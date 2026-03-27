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

    /**
     * 核心逻辑：查找某个专家在指定时间段内是否有重叠的预约
     * 逻辑：已有开始时间 < 新结束时间 AND 已有结束时间 > 新开始时间
     */
    @Query("SELECT a FROM AvailabilitySlot a WHERE a.specialistId = :specId " +
           "AND a.startTime < :end AND a.endTime > :start")
    List<AvailabilitySlot> findOverlappingSlots(
            @Param("specId") Long specialistId,
            @Param("start") LocalDateTime startTime,
            @Param("end") LocalDateTime endTime);
}