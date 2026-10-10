package com.baraigad.workbanner.service;

import com.baraigad.workbanner.dto.WorkBannerDto;
import com.baraigad.workbanner.entity.WorkBanner;
import com.baraigad.workbanner.repository.WorkBannerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service @RequiredArgsConstructor
public class WorkBannerService {
    private final WorkBannerRepository repository;
    private final WorkBannerStorageService storage;
    private static final List<String> PAGES = List.of("social", "environment", "donation", "donation-en", "donation-mr", "community");
    public List<WorkBannerDto> get(String pageKey) { return repository.findByPageKeyOrderByDisplayOrderAscBannerIdAsc(valid(pageKey)).stream().map(this::dto).toList(); }
    @Transactional
    public List<WorkBannerDto> add(String pageKey, List<String> urls) {
        String page = valid(pageKey);
        if ("community".equals(page)) throw new IllegalArgumentException("Save the WhatsApp Community Group Link instead of uploading an image.");
        // Contact Us displays one account/QR details image. A newly uploaded
        // donation image replaces the previous database record.
        if (page.startsWith("donation")) {
            List<WorkBanner> existing = repository.findByPageKeyOrderByDisplayOrderAscBannerIdAsc(page);
            repository.deleteAll(existing);
            existing.forEach(banner -> storage.delete(banner.getImageUrl()));
        }
        int next = (int) repository.countByPageKey(page);
        List<WorkBanner> saved = new java.util.ArrayList<>();
        for (String url : urls) if (url != null && !url.isBlank()) saved.add(WorkBanner.builder().pageKey(page).imageUrl(url).displayOrder(next++).createdDate(LocalDateTime.now()).build());
        return repository.saveAll(saved).stream().map(this::dto).toList();
    }
    public WorkBannerDto saveCommunityLink(String link) {
        String value = link == null ? "" : link.trim();
        if (!value.startsWith("https://chat.whatsapp.com/")) throw new IllegalArgumentException("Enter a valid WhatsApp Community Group Link starting with https://chat.whatsapp.com/.");
        repository.deleteAll(repository.findByPageKeyOrderByDisplayOrderAscBannerIdAsc("community"));
        return dto(repository.save(WorkBanner.builder().pageKey("community").imageUrl(value).displayOrder(0).createdDate(LocalDateTime.now()).build()));
    }
    @Transactional
    public void delete(Long bannerId) {
        WorkBanner banner = repository.findById(bannerId).orElseThrow(() -> new IllegalArgumentException("Banner image was not found."));
        repository.delete(banner);
        storage.delete(banner.getImageUrl());
    }
    private String valid(String pageKey) { String page = pageKey == null ? "" : pageKey.trim().toLowerCase(); if (!PAGES.contains(page)) throw new IllegalArgumentException("Banner page must be social, environment, donation, donation-en, donation-mr or community."); return page; }
    private WorkBannerDto dto(WorkBanner banner) { return WorkBannerDto.builder().bannerId(banner.getBannerId()).pageKey(banner.getPageKey()).imageUrl(banner.getImageUrl()).displayOrder(banner.getDisplayOrder()).build(); }
}
