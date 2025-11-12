package com.cody.wardrobe.domain.history.repository;

import com.cody.wardrobe.domain.history.History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface HistoryRepository extends JpaRepository<History, Long> {

    /**
     * 특정 코디의 착용 기록 조회
     */
    List<History> findByOutfitId(Long outfitId);

    /**
     * 특정 날짜의 착용 기록 조회
     */
    List<History> findByWornDate(LocalDate wornDate);

    /**
     * 특정 코디의 특정 날짜 착용 기록 조회
     */
    Optional<History> findByOutfitIdAndWornDate(Long outfitId, LocalDate wornDate);

    /**
     * 날짜 범위로 착용 기록 조회
     */
    List<History> findByWornDateBetweenOrderByWornDateDesc(LocalDate startDate, LocalDate endDate);

    /**
     * 특정 코디의 착용 횟수 조회
     */
    long countByOutfitId(Long outfitId);

    /**
     * 특정 날짜에 착용 기록이 있는지 확인
     */
    boolean existsByOutfitIdAndWornDate(Long outfitId, LocalDate wornDate);

    /**
     * 최근 착용 기록 조회 (상위 N개)
     */
    @Query("SELECT h FROM History h ORDER BY h.wornDate DESC, h.createdAt DESC")
    List<History> findRecentHistory();

    /**
     * 특정 월의 착용 기록 조회
     */
    @Query("SELECT h FROM History h WHERE " +
            "YEAR(h.wornDate) = :year AND MONTH(h.wornDate) = :month " +
            "ORDER BY h.wornDate ASC")
    List<History> findByYearAndMonth(@Param("year") int year, @Param("month") int month);

    /**
     * 특정 코디의 마지막 착용일 조회
     */
    @Query("SELECT h FROM History h WHERE h.outfit.id = :outfitId ORDER BY h.wornDate DESC")
    List<History> findLatestByOutfitId(@Param("outfitId") Long outfitId);
}
