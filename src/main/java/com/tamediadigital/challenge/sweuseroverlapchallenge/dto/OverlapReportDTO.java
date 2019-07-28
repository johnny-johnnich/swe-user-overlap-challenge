package com.tamediadigital.challenge.sweuseroverlapchallenge.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OverlapReportDTO {

    private Long totalUserCount;
    private Long overlapUserCount;
    private BigDecimal overlapPercentage;
}
