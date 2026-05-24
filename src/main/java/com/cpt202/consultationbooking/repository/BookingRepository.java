package com.cpt202.consultationbooking.repository;

import com.cpt202.consultationbooking.entity.Booking;
import com.cpt202.consultationbooking.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByCustomer_CustomerId(Long customerId);

    List<Booking> findBySpecialist_SpecialistId(Long specialistId);

    boolean existsBySlot_SlotId(Long slotId);

    boolean existsBySlot_SlotIdAndAppointmentDateAndStatusIn(
            Long slotId,
            LocalDate appointmentDate,
            List<BookingStatus> statuses
    );

    Optional<Booking> findBySlot_SlotIdAndAppointmentDateAndStatusIn(
            Long slotId,
            LocalDate appointmentDate,
            List<BookingStatus> statuses
    );

    boolean existsBySpecialist_SpecialistIdAndStatusIn(
            Long specialistId,
            List<BookingStatus> statuses
    );

    boolean existsBySlot_SlotIdAndStatusIn(
            Long slotId,
            List<BookingStatus> statuses
    );
}