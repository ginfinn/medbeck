package com.realityflex.medback.repository;

import com.realityflex.medback.entity.Tonometer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface TonometerRepository extends JpaRepository<Tonometer, Integer> {
    List<Tonometer> findAllByDateUpdateBetweenAndFakePatientId(Date from, Date to, Integer id);
    List<Tonometer> findAllByFakePatientId( Integer id);
}

