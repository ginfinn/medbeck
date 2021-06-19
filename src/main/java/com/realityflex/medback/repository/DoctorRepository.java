package com.realityflex.medback.repository;

import com.realityflex.medback.entity.Doctor;
import com.realityflex.medback.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorRepository extends JpaRepository<Doctor, Integer> {
    Doctor findByLogin(String login);
    Boolean existsByLogin(String name);
}
