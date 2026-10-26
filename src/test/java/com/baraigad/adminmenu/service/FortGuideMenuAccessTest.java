package com.baraigad.adminmenu.service;

import com.baraigad.adminmenu.repository.AdminMenuAccessRepository;
import com.baraigad.adminuser.entity.AdminUser;
import com.baraigad.adminuser.repository.AdminUserRepository;
import org.junit.jupiter.api.Test;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FortGuideMenuAccessTest {
    @Test void guidePermissionIsIndependentOfExistingFortAndOtherMenus() {
        var access=mock(AdminMenuAccessRepository.class);var users=mock(AdminUserRepository.class);
        var user=new AdminUser();user.setUserId("admin-test");user.setUserRole("ADMIN");
        when(users.findByUserEmailAndDelFlgFalse("test@example.test")).thenReturn(Optional.of(user));
        when(access.existsByUserId("admin-test")).thenReturn(true);
        when(access.existsByUserIdAndMenuKey("admin-test","Fort")).thenReturn(true);
        var service=new AdminMenuAccessService(access,users);
        assertTrue(service.isRequestAllowed("test@example.test","/api/v1/forts/3"));
        assertFalse(service.isRequestAllowed("test@example.test","/api/v1/fort-guides/3/publish"));
        when(access.existsByUserIdAndMenuKey("admin-test","Fort Guide Management")).thenReturn(true);
        assertTrue(service.isRequestAllowed("test@example.test","/api/v1/fort-guides/3"));
        assertTrue(service.isRequestAllowed("test@example.test","/api/v1/fort-guides/3/publish"));
        assertFalse(service.isRequestAllowed("test@example.test","/api/v1/blog/3"));
        assertTrue(AdminMenuAccessService.ADMIN_MENU_KEYS.contains("Fort Guide Management"));
    }
    @Test void defaultAdminAndSuperAdminKeepExistingAccessRules() {
        var access=mock(AdminMenuAccessRepository.class);var users=mock(AdminUserRepository.class);
        var user=new AdminUser();user.setUserId("admin-test");user.setUserRole("ADMIN");
        when(users.findByUserEmailAndDelFlgFalse("test@example.test")).thenReturn(Optional.of(user));
        var service=new AdminMenuAccessService(access,users);
        assertTrue(service.isRequestAllowed("test@example.test","/api/v1/fort-guides/3"));
        user.setUserRole("SUPER_ADMIN");when(access.existsByUserId("admin-test")).thenReturn(true);
        assertTrue(service.isRequestAllowed("test@example.test","/api/v1/fort-guides/3/publish"));
    }
}
