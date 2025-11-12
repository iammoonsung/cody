package com.cody.wardrobe.domain.item.dto;

import com.cody.wardrobe.controller.dto.item.ItemRequest;
import com.cody.wardrobe.domain.item.ItemCategory;
import com.cody.wardrobe.domain.item.Season;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Service Layer에서 사용하는 아이템 생성 DTO
 * Controller의 ItemRequest를 변환하여 전달
 */
@Getter
@AllArgsConstructor
public class ItemCreateDto {
    private ItemCategory category;
    private String imageUrl;
    private String name;
    private String color;
    private Season season;

    /**
     * ItemRequest를 ItemCreateDto로 변환하는 정적 팩토리 메서드
     */
    public static ItemCreateDto from(ItemRequest request) {
        return new ItemCreateDto(
                request.getCategory(),
                request.getImageUrl(),
                request.getName(),
                request.getColor(),
                request.getSeason()
        );
    }
}
