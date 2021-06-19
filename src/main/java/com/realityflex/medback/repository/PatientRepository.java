package com.realityflex.medback.repository;

import com.realityflex.medback.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PatientRepository extends JpaRepository<Patient, Integer> {
    Patient findByLogin(String login);

    Boolean existsByLogin(String name);

Patient findByInn(String inn);
    List<Patient> findAllByDoctorName(String name);
}
