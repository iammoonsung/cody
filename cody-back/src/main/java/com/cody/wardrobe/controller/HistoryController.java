package com.cody.wardrobe.controller;

import com.cody.wardrobe.common.ApiResponse;
import com.cody.wardrobe.controller.dto.history.HistoryRequest;
import com.cody.wardrobe.controller.dto.history.HistoryResponse;
import com.cody.wardrobe.domain.history.dto.HistoryCreateDto;
import com.cody.wardrobe.domain.history.dto.HistoryDto;
import com.cody.wardrobe.domain.history.service.HistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "History", description = "착용 기록 관리 API")
@RestController
@RequestMapping("/api/histories")
@RequiredArgsConstructor
public class HistoryController {

    private final HistoryService historyService;

    @Operation(summary = "착용 기록 등록", description = "코디의 착용 기록을 등록합니다.")
    @PostMapping
    public ResponseEntity<ApiResponse<HistoryResponse>> createHistory(@Valid @RequestBody HistoryRequest request) {
        // Request → Service DTO 변환
        HistoryCreateDto createDto = HistoryCreateDto.from(request);

        // Service 호출
        HistoryDto historyDto = historyService.createHistory(createDto);

        // Service DTO → Response 변환
        HistoryResponse response = HistoryResponse.from(historyDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response));
    }

    @Operation(summary = "착용 기록 단건 조회", description = "ID로 착용 기록을 조회합니다.")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<HistoryResponse>> getHistory(@PathVariable Long id) {
        HistoryDto historyDto = historyService.getHistory(id);
        HistoryResponse response = HistoryResponse.from(historyDto);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "모든 착용 기록 조회", description = "모든 착용 기록을 최신순으로 조회합니다.")
    @GetMapping
    public ResponseEntity<ApiResponse<List<HistoryResponse>>> getAllHistories() {
        List<HistoryDto> histories = historyService.getAllHistories();
        List<HistoryResponse> responses = histories.stream()
                .map(HistoryResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(responses));
    }

    @Operation(summary = "코디별 착용 기록 조회", description = "특정 코디의 모든 착용 기록을 조회합니다.")
    @GetMapping("/outfit/{outfitId}")
    public ResponseEntity<ApiResponse<List<HistoryResponse>>> getHistoriesByOutfit(
            @Parameter(description = "코디 ID") @PathVariable Long outfitId) {
        List<HistoryDto> histories = historyService.getHistoriesByOutfit(outfitId);
        List<HistoryResponse> responses = histories.stream()
                .map(HistoryResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(responses));
    }

    @Operation(summary = "날짜 범위로 착용 기록 조회", description = "특정 날짜 범위의 착용 기록을 조회합니다.")
    @GetMapping("/range")
    public ResponseEntity<ApiResponse<List<HistoryResponse>>> getHistoriesByDateRange(
            @Parameter(description = "시작 날짜 (YYYY-MM-DD)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "종료 날짜 (YYYY-MM-DD)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<HistoryDto> histories = historyService.getHistoriesByDateRange(startDate, endDate);
        List<HistoryResponse> responses = histories.stream()
                .map(HistoryResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(responses));
    }

    @Operation(summary = "월별 착용 기록 조회", description = "특정 년월의 착용 기록을 조회합니다.")
    @GetMapping("/month")
    public ResponseEntity<ApiResponse<List<HistoryResponse>>> getHistoriesByMonth(
            @Parameter(description = "년도 (예: 2025)") @RequestParam int year,
            @Parameter(description = "월 (1-12)") @RequestParam int month) {
        List<HistoryDto> histories = historyService.getHistoriesByMonth(year, month);
        List<HistoryResponse> responses = histories.stream()
                .map(HistoryResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(responses));
    }

    @Operation(summary = "착용 기록 삭제", description = "착용 기록을 삭제합니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteHistory(@PathVariable Long id) {
        historyService.deleteHistory(id);
        return ResponseEntity.ok(ApiResponse.success());
    }
}
