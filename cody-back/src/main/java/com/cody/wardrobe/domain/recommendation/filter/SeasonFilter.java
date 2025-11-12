package com.cody.wardrobe.domain.recommendation.filter;

import com.cody.wardrobe.domain.item.Season;
import com.cody.wardrobe.domain.outfit.Outfit;
import com.cody.wardrobe.domain.outfit.OutfitItem;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 추천 알고리즘 고도화 - 계절 필터
 *
 * 특정 계절에 적합한 코디만 필터링합니다.
 * 코디 내 아이템 중 50% 이상이 해당 계절이거나 계절 정보가 없으면 통과합니다.
 */
//@Component
//public class SeasonFilter {
//
//    /**
//     * 계절 필터 적용
//     *
//     * @param outfits 필터링할 코디 목록
//     * @param targetSeason 목표 계절
//     * @return 필터링된 코디 목록
//     */
//    public List<Outfit> filter(List<Outfit> outfits, Season targetSeason) {
//        if (targetSeason == null) {
//            return outfits;
//        }
//
//        return outfits.stream()
//            .filter(outfit -> isSeasonAppropriate(outfit, targetSeason))
//            .collect(Collectors.toList());
//    }
//
//    /**
//     * 코디가 해당 계절에 적합한지 판단
//     *
//     * @param outfit 판단할 코디
//     * @param targetSeason 목표 계절
//     * @return 적합하면 true
//     */
//    private boolean isSeasonAppropriate(Outfit outfit, Season targetSeason) {
//        if (outfit.getOutfitItems() == null || outfit.getOutfitItems().isEmpty()) {
//            return false;
//        }
//
//        long totalItems = outfit.getOutfitItems().size();
//
//        // 목표 계절과 일치하는 아이템 개수
//        long matchingItems = outfit.getOutfitItems().stream()
//            .map(OutfitItem::getItem)
//            .filter(item -> item.getSeason() == targetSeason)
//            .count();
//
//        // 계절 정보가 없는 아이템 개수 (모든 계절에 사용 가능)
//        long noSeasonItems = outfit.getOutfitItems().stream()
//            .map(OutfitItem::getItem)
//            .filter(item -> item.getSeason() == null)
//            .count();
//
//        // 일치하는 아이템 + 계절 무관 아이템이 50% 이상이면 통과
//        return (matchingItems + noSeasonItems) >= (totalItems * 0.5);
//    }
//}
