package com.cody.wardrobe.domain.recommendation.dto;

import com.cody.wardrobe.domain.item.Season;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * 추천 알고리즘 고도화 - 추천 조건 DTO
 *
 * 사용 예시:
 * RecommendationCriteria criteria = RecommendationCriteria.builder()
 *     .minRating(4)
 *     .minFormality(3)
 *     .currentSeason(Season.WINTER)
 *     .excludeRecentDays(7)
 *     .mustHaveItemIds(List.of(1L, 2L))
 *     .build();
 */
//@Getter
//@Builder
//@AllArgsConstructor
//public class RecommendationCriteria {
//
//    /** 최소 평점 (1-5) */
//    private Integer minRating;
//
//    /** 최소 격식도 (1-5) */
//    private Integer minFormality;
//
//    /** 최대 격식도 (1-5) */
//    private Integer maxFormality;
//
//    /** 현재 계절 (계절 기반 추천용) */
//    private Season currentSeason;
//
//    /** 최근 N일 내 착용한 코디 제외 */
//    private Integer excludeRecentDays;
//
//    /** 반드시 포함되어야 할 아이템 ID 목록 */
//    private List<Long> mustHaveItemIds;
//
//    /** 제외할 아이템 ID 목록 */
//    private List<Long> excludeItemIds;
//
//    /** 색상 조화 선호도 */
//    private ColorHarmonyType colorHarmony;
//
//    /** 스코어링 가중치 설정 */
//    private ScoringWeights weights;
//
//    public enum ColorHarmonyType {
//        COMPLEMENTARY,  // 보색
//        ANALOGOUS,      // 유사색
//        MONOCHROME,     // 단색
//        TRIADIC,        // 삼원색
//        ANY             // 상관없음
//    }
//}
