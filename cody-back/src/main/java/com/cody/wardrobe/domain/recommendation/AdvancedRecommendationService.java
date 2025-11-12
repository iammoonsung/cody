package com.cody.wardrobe.domain.recommendation;

//import com.cody.wardrobe.domain.outfit.Outfit;
//import com.cody.wardrobe.domain.outfit.dto.OutfitDto;
//import com.cody.wardrobe.domain.outfit.repository.OutfitRepository;
//import com.cody.wardrobe.domain.recommendation.dto.RecommendationCriteria;
//import com.cody.wardrobe.domain.recommendation.dto.ScoredOutfitDto;
//import com.cody.wardrobe.domain.recommendation.dto.ScoringWeights;
//import com.cody.wardrobe.domain.recommendation.filter.ItemCombinationFilter;
//import com.cody.wardrobe.domain.recommendation.filter.SeasonFilter;
//import com.cody.wardrobe.domain.recommendation.scoring.*;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDate;
//import java.util.Comparator;
//import java.util.List;
//import java.util.stream.Collectors;

/**
 * 추천 알고리즘 고도화 - 고급 추천 서비스
 *
 * 다양한 요소를 종합적으로 고려하여 코디를 추천합니다.
 *
 * 사용 예시:
 * <pre>
 * RecommendationCriteria criteria = RecommendationCriteria.builder()
 *     .minRating(4)
 *     .minFormality(3)
 *     .maxFormality(4)
 *     .currentSeason(Season.WINTER)
 *     .excludeRecentDays(7)
 *     .mustHaveItemIds(List.of(1L, 2L))
 *     .weights(ScoringWeights.getFreshnessFirst())
 *     .build();
 *
 * List<ScoredOutfitDto> recommendations = service.recommendOutfits(criteria, 5);
 * </pre>
 */
//@Slf4j
//@Service
//@RequiredArgsConstructor
//@Transactional(readOnly = true)
//public class AdvancedRecommendationService {
//
//    private final OutfitRepository outfitRepository;
//    private final RatingScorer ratingScorer;
//    private final FormalityScorer formalityScorer;
//    private final FreshnessScorer freshnessScorer;
//    private final SeasonScorer seasonScorer;
//    private final SeasonFilter seasonFilter;
//    private final ItemCombinationFilter itemCombinationFilter;
//
//    /**
//     * 고도화된 추천 알고리즘
//     *
//     * @param criteria 추천 조건
//     * @param limit 추천할 코디 개수
//     * @return 점수가 포함된 추천 코디 목록 (점수 높은 순)
//     */
//    public List<ScoredOutfitDto> recommendOutfits(RecommendationCriteria criteria, int limit) {
//        log.debug("Starting advanced recommendation with criteria: {}", criteria);
//
//        // 1단계: 기본 필터링 (평점, 격식도, 최근 착용)
//        List<Outfit> candidates = applyBasicFilters(criteria);
//        log.debug("After basic filters: {} outfits", candidates.size());
//
//        // 2단계: 계절 필터링
//        candidates = seasonFilter.filter(candidates, criteria.getCurrentSeason());
//        log.debug("After season filter: {} outfits", candidates.size());
//
//        // 3단계: 아이템 조합 필터링
//        candidates = itemCombinationFilter.filterByMustHaveItems(
//            candidates, criteria.getMustHaveItemIds()
//        );
//        candidates = itemCombinationFilter.filterByExcludeItems(
//            candidates, criteria.getExcludeItemIds()
//        );
//        log.debug("After item combination filter: {} outfits", candidates.size());
//
//        // 4단계: 스코어링
//        ScoringWeights weights = criteria.getWeights() != null
//            ? criteria.getWeights()
//            : ScoringWeights.getDefault();
//
//        List<ScoredOutfitDto> scoredOutfits = candidates.stream()
//            .map(outfit -> calculateScore(outfit, criteria, weights))
//            .sorted(Comparator.comparing(ScoredOutfitDto::getTotalScore).reversed())
//            .limit(limit)
//            .collect(Collectors.toList());
//
//        log.debug("Recommendation completed. Top {} outfits selected", scoredOutfits.size());
//        return scoredOutfits;
//    }
//
//    /**
//     * 기본 필터 적용
//     */
//    private List<Outfit> applyBasicFilters(RecommendationCriteria criteria) {
//        Integer minRating = criteria.getMinRating() != null ? criteria.getMinRating() : 1;
//        Integer minFormality = criteria.getMinFormality() != null ? criteria.getMinFormality() : 1;
//
//        // 최근 착용 제외
//        if (criteria.getExcludeRecentDays() != null && criteria.getExcludeRecentDays() > 0) {
//            LocalDate excludeAfter = LocalDate.now().minusDays(criteria.getExcludeRecentDays());
//            return outfitRepository.findByRatingAndFormalityExcludingRecent(
//                minRating, minFormality, excludeAfter
//            );
//        } else {
//            return outfitRepository.findByRatingAndFormalityForRecommendation(
//                minRating, minFormality
//            );
//        }
//    }
//
//    /**
//     * 코디의 종합 점수 계산
//     */
//    private ScoredOutfitDto calculateScore(
//        Outfit outfit,
//        RecommendationCriteria criteria,
//        ScoringWeights weights
//    ) {
//        // 각 요소별 점수 계산
//        double ratingScore = ratingScorer.calculate(outfit, criteria);
//        double formalityScore = formalityScorer.calculate(outfit, criteria);
//        double freshnessScore = freshnessScorer.calculate(outfit, criteria);
//        double seasonScore = seasonScorer.calculate(outfit, criteria);
//
//        // 가중치 적용하여 총점 계산
//        double totalScore =
//            (ratingScore * weights.getRatingWeight()) +
//            (formalityScore * weights.getFormalityWeight()) +
//            (freshnessScore * weights.getFreshnessWeight()) +
//            (seasonScore * weights.getSeasonWeight());
//
//        return new ScoredOutfitDto(
//            OutfitDto.from(outfit),
//            totalScore,
//            ratingScore,
//            formalityScore,
//            freshnessScore,
//            seasonScore
//        );
//    }
//
//    /**
//     * 간편 추천: 신선도 우선
//     * "오래 안 입은 옷 추천해줘"
//     */
//    public List<ScoredOutfitDto> recommendFreshOutfits(int minRating, int limit) {
//        RecommendationCriteria criteria = RecommendationCriteria.builder()
//            .minRating(minRating)
//            .weights(ScoringWeights.getFreshnessFirst())
//            .build();
//
//        return recommendOutfits(criteria, limit);
//    }
//
//    /**
//     * 간편 추천: 평점 우선
//     * "내가 좋아하는 옷 추천해줘"
//     */
//    public List<ScoredOutfitDto> recommendFavoriteOutfits(int minFormality, int limit) {
//        RecommendationCriteria criteria = RecommendationCriteria.builder()
//            .minFormality(minFormality)
//            .weights(ScoringWeights.getRatingFirst())
//            .build();
//
//        return recommendOutfits(criteria, limit);
//    }
//}
