package com.cody.wardrobe.domain.outfit.repository;

import com.cody.wardrobe.domain.outfit.Outfit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface OutfitRepository extends JpaRepository<Outfit, Long> {

    /**
     * 평점으로 코디 조회
     */
    List<Outfit> findByRating(Integer rating);

    /**
     * 최소 평점 이상의 코디 조회
     */
    List<Outfit> findByRatingGreaterThanEqual(Integer minRating);

    /**
     * 격식도로 코디 조회
     */
    List<Outfit> findByFormalityLevel(Integer formalityLevel);

    /**
     * 격식도 범위로 코디 조회
     */
    List<Outfit> findByFormalityLevelBetween(Integer minFormality, Integer maxFormality);

    /**
     * 평점과 격식도 조건으로 코디 조회
     */
    @Query("SELECT o FROM Outfit o WHERE " +
            "o.rating >= :minRating AND " +
            "o.formalityLevel >= :minFormality " +
            "ORDER BY o.lastWornDate ASC NULLS FIRST")
    List<Outfit> findByRatingAndFormalityForRecommendation(
            @Param("minRating") Integer minRating,
            @Param("minFormality") Integer minFormality
    );

    /**
     * 최근 N일 내 착용하지 않은 코디 조회
     */
    @Query("SELECT o FROM Outfit o WHERE " +
            "o.rating >= :minRating AND " +
            "o.formalityLevel >= :minFormality AND " +
            "(o.lastWornDate IS NULL OR o.lastWornDate < :excludeAfter) " +
            "ORDER BY o.lastWornDate ASC NULLS FIRST")
    List<Outfit> findByRatingAndFormalityExcludingRecent(
            @Param("minRating") Integer minRating,
            @Param("minFormality") Integer minFormality,
            @Param("excludeAfter") LocalDate excludeAfter
    );

    /**
     * 착용 횟수로 정렬된 코디 조회
     */
    List<Outfit> findAllByOrderByWornCountDesc();

    /**
     * 마지막 착용일 기준 오래된 순으로 조회
     */
    @Query("SELECT o FROM Outfit o ORDER BY o.lastWornDate ASC NULLS FIRST")
    List<Outfit> findAllOrderByLastWornDateAsc();
}
