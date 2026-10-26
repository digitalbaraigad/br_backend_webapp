package com.baraigad.workbanner.service;

import com.baraigad.workbanner.repository.WorkBannerRepository;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class WorkBannerServiceTest {

    @Test
    void loadsBannersForTheNewOurWorkPages() {
        WorkBannerRepository repository = mock(WorkBannerRepository.class);
        when(repository.findByPageKeyOrderByDisplayOrderAscBannerIdAsc("trekking")).thenReturn(List.of());
        when(repository.findByPageKeyOrderByDisplayOrderAscBannerIdAsc("ceremonies")).thenReturn(List.of());
        when(repository.findByPageKeyOrderByDisplayOrderAscBannerIdAsc("adventure")).thenReturn(List.of());
        WorkBannerService service = new WorkBannerService(repository, mock(WorkBannerStorageService.class));

        assertDoesNotThrow(() -> service.get("trekking"));
        assertDoesNotThrow(() -> service.get("ceremonies"));
        assertDoesNotThrow(() -> service.get("adventure"));

        verify(repository).findByPageKeyOrderByDisplayOrderAscBannerIdAsc(eq("trekking"));
        verify(repository).findByPageKeyOrderByDisplayOrderAscBannerIdAsc(eq("ceremonies"));
        verify(repository).findByPageKeyOrderByDisplayOrderAscBannerIdAsc(eq("adventure"));
    }
}
