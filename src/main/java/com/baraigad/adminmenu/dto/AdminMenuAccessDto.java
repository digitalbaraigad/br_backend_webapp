package com.baraigad.adminmenu.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminMenuAccessDto {
    private String userId;
    private boolean customAccessConfigured;
    private List<String> menuKeys;
}
