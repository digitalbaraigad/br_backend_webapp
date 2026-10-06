package com.baraigad.mohim.service;

import com.baraigad.common.response.PagedResponseDto;
import com.baraigad.mohim.dto.MohimDto;
import java.util.List;

public interface MohimService {

    MohimDto createMohim(
            MohimDto dto);

    MohimDto getMohimById(
            Long mohimId);

    PagedResponseDto<MohimDto> getAllMohims(
            Integer pageNo,
            Integer pageSize);

    MohimDto updateMohim(
            Long mohimId,
            MohimDto dto);

    MohimDto addPhotoUrls(Long mohimId, List<String> photoUrls);

    void deleteMohim(
            Long mohimId);
}
