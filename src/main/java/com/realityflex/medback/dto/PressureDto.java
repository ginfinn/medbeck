package com.realityflex.medback.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PressureDto {
    Integer top;
    Integer bottom;
    Integer pulse;
}
