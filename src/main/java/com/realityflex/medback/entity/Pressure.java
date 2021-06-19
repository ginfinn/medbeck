package com.realityflex.medback.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Pressure {
    @Id
    private final Date id = new Date();
    int top;
    int bottom;
    int pulse;
    String patientMessage;
    @Column(name = "patient_id")
    Integer fakePatientId;

}
