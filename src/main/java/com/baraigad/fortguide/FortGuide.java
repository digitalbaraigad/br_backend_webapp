package com.baraigad.fortguide;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity @Table(name = "fort_guides") @Getter @Setter
public class FortGuide {
    @Id private Long fortId;
    @Version private Long revision;
    @Column(nullable = false, columnDefinition = "TEXT") private String draftJson;
    @Column(columnDefinition = "TEXT") private String publishedJson;
    private int packageVersion;
    private boolean published;
}
