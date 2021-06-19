package com.realityflex.medback.config.jwt;


import com.realityflex.medback.entity.Doctor;
import com.realityflex.medback.entity.Patient;
import com.realityflex.medback.entity.Role;
import com.realityflex.medback.repository.DoctorRepository;
import com.realityflex.medback.repository.PatientRepository;

import com.realityflex.medback.repository.RoleRepository;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private RoleRepository roleEntityRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;


    public void savePatient(Patient patient) {

        Role userRole = roleEntityRepository.findByName("ROLE_USER");
        patient.setRole(userRole);
        patient.setPassword(passwordEncoder.encode(patient.getPassword()));
        patientRepository.save(patient);


    }

    public void saveDoctor(Doctor doctor) {


        Role userRole = roleEntityRepository.findByName("ROLE_DOCTOR");
        doctor.setRole(userRole);
        doctor.setPassword(passwordEncoder.encode(doctor.getPassword()));
        doctorRepository.save(doctor);


    }

    public Doctor findByLoginDoctor(String login) {

        return doctorRepository.findByLogin(login);


    }

    public Patient findByLoginPatient(String login) {


        return patientRepository.findByLogin(login);

    }

    public Patient findByLoginAndPasswordPatient(String login, String password) {
        val memberEntity = findByLoginPatient(login);
        if (memberEntity != null) {
            if (passwordEncoder.matches(password, memberEntity.getPassword())) {
                return memberEntity;
            }
        }
        return null;
    }


    public Doctor findByLoginAndPasswordDoctor(String login, String password) {
        val memberEntity = findByLoginDoctor(login);
        if (memberEntity != null) {
            if (passwordEncoder.matches(password, memberEntity.getPassword())) {
                return memberEntity;
            }
        }
        return null;
    }
}