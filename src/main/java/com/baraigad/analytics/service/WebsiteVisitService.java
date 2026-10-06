package com.baraigad.analytics.service;

import com.baraigad.analytics.dto.WebsiteVisitMetricsDto;

public interface WebsiteVisitService {
    void registerVisit(String visitorId);
    WebsiteVisitMetricsDto getMetrics();
}
