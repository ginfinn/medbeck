package com.realityflex.medback.controller;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HtmlController {

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


    @RequestMapping(value = "/sendMsg", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = {MediaType.APPLICATION_ATOM_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public @ResponseBody
    String doctorModel(String text) throws Exception {
        //System.out.println(text);
        return text;
    }
}
