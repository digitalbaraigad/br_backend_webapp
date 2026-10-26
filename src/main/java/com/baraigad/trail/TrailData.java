package com.baraigad.trail;

import java.util.List;

/** Longitude precedes latitude, matching GeoJSON. No fabricated routes are seeded. */
public record TrailData(Long fortId, String title, String description,
                        List<List<Double>> coordinates, List<Waypoint> waypoints,
                        String verifiedBy) {
    public record Waypoint(String name, String kind, double longitude, double latitude) {}
    public record SaveRequest(Long revision, TrailData trail) {}
    public record PublishRequest(Long revision, boolean verified) {}
    public record PackageData(int schemaVersion, long id, int version, String publishedAt, TrailData trail) {}
    public record Manifest(long id, Long fortId, String title, int version, long sizeBytes,
                           String sha256, String publishedAt, String downloadUrl) {}
    public record Draft(long id, Long revision, int version, boolean published, TrailData trail) {}
}
