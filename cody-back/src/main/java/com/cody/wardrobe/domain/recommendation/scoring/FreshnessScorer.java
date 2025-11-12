package com.cody.wardrobe.domain.recommendation.scoring;

//import com.cody.wardrobe.domain.outfit.Outfit;
//import com.cody.wardrobe.domain.recommendation.dto.RecommendationCriteria;
//import org.springframework.stereotype.Component;
//
//import java.time.LocalDate;
//import java.time.temporal.ChronoUnit;

/**
 * 추천 알고리즘 고도화 - 신선도 점수 계산기
 *
 * 얼마나 오래 입지 않았는지를 기반으로 점수를 계산합니다.
 * 오래 입지 않을수록 점수가 높습니다.
 */
//@Component
//public class FreshnessScorer implements ScoreCalculator {
//
//    private static final int MAX_DAYS_FOR_FULL_SCORE = 30; // 30일 이상 안 입으면 만점
//
//    @Override
//    public double calculate(Outfit outfit, RecommendationCriteria criteria) {
//        // 한 번도 입지 않은 코디는 최고 점수
//        if (outfit.getLastWornDate() == null) {
//            return 1.0;
//        }
//
//        // 마지막 착용일로부터 경과한 일수 계산
//        long daysSinceWorn = ChronoUnit.DAYS.between(
//            outfit.getLastWornDate(),
//            LocalDate.now()
//        );
//
//        // 음수면 미래 날짜이므로 0점
//        if (daysSinceWorn < 0) {
//            return 0.0;
//        }
//
//        // 30일 이상 안 입었으면 만점
//        // 그 이하는 비례해서 점수 부여
//        return Math.min(daysSinceWorn / (double) MAX_DAYS_FOR_FULL_SCORE, 1.0);
//    }
//
//    @Override
//    public String getName() {
//        return "Freshness";
//    }
//}
