package com.cody.wardrobe.controller.dto.item;

import com.cody.wardrobe.domain.item.ItemCategory;
import com.cody.wardrobe.domain.item.Season;
import com.cody.wardrobe.domain.item.dto.ItemDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * Controller Layer에서 사용하는 아이템 응답 DTO
 * Service DTO를 변환하여 클라이언트에 전달
 */
@Getter
@AllArgsConstructor
public class ItemResponse {
    private Long id;
    private ItemCategory category;
    private String name;
    private String imageUrl;
    private String color;
    private Season season;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * ItemDto를 ItemResponse로 변환하는 정적 팩토리 메서드
     */
    public static ItemResponse from(ItemDto dto) {
        return new ItemResponse(
                dto.getId(),
                dto.getCategory(),
                dto.getName(),
                dto.getImageUrl(),
                dto.getColor(),
                dto.getSeason(),
                dto.getCreatedAt(),
                dto.getUpdatedAt()
        );
    }
}
