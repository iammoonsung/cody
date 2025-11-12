package com.cody.wardrobe.controller;

import com.cody.wardrobe.common.ApiResponse;
import com.cody.wardrobe.controller.dto.outfit.OutfitRequest;
import com.cody.wardrobe.controller.dto.outfit.OutfitResponse;
import com.cody.wardrobe.domain.outfit.dto.OutfitCreateDto;
import com.cody.wardrobe.domain.outfit.dto.OutfitDto;
import com.cody.wardrobe.domain.outfit.dto.OutfitUpdateDto;
import com.cody.wardrobe.domain.outfit.service.OutfitService;
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

@Tag(name = "Outfit", description = "코디 관리 API")
@RestController
@RequestMapping("/api/outfits")
@RequiredArgsConstructor
public class OutfitController {

    private final OutfitService outfitService;

    @Operation(summary = "코디 등록", description = "새로운 코디를 등록합니다. 최소 1개 이상의 아이템이 필요합니다.")
    @PostMapping
    public ResponseEntity<ApiResponse<OutfitResponse>> createOutfit(@Valid @RequestBody OutfitRequest request) {
        // Request → Service DTO 변환
        OutfitCreateDto createDto = OutfitCreateDto.from(request);

        // Service 호출
        OutfitDto outfitDto = outfitService.createOutfit(createDto);

        // Service DTO → Response 변환
        OutfitResponse response = OutfitResponse.from(outfitDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response));
    }

    @Operation(summary = "코디 단건 조회", description = "ID로 코디를 조회합니다.")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OutfitResponse>> getOutfit(@PathVariable Long id) {
        OutfitDto outfitDto = outfitService.getOutfit(id);
        OutfitResponse response = OutfitResponse.from(outfitDto);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "모든 코디 조회", description = "모든 코디를 조회합니다.")
    @GetMapping
    public ResponseEntity<ApiResponse<List<OutfitResponse>>> getAllOutfits() {
        List<OutfitDto> outfits = outfitService.getAllOutfits();
        List<OutfitResponse> responses = outfits.stream()
                .map(OutfitResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(responses));
    }

    @Operation(
        summary = "코디 추천 (기본)",
        description = """
            평점과 격식도 조건에 맞는 코디를 추천합니다.
            착용한 지 오래된 순서대로 정렬됩니다.
            """
    )
    @GetMapping("/recommend")
    public ResponseEntity<ApiResponse<List<OutfitResponse>>> recommendOutfits(
            @Parameter(description = "최소 평점 (1-5)") @RequestParam(defaultValue = "3") Integer minRating,
            @Parameter(description = "최소 격식도 (1-5)") @RequestParam(defaultValue = "3") Integer minFormality,
            @Parameter(description = "최근 N일 내 착용 제외 여부") @RequestParam(defaultValue = "true") Boolean excludeRecent,
            @Parameter(description = "제외 기간 (일)") @RequestParam(defaultValue = "2") Integer excludeDays) {

        List<OutfitDto> outfits;
        if (excludeRecent) {
            outfits = outfitService.recommendOutfitsExcludingRecent(minRating, minFormality, excludeDays);
        } else {
            outfits = outfitService.recommendOutfits(minRating, minFormality);
        }

        List<OutfitResponse> responses = outfits.stream()
                .map(OutfitResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(responses));
    }

    @Operation(summary = "평점 기준 코디 조회", description = "최소 평점 이상의 코디를 조회합니다.")
    @GetMapping("/rating/{minRating}")
    public ResponseEntity<ApiResponse<List<OutfitResponse>>> getOutfitsByRating(
            @Parameter(description = "최소 평점 (1-5)") @PathVariable Integer minRating) {
        List<OutfitDto> outfits = outfitService.getOutfitsByRating(minRating);
        List<OutfitResponse> responses = outfits.stream()
                .map(OutfitResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(responses));
    }

    @Operation(summary = "격식도 기준 코디 조회", description = "격식도 범위에 해당하는 코디를 조회합니다.")
    @GetMapping("/formality")
    public ResponseEntity<ApiResponse<List<OutfitResponse>>> getOutfitsByFormalityLevel(
            @Parameter(description = "최소 격식도 (1-5)") @RequestParam Integer minFormality,
            @Parameter(description = "최대 격식도 (1-5)") @RequestParam Integer maxFormality) {
        List<OutfitDto> outfits = outfitService.getOutfitsByFormalityLevel(minFormality, maxFormality);
        List<OutfitResponse> responses = outfits.stream()
                .map(OutfitResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(responses));
    }

    @Operation(summary = "코디 수정", description = "코디 정보를 수정합니다.")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<OutfitResponse>> updateOutfit(
            @PathVariable Long id,
            @Valid @RequestBody OutfitRequest request) {
        // Request → Service DTO 변환
        OutfitUpdateDto updateDto = OutfitUpdateDto.from(request);

        // Service 호출
        OutfitDto outfitDto = outfitService.updateOutfit(id, updateDto);

        // Service DTO → Response 변환
        OutfitResponse response = OutfitResponse.from(outfitDto);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "코디 착용 기록", description = "코디를 착용한 날짜를 기록합니다.")
    @PostMapping("/{id}/worn")
    public ResponseEntity<ApiResponse<Void>> recordWorn(
            @PathVariable Long id,
            @Parameter(description = "착용 날짜 (YYYY-MM-DD)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        outfitService.recordWorn(id, date);
        return ResponseEntity.ok(ApiResponse.success());
    }

    @Operation(summary = "코디 삭제", description = "코디를 삭제합니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteOutfit(@PathVariable Long id) {
        outfitService.deleteOutfit(id);
        return ResponseEntity.ok(ApiResponse.success());
    }
}
