package com.cody.wardrobe.domain.outfit.repository;

import com.cody.wardrobe.domain.outfit.OutfitItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OutfitItemRepository extends JpaRepository<OutfitItem, Long> {

    /**
     * 특정 코디의 모든 아이템 조회
     */
    List<OutfitItem> findByOutfitId(Long outfitId);

    /**
     * 특정 아이템이 포함된 모든 코디 조회
     */
    List<OutfitItem> findByItemId(Long itemId);

    /**
     * 특정 코디의 아이템 개수 조회
     */
    long countByOutfitId(Long outfitId);

    /**
     * 특정 아이템이 사용된 코디 개수 조회
     */
    long countByItemId(Long itemId);

    /**
     * 특정 코디의 모든 아이템 삭제
     */
    @Modifying
    @Query("DELETE FROM OutfitItem oi WHERE oi.outfit.id = :outfitId")
    void deleteByOutfitId(@Param("outfitId") Long outfitId);

    /**
     * 특정 아이템이 특정 코디에 포함되어 있는지 확인
     */
    boolean existsByOutfitIdAndItemId(Long outfitId, Long itemId);
}
