package com.cody.wardrobe.controller.dto.outfit;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class OutfitRequest {

    @Size(max = 200, message = "코디 이름은 200자 이하여야 합니다.")
    private String name;

    @NotNull(message = "평점은 필수입니다.")
    @Min(value = 1, message = "평점은 1 이상이어야 합니다.")
    @Max(value = 5, message = "평점은 5 이하여야 합니다.")
    private Integer rating;

    @NotNull(message = "격식도는 필수입니다.")
    @Min(value = 1, message = "격식도는 1 이상이어야 합니다.")
    @Max(value = 5, message = "격식도는 5 이하여야 합니다.")
    private Integer formalityLevel;

    private String memo;

    @NotEmpty(message = "최소 1개 이상의 아이템이 필요합니다.")
    private List<Long> itemIds;

    public OutfitRequest(String name, Integer rating, Integer formalityLevel, String memo, List<Long> itemIds) {
        this.name = name;
        this.rating = rating;
        this.formalityLevel = formalityLevel;
        this.memo = memo;
        this.itemIds = itemIds;
    }
}
