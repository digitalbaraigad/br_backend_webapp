package com.baraigad.adminmenu.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "admin_menu_access", uniqueConstraints =
        @UniqueConstraint(name = "uk_admin_menu_access_user_menu", columnNames = {"user_id", "menu_key"}))
@Getter
@Setter
@NoArgsConstructor
public class AdminMenuAccess {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "menu_key", nullable = false)
    private String menuKey;

    public AdminMenuAccess(String userId, String menuKey) {
        this.userId = userId;
        this.menuKey = menuKey;
    }
}
