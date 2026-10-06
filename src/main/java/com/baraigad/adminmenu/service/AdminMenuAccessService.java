package com.baraigad.adminmenu.service;

import com.baraigad.adminmenu.dto.AdminMenuAccessDto;
import com.baraigad.adminmenu.entity.AdminMenuAccess;
import com.baraigad.adminmenu.repository.AdminMenuAccessRepository;
import com.baraigad.adminuser.entity.AdminRole;
import com.baraigad.adminuser.entity.AdminUser;
import com.baraigad.adminuser.repository.AdminUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AdminMenuAccessService {
    private static final String NO_MENU_ACCESS = "__NO_MENU_ACCESS__";
    public static final List<String> ADMIN_MENU_KEYS = List.of(
            "Volunteer", "Participants", "Contact Enquiries", "Dynamic Forms",
            "Volunteer Activity", "Our Work", "Work Banners", "Fort", "Mohim", "Gallery", "Blog",
            "Documents", "Birthday Count", "Test Email");

    private final AdminMenuAccessRepository accessRepository;
    private final AdminUserRepository userRepository;

    public AdminMenuAccessDto getForUser(String userId) {
        AdminUser user = activeUser(userId);
        List<String> savedKeys = accessRepository.findByUserIdOrderByMenuKeyAsc(user.getUserId())
                .stream().map(AdminMenuAccess::getMenuKey).toList();
        List<String> keys = savedKeys.stream().filter(key -> !NO_MENU_ACCESS.equals(key)).toList();
        return new AdminMenuAccessDto(user.getUserId(), !savedKeys.isEmpty(), keys);
    }

    @Transactional
    public AdminMenuAccessDto replaceForAdmin(String userId, List<String> requestedKeys) {
        AdminUser user = activeUser(userId);
        if (AdminRole.SUPER_ADMIN.name().equals(AdminRole.normalize(user.getUserRole()))) {
            throw new IllegalArgumentException("Super Admin access cannot be restricted with menu mapping.");
        }

        List<String> keys = requestedKeys == null ? List.of() : requestedKeys.stream()
                .filter(Objects::nonNull).map(String::trim).filter(value -> !value.isEmpty())
                .distinct().sorted().toList();
        if (!ADMIN_MENU_KEYS.containsAll(keys)) {
            throw new IllegalArgumentException("One or more selected admin menus are invalid.");
        }

        // Execute and flush the replacement delete before inserting the new selection.
        // Without this, Hibernate can attempt an insert before the queued delete and
        // violate the unique (user_id, menu_key) database constraint.
        accessRepository.deleteByUserId(user.getUserId());
        accessRepository.flush();
        List<String> keysToSave = keys.isEmpty() ? List.of(NO_MENU_ACCESS) : keys;
        accessRepository.saveAllAndFlush(keysToSave.stream()
                .map(key -> new AdminMenuAccess(user.getUserId(), key))
                .toList());
        return new AdminMenuAccessDto(user.getUserId(), true, keys);
    }

    public AdminMenuAccessDto getForEmail(String email) {
        AdminUser user = userRepository.findByUserEmailAndDelFlgFalse(email)
                .orElseThrow(() -> new IllegalArgumentException("Admin user was not found."));
        if (AdminRole.SUPER_ADMIN.name().equals(AdminRole.normalize(user.getUserRole()))) {
            return new AdminMenuAccessDto(user.getUserId(), false, ADMIN_MENU_KEYS);
        }
        return getForUser(user.getUserId());
    }

    /** A user with no saved mapping keeps the existing full standard-Admin access. */
    public boolean isRequestAllowed(String email, String path) {
        AdminUser user = userRepository.findByUserEmailAndDelFlgFalse(email).orElse(null);
        if (user == null || AdminRole.SUPER_ADMIN.name().equals(AdminRole.normalize(user.getUserRole()))) return true;
        String menuKey = menuForPath(path);
        return menuKey == null || !accessRepository.existsByUserId(user.getUserId())
                || accessRepository.existsByUserIdAndMenuKey(user.getUserId(), menuKey);
    }

    private AdminUser activeUser(String userId) {
        return userRepository.findByUserIdAndDelFlgFalse(userId)
                .orElseThrow(() -> new IllegalArgumentException("Admin user was not found."));
    }

    private String menuForPath(String path) {
        if (path.startsWith("/api/v1/volunteer")) return "Volunteer";
        if (path.startsWith("/api/v1/participants")) return "Participants";
        if (path.startsWith("/api/v1/contact-enquiries")) return "Contact Enquiries";
        if (path.startsWith("/api/v1/dynamic-forms")) return "Dynamic Forms";
        if (path.startsWith("/api/v1/volunteer-activities") || path.startsWith("/api/v1/activities")) return "Volunteer Activity";
        if (path.startsWith("/api/v1/our-work")) return "Our Work";
        if (path.startsWith("/api/v1/work-banners")) return "Work Banners";
        if (path.startsWith("/api/v1/forts")) return "Fort";
        if (path.startsWith("/api/v1/mohim")) return "Mohim";
        if (path.startsWith("/api/v1/gallery")) return "Gallery";
        if (path.startsWith("/api/v1/blog")) return "Blog";
        if (path.startsWith("/api/v1/document")) return "Documents";
        if (path.startsWith("/api/email/birthday/count")) return "Birthday Count";
        if (path.startsWith("/api/email/")) return "Test Email";
        return null;
    }
}
