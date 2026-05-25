package com.cpt202.consultationbooking.repository;

import com.cpt202.consultationbooking.entity.Booking;
import com.cpt202.consultationbooking.enums.BookingStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByCustomer_CustomerId(Long customerId);

    List<Booking> findBySpecialist_SpecialistId(Long specialistId);

    boolean existsBySlot_SlotId(Long slotId);

    boolean existsBySlot_SlotIdAndStatusIn(Long slotId, List<BookingStatus> statuses);

    List<Booking> findBySlot_SlotIdAndStatusIn(Long slotId, List<BookingStatus> statuses);

    boolean existsBySpecialist_SpecialistIdAndStatusIn(Long specialistId, List<BookingStatus> statuses);

    boolean existsBySlot_SlotIdAndAppointmentDateAndStatusIn(Long slotId, LocalDate appointmentDate, List<BookingStatus> statuses);

    Optional<Booking> findBySlot_SlotIdAndAppointmentDateAndStatusIn(Long slotId, LocalDate appointmentDate, List<BookingStatus> statuses);

    /**
     * Pessimistic lock query for booking existence check.
     * Used in createBooking to prevent double-booking race conditions.
     * The FOR UPDATE lock ensures serialized access.
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END " +
           "FROM Booking b " +
           "WHERE b.slot.slotId = :slotId " +
           "AND b.appointmentDate = :appointmentDate " +
           "AND b.status IN :statuses")
    boolean existsBySlotIdAndAppointmentDateAndStatusInWithLock(
            @Param("slotId") Long slotId,
            @Param("appointmentDate") LocalDate appointmentDate,
            @Param("statuses") List<BookingStatus> statuses);

    /**
     * Pessimistic lock query for booking existence check (excluding current booking).
     * Used in rescheduleBooking to avoid false positives when rescheduling.
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END " +
           "FROM Booking b " +
           "WHERE b.slot.slotId = :slotId " +
           "AND b.appointmentDate = :appointmentDate " +
           "AND b.status IN :statuses " +
           "AND b.bookingId <> :excludeBookingId")
    boolean existsBySlotIdAndAppointmentDateAndStatusInWithLockExcludingBooking(
            @Param("slotId") Long slotId,
            @Param("appointmentDate") LocalDate appointmentDate,
            @Param("statuses") List<BookingStatus> statuses,
            @Param("excludeBookingId") Long excludeBookingId);
}
