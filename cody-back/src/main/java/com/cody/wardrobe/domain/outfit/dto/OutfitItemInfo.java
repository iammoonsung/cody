package com.cody.wardrobe.domain.outfit.dto;

import com.cody.wardrobe.domain.item.dto.ItemDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Outfit에 포함된 아이템 정보
 */
@Getter
@AllArgsConstructor
public class OutfitItemInfo {
    private Long id;  // 프론트엔드와 일치시키기 위해 outfitItemId -> id로 변경
    private ItemDto item;

    public static OutfitItemInfo of(Long outfitItemId, ItemDto itemDto) {
        return new OutfitItemInfo(outfitItemId, itemDto);
    }
}
