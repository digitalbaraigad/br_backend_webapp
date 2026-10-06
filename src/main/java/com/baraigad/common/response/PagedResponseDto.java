package com.baraigad.common.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PagedResponseDto<T> {

    private List<T> records;

    private Long totalCount;

    private Integer pageNo;

    private Integer pageSize;

    private Integer totalPages;
}