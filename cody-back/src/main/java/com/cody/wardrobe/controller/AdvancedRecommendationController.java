package com.cody.wardrobe.controller;

//import com.cody.wardrobe.common.ApiResponse;
//import com.cody.wardrobe.domain.item.Season;
//import com.cody.wardrobe.domain.recommendation.AdvancedRecommendationService;
//import com.cody.wardrobe.domain.recommendation.dto.RecommendationCriteria;
//import com.cody.wardrobe.domain.recommendation.dto.ScoredOutfitDto;
//import com.cody.wardrobe.domain.recommendation.dto.ScoringWeights;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.Parameter;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;

/**
 * 추천 알고리즘 고도화 - 고급 추천 API (주석처리됨)
 *
 * 사용하려면:
 * 1. 모든 클래스의 주석(//)을 제거
 * 2. @Component, @Service 등의 어노테이션 활성화
 * 3. 애플리케이션 재시작
 *
 * API 엔드포인트:
 * - POST /api/recommendations/advanced - 상세 조건으로 추천
 * - GET /api/recommendations/fresh - 오래 안 입은 옷 추천
 * - GET /api/recommendations/favorite - 좋아하는 옷 추천
 */
//@Tag(name = "Advanced Recommendation", description = "고도화된 코디 추천 API")
//@RestController
//@RequestMapping("/api/recommendations")
//@RequiredArgsConstructor
//public class AdvancedRecommendationController {
//
//    private final AdvancedRecommendationService recommendationService;
//
//    @Operation(
//        summary = "고급 추천",
//        description = """
//            다양한 조건을 종합적으로 고려하여 코디를 추천합니다.
//
//            **추천 요소:**
//            - 평점 (30%)
//            - 격식도 매칭 (20%)
//            - 신선도 - 얼마나 안 입었는지 (30%)
//            - 계절 매칭 (20%)
//
//            **가중치 조절 가능:**
//            - default: 균형잡힌 추천
//            - freshness_first: 오래 안 입은 옷 우선
//            - rating_first: 좋아하는 옷 우선
//            """
//    )
//    @PostMapping("/advanced")
//    public ResponseEntity<ApiResponse<List<ScoredOutfitDto>>> recommendAdvanced(
//        @Parameter(description = "최소 평점 (1-5)") @RequestParam(defaultValue = "3") Integer minRating,
//        @Parameter(description = "최소 격식도 (1-5)") @RequestParam(defaultValue = "1") Integer minFormality,
//        @Parameter(description = "최대 격식도 (1-5)") @RequestParam(required = false) Integer maxFormality,
//        @Parameter(description = "현재 계절") @RequestParam(required = false) Season season,
//        @Parameter(description = "최근 N일 내 착용 제외") @RequestParam(defaultValue = "7") Integer excludeDays,
//        @Parameter(description = "추천 개수") @RequestParam(defaultValue = "5") Integer limit,
//        @Parameter(description = "가중치 프리셋 (default/freshness_first/rating_first)")
//            @RequestParam(defaultValue = "default") String weightPreset
//    ) {
//        // 가중치 설정
//        ScoringWeights weights = switch (weightPreset) {
//            case "freshness_first" -> ScoringWeights.getFreshnessFirst();
//            case "rating_first" -> ScoringWeights.getRatingFirst();
//            default -> ScoringWeights.getDefault();
//        };
//
//        // 추천 조건 생성
//        RecommendationCriteria criteria = RecommendationCriteria.builder()
//            .minRating(minRating)
//            .minFormality(minFormality)
//            .maxFormality(maxFormality)
//            .currentSeason(season)
//            .excludeRecentDays(excludeDays)
//            .weights(weights)
//            .build();
//
//        // 추천 실행
//        List<ScoredOutfitDto> recommendations = recommendationService.recommendOutfits(criteria, limit);
//
//        return ResponseEntity.ok(ApiResponse.success(recommendations));
//    }
//
//    @Operation(
//        summary = "오래 안 입은 옷 추천",
//        description = "신선도를 최우선으로 하여 오래 입지 않은 코디를 추천합니다."
//    )
//    @GetMapping("/fresh")
//    public ResponseEntity<ApiResponse<List<ScoredOutfitDto>>> recommendFresh(
//        @Parameter(description = "최소 평점 (1-5)") @RequestParam(defaultValue = "3") Integer minRating,
//        @Parameter(description = "추천 개수") @RequestParam(defaultValue = "5") Integer limit
//    ) {
//        List<ScoredOutfitDto> recommendations = recommendationService.recommendFreshOutfits(minRating, limit);
//        return ResponseEntity.ok(ApiResponse.success(recommendations));
//    }
//
//    @Operation(
//        summary = "좋아하는 옷 추천",
//        description = "평점을 최우선으로 하여 자주 입는 좋아하는 코디를 추천합니다."
//    )
//    @GetMapping("/favorite")
//    public ResponseEntity<ApiResponse<List<ScoredOutfitDto>>> recommendFavorite(
//        @Parameter(description = "최소 격식도 (1-5)") @RequestParam(defaultValue = "1") Integer minFormality,
//        @Parameter(description = "추천 개수") @RequestParam(defaultValue = "5") Integer limit
//    ) {
//        List<ScoredOutfitDto> recommendations = recommendationService.recommendFavoriteOutfits(minFormality, limit);
//        return ResponseEntity.ok(ApiResponse.success(recommendations));
//    }
//}
