package com.realityflex.medback.controller;

import com.realityflex.medback.config.jwt.JwtProvider;
import com.realityflex.medback.config.jwt.UserService;
import com.realityflex.medback.dto.PressureDto;
import com.realityflex.medback.entity.*;
import com.realityflex.medback.repository.*;
import com.realityflex.medback.service.RestTemplateGetJson;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import javax.servlet.http.Cookie;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

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


    @Autowired
    RestTemplateGetJson restTemplateGetJson;


    @PostMapping("/register/patient")
    public Object registerPatient(@RequestBody RegistrationRequest registrationRequest) {
        List<String> fullName = new ArrayList<>();
        fullName.add("Артемий Матчин");
        fullName.add("Дмитрий Ходорковский");
        fullName.add("Филип Кравич");
        fullName.add("Александр Гопак");
        fullName.add("Наталья Медичи");
            Random random =new Random();
        Patient u = new Patient();
        u.setPassword(registrationRequest.getPassword());
        u.setSnils(registrationRequest.getLogin());
        u.setFullName(fullName.get(random.nextInt(5)));
        if (patientRepository.existsBySnils(registrationRequest.getLogin())) {
            Patient memberEntity = userService.findByLoginAndPasswordPatient(registrationRequest.getLogin(), registrationRequest.getPassword());
            String token = jwtProvider.generateToken(memberEntity.getSnils());
            if (token != null) {
                return new AuthResponse(token);
            } else {
                return "такой пользователь уже существует";
            }
        } else {

            userService.savePatient(u);
            Patient memberEntity = userService.findByLoginAndPasswordPatient(registrationRequest.getLogin(), registrationRequest.getPassword());
            String token = jwtProvider.generateToken(memberEntity.getSnils());
            return new AuthResponse(token);
        }
    }

    @PostMapping("/auth/Patient")
    public AuthResponse authPatient(@RequestBody AuthRequest request) {
        Patient memberEntity = userService.findByLoginAndPasswordPatient(request.getLogin(), request.getPassword());
        String token = jwtProvider.generateToken(memberEntity.getSnils());
        return new AuthResponse(token);
    }

    @GetMapping("/patient/actualDoctorMessage")
    public List<DoctorMessage> actualDoctorMessage(@RequestHeader("Authorization") String token) {
        String patientLogin = decoder(token);
        val patient = patientRepository.findBySnils(patientLogin);
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
        val patient = patientRepository.findBySnils(patientLogin);


        return doctorMessageRepository.findAllByFakePatientId(patient.getId());
    }

    @GetMapping("/patient/allPressure")
    public List<Pressure> allPressureForPatient(@RequestHeader("Authorization") String token) {
        String patientLogin = decoder(token);
        val patient = patientRepository.findBySnils(patientLogin);


        return pressureRepository.findAllByFakePatientId(patient.getId());
    }

    @PostMapping("/doctor/allPressure")
    public List<Pressure> allPressureForDoctor(@RequestHeader("Authorization") String token) {
        String patientLogin = decoder(token);
        val patient = patientRepository.findBySnils(patientLogin);


        return pressureRepository.findAllByFakePatientId(patient.getId());
    }

    @GetMapping("patient/addActivityType")
    public void addActivityType(@RequestHeader("Authorization") String token, String activityType) {
        String patientLogin = decoder(token);
        val pressure = pressureRepository.findFirstByOrderByIdDesc();
        pressure.setActivityType(activityType);
        pressureRepository.save(pressure);
    }

    @GetMapping("/patient/addPressure")
    public void addPressure(@RequestHeader("Authorization") String token, Integer top, Integer bottom, Integer pulse) {
        String patientLogin = decoder(token);
        Pressure pressure = new Pressure();
        pressure.setBottom(bottom);
        pressure.setPulse(pulse);
        pressure.setTop(top);
        val patient = patientRepository.findBySnils(patientLogin);
        patient.getPressures().add(pressure);
        patientRepository.save(patient);


    }

    @PostMapping("/patient/addTonometer")
    public void addTonometer(@RequestHeader("Authorization") String token, String model, String serialNumber, String manufacturer) {
        String patientLogin = decoder(token);
        Tonometer tonometer = new Tonometer();
        List<Tonometer> tonometers = new ArrayList<>();
        tonometer.setModel(model);
        tonometer.setSerialNumber(serialNumber);
        tonometer.setManufacturer(manufacturer);
        tonometers.add(tonometer);
        val patient = patientRepository.findBySnils(patientLogin);
        patient.setTonometer(tonometers);

        patientRepository.save(patient);

    }


    @GetMapping("/getTable/{name}")
    public List<Patient> getTable(@PathVariable(value = "name") String name) {
        return patientRepository.findAllByDoctorName(name);
    }

    @GetMapping("/getAllPatients")
    public List<Patient> getAllPatients() {

        val patients = patientRepository.findAll();
        return patients.stream().sorted(Comparator.comparing(Patient::getId)).collect(Collectors.toList());

    }

    @GetMapping("/findByInn")
    public Patient findByInn(String snils) {
        return patientRepository.findBySnils(snils);
    }


    @GetMapping("/addDoctorForPatient")
    public void addDoctorForPatient(String patientId, String doctorName) {
        System.out.println("2222222 " + patientId + " " + doctorName);
        val patient = patientRepository.findBySnils(patientId);
        patient.setDoctorName(doctorName);
        patientRepository.save(patient);
    }

    @GetMapping("/deleteDoctorForPatient")
    public void deleteDoctorForPatient(String patientId) {
        val patient = patientRepository.findBySnils(patientId);
        System.out.println("3333333333 " + patientId);
        patient.setDoctorName(null);
        patientRepository.save(patient);
    }


    @PostMapping("/patient/getPhoto")
    public PressureDto addPhoto(@RequestHeader("Authorization") String token, @RequestParam("image") MultipartFile image) throws IOException {
        String patientLogin = decoder(token);
        Pressure pressure = new Pressure();
        Random random = new Random();
        val patient = patientRepository.findBySnils(patientLogin);
        val a = restTemplateGetJson.loadInvoices(image);
        val b = a.split(",");
        pressure.setTop(Integer.parseInt(b[0]));
        pressure.setBottom(Integer.parseInt(b[1]));
        pressure.setPulse(Integer.parseInt(b[2]));
        patient.getPressures().add(pressure);
        patientRepository.save(patient);
        PressureDto pressureDto = new PressureDto();
        pressureDto.setBottom(Integer.parseInt(b[0]));
        pressureDto.setPulse(Integer.parseInt(b[1]));
        pressureDto.setTop(Integer.parseInt(b[2]));

        return pressureDto;
    }


    /* @GetMapping("/photos/")
     public ResponseEntity<byte[]> getPhoto(@RequestParam String id, Model model) {


        // byte[] image = photoService.getPhoto(id);
        // return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(image);
     //}

     */
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
