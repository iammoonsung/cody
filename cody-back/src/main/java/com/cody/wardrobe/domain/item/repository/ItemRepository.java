package com.cody.wardrobe.domain.item.repository;

import com.cody.wardrobe.domain.item.Item;
import com.cody.wardrobe.domain.item.ItemCategory;
import com.cody.wardrobe.domain.item.Season;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    /**
     * 카테고리별 아이템 조회
     */
    List<Item> findByCategory(ItemCategory category);

    /**
     * 계절별 아이템 조회
     */
    List<Item> findBySeason(Season season);

    /**
     * 카테고리와 계절로 아이템 조회
     */
    List<Item> findByCategoryAndSeason(ItemCategory category, Season season);

    /**
     * 색상으로 아이템 조회
     */
    List<Item> findByColor(String color);

    /**
     * 이름으로 아이템 검색 (부분 일치)
     */
    List<Item> findByNameContainingIgnoreCase(String name);
}
