package com.realityflex.medback.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Tonometer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer id;
    String model;
    String serialNumber;
    @Column(name = "patient_id")
    Integer fakePatientId;
}
