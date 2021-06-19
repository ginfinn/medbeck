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
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int id;
    String login;
    String password;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "roleid")
    Role role;

}
