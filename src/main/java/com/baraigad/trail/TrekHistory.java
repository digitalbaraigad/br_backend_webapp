package com.baraigad.trail;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity @Table(name = "trek_histories", uniqueConstraints = @UniqueConstraint(columnNames = {"owner_email", "client_id"}))
@Getter @Setter
public class TrekHistory {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(name = "owner_email", nullable = false) private String ownerEmail;
    @Column(name = "client_id", nullable = false, length = 36) private String clientId;
    @Column(nullable = false, columnDefinition = "TEXT") private String payload;
}
