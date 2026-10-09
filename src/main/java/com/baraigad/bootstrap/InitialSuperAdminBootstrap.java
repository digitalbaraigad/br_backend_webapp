package com.baraigad.bootstrap;

import com.baraigad.adminuser.entity.AdminRole;
import com.baraigad.adminuser.entity.AdminUser;
import com.baraigad.adminuser.repository.AdminUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * Creates the first administrative account on a fresh environment.
 *
 * <p>The three values are supplied only through environment variables. The
 * password is hashed before persistence and is never written to application
 * logs. Once the account is created, remove the password variable from the
 * deployment environment.</p>
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class InitialSuperAdminBootstrap implements ApplicationRunner {

    private static final String BOOTSTRAP_USER_ID = "SUPER_ADMIN";

    private final AdminUserRepository adminUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${INITIAL_SUPER_ADMIN_EMAIL:}")
    private String initialEmail;

    @Value("${INITIAL_SUPER_ADMIN_PASSWORD:}")
    private String initialPassword;

    @Value("${INITIAL_SUPER_ADMIN_NAME:}")
    private String initialName;

    @Override
    public void run(ApplicationArguments args) {
        if (isBlank(initialEmail) || isBlank(initialPassword) || isBlank(initialName)) {
            log.info("Initial SUPER_ADMIN bootstrap is not configured; skipping it.");
            return;
        }

        String email = initialEmail.trim().toLowerCase(Locale.ROOT);
        if (adminUserRepository.existsByUserRoleAndDelFlgFalse(AdminRole.SUPER_ADMIN.name())) {
            log.info("An active SUPER_ADMIN already exists; skipping initial account bootstrap.");
            return;
        }

        if (adminUserRepository.findByUserEmail(email).isPresent()) {
            log.warn("Initial SUPER_ADMIN bootstrap skipped because its email already belongs to an existing user.");
            return;
        }

        if (adminUserRepository.findByUserId(BOOTSTRAP_USER_ID).isPresent()) {
            log.warn("Initial SUPER_ADMIN bootstrap skipped because the reserved bootstrap user ID already exists.");
            return;
        }

        AdminUser superAdmin = new AdminUser();
        superAdmin.setUserId(BOOTSTRAP_USER_ID);
        superAdmin.setUserFName(initialName.trim());
        superAdmin.setUserEmail(email);
        superAdmin.setUserPassword(passwordEncoder.encode(initialPassword));
        superAdmin.setUserStatus("ACTIVE");
        superAdmin.setUserRole(AdminRole.SUPER_ADMIN.name());
        superAdmin.setUserType("ADMIN");
        superAdmin.setDelFlg(false);

        adminUserRepository.save(superAdmin);
        log.info("Initial SUPER_ADMIN account created for {}.", email);
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
