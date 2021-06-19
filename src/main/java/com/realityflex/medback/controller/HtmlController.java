package com.realityflex.medback.controller;

import com.realityflex.medback.entity.DoctorMessage;
import com.realityflex.medback.repository.PatientRepository;
import com.realityflex.medback.repository.PressureRepository;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import com.realityflex.medback.config.jwt.JwtProvider;
import com.realityflex.medback.config.jwt.UserService;
import com.realityflex.medback.entity.Doctor;
import com.realityflex.medback.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import com.realityflex.medback.config.jwt.JwtProvider;
import com.realityflex.medback.config.jwt.UserService;
import com.realityflex.medback.entity.Doctor;
import com.realityflex.medback.entity.DoctorMessage;
import com.realityflex.medback.repository.DoctorRepository;
import com.realityflex.medback.repository.PatientRepository;
import com.realityflex.medback.repository.PressureRepository;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import org.springframework.web.bind.annotation.*;

@Controller
public class HtmlController {
    @Autowired
    PressureRepository pressureRepository;
    @Autowired
    PatientRepository patientRepository;

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
        if (to != null & from != null) {
            val sortedPressures = pressureRepository.findAllByIdBetweenAndFakePatientId(to, from, patientId);
            for (val sortedPressure : sortedPressures) {
                arrUp.add(sortedPressure.getTop());
                arrDown.add(sortedPressure.getBottom());
                arrPulse.add(sortedPressure.getPulse());
                dates.add(sortedPressure.getId());
            }

        } else {
            val pressures = pressureRepository.findAllByFakePatientId(patientId);
            for (val pressure : pressures) {
                arrUp.add(pressure.getTop());
                arrDown.add(pressure.getBottom());
                arrPulse.add(pressure.getPulse());
                dates.add(pressure.getId());
            }

        }

        model.addAttribute("ArrUp", arrUp);
        model.addAttribute("ArrDown", arrDown);
        model.addAttribute("ArrPulse", arrPulse);
        model.addAttribute("Date",dates);
        return "create-project";
    }

    @GetMapping("/createTable")
    public String createTable(Model model,@RequestHeader("Authorization") String token) {
        String doctorLogin = decoder(token);
       model.addAttribute("patients", patientRepository.findAllByDoctorName(doctorLogin));
        return "htmlTable";
    }

    @GetMapping("/")
    public String login(Model model) {
        return "login";
    }


    @RequestMapping(value = "/sendMsg", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = {MediaType.APPLICATION_ATOM_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public @ResponseBody
    void doctorModel(String text, Integer patientId) throws Exception {
        DoctorMessage doctorMessage = new DoctorMessage();
        doctorMessage.setText(text);
        val patient = patientRepository.findById(patientId).get();
        patient.getDoctorMessages().add(doctorMessage);
        patientRepository.save(patient);


    }
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

    @PostMapping("/auth/Doctor")
    public String authDoctor(@RequestBody AuthRequest request,Model model) {
        Doctor memberEntity = userService.findByLoginAndPasswordDoctor(request.getLogin(), request.getPassword());
        String token = jwtProvider.generateToken(memberEntity.getLogin());
        model.addAttribute("token", token);
        return "htmlTable";
    }

    //@RequestMapping(params = "reg", method = RequestMethod.POST, path = "/register/Doctor")
    @RequestMapping(value = "/register/Doctor", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = {MediaType.APPLICATION_ATOM_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    //@PostMapping("/register/Doctor")
    public String registerDoctor(RegistrationRequest registrationRequest,Model model) {
        Doctor u = new Doctor();
        u.setPassword(registrationRequest.getPassword());
        u.setLogin(registrationRequest.getLogin());
        if (doctorRepository.existsByLogin(registrationRequest.getLogin())) {
            return "такой пользователь уже существует";
        } else {
            userService.saveDoctor(u);
            Doctor memberEntity = userService.findByLoginAndPasswordDoctor(registrationRequest.getLogin(), registrationRequest.getPassword());
            String token = jwtProvider.generateToken(memberEntity.getLogin());
            model.addAttribute("token", token);
            return "htmlTable";
        }
    }
}
