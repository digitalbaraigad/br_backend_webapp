package com.baraigad.bulkcommunication.dto;

import java.util.List;

public record BulkDispatchResultDto(
        int requested,
        int sent,
        int skipped,
        int failed,
        List<String> notes
) {}
