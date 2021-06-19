package com.realityflex.medback.controller;

import com.realityflex.medback.entity.DoctorMessage;
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

@Controller
public class HtmlController {
    @Autowired
    PressureRepository pressureRepository;
    @Autowired
    PatientRepository patientRepository;

    @GetMapping("/create-project")
    public String createProjectForm(Model model, Date to, Date from, int patientId) {
        List<Integer> arrUp = new ArrayList<>();
        List<Integer> arrDown = new ArrayList<>();
        List<Integer> arrPulse = new ArrayList<>();
        if (to != null & from != null) {
            val sortedPressures = pressureRepository.findAllByIdBetweenAndFakePatientId(to, from, patientId);
            for (val sortedPressure : sortedPressures) {
                arrUp.add(sortedPressure.getTop());
                arrDown.add(sortedPressure.getBottom());
                arrPulse.add(sortedPressure.getPulse());
            }

        } else {
            val pressures = pressureRepository.findAllByFakePatientId(patientId);
            for (val Pressure : pressures) {
                arrUp.add(Pressure.getTop());
                arrDown.add(Pressure.getBottom());
                arrPulse.add(Pressure.getPulse());
            }

        }

        model.addAttribute("ArrUp", arrUp);
        model.addAttribute("ArrDown", arrDown);
        model.addAttribute("ArrPulse", arrPulse);
        return "create-project";
    }

    @GetMapping("/createTable")
    public String createTable(Model model,@RequestHeader("Authorization") String token) {
        String doctorLogin = decoder(token);
       model.addAttribute("patients", patientRepository.findAllByDoctorName(doctorLogin));
        return "htmlTable";
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
}
