package cpt2021.test.repository;

import cpt2021.test.entity.Booking;
import cpt2021.test.entity.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByCustomerId(Long customerId);

    List<Booking> findBySpecialistId(Long specialistId);

    List<Booking> findByStatus(BookingStatus status);
}