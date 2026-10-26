package com.baraigad.fortguide;

import java.util.List;

public record GuideData(Long fortId, String title, String titleMr, String description, String descriptionMr,
                        String verifiedBy, List<Point> points, Boolean managedPoints) {
    public GuideData(Long fortId, String title, String titleMr, String description, String descriptionMr,
                     String verifiedBy, List<Point> points) {
        this(fortId,title,titleMr,description,descriptionMr,verifiedBy,points,null);
    }
    public record Point(String id, String name, String nameMr, Double longitude, Double latitude,
                        String text, String textMr, String audioData, String audioDataMr,
                        String imageData, String imageKind, String imageCredit, String imageSource, String category) {
        public Point(String id, String name, String nameMr, Double longitude, Double latitude,
                     String text, String textMr, String audioData, String audioDataMr,
                     String imageData, String imageKind, String imageCredit, String imageSource) {
            this(id,name,nameMr,longitude,latitude,text,textMr,audioData,audioDataMr,imageData,imageKind,imageCredit,imageSource,null);
        }
    }
    public record Save(Long revision, GuideData guide) {}
    public record Publish(Long revision, boolean reviewed) {}
    public record Draft(long fortId, Long revision, int version, boolean published, GuideData guide) {}
    public record Package(int schemaVersion, String format, long fortId, int version, String publishedAt, GuideData guide) {}
    public record Manifest(long fortId, String title, String titleMr, int pointCount, int version,
                           long sizeBytes, String sha256, String publishedAt) {}
}
