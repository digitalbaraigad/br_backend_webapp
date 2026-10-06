package com.baraigad.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeaturedFortDto {

    private Long fortId;

    private String fortName;

    private String district;

    private String state;

    private Long coverMediaId;
}