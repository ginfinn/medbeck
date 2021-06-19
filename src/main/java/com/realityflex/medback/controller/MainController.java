package com.realityflex.medback.controller;

import com.realityflex.medback.config.jwt.JwtProvider;
import com.realityflex.medback.config.jwt.UserService;
import com.realityflex.medback.entity.*;
import com.realityflex.medback.repository.*;
//import com.realityflex.medback.service.PhotoService;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.List;

@RestController
public class MainController {
    @Autowired
    DoctorMessageRepository doctorMessageRepository;
    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    UserService userService;
    @Autowired
    PatientRepository patientRepository;
    @Autowired
    DoctorRepository doctorRepository;
    @Autowired
    PressureRepository pressureRepository;

    //    @Autowired
//    PhotoService photoService;
    @PostMapping("/register/patient")
    public Object registerPatient(@RequestBody RegistrationRequest registrationRequest) {
        Patient u = new Patient();
        u.setPassword(registrationRequest.getPassword());
        u.setLogin(registrationRequest.getLogin());
        if (patientRepository.existsByLogin(registrationRequest.getLogin())) {
            return "такой пользователь уже существует";
        } else {

            userService.savePatient(u);
            Patient memberEntity = userService.findByLoginAndPasswordPatient(registrationRequest.getLogin(), registrationRequest.getPassword());
            String token = jwtProvider.generateToken(memberEntity.getLogin());
            return new AuthResponse(token);
        }
    }

    @PostMapping("/auth/Patient")
    public AuthResponse authPatient(@RequestBody AuthRequest request) {
        Patient memberEntity = userService.findByLoginAndPasswordPatient(request.getLogin(), request.getPassword());
        String token = jwtProvider.generateToken(memberEntity.getLogin());
        return new AuthResponse(token);
    }

    @GetMapping("/patient/actualDoctorMessage")
    public List<DoctorMessage> actualDoctorMessage(@RequestHeader("Authorization") String token) {
        String patientLogin = decoder(token);
        val patient = patientRepository.findByLogin(patientLogin);
        val actualMessages = doctorMessageRepository.findAllByFakePatientIdAndActual(patient.getId(), true
        );
        for (val actualMessage : actualMessages) {
            actualMessage.setActual(false);
        }
        doctorMessageRepository.saveAll(actualMessages);
        return actualMessages;
    }

    @GetMapping("/patient/allDoctorMessage")
    public List<DoctorMessage> allDoctorMessage(@RequestHeader("Authorization") String token) {
        String patientLogin = decoder(token);
        val patient = patientRepository.findByLogin(patientLogin);


        return doctorMessageRepository.findAllByFakePatientId(patient.getId());
    }

    @GetMapping("/patient/allPressure")
    public List<Pressure> allPressureForPatient(@RequestHeader("Authorization") String token) {
        String patientLogin = decoder(token);
        val patient = patientRepository.findByLogin(patientLogin);


        return pressureRepository.findAllByFakePatientId(patient.getId());
    }

    @PostMapping("/doctor/allPressure")
    public List<Pressure> allPressureForDoctor(@RequestHeader("Authorization") String token) {
        String patientLogin = decoder(token);
        val patient = patientRepository.findByLogin(patientLogin);


        return pressureRepository.findAllByFakePatientId(patient.getId());
    }

    @GetMapping("/patient/addPressure")
    public void addPressure(@RequestHeader("Authorization") String token, Integer top, Integer bottom, Integer pulse) {
        String patientLogin = decoder(token);
        Pressure pressure = new Pressure();
        pressure.setBottom(bottom);
        pressure.setPulse(pulse);
        pressure.setTop(top);
        val patient = patientRepository.findByLogin(patientLogin);
        patient.getPressures().add(pressure);
        patientRepository.save(patient);

    }

    @PostMapping("/patient/addTonometer")
    public void addTonometer(@RequestParam String token, String model, String serialNumber) {
        String patientLogin = decoder(token);
        val patient = patientRepository.findByLogin(patientLogin);
        patient.getTonometer().setModel(model);
        patient.getTonometer().setSerialNumber(serialNumber);
        patientRepository.save(patient);

    }
    @GetMapping("/getAllPatients")
    public List<Patient> getAllPatients(){
      return patientRepository.findAll();
    }
    @GetMapping("/findByInn")
    public Patient findByInn(String inn){
        return patientRepository.findByInn(inn);
    }
    @PostMapping("/addDoctorForPatient")
    public void addDoctorForPatient(Integer patientId,String doctorName){
        val patient = patientRepository.findById(patientId).get();
        patient.setDoctorName(doctorName);
        patientRepository.save(patient);
    }
    @PostMapping("/deleteDoctorForPatient")
    public  void deleteDoctorForPatient(Integer patientId){
        val patient = patientRepository.findById(patientId).get();
        patient.setDoctorName("");
        patientRepository.save(patient);
    }



//    @PostMapping("/addPhoto")
//    public String addPhoto(String title, @RequestParam("file") MultipartFile image, Model model) throws IOException {
//
//        String id = photoService.addPhoto(title, image);
//        return "redirect:/photos/" + id;
//    }


    /* @GetMapping("/photos/")
     public ResponseEntity<byte[]> getPhoto(@RequestParam String id, Model model) {


        // byte[] image = photoService.getPhoto(id);
        // return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(image);
     //}
    // @PostMapping("/addMessage")*/
    public String decoder(String token) {
        Base64.Decoder decoder = Base64.getDecoder();
        String[] chunks;
        chunks = token.split("Bearer ");
        String a = chunks[1];
        String[] chunks2 = a.split("\\.");
        String[] v;
        String payload = new String(decoder.decode(chunks2[1]));
        String[] c = payload.split(":");
        v = c[1].split(",");
        String memberName = v[0];
        memberName = memberName.replace("\"", "");
        return memberName;

    }

}
