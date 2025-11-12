package com.cody.wardrobe.domain.recommendation.scoring;

//import com.cody.wardrobe.domain.item.Season;
//import com.cody.wardrobe.domain.outfit.Outfit;
//import com.cody.wardrobe.domain.outfit.OutfitItem;
//import com.cody.wardrobe.domain.recommendation.dto.RecommendationCriteria;
//import org.springframework.stereotype.Component;
//
//import java.util.Map;
//import java.util.stream.Collectors;

/**
 * 추천 알고리즘 고도화 - 계절 매칭 점수 계산기
 *
 * 현재 계절과 코디 내 아이템들의 계절 정보를 비교하여 점수를 계산합니다.
 * 계절이 일치하는 아이템이 많을수록 점수가 높습니다.
 */
//@Component
//public class SeasonScorer implements ScoreCalculator {
//
//    @Override
//    public double calculate(Outfit outfit, RecommendationCriteria criteria) {
//        // 현재 계절 정보가 없으면 중립 점수
//        if (criteria.getCurrentSeason() == null) {
//            return 0.5;
//        }
//
//        // 코디에 아이템이 없으면 중립 점수
//        if (outfit.getOutfitItems() == null || outfit.getOutfitItems().isEmpty()) {
//            return 0.5;
//        }
//
//        Season currentSeason = criteria.getCurrentSeason();
//
//        // 각 아이템의 계절 정보 수집
//        Map<Season, Long> seasonCounts = outfit.getOutfitItems().stream()
//            .map(OutfitItem::getItem)
//            .filter(item -> item.getSeason() != null)
//            .collect(Collectors.groupingBy(
//                item -> item.getSeason(),
//                Collectors.counting()
//            ));
//
//        // 계절 정보가 없는 아이템 개수
//        long noSeasonCount = outfit.getOutfitItems().stream()
//            .map(OutfitItem::getItem)
//            .filter(item -> item.getSeason() == null)
//            .count();
//
//        long totalItems = outfit.getOutfitItems().size();
//
//        // 현재 계절과 일치하는 아이템 개수
//        long matchingItems = seasonCounts.getOrDefault(currentSeason, 0L);
//
//        // 계절 정보가 없는 아이템은 0.5점으로 계산
//        double score = (matchingItems + (noSeasonCount * 0.5)) / totalItems;
//
//        return score;
//    }
//
//    @Override
//    public String getName() {
//        return "Season";
//    }
//}
