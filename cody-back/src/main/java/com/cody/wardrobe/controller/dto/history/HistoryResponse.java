package com.cody.wardrobe.controller.dto.history;

import com.cody.wardrobe.domain.history.dto.HistoryDto;
import com.cody.wardrobe.domain.outfit.dto.OutfitDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Controller Layer에서 사용하는 착용 기록 응답 DTO
 * Service DTO를 변환하여 클라이언트에 전달
 */
@Getter
@AllArgsConstructor
public class HistoryResponse {
    private Long id;
    private OutfitDto outfit;
    private LocalDate wornDate;
    private LocalDateTime createdAt;

    /**
     * HistoryDto를 HistoryResponse로 변환하는 정적 팩토리 메서드
     */
    public static HistoryResponse from(HistoryDto dto) {
        return new HistoryResponse(
                dto.getId(),
                dto.getOutfit(),
                dto.getWornDate(),
                dto.getCreatedAt()
        );
    }
}
