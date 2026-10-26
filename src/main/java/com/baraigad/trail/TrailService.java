package com.baraigad.trail;

import com.baraigad.common.exception.ResourceNotFoundException;
import com.baraigad.fort.repository.FortRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.HexFormat;
import java.util.List;
import java.util.Objects;
import static com.baraigad.trail.TrailData.*;

@Service @RequiredArgsConstructor
public class TrailService {
    private final TrailRepository repository;
    private final FortRepository forts;
    private final ObjectMapper json;

    public static void validate(TrailData data) {
        if (data == null || data.fortId() == null || data.fortId() < 1
                || data.title() == null || data.title().isBlank() || data.title().length() > 160
                || data.description() == null || data.description().length() > 10000)
            throw new IllegalArgumentException("Fort, title (up to 160 characters) and description are required.");
        if (data.coordinates() == null || data.coordinates().size() < 2 || data.coordinates().size() > 20000)
            throw new IllegalArgumentException("A trail needs 2–20,000 coordinates.");
        for (List<Double> point : data.coordinates()) {
            if (point == null || point.size() != 2 || point.get(0) == null || point.get(1) == null)
                throw new IllegalArgumentException("Each coordinate must be [longitude, latitude].");
            validateCoordinate(point.get(0), point.get(1));
        }
        if (data.waypoints() == null || data.waypoints().size() > 500)
            throw new IllegalArgumentException("At most 500 waypoints are supported.");
        for (Waypoint point : data.waypoints()) {
            if (point == null || point.name() == null || point.name().isBlank() || point.name().length() > 160
                    || point.kind() == null || !List.of("gate", "water", "junction", "summit", "warning", "other").contains(point.kind()))
                throw new IllegalArgumentException("Every waypoint needs a name and valid kind.");
            validateCoordinate(point.longitude(), point.latitude());
        }
        if (data.verifiedBy() == null || data.verifiedBy().length() > 160)
            throw new IllegalArgumentException("Reviewer name must be up to 160 characters.");
    }

    public static void validateCoordinate(double lon, double lat) {
        if (!Double.isFinite(lon) || !Double.isFinite(lat) || lon < -180 || lon > 180 || lat < -90 || lat > 90)
            throw new IllegalArgumentException("Invalid longitude or latitude.");
    }

    @Transactional
    public Draft save(Long id, SaveRequest request) {
        if (request == null) throw new IllegalArgumentException("Trail is required.");
        validate(request.trail());
        var fort = forts.findById(request.trail().fortId()).orElseThrow(() -> new IllegalArgumentException("Fort was not found."));
        if (Boolean.TRUE.equals(fort.getDelFlg()) || Boolean.FALSE.equals(fort.getStatus()))
            throw new IllegalArgumentException("Fort is deleted or inactive.");
        TrailRoute route = id == null ? new TrailRoute() : route(id);
        if (id != null) checkRevision(route, request.revision());
        route.setDraftJson(write(request.trail()));
        return draft(repository.saveAndFlush(route));
    }

    @Transactional
    public Draft publish(long id, PublishRequest request) {
        TrailRoute route = route(id);
        if (request == null || !request.verified()) throw new IllegalArgumentException("Confirm that the route was verified before publishing.");
        checkRevision(route, request.revision());
        TrailData trail = read(route.getDraftJson(), TrailData.class);
        validate(trail);
        if (trail.verifiedBy().isBlank()) throw new IllegalArgumentException("Enter the route reviewer name.");
        route.setPackageVersion(route.getPackageVersion() + 1);
        route.setPublishedJson(write(new PackageData(1, id, route.getPackageVersion(), Instant.now().toString(), trail)));
        route.setPublished(true);
        return draft(repository.saveAndFlush(route));
    }

    @Transactional
    public Draft unpublish(long id, PublishRequest request) {
        TrailRoute route = route(id);
        if (request == null) throw new IllegalArgumentException("Revision is required.");
        checkRevision(route, request.revision());
        route.setPublished(false);
        return draft(repository.saveAndFlush(route));
    }

    public List<Draft> drafts(int page) {
        return repository.findAll(PageRequest.of(page(page), 50)).stream().map(this::draft).toList();
    }

    public List<Manifest> catalogue(int page) {
        return repository.findByPublishedTrue(PageRequest.of(page(page), 50)).stream().map(this::manifest).toList();
    }

    public byte[] download(long id) {
        TrailRoute route = route(id);
        if (!route.isPublished()) throw new ResourceNotFoundException("Trail is not published.");
        return route.getPublishedJson().getBytes(StandardCharsets.UTF_8);
    }

    private int page(int page) {
        if (page < 0) throw new IllegalArgumentException("Page must be zero or greater.");
        return page;
    }

    private TrailRoute route(long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Trail was not found."));
    }

    private void checkRevision(TrailRoute route, Long revision) {
        if (!Objects.equals(route.getRevision(), revision)) throw new IllegalArgumentException("Trail changed. Reload drafts before saving or publishing.");
    }

    private Draft draft(TrailRoute route) {
        return new Draft(route.getId(), route.getRevision(), route.getPackageVersion(), route.isPublished(), read(route.getDraftJson(), TrailData.class));
    }

    public Manifest manifest(TrailRoute route) {
        byte[] bytes = route.getPublishedJson().getBytes(StandardCharsets.UTF_8);
        PackageData data = read(route.getPublishedJson(), PackageData.class);
        try {
            String hash = HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(bytes));
            return new Manifest(route.getId(), data.trail().fortId(), data.trail().title(), data.version(), bytes.length,
                    hash, data.publishedAt(), "/api/public/trails/" + route.getId() + "/package");
        } catch (java.security.NoSuchAlgorithmException ex) { throw new IllegalStateException(ex); }
    }

    private String write(Object data) {
        try { return json.writeValueAsString(data); }
        catch (Exception ex) { throw new IllegalArgumentException("Trail data cannot be serialized.", ex); }
    }
    private <T> T read(String data, Class<T> type) {
        try { return json.readValue(data, type); }
        catch (Exception ex) { throw new IllegalStateException("Stored trail is invalid.", ex); }
    }
}
