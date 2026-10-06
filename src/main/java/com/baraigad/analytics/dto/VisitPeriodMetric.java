package com.baraigad.analytics.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VisitPeriodMetric {
    private long visits;
    private long previousVisits;
    private double percentageChange;
    private String trend;
}
