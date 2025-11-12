# 추천 알고리즘 고도화 가이드

## 📋 개요

현재 구현된 기본 추천 알고리즘을 고도화하여 다양한 요소를 종합적으로 고려하는 스마트 추천 시스템입니다.

**현재 상태:** 모든 코드가 주석 처리되어 있어 빌드에는 영향을 주지 않습니다.

## 🎯 주요 기능

### 1. 스코어링 시스템

각 코디에 대해 4가지 요소의 점수를 계산하고 가중치를 적용하여 최종 점수를 산출합니다.

```
총점 = (평점 × 0.3) + (격식도 × 0.2) + (신선도 × 0.3) + (계절 × 0.2)
```

#### 평점 점수 (Rating Score)
- 코디의 평점 1~5를 0.0~1.0으로 정규화
- 사용자가 좋아하는 코디 우선 추천

#### 격식도 점수 (Formality Score)
- 사용자가 원하는 격식도 범위와 코디의 격식도 매칭 정도
- 범위 내: 1.0점
- 범위 밖: 거리에 따라 감소

#### 신선도 점수 (Freshness Score)
- 마지막 착용일로부터 경과한 일수 기반
- 30일 이상 안 입었으면 만점
- 한 번도 안 입은 코디는 최고 점수

#### 계절 점수 (Season Score)
- 현재 계절과 코디 내 아이템들의 계절 정보 매칭
- 계절이 일치하는 아이템이 많을수록 점수 상승

### 2. 필터링 시스템

#### 계절 필터
- 코디 내 아이템의 50% 이상이 해당 계절이거나 계절 무관이면 통과

#### 아이템 조합 필터
- 반드시 포함되어야 할 아이템 지정
- 제외할 아이템 지정
- 특정 아이템을 활용한 코디 추천

### 3. 가중치 프리셋

```java
// 균형잡힌 기본 설정
ScoringWeights.getDefault()

// 신선도 우선 (오래 안 입은 옷)
ScoringWeights.getFreshnessFirst()

// 평점 우선 (좋아하는 옷)
ScoringWeights.getRatingFirst()
```

## 📁 파일 구조

```
com.cody.wardrobe.domain.recommendation/
├── AdvancedRecommendationService.java       # 메인 추천 서비스
├── dto/
│   ├── RecommendationCriteria.java          # 추천 조건
│   ├── ScoringWeights.java                  # 가중치 설정
│   └── ScoredOutfitDto.java                 # 점수 포함 결과
├── scoring/
│   ├── ScoreCalculator.java                 # 계산기 인터페이스
│   ├── RatingScorer.java                    # 평점 점수
│   ├── FormalityScorer.java                 # 격식도 점수
│   ├── FreshnessScorer.java                 # 신선도 점수
│   └── SeasonScorer.java                    # 계절 점수
└── filter/
    ├── SeasonFilter.java                    # 계절 필터
    └── ItemCombinationFilter.java           # 아이템 조합 필터

com.cody.wardrobe.controller/
└── AdvancedRecommendationController.java    # API 엔드포인트
```

## 🚀 사용 방법

### 1단계: 주석 제거

모든 Java 파일에서 클래스 선언 앞의 `//` 주석을 제거합니다.

**예시:**
```java
// Before
//@Service
//@RequiredArgsConstructor
//public class AdvancedRecommendationService {

// After
@Service
@RequiredArgsConstructor
public class AdvancedRecommendationService {
```

### 2단계: 빌드 및 재시작

```bash
./gradlew clean build -x test
./gradlew bootRun
```

### 3단계: API 호출

#### 고급 추천 API

```bash
# 기본 추천
curl "http://localhost:8080/api/recommendations/advanced?minRating=3&season=WINTER&limit=5"

# 신선도 우선 추천
curl "http://localhost:8080/api/recommendations/advanced?minRating=4&weightPreset=freshness_first&limit=3"

# 평점 우선 추천
curl "http://localhost:8080/api/recommendations/advanced?minFormality=3&maxFormality=4&weightPreset=rating_first&limit=5"
```

#### 간편 추천 API

```bash
# 오래 안 입은 옷 추천
curl "http://localhost:8080/api/recommendations/fresh?minRating=3&limit=5"

# 좋아하는 옷 추천
curl "http://localhost:8080/api/recommendations/favorite?minFormality=2&limit=5"
```

## 📊 응답 예시

```json
{
  "result": true,
  "data": [
    {
      "outfit": {
        "id": 1,
        "name": "Casual Look",
        "rating": 4,
        "formalityLevel": 2,
        ...
      },
      "totalScore": 0.85,
      "ratingScore": 0.8,
      "formalityScore": 1.0,
      "freshnessScore": 0.9,
      "seasonScore": 0.75
    }
  ]
}
```

## 🔧 커스터마이징

### 가중치 조절

```java
// 커스텀 가중치 생성
ScoringWeights customWeights = ScoringWeights.builder()
    .ratingWeight(0.4)      // 평점 40%
    .formalityWeight(0.3)   // 격식도 30%
    .freshnessWeight(0.2)   // 신선도 20%
    .seasonWeight(0.1)      // 계절 10%
    .build();

RecommendationCriteria criteria = RecommendationCriteria.builder()
    .weights(customWeights)
    .build();
```

### 신선도 기준일 변경

`FreshnessScorer.java`의 `MAX_DAYS_FOR_FULL_SCORE` 상수를 수정하세요.

```java
private static final int MAX_DAYS_FOR_FULL_SCORE = 30; // 30일 → 원하는 값
```

### 계절 매칭 기준 변경

`SeasonFilter.java`의 매칭 비율을 조정하세요.

```java
return (matchingItems + noSeasonItems) >= (totalItems * 0.5);  // 50% → 원하는 비율
```

## 📈 향후 개선 방향

### 단기 (즉시 추가 가능)

1. **색상 조화 분석**
   - 보색, 유사색, 대비색 계산
   - 색상 조화도 점수 추가

2. **카테고리 필수 조합**
   - 상의+하의 필수 검증
   - 신발 포함 여부 옵션

3. **날씨 API 연동**
   - 실시간 날씨 기반 계절 자동 설정
   - 온도별 아이템 필터링

### 중기 (데이터 축적 후)

1. **통계 기반 추천**
   - 요일별 착용 패턴 분석
   - 시간대별 선호도 분석

2. **트렌드 분석**
   - 최근 자주 입는 스타일 파악
   - 계절별 인기 코디 추천

### 장기 (사용자 기능 추가 후)

1. **협업 필터링**
   - 비슷한 스타일의 다른 사용자 코디 추천
   - 사용자 간 유사도 분석

2. **머신러닝 개인화**
   - 착용 기록 학습
   - 개인별 스타일 프로필 생성

## ⚠️ 주의사항

1. **성능 최적화**
   - 코디 수가 많을 경우 페이징 처리 권장
   - N+1 쿼리 문제 해결 필요 (fetch join 적용)

2. **데이터 정합성**
   - 계절 정보가 없는 아이템 처리 로직 검증
   - 삭제된 아이템 참조 방지

3. **가중치 합계**
   - 모든 가중치의 합은 1.0이어야 함
   - ScoringWeights 생성 시 검증 로직 추가 권장

## 📞 문의

추가 기능이 필요하거나 질문이 있으시면 이슈를 등록해주세요.
