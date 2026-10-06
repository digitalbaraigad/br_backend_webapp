package com.baraigad.adminuser.entity;

import java.util.Locale;

/** Roles accepted by the administrative portal. */
public enum AdminRole {
    ADMIN,
    SUPER_ADMIN;

    public static String normalize(String value) {
        if (value == null || value.isBlank()) {
            return ADMIN.name();
        }

        String normalized = value.trim().toUpperCase(Locale.ROOT).replace('-', '_').replace(' ', '_');
        try {
            return AdminRole.valueOf(normalized).name();
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException("User role must be ADMIN or SUPER_ADMIN.");
        }
    }
}
