package com.cody.wardrobe.domain.recommendation.scoring;

//import com.cody.wardrobe.domain.outfit.Outfit;
//import com.cody.wardrobe.domain.recommendation.dto.RecommendationCriteria;
//import org.springframework.stereotype.Component;

/**
 * 추천 알고리즘 고도화 - 격식도 매칭 점수 계산기
 *
 * 사용자가 원하는 격식도 범위와 코디의 격식도 간 매칭 정도를 계산합니다.
 * 범위 내에 있으면 1.0, 벗어날수록 점수가 낮아집니다.
 */
//@Component
//public class FormalityScorer implements ScoreCalculator {
//
//    @Override
//    public double calculate(Outfit outfit, RecommendationCriteria criteria) {
//        int formalityLevel = outfit.getFormalityLevel();
//        int minFormality = criteria.getMinFormality() != null ? criteria.getMinFormality() : 1;
//        int maxFormality = criteria.getMaxFormality() != null ? criteria.getMaxFormality() : 5;
//
//        // 범위 내에 있으면 만점
//        if (formalityLevel >= minFormality && formalityLevel <= maxFormality) {
//            return 1.0;
//        }
//
//        // 범위를 벗어난 정도에 따라 점수 감소
//        int distance;
//        if (formalityLevel < minFormality) {
//            distance = minFormality - formalityLevel;
//        } else {
//            distance = formalityLevel - maxFormality;
//        }
//
//        // 최대 거리는 4 (1과 5 사이)
//        return Math.max(0.0, 1.0 - (distance / 4.0));
//    }
//
//    @Override
//    public String getName() {
//        return "Formality";
//    }
//}
