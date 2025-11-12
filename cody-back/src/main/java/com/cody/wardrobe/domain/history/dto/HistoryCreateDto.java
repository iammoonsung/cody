package com.cody.wardrobe.domain.history.dto;

import com.cody.wardrobe.controller.dto.history.HistoryRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

/**
 * Service Layer에서 사용하는 착용 기록 생성 DTO
 * Controller의 HistoryRequest를 변환하여 전달
 */
@Getter
@AllArgsConstructor
public class HistoryCreateDto {
    private Long outfitId;
    private LocalDate wornDate;

    /**
     * HistoryRequest를 HistoryCreateDto로 변환하는 정적 팩토리 메서드
     */
    public static HistoryCreateDto from(HistoryRequest request) {
        return new HistoryCreateDto(
                request.getOutfitId(),
                request.getWornDate()
        );
    }
}
