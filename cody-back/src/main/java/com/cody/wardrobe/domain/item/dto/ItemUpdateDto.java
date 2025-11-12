package com.cody.wardrobe.domain.item.dto;

import com.cody.wardrobe.controller.dto.item.ItemRequest;
import com.cody.wardrobe.domain.item.ItemCategory;
import com.cody.wardrobe.domain.item.Season;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Service Layer에서 사용하는 아이템 수정 DTO
 * Controller의 ItemRequest를 변환하여 전달
 */
@Getter
@AllArgsConstructor
public class ItemUpdateDto {
    private ItemCategory category;
    private String imageUrl;
    private String name;
    private String color;
    private Season season;

    /**
     * ItemRequest를 ItemUpdateDto로 변환하는 정적 팩토리 메서드
     */
    public static ItemUpdateDto from(ItemRequest request) {
        return new ItemUpdateDto(
                request.getCategory(),
                request.getImageUrl(),
                request.getName(),
                request.getColor(),
                request.getSeason()
        );
    }
}
