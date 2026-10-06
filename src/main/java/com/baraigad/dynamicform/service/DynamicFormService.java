package com.baraigad.dynamicform.service;
import com.baraigad.dynamicform.dto.*;
import com.baraigad.common.response.PagedResponseDto;
import java.util.List;
public interface DynamicFormService {
    DynamicFormDto create(DynamicFormDto form);
    PagedResponseDto<DynamicFormDto> getAll(Integer pageNo, Integer pageSize);
    DynamicFormDto getPublicForm(String publicId);
    DynamicFormSubmissionDto submit(String publicId, DynamicFormSubmissionRequest request);
    List<DynamicFormSubmissionDto> getSubmissions(Long formId);
    java.util.Map<String, String> findSharedFormProfileByMobile(String mobileNumber);
}
