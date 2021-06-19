package com.realityflex.medback.repository;

import com.realityflex.medback.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PatientRepository extends JpaRepository<Patient, Integer> {
    Patient findBySnils(String snils);

    Boolean existsBySnils(String name);


    List<Patient> findAllByDoctorName(String name);
}
