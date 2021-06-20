package com.realityflex.medback.repository;

import com.realityflex.medback.entity.DoctorMessage;
import com.realityflex.medback.entity.Pressure;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;


public interface PressureRepository extends JpaRepository<Pressure, Integer> {
    public List<Pressure> findAllByIdBetweenAndFakePatientId(Date from, Date to,int patientId);
    public List<Pressure> findAllByFakePatientId(int id);
    Pressure findFirstByOrderByIdDesc();
}
