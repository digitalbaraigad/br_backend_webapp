package com.baraigad.fort.service;

import com.baraigad.common.response.PagedResponseDto;
import com.baraigad.common.response.PaginationUtil;
import com.baraigad.fort.dto.FortDto;
import com.baraigad.fort.entity.Fort;
import com.baraigad.fort.mapper.FortMapper;
import com.baraigad.fort.repository.FortRepository;
import com.baraigad.fort.service.FortService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FortServiceImpl implements FortService {

    private final FortRepository fortRepository;

    @Override
    public FortDto createFort(FortDto fortDto) {

        Fort fort = FortMapper.mapToEntity(fortDto);

        fort.setFortId(null);

        Fort savedFort = fortRepository.save(fort);

        return FortMapper.mapToDto(savedFort);
    }

    @Override
    public FortDto getFortById(Long fortId) {

        Fort fort = fortRepository.findById(fortId)
                .orElseThrow(() ->
                        new RuntimeException("Fort not found"));

        return FortMapper.mapToDto(fort);
    }

   /* @Override
    public List<FortDto> getAllForts() {

        return fortRepository.findAll()
                .stream()
                .map(FortMapper::mapToDto)
                .toList();
    }*/

    @Override
    public PagedResponseDto<FortDto> getAllForts(
            Integer pageNo,
            Integer pageSize) {

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);

        Page<Fort> fortPage = fortRepository.findByDelFlgFalse(pageable);

        return PaginationUtil.build(
                fortPage,
                FortMapper::mapToDto);
    }

    @Override
    public FortDto updateFort(Long fortId,
                              FortDto fortDto) {

        Fort fort = fortRepository.findById(fortId)
                .orElseThrow(() ->
                        new RuntimeException("Fort not found"));

        fort.setFortName(fortDto.getFortName());
        fort.setDistrict(fortDto.getDistrict());
        fort.setDistrictMr(fortDto.getDistrictMr());
        fort.setState(fortDto.getState());
        fort.setElevation(fortDto.getElevation());
        fort.setLatitude(fortDto.getLatitude());
        fort.setLongitude(fortDto.getLongitude());
        fort.setDescriptionEn(fortDto.getDescriptionEn());
        fort.setDescriptionMr(fortDto.getDescriptionMr());
        fort.setFortTitleEn(fortDto.getFortTitleEn());
        fort.setFortTitleMr(fortDto.getFortTitleMr());
        fort.setHeadingEn(fortDto.getHeadingEn());
        fort.setHeadingMr(fortDto.getHeadingMr());
        fort.setSubHeadingEn(fortDto.getSubHeadingEn());
        fort.setSubHeadingMr(fortDto.getSubHeadingMr());
        fort.setAboutHeadingEn(fortDto.getAboutHeadingEn());
        fort.setAboutHeadingMr(fortDto.getAboutHeadingMr());
        fort.setFortTypeEn(fortDto.getFortTypeEn());
        fort.setFortTypeMr(fortDto.getFortTypeMr());
        fort.setConservationStatusEn(fortDto.getConservationStatusEn());
        fort.setConservationStatusMr(fortDto.getConservationStatusMr());
        fort.setCoverMediaId(fortDto.getCoverMediaId());
        fort.setMediaUrls(fortDto.getMediaUrls() == null ? new ArrayList<>() : new ArrayList<>(fortDto.getMediaUrls()));
        fort.setStatus(fortDto.getStatus());

        Fort updatedFort = fortRepository.save(fort);

        return FortMapper.mapToDto(updatedFort);
    }

    @Override
    public FortDto addMediaUrls(Long fortId, List<String> mediaUrls) {
        Fort fort = fortRepository.findById(fortId)
                .orElseThrow(() -> new RuntimeException("Fort not found"));
        List<String> existing = fort.getMediaUrls() == null ? new ArrayList<>() : new ArrayList<>(fort.getMediaUrls());
        existing.addAll(mediaUrls == null ? List.of() : mediaUrls.stream().filter(url -> url != null && !url.isBlank()).toList());
        fort.setMediaUrls(existing);
        return FortMapper.mapToDto(fortRepository.save(fort));
    }

    @Override
    public void deleteFort(Long fortId) {

        Fort fort = fortRepository.findById(fortId)
                .orElseThrow(() ->
                        new RuntimeException("Fort not found"));

        fortRepository.delete(fort);
    }
}
