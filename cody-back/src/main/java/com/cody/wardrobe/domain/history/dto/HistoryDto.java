package com.cody.wardrobe.domain.history.dto;

import com.cody.wardrobe.domain.history.History;
import com.cody.wardrobe.domain.outfit.dto.OutfitDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Service Layer에서 사용하는 착용 기록 조회 DTO
 * Entity를 변환하여 Controller로 전달
 */
@Getter
@AllArgsConstructor
public class HistoryDto {
    private Long id;
    private OutfitDto outfit;
    private LocalDate wornDate;
    private LocalDateTime createdAt;

    /**
     * History Entity를 HistoryDto로 변환하는 정적 팩토리 메서드
     */
    public static HistoryDto from(History history) {
        return new HistoryDto(
                history.getId(),
                OutfitDto.from(history.getOutfit()),
                history.getWornDate(),
                history.getCreatedAt()
        );
    }
}
