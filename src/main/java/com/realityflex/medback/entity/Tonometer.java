package com.realityflex.medback.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Tonometer {
    @Id
    Date dateUpdate = new Date();
    String model;
    String serialNumber;
    @Column(name = "patient_id")
    Integer fakePatientId;
}
