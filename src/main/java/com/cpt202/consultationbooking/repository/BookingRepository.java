package com.cpt202.consultationbooking.repository;

import com.cpt202.consultationbooking.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    
    List<Booking> findByCustomer_CustomerId(Long customerId);
    
    List<Booking> findBySpecialist_SpecialistId(Long specialistId);
    
    boolean existsBySlot_SlotId(Long slotId);
}
