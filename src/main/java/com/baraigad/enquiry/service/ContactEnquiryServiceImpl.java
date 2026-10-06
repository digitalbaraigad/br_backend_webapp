package com.baraigad.enquiry.service;

import com.baraigad.enquiry.dto.ContactEnquiryDto;
import com.baraigad.enquiry.entity.ContactEnquiry;
import com.baraigad.enquiry.repository.ContactEnquiryRepository;
import com.baraigad.common.response.PagedResponseDto;
import com.baraigad.common.response.PaginationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContactEnquiryServiceImpl implements ContactEnquiryService {
    private final ContactEnquiryRepository repository;

    @Override
    public ContactEnquiryDto create(ContactEnquiryDto dto) {
        ContactEnquiry enquiry = new ContactEnquiry();
        enquiry.setFullName(dto.getFullName().trim());
        enquiry.setEmailId(dto.getEmailId().trim());
        enquiry.setPhoneNumber(dto.getPhoneNumber().trim());
        enquiry.setWhatsappPhoneNumber(dto.getWhatsappPhoneNumber().trim());
        enquiry.setMessage(dto.getMessage().trim());
        return toDto(repository.save(enquiry));
    }

    @Override
    public PagedResponseDto<ContactEnquiryDto> getAll(Integer pageNo, Integer pageSize) {
        int safePageNo = Math.max(1, pageNo == null ? 1 : pageNo);
        int safePageSize = Math.max(1, Math.min(pageSize == null ? 10 : pageSize, 100));
        Pageable pageable = PageRequest.of(
                safePageNo - 1,
                safePageSize,
                Sort.by(Sort.Direction.DESC, "createdAt"));
        return PaginationUtil.build(repository.findAll(pageable), this::toDto);
    }

    private ContactEnquiryDto toDto(ContactEnquiry enquiry) {
        ContactEnquiryDto dto = new ContactEnquiryDto();
        dto.setEnquiryId(enquiry.getEnquiryId());
        dto.setFullName(enquiry.getFullName());
        dto.setEmailId(enquiry.getEmailId());
        dto.setPhoneNumber(enquiry.getPhoneNumber());
        dto.setWhatsappPhoneNumber(enquiry.getWhatsappPhoneNumber());
        dto.setMessage(enquiry.getMessage());
        dto.setCreatedAt(enquiry.getCreatedAt());
        return dto;
    }
}
