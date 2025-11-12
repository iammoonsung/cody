package com.cody.wardrobe.domain.item.dto;

import com.cody.wardrobe.domain.item.Item;
import com.cody.wardrobe.domain.item.ItemCategory;
import com.cody.wardrobe.domain.item.Season;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * Service Layer에서 사용하는 아이템 조회 DTO
 * Entity를 변환하여 Controller로 전달
 */
@Getter
@AllArgsConstructor
public class ItemDto {
    private Long id;
    private ItemCategory category;
    private String name;
    private String imageUrl;
    private String color;
    private Season season;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * Item Entity를 ItemDto로 변환하는 정적 팩토리 메서드
     */
    public static ItemDto from(Item item) {
        return new ItemDto(
                item.getId(),
                item.getCategory(),
                item.getName(),
                item.getImageUrl(),
                item.getColor(),
                item.getSeason(),
                item.getCreatedAt(),
                item.getUpdatedAt()
        );
    }
}
