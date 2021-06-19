package com.realityflex.medback.repository;

import com.realityflex.medback.entity.DoctorMessage;
import com.realityflex.medback.entity.Pressure;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface PressureRepository extends JpaRepository<Pressure, String> {
    List<Pressure> findAllByFakePatientId(Integer id);
}
