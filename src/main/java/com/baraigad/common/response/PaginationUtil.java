package com.baraigad.common.response;

import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Function;

public final class PaginationUtil {

    private PaginationUtil() {
    }

    public static <E, D> PagedResponseDto<D> build(
            Page<E> page,
            Function<E, D> mapper) {

        List<D> records = page.getContent()
                .stream()
                .map(mapper)
                .toList();

        return PagedResponseDto.<D>builder()
                .records(records)
                .totalCount(page.getTotalElements())
                .pageNo(page.getNumber() + 1)
                .pageSize(page.getSize())
                .totalPages(page.getTotalPages())
                .build();
    }
}