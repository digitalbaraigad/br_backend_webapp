package com.baraigad.fort.mapper;

import com.baraigad.fort.dto.FortDto;
import com.baraigad.fort.entity.Fort;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FortMapperTest {

    @Test
    void preservesEnglishAndMarathiFortNamesInBothDirections() {
        Fort fort = Fort.builder()
                .fortId(11L)
                .fortName("Raigad Fort")
                .fortNameMr("रायगड किल्ला")
                .build();

        FortDto dto = FortMapper.mapToDto(fort);

        assertEquals("Raigad Fort", dto.getFortName());
        assertEquals("रायगड किल्ला", dto.getFortNameMr());

        Fort mappedFort = FortMapper.mapToEntity(dto);
        assertEquals("Raigad Fort", mappedFort.getFortName());
        assertEquals("रायगड किल्ला", mappedFort.getFortNameMr());
    }
}
