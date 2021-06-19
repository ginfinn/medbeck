package com.realityflex.medback.config;


import com.realityflex.medback.config.jwt.UserService;
import com.realityflex.medback.entity.Doctor;
import com.realityflex.medback.entity.Patient;

import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserService userService;

    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if(userService.findByLoginPatient(username).getLogin()==null) {
            val a =userService.findByLoginPatient(username);
            return  CustomUserDetails.fromUserEntityToCustomPatientDetails(a);
        }else{
        Doctor doctorEntity = userService.findByLoginDoctor(username);
        return CustomUserDetails.fromUserEntityToCustomDoctorDetails(doctorEntity);

    }
    }
}
