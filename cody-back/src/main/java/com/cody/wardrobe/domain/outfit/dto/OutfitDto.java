package com.cody.wardrobe.domain.outfit.dto;

import com.cody.wardrobe.domain.item.dto.ItemDto;
import com.cody.wardrobe.domain.outfit.Outfit;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Layer에서 사용하는 코디 조회 DTO
 * Entity를 변환하여 Controller로 전달
 */
@Getter
@AllArgsConstructor
public class OutfitDto {
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
     * Outfit Entity를 OutfitDto로 변환하는 정적 팩토리 메서드
     */
    public static OutfitDto from(Outfit outfit) {
        List<OutfitItemInfo> itemInfos = outfit.getOutfitItems().stream()
                .map(outfitItem -> OutfitItemInfo.of(
                        outfitItem.getId(),
                        ItemDto.from(outfitItem.getItem())
                ))
                .collect(Collectors.toList());

        return new OutfitDto(
                outfit.getId(),
                outfit.getName(),
                outfit.getRating(),
                outfit.getFormalityLevel(),
                outfit.getMemo(),
                outfit.getWornCount(),
                outfit.getLastWornDate(),
                itemInfos,
                outfit.getCreatedAt(),
                outfit.getUpdatedAt()
        );
    }
}
