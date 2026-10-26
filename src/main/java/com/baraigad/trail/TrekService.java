package com.baraigad.trail;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service @RequiredArgsConstructor
public class TrekService {
    public record Point(double longitude, double latitude, double accuracy, long timestamp) {}
    public record Trek(String clientId, long trailId, int version, String title, String startedAt,
                       String endedAt, List<Point> points) {}
    private final TrekRepository repository;
    private final ObjectMapper json;

    public static void validate(Trek trek) {
        if (trek == null) throw new IllegalArgumentException("Trek is required.");
        try { UUID.fromString(trek.clientId()); }
        catch (Exception ex) { throw new IllegalArgumentException("A UUID clientId is required."); }
        if (trek.trailId() < 1 || trek.version() < 1 || trek.title() == null || trek.title().length() > 160)
            throw new IllegalArgumentException("Trail, package version and title are required.");
        long start, end;
        try { start = Instant.parse(trek.startedAt()).toEpochMilli(); end = Instant.parse(trek.endedAt()).toEpochMilli(); }
        catch (Exception ex) { throw new IllegalArgumentException("Invalid trek dates."); }
        if (end < start || end > System.currentTimeMillis() + 300000)
            throw new IllegalArgumentException("Invalid trek time range.");
        if (trek.points() == null || trek.points().isEmpty() || trek.points().size() > 20000)
            throw new IllegalArgumentException("A trek needs 1–20,000 GPS points.");
        long previous = start;
        for (Point point : trek.points()) {
            if (point == null) throw new IllegalArgumentException("Invalid GPS point.");
            TrailService.validateCoordinate(point.longitude(), point.latitude());
            if (!Double.isFinite(point.accuracy()) || point.accuracy() < 0 || point.timestamp() < previous || point.timestamp() > end)
                throw new IllegalArgumentException("Invalid GPS accuracy or timestamp.");
            previous = point.timestamp();
        }
    }

    /** Sequential retry returns the same receipt; database uniqueness also prevents racing duplicates. */
    @Transactional
    public Long sync(String owner, Trek trek) {
        validate(trek);
        var existing = repository.findByOwnerEmailAndClientId(owner, trek.clientId());
        if (existing.isPresent()) return existing.get().getId();
        TrekHistory row = new TrekHistory();
        row.setOwnerEmail(owner);
        row.setClientId(trek.clientId());
        try { row.setPayload(json.writeValueAsString(trek)); }
        catch (Exception ex) { throw new IllegalArgumentException("Invalid trek data.", ex); }
        return repository.saveAndFlush(row).getId();
    }

    public List<Trek> history(String owner, int page) {
        if (page < 0) throw new IllegalArgumentException("Page must be zero or greater.");
        return repository.findByOwnerEmailOrderByIdDesc(owner, PageRequest.of(page, 20)).stream().map(row -> {
            try { return json.readValue(row.getPayload(), Trek.class); }
            catch (Exception ex) { throw new IllegalStateException("Stored trek is invalid.", ex); }
        }).toList();
    }
}
