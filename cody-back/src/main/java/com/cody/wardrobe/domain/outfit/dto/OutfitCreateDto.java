package com.cody.wardrobe.domain.outfit.dto;

import com.cody.wardrobe.controller.dto.outfit.OutfitRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * Service Layer에서 사용하는 코디 생성 DTO
 * Controller의 OutfitRequest를 변환하여 전달
 */
@Getter
@AllArgsConstructor
public class OutfitCreateDto {
    private String name;
    private Integer rating;
    private Integer formalityLevel;
    private String memo;
    private List<Long> itemIds;

    /**
     * OutfitRequest를 OutfitCreateDto로 변환하는 정적 팩토리 메서드
     */
    public static OutfitCreateDto from(OutfitRequest request) {
        return new OutfitCreateDto(
                request.getName(),
                request.getRating(),
                request.getFormalityLevel(),
                request.getMemo(),
                request.getItemIds()
        );
    }
}
