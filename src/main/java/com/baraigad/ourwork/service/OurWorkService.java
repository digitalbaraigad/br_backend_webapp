package com.baraigad.ourwork.service;
import com.baraigad.ourwork.dto.OurWorkDto;
import java.util.List;
public interface OurWorkService {
    OurWorkDto create(OurWorkDto dto); List<OurWorkDto> getAll(); OurWorkDto get(Long id); OurWorkDto update(Long id, OurWorkDto dto); OurWorkDto addMedia(Long id, List<String> urls); void delete(Long id);
}
