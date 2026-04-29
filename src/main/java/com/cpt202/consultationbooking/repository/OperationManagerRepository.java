package com.cpt202.consultationbooking.repository;

import com.cpt202.consultationbooking.entity.OperationManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OperationManagerRepository extends JpaRepository<OperationManager, Long> {
}
