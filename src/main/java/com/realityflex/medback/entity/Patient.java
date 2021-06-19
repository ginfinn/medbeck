package com.realityflex.medback.entity;

import lombok.*;


import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int id;
    String login;
    String password;
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


}
