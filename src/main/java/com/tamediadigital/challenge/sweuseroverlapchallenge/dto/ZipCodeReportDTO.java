package com.tamediadigital.challenge.sweuseroverlapchallenge.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ZipCodeReportDTO {

    private Integer zipCode;
    private Integer count;
}