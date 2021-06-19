package com.realityflex.medback.controller;

import com.realityflex.medback.config.jwt.JwtProvider;
import com.realityflex.medback.config.jwt.UserService;
import com.realityflex.medback.entity.Doctor;
import com.realityflex.medback.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class HtmlController {

    @Autowired
    DoctorRepository doctorRepository;
    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    UserService userService;

    @GetMapping("/create-project")
    public String createProjectForm(Model model) {

        model.addAttribute("ArrUp", new int[]{1,23,432,654,13,543,76});
        model.addAttribute("ArrDown", new int[]{423,543,123,76});
        model.addAttribute("ArrPulse", new int[]{77,88,99,22,66,77});
        return "create-project";
    }

    @GetMapping("/createTable")
    public String createTable(Model model) {

        return "htmlTable";
    }

    @GetMapping("/")
    public String login(Model model) {
        return "login";
    }


    @RequestMapping(value = "/sendMsg", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = {MediaType.APPLICATION_ATOM_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public @ResponseBody
    String doctorModel(String text) throws Exception {
        //System.out.println(text);
        return text;
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
