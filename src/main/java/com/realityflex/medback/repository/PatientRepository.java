package com.realityflex.medback.repository;

import com.realityflex.medback.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, String> {
   Patient findByLogin(String login);
  Boolean existsByLogin(String name);

}
