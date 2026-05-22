package com.cpt202.consultationbooking.repository;

import com.cpt202.consultationbooking.entity.SpecialistUpdateRequest;
import com.cpt202.consultationbooking.enums.SpecialistUpdateRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpecialistUpdateRequestRepository extends JpaRepository<SpecialistUpdateRequest, Long> {

    List<SpecialistUpdateRequest> findAllByOrderByCreatedAtDesc();

    List<SpecialistUpdateRequest> findByStatusOrderByCreatedAtDesc(SpecialistUpdateRequestStatus status);

    List<SpecialistUpdateRequest> findBySpecialist_SpecialistIdOrderByCreatedAtDesc(Long specialistId);
}
