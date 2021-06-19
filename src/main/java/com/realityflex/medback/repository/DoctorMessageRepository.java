package com.realityflex.medback.repository;

import com.realityflex.medback.entity.DoctorMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DoctorMessageRepository extends JpaRepository<DoctorMessage, String> {
List<DoctorMessage> findAllByFakePatientIdAndActual(Integer id, Boolean actual);
    List<DoctorMessage> findAllByFakePatientId(Integer id);
}
