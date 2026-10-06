package com.baraigad.mohim.service;

import com.baraigad.activity.entity.ActivityType;
import com.baraigad.activity.entity.ActivityMaster;
import com.baraigad.activity.repository.ActivityMasterRepository;
import com.baraigad.common.exception.DuplicateResourceException;
import com.baraigad.common.exception.ResourceNotFoundException;
import com.baraigad.common.response.PagedResponseDto;
import com.baraigad.common.response.PaginationUtil;
import com.baraigad.mohim.dto.MohimDto;
import com.baraigad.mohim.entity.Mohim;
import com.baraigad.mohim.mapper.MohimMapper;
import com.baraigad.mohim.repository.MohimRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MohimServiceImpl implements MohimService {

    private final MohimRepository mohimRepository;
    @Autowired
    private ActivityMasterRepository activityRepository;

    @Override
    public MohimDto createMohim(MohimDto dto) {

        if (mohimRepository.existsByMohimNameAndDelFlgFalse(
                dto.getMohimName())) {

            throw new DuplicateResourceException(
                    "Mohim already exists : "
                            + dto.getMohimName());
        }

        Mohim mohim = MohimMapper.mapToEntity(dto);

        mohim.setCreatedDate(LocalDateTime.now());
        mohim.setDelFlg(false);

        ActivityMaster activity = new ActivityMaster();

        activity.setActivityName(dto.getMohimName());
        activity.setActivityType(ActivityType.MOHIM);
        activity.setStartDate(dto.getStartDate());
        activity.setEndDate(dto.getEndDate());
        activity.setDescription(dto.getDescription());
        activity.setLocation(dto.getLocation());
        activity.setStatus(dto.getStatus());

        activity = activityRepository.save(activity);

        mohim.setActivityId(activity.getActivityId());
        Mohim savedMohim =
                mohimRepository.save(mohim);

        return MohimMapper.mapToDto(savedMohim);
    }

    @Override
    public MohimDto getMohimById(Long mohimId) {

        Mohim mohim =
                mohimRepository
                        .findByMohimIdAndDelFlgFalse(
                                mohimId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Mohim not found"));

        return MohimMapper.mapToDto(mohim);
    }

    @Override
    public PagedResponseDto<MohimDto> getAllMohims(
            Integer pageNo,
            Integer pageSize) {

        Pageable pageable =
                PageRequest.of(
                        pageNo - 1,
                        pageSize);

        Page<Mohim> mohimPage =
                mohimRepository
                        .findByDelFlgFalse(
                                pageable);

        return PaginationUtil.build(
                mohimPage,
                MohimMapper::mapToDto);
    }

    @Override
    public MohimDto updateMohim(
            Long mohimId,
            MohimDto dto) {

        Mohim mohim =
                mohimRepository
                        .findByMohimIdAndDelFlgFalse(
                                mohimId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Mohim not found"));

        Mohim existingMohim =
                mohimRepository
                        .findByMohimNameAndDelFlgFalse(
                                dto.getMohimName())
                        .orElse(null);

        if (existingMohim != null
                && !existingMohim.getMohimId()
                .equals(mohimId)) {

            throw new DuplicateResourceException(
                    "Mohim already exists : "
                            + dto.getMohimName());
        }

        mohim.setMohimName(dto.getMohimName());
        mohim.setMohimNameMr(dto.getMohimNameMr());
        mohim.setMohimType(dto.getMohimType());
        mohim.setMohimTypeMr(dto.getMohimTypeMr());
        mohim.setFortId(dto.getFortId());
        mohim.setStartDate(dto.getStartDate());
        mohim.setEndDate(dto.getEndDate());
        mohim.setLocation(dto.getLocation());
        mohim.setLocationMr(dto.getLocationMr());
        mohim.setDescription(dto.getDescription());
        mohim.setDescriptionMr(dto.getDescriptionMr());
        mohim.setOrganizerName(dto.getOrganizerName());
        mohim.setOrganizerNameMr(dto.getOrganizerNameMr());
        mohim.setStartPoint(dto.getStartPoint());
        mohim.setStartPointMr(dto.getStartPointMr());
        mohim.setEndPoint(dto.getEndPoint());
        mohim.setEndPointMr(dto.getEndPointMr());
        mohim.setDistance(dto.getDistance());
        mohim.setDuration(dto.getDuration());
        mohim.setDurationMr(dto.getDurationMr());
        mohim.setDifficulty(dto.getDifficulty());
        mohim.setDifficultyMr(dto.getDifficultyMr());
        mohim.setWhatsappGroupLink(dto.getWhatsappGroupLink());
        mohim.setPhotoUrls(dto.getPhotoUrls() == null ? new ArrayList<>() : new ArrayList<>(dto.getPhotoUrls()));
        mohim.setStatus(dto.getStatus());

        mohim.setUpdatedDate(LocalDateTime.now());

        Mohim updatedMohim =
                mohimRepository.save(mohim);

        return MohimMapper.mapToDto(updatedMohim);
    }

    @Override
    public MohimDto addPhotoUrls(Long mohimId, List<String> photoUrls) {
        Mohim mohim = mohimRepository.findByMohimIdAndDelFlgFalse(mohimId)
                .orElseThrow(() -> new ResourceNotFoundException("Mohim not found"));
        List<String> existing = mohim.getPhotoUrls() == null
                ? new ArrayList<>()
                : new ArrayList<>(mohim.getPhotoUrls());
        existing.addAll(photoUrls == null ? List.of() : photoUrls.stream()
                .filter(url -> url != null && !url.isBlank())
                .toList());
        mohim.setPhotoUrls(existing);
        mohim.setUpdatedDate(LocalDateTime.now());
        return MohimMapper.mapToDto(mohimRepository.save(mohim));
    }

    @Override
    public void deleteMohim(Long mohimId) {

        Mohim mohim =
                mohimRepository
                        .findByMohimIdAndDelFlgFalse(
                                mohimId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Mohim not found"));

        mohim.setDelFlg(true);
        mohim.setUpdatedDate(LocalDateTime.now());

        mohimRepository.save(mohim);
    }
}
