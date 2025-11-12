package com.cody.wardrobe.domain.recommendation.filter;

import com.cody.wardrobe.domain.outfit.Outfit;
import com.cody.wardrobe.domain.outfit.OutfitItem;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 추천 알고리즘 고도화 - 아이템 조합 필터
 *
 * 특정 아이템을 포함하거나 제외하는 코디를 필터링합니다.
 */
//@Component
//public class ItemCombinationFilter {
//
//    /**
//     * 반드시 포함해야 할 아이템으로 필터링
//     *
//     * @param outfits 필터링할 코디 목록
//     * @param mustHaveItemIds 반드시 포함되어야 할 아이템 ID 목록
//     * @return 필터링된 코디 목록
//     */
//    public List<Outfit> filterByMustHaveItems(List<Outfit> outfits, List<Long> mustHaveItemIds) {
//        if (mustHaveItemIds == null || mustHaveItemIds.isEmpty()) {
//            return outfits;
//        }
//
//        return outfits.stream()
//            .filter(outfit -> containsAllItems(outfit, mustHaveItemIds))
//            .collect(Collectors.toList());
//    }
//
//    /**
//     * 제외할 아이템으로 필터링
//     *
//     * @param outfits 필터링할 코디 목록
//     * @param excludeItemIds 제외할 아이템 ID 목록
//     * @return 필터링된 코디 목록
//     */
//    public List<Outfit> filterByExcludeItems(List<Outfit> outfits, List<Long> excludeItemIds) {
//        if (excludeItemIds == null || excludeItemIds.isEmpty()) {
//            return outfits;
//        }
//
//        return outfits.stream()
//            .filter(outfit -> !containsAnyItem(outfit, excludeItemIds))
//            .collect(Collectors.toList());
//    }
//
//    /**
//     * 코디가 모든 아이템을 포함하는지 확인
//     */
//    private boolean containsAllItems(Outfit outfit, List<Long> itemIds) {
//        Set<Long> outfitItemIds = outfit.getOutfitItems().stream()
//            .map(OutfitItem::getItem)
//            .map(item -> item.getId())
//            .collect(Collectors.toSet());
//
//        return outfitItemIds.containsAll(itemIds);
//    }
//
//    /**
//     * 코디가 특정 아이템 중 하나라도 포함하는지 확인
//     */
//    private boolean containsAnyItem(Outfit outfit, List<Long> itemIds) {
//        Set<Long> outfitItemIds = outfit.getOutfitItems().stream()
//            .map(OutfitItem::getItem)
//            .map(item -> item.getId())
//            .collect(Collectors.toSet());
//
//        return itemIds.stream().anyMatch(outfitItemIds::contains);
//    }
//}
