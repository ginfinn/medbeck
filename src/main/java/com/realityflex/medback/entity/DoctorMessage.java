package com.realityflex.medback.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class DoctorMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int id;
    String text;
    String doctorName;
    String phone;
    @Builder.Default
    Date date = new Date();
    @Builder.Default
    Boolean actual = true;
    @Column(name = "patient_id")
    Integer fakePatientId;
}
