package com.realityflex.medback.entity;

import lombok.*;


import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer id;
    String login;
    String password;
    String inn;
    String fullName;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "role_id")
    Role role;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "patient_id")
    List<DoctorMessage> doctorMessages;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "patient_id")
    List<Pressure>  pressures;
    String doctorName;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name="patient_id")
    List<Tonometer> tonometer;


}
