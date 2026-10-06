package com.baraigad.fort.service;

import com.baraigad.common.response.PagedResponseDto;
import com.baraigad.fort.dto.FortDto;

import java.util.List;

public interface FortService {

    PagedResponseDto<FortDto> getAllForts(
            Integer pageNo,
            Integer pageSize);

//Start
FortDto createFort(FortDto dto);

    FortDto getFortById(Long id);

   // List<FortDto> getAllForts();

    FortDto updateFort(Long id, FortDto dto);

    FortDto addMediaUrls(Long id, List<String> mediaUrls);

    void deleteFort(Long id);
}
