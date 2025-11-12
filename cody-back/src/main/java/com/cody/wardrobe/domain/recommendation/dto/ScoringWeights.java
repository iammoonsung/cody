package com.cody.wardrobe.domain.recommendation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * 추천 알고리즘 고도화 - 스코어링 가중치
 *
 * 각 요소별 가중치를 조절하여 추천 전략을 변경할 수 있습니다.
 * 모든 가중치의 합은 1.0이 되어야 합니다.
 */
//@Getter
//@Builder
//@AllArgsConstructor
//public class ScoringWeights {
//
//    /** 평점 가중치 (기본 0.3) */
//    @Builder.Default
//    private Double ratingWeight = 0.3;
//
//    /** 격식도 매칭 가중치 (기본 0.2) */
//    @Builder.Default
//    private Double formalityWeight = 0.2;
//
//    /** 신선도 가중치 - 얼마나 안 입었는지 (기본 0.3) */
//    @Builder.Default
//    private Double freshnessWeight = 0.3;
//
//    /** 계절 매칭 가중치 (기본 0.2) */
//    @Builder.Default
//    private Double seasonWeight = 0.2;
//
//    /**
//     * 기본 가중치 설정 반환
//     */
//    public static ScoringWeights getDefault() {
//        return ScoringWeights.builder().build();
//    }
//
//    /**
//     * 신선도 우선 가중치 (오래 안 입은 옷 위주)
//     */
//    public static ScoringWeights getFreshnessFirst() {
//        return ScoringWeights.builder()
//                .freshnessWeight(0.5)
//                .ratingWeight(0.2)
//                .formalityWeight(0.2)
//                .seasonWeight(0.1)
//                .build();
//    }
//
//    /**
//     * 평점 우선 가중치 (좋아하는 옷 위주)
//     */
//    public static ScoringWeights getRatingFirst() {
//        return ScoringWeights.builder()
//                .ratingWeight(0.5)
//                .formalityWeight(0.2)
//                .freshnessWeight(0.2)
//                .seasonWeight(0.1)
//                .build();
//    }
//}
