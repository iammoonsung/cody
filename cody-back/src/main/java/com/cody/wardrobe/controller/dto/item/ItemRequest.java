package com.cody.wardrobe.controller.dto.item;

import com.cody.wardrobe.domain.item.ItemCategory;
import com.cody.wardrobe.domain.item.Season;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ItemRequest {

    @NotNull(message = "카테고리는 필수입니다.")
    private ItemCategory category;

    @NotBlank(message = "이미지 URL은 필수입니다.")
    // Base64 이미지를 지원하기 위해 크기 제한 제거 (DB에서 MEDIUMTEXT로 관리)
    private String imageUrl;

    @Size(max = 200, message = "이름은 200자 이하여야 합니다.")
    private String name;

    @Size(max = 50, message = "색상은 50자 이하여야 합니다.")
    private String color;

    private Season season;

    public ItemRequest(ItemCategory category, String imageUrl, String name, String color, Season season) {
        this.category = category;
        this.imageUrl = imageUrl;
        this.name = name;
        this.color = color;
        this.season = season;
    }
}
