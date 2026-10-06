package com.baraigad.common.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ListResponseDto<T> {

    private List<T> records;
    private Long totalCount;
}