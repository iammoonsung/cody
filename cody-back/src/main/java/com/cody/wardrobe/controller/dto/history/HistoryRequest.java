package com.cody.wardrobe.controller.dto.history;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class HistoryRequest {

    @NotNull(message = "코디 ID는 필수입니다.")
    private Long outfitId;

    @NotNull(message = "착용 날짜는 필수입니다.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate wornDate;

    public HistoryRequest(Long outfitId, LocalDate wornDate) {
        this.outfitId = outfitId;
        this.wornDate = wornDate;
    }
}
