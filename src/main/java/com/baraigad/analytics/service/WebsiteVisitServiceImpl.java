package com.baraigad.analytics.service;

import com.baraigad.analytics.dto.VisitPeriodMetric;
import com.baraigad.analytics.dto.WebsiteVisitMetricsDto;
import com.baraigad.analytics.entity.WebsiteVisit;
import com.baraigad.analytics.repository.WebsiteVisitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class WebsiteVisitServiceImpl implements WebsiteVisitService {
    private final WebsiteVisitRepository websiteVisitRepository;

    @Override
    public void registerVisit(String visitorId) {
        WebsiteVisit visit = new WebsiteVisit();
        visit.setVisitorId(visitorId.trim());
        visit.setVisitedAt(LocalDateTime.now());
        websiteVisitRepository.save(visit);
    }

    @Override
    public WebsiteVisitMetricsDto getMetrics() {
        LocalDate today = LocalDate.now();
        LocalDate weekStart = today.with(DayOfWeek.MONDAY);
        LocalDate monthStart = today.withDayOfMonth(1);
        LocalDate yearStart = today.withDayOfYear(1);
        return new WebsiteVisitMetricsDto(
                metric(today, today.plusDays(1), today.minusDays(1), today),
                metric(weekStart, weekStart.plusWeeks(1), weekStart.minusWeeks(1), weekStart),
                metric(monthStart, monthStart.plusMonths(1), monthStart.minusMonths(1), monthStart),
                metric(yearStart, yearStart.plusYears(1), yearStart.minusYears(1), yearStart)
        );
    }

    private VisitPeriodMetric metric(LocalDate start, LocalDate end, LocalDate previousStart, LocalDate previousEnd) {
        long current = websiteVisitRepository.countByVisitedAtGreaterThanEqualAndVisitedAtLessThan(start.atStartOfDay(), end.atStartOfDay());
        long previous = websiteVisitRepository.countByVisitedAtGreaterThanEqualAndVisitedAtLessThan(previousStart.atStartOfDay(), previousEnd.atStartOfDay());
        double change = previous == 0 ? (current == 0 ? 0 : 100) : Math.round(((current - previous) * 10000.0 / previous)) / 100.0;
        return new VisitPeriodMetric(current, previous, change, change > 0 ? "UP" : change < 0 ? "DOWN" : "UNCHANGED");
    }
}
