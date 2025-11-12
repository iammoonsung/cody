package com.cody.wardrobe.domain.recommendation.dto;

import com.cody.wardrobe.domain.outfit.dto.OutfitDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 추천 알고리즘 고도화 - 점수가 포함된 코디 DTO
 *
 * 추천 알고리즘의 결과로 반환되며, 각 코디의 추천 점수를 포함합니다.
 */
//@Getter
//@AllArgsConstructor
//public class ScoredOutfitDto {
//
//    /** 코디 정보 */
//    private OutfitDto outfit;
//
//    /** 총 추천 점수 (0.0 ~ 1.0) */
//    private Double totalScore;
//
//    /** 평점 점수 */
//    private Double ratingScore;
//
//    /** 격식도 매칭 점수 */
//    private Double formalityScore;
//
//    /** 신선도 점수 */
//    private Double freshnessScore;
//
//    /** 계절 매칭 점수 */
//    private Double seasonScore;
//
//    /**
//     * 점수 상세 정보를 문자열로 반환
//     */
//    public String getScoreBreakdown() {
//        return String.format(
//            "Total: %.2f (Rating: %.2f, Formality: %.2f, Freshness: %.2f, Season: %.2f)",
//            totalScore, ratingScore, formalityScore, freshnessScore, seasonScore
//        );
//    }
//}
