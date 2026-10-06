package com.baraigad.ourwork.dto;
import lombok.*;
import java.util.ArrayList;
import java.util.List;
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class OurWorkDto {
    private Long workId; private String titleEn; private String titleMr; private String headingEn; private String headingMr;
    private String subHeadingEn; private String subHeadingMr; private String descriptionEn; private String descriptionMr;
    private String workTypeEn; private String workTypeMr; private String conservationStatusEn; private String conservationStatusMr;
    private Boolean active;
    @Builder.Default private List<String> mediaUrls = new ArrayList<>();
}
