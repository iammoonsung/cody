package com.cody.wardrobe.controller.dto.outfit;

import com.cody.wardrobe.domain.outfit.dto.OutfitDto;
import com.cody.wardrobe.domain.outfit.dto.OutfitItemInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Controller Layer에서 사용하는 코디 응답 DTO
 * Service DTO를 변환하여 클라이언트에 전달
 */
@Getter
@AllArgsConstructor
public class OutfitResponse {
    private Long id;
    private String name;
    private Integer rating;
    private Integer formalityLevel;
    private String memo;
    private Integer wornCount;
    private LocalDate lastWornDate;
    private List<OutfitItemInfo> outfitItems;  // 프론트엔드와 일치시키기 위해 items -> outfitItems로 변경
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * OutfitDto를 OutfitResponse로 변환하는 정적 팩토리 메서드
     */
    public static OutfitResponse from(OutfitDto dto) {
        return new OutfitResponse(
                dto.getId(),
                dto.getName(),
                dto.getRating(),
                dto.getFormalityLevel(),
                dto.getMemo(),
                dto.getWornCount(),
                dto.getLastWornDate(),
                dto.getOutfitItems(),
                dto.getCreatedAt(),
                dto.getUpdatedAt()
        );
    }
}
