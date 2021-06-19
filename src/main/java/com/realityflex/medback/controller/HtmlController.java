package com.realityflex.medback.controller;

import com.realityflex.medback.repository.*;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import com.realityflex.medback.config.jwt.JwtProvider;
import com.realityflex.medback.config.jwt.UserService;
import com.realityflex.medback.entity.Doctor;
import com.realityflex.medback.entity.DoctorMessage;
import com.realityflex.medback.repository.DoctorRepository;
import com.realityflex.medback.repository.PatientRepository;
import com.realityflex.medback.repository.PressureRepository;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Controller
public class HtmlController {
    @Autowired
    PressureRepository pressureRepository;
    @Autowired
    PatientRepository patientRepository;
    @Autowired
    TonometerRepository tonometerRepository;

    @Autowired
    DoctorRepository doctorRepository;
    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    UserService userService;

    @GetMapping("/create-project")
    public String createProjectForm(Model model, Date to, Date from, int patientId) {
        List<Integer> arrUp = new ArrayList<>();
        List<Integer> arrDown = new ArrayList<>();
        List<Integer> arrPulse = new ArrayList<>();
        List<Date> dates = new ArrayList<>();
        List<Date> tonometerUpdate = new ArrayList<>();
        if (to != null & from != null) {
            val sortedTonometers = tonometerRepository.findAllByDateUpdateBetweenAndFakePatientId(to, from, patientId);
            val sortedPressures = pressureRepository.findAllByIdBetweenAndFakePatientId(to, from, patientId);
            for (val sortedPressure : sortedPressures) {
                for (val sortedTonometer : sortedTonometers) {
                    arrUp.add(sortedPressure.getTop());
                    arrDown.add(sortedPressure.getBottom());
                    arrPulse.add(sortedPressure.getPulse());
                    dates.add(sortedPressure.getId());
                    tonometerUpdate.add(sortedTonometer.getDateUpdate());
                }
            }
        } else {
            val tonometers = tonometerRepository.findAllByFakePatientId(patientId);
            val pressures = pressureRepository.findAllByFakePatientId(patientId);
            for (val pressure : pressures) {
                for (val tonometer : tonometers) {
                    arrUp.add(pressure.getTop());
                    arrDown.add(pressure.getBottom());
                    arrPulse.add(pressure.getPulse());
                    dates.add(pressure.getId());
                    tonometerUpdate.add(tonometer.getDateUpdate());
                }

            }
        }

        model.addAttribute("ArrUp", arrUp);
        model.addAttribute("ArrDown", arrDown);
        model.addAttribute("ArrPulse", arrPulse);
        model.addAttribute("Date", dates);
        model.addAttribute("DateUpdateTonometers", tonometerUpdate);
        return "create-project";
    }

    @GetMapping("/doctor/createTable/{name}")
    public String createTable(@PathVariable(value = "name") String name, Model model) {
        model.addAttribute("patients", patientRepository.findAllByDoctorName(name));
        System.out.println(":::: " + patientRepository.findAllByDoctorName(name).toString());
        return "htmlTable";
    }

    @GetMapping("/doctor/showStats/{name}")
    public String displayStats(@PathVariable(value="name") String name, Model model) {
        val patient = patientRepository.findBySnils(patientRepository.findBySnils(name).getSnils());
        model.addAttribute("stats", pressureRepository.findAllByFakePatientId(patient.getId()));
        return "create-project";
    }

    @GetMapping("/")
    public String login(Model model) {
        return "login";
    }


    @RequestMapping(value = "/sendMsg", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = {MediaType.APPLICATION_ATOM_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public @ResponseBody
    void doctorModel(String text, Integer patientId, String phone, String doctorName) {
        DoctorMessage doctorMessage = new DoctorMessage();
        doctorMessage.setText(text);
        doctorMessage.setPhone(phone);
        doctorMessage.setDoctorName(doctorName);
        val patient = patientRepository.findById(patientId).get();
        patient.getDoctorMessages().add(doctorMessage);
        patientRepository.save(patient);
    }



    @PostMapping("/auth/Doctor")
    public String authDoctor(@RequestBody AuthRequest request, Model model) {
        Doctor memberEntity = userService.findByLoginAndPasswordDoctor(request.getLogin(), request.getPassword());
        String token = jwtProvider.generateToken(memberEntity.getLogin());
        model.addAttribute("token", token);
        return "htmlTable";
    }

    //@RequestMapping(params = "reg", method = RequestMethod.POST, path = "/register/Doctor")
    @RequestMapping(value = "/register/Doctor", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = {MediaType.APPLICATION_ATOM_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    //@PostMapping("/register/Doctor")
    public String registerDoctor(RegistrationRequest registrationRequest, HttpServletResponse response, Model model) {
        Doctor u = new Doctor();
        u.setPassword(registrationRequest.getPassword());
        u.setLogin(registrationRequest.getLogin());
        if (doctorRepository.existsByLogin(registrationRequest.getLogin())) {

            Doctor memberEntity = userService.findByLoginAndPasswordDoctor(registrationRequest.getLogin(), registrationRequest.getPassword());
            String token = jwtProvider.generateToken(memberEntity.getLogin());
            model.addAttribute("token", token);
            if ( token != null) {
                response.addHeader("Authorization", "Bearer " + token);
                Cookie cookie = new Cookie("Authorization", token);
                response.addCookie(cookie);
                String userLogin = jwtProvider.getLoginFromToken(token);
                return "redirect:/doctor/createTable/" + userLogin;
            } else {
                return "такой пользователь уже существует";
            }
        } else {
            userService.saveDoctor(u);
            Doctor memberEntity = userService.findByLoginAndPasswordDoctor(registrationRequest.getLogin(), registrationRequest.getPassword());
            String token = jwtProvider.generateToken(memberEntity.getLogin());
            model.addAttribute("token", token);
            response.addHeader("Authorization", "Bearer " + token);
            Cookie cookie = new Cookie("Authorization", token);
            response.addCookie(cookie);
            String userLogin = jwtProvider.getLoginFromToken(token);
            return "redirect:/doctor/createTable/" + userLogin;
        }
    }


    @GetMapping("/testApi/{name}")
    public @ResponseBody
    String getAttr(@PathVariable(value = "name") String name) {
        return name;
    }

}
