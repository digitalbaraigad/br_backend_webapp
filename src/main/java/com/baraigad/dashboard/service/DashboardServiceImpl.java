package com.baraigad.dashboard.service;

import com.baraigad.blog.entity.Blog;
import com.baraigad.blog.repository.BlogRepository;
import com.baraigad.dashboard.dto.*;
import com.baraigad.dashboard.service.DashboardService;
import com.baraigad.document.entity.DocumentRepository;
import com.baraigad.document.repository.DocumentRepositoryRepository;
import com.baraigad.fort.entity.Fort;
import com.baraigad.fort.repository.FortRepository;
import com.baraigad.gallery.entity.Gallery;
import com.baraigad.gallery.repository.GalleryRepository;
import com.baraigad.mohim.entity.Mohim;
import com.baraigad.mohim.repository.MohimRepository;
import com.baraigad.volunteer.repository.VolunteerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl
        implements DashboardService {

    private final VolunteerRepository volunteerRepository;
    private final FortRepository fortRepository;
    private final MohimRepository mohimRepository;
    private final GalleryRepository galleryRepository;
    private final BlogRepository blogRepository;
    private final DocumentRepositoryRepository documentRepository;

    @Override
    public DashboardResponseDto getHomepageDashboard() {

        DashboardStatisticsDto statistics =
                DashboardStatisticsDto.builder()
                        .volunteerCount(
                                volunteerRepository.countByDelFlgFalse())
                        .fortCount(
                                fortRepository.countByDelFlgFalse())
                        .mohimCount(
                                mohimRepository.countByDelFlgFalse())
                        .galleryCount(
                                galleryRepository.countByDelFlgFalse())
                        .blogCount(
                                blogRepository.countByDelFlgFalse())
                        .documentCount(
                                documentRepository.countByDelFlgFalse())
                        .build();

        BannerDto banner =
                BannerDto.builder()
                        .title("Together for Our Forts, Heritage & Future")
                        .subTitle("Digital BaRaigad is committed to preserving our heritage.")
                        .bannerImage("/images/banner/home-banner.jpg")
                        .build();

        List<UpcomingMohimDto> upcomingMohims =
                mohimRepository
                        .findTop3ByDelFlgFalseAndStartDateGreaterThanEqualOrderByStartDateAsc(
                                LocalDate.now())
                        .stream()
                        .map(this::mapMohim)
                        .toList();

        List<RecentBlogDto> recentBlogs =
                blogRepository
                        .findTop3ByDelFlgFalseAndStatusOrderByPublishDateDesc(
                                "PUBLISHED")
                        .stream()
                        .map(this::mapBlog)
                        .toList();

        List<RecentGalleryDto> recentGallery =
                galleryRepository
                        .findTop6ByDelFlgFalseAndStatusOrderByCreatedDateDesc(
                                "ACTIVE")
                        .stream()
                        .map(this::mapGallery)
                        .toList();

        List<RecentDocumentDto> recentDocuments =
                documentRepository
                        .findTop5ByDelFlgFalseOrderByCreatedDateDesc()
                        .stream()
                        .map(this::mapDocument)
                        .toList();

        List<FeaturedFortDto> featuredForts =
                fortRepository
                        .findTop4ByDelFlgFalseAndStatusTrueOrderByCreatedDateDesc()
                        .stream()
                        .map(this::mapFort)
                        .toList();

        return DashboardResponseDto.builder()
                .statistics(statistics)
                .banner(banner)
                .upcomingMohims(upcomingMohims)
                .recentBlogs(recentBlogs)
                .recentGallery(recentGallery)
                .recentDocuments(recentDocuments)
                .featuredForts(featuredForts)
                .build();
    }

    private RecentBlogDto mapBlog(Blog blog) {

        return RecentBlogDto.builder()
                .blogId(blog.getBlogId())
                .blogTitle(blog.getBlogTitle())
                .blogSlug(blog.getBlogSlug())
                .blogAuthor(blog.getBlogAuthor())
                .coverMediaId(blog.getCoverMediaId())
                .publishDate(
                        blog.getPublishDate() != null
                                ? blog.getPublishDate().toString()
                                : null)
                .build();
    }

    private UpcomingMohimDto mapMohim(Mohim mohim) {

        return UpcomingMohimDto.builder()
                .mohimId(mohim.getMohimId())
                .mohimName(mohim.getMohimName())
                .mohimNameMr(mohim.getMohimNameMr())
                .location(mohim.getLocation())
                .locationMr(mohim.getLocationMr())
                .startDate(
                        mohim.getStartDate() != null
                                ? mohim.getStartDate().toString()
                                : null)
                .endDate(
                        mohim.getEndDate() != null
                                ? mohim.getEndDate().toString()
                                : null)
                .build();
    }

    private RecentGalleryDto mapGallery(Gallery gallery) {

        return RecentGalleryDto.builder()
                .galleryId(gallery.getGalleryId())
                .galleryTitle(gallery.getGalleryTitle())
                .mediaUrl(gallery.getMediaUrl())
                .thumbnailUrl(gallery.getThumbnailUrl())
                .mediaType(gallery.getMediaType())
                .build();
    }

    private RecentDocumentDto mapDocument(
            DocumentRepository document) {

        return RecentDocumentDto.builder()
                .documentId(document.getDocumentId())
                .documentTitle(document.getDocumentTitle())
                .documentType(
                        document.getDocumentType() != null
                                ? document.getDocumentType().name()
                                : null)
                .documentUrl(document.getDocumentUrl())
                .build();
    }

    private FeaturedFortDto mapFort(Fort fort) {

        return FeaturedFortDto.builder()
                .fortId(fort.getFortId())
                .fortName(fort.getFortName())
                .district(fort.getDistrict())
                .state(fort.getState())
                .coverMediaId(fort.getCoverMediaId())
                .build();
    }
}
