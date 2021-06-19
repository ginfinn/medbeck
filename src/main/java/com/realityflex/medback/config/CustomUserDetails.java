package com.realityflex.medback.config;


import com.realityflex.medback.entity.Doctor;
import com.realityflex.medback.entity.Patient;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails {

    private String login;
    private String password;
    private Collection<? extends GrantedAuthority> grantedAuthorities;

    public static CustomUserDetails fromUserEntityToCustomPatientDetails(Patient patient) {

            CustomUserDetails c = new CustomUserDetails();
            c.login = patient.getSnils();
            c.password = patient.getPassword();
            c.grantedAuthorities = Collections.singletonList(new SimpleGrantedAuthority(patient.getRole().getName()));
            return c;


    }
    public static CustomUserDetails fromUserEntityToCustomDoctorDetails( Doctor doctor) {


            CustomUserDetails c = new CustomUserDetails();
            c.login = doctor.getLogin();
            c.password = doctor.getPassword();
            c.grantedAuthorities = Collections.singletonList(new SimpleGrantedAuthority(doctor.getRole().getName()));
            return c;

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}