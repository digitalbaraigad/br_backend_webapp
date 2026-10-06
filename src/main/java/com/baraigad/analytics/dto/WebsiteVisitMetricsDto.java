package com.baraigad.analytics.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WebsiteVisitMetricsDto {
    private VisitPeriodMetric day;
    private VisitPeriodMetric week;
    private VisitPeriodMetric month;
    private VisitPeriodMetric year;
}
