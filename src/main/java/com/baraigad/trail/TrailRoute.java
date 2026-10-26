package com.baraigad.trail;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity @Table(name = "trail_routes") @Getter @Setter
public class TrailRoute {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Version private Long revision;
    @Column(nullable = false, columnDefinition = "TEXT") private String draftJson;
    @Column(columnDefinition = "TEXT") private String publishedJson;
    private int packageVersion;
    private boolean published;
}
