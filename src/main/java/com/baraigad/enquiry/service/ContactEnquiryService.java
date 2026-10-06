package com.baraigad.enquiry.service;

import com.baraigad.enquiry.dto.ContactEnquiryDto;
import com.baraigad.common.response.PagedResponseDto;

public interface ContactEnquiryService {
    ContactEnquiryDto create(ContactEnquiryDto dto);
    PagedResponseDto<ContactEnquiryDto> getAll(Integer pageNo, Integer pageSize);
}
