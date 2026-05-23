package com.cpt202.consultationbooking.repository;

import com.cpt202.consultationbooking.entity.SpecialistUpdateRequest;
import com.cpt202.consultationbooking.enums.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpecialistUpdateRequestRepository extends JpaRepository<SpecialistUpdateRequest, Long> {

    List<SpecialistUpdateRequest> findBySpecialist_SpecialistIdOrderByCreatedAtDesc(Long specialistId);

    List<SpecialistUpdateRequest> findByStatusOrderByCreatedAtDesc(RequestStatus status);

    List<SpecialistUpdateRequest> findAllByOrderByCreatedAtDesc();
}
