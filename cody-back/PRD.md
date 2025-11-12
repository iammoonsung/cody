# 오늘의 코디 추천 앱 PRD (Product Requirements Document) - v2.0

## 📋 프로젝트 개요

### 제품명
**Cody - What to Wear Today (오늘뭐입지)**

### 목적
사용자의 옷장을 디지털화하고, AI 기반 추천 알고리즘을 통해 매일 최적의 코디를 제안하는 개인 스타일링 도우미

### 기술 스택
- **Backend**: Spring Boot 3.3.5, Java 17, JPA/Hibernate
- **Frontend**: Next.js 16.0.0, TypeScript, TailwindCSS, shadcn/ui
- **Database**: H2 (Development), PostgreSQL (Production Ready)
- **Build Tools**: Gradle, npm

---

## 🎯 핵심 기능

### 1. 옷장 관리 (Wardrobe Management)
#### 1.1 아이템 등록
- ✅ **구현 완료**
- 카테고리: TOPS, BOTTOMS, SHOES, OUTERWEAR, ACCESSORIES
- 필수 정보: 이미지, 카테고리, 이름
- 선택 정보: 색상, 시즌 (SPRING, SUMMER, FALL, WINTER, All)
- API: `POST /api/items`

#### 1.2 아이템 조회/수정/삭제
- ✅ **구현 완료**
- 전체 조회, 단건 조회
- 카테고리별, 시즌별, 색상별 필터링
- 아이템 정보 수정 및 삭제
- API: `GET/PUT/DELETE /api/items/{id}`

#### 1.3 아이템 필터링
- ✅ **구현 완료**
- 카테고리별: `GET /api/items/category/{category}`
- 시즌별: `GET /api/items/season/{season}`
- 색상별: `GET /api/items/color/{color}`

---

### 2. 코디 관리 (Outfit Management)
#### 2.1 코디 생성
- ✅ **구현 완료**
- 최소 1개 이상의 아이템 선택 필수
- 필수 정보: 아이템 목록, 평점 (1-5), 격식도 (1-5)
- 선택 정보: 코디 이름, 메모
- API: `POST /api/outfits`

#### 2.2 코디 조회/수정/삭제
- ✅ **구현 완료**
- 전체 코디 조회
- 코디 상세 정보 조회 (아이템 목록 포함)
- 코디 정보 수정 (이름, 평점, 격식도, 아이템 목록)
- 코디 삭제
- API: `GET/PUT/DELETE /api/outfits/{id}`

#### 2.3 격식도 (Formality Level) 시스템
- ✅ **구현 완료**
- **1단계 (Home)**: 집에서 편하게 - 매우 캐주얼
- **2단계 (Neighborhood)**: 동네 외출 - 캐주얼
- **3단계 (Outing)**: 일반 외출 - 스마트 캐주얼
- **4단계 (Work)**: 업무/회의 - 비즈니스
- **5단계 (Formal)**: 공식 행사 - 포멀

#### 2.4 평점 시스템
- ✅ **구현 완료**
- 1-5점 별점 시스템
- 사용자가 코디에 대한 만족도 평가
- 추천 알고리즘에 활용

---

### 3. 코디 추천 (Outfit Recommendation)
#### 3.1 기본 추천
- ✅ **구현 완료**
- 최소 평점 필터링 (기본값: 3점)
- 최소 격식도 필터링 (기본값: 3)
- 착용 이력 기반 정렬 (오래 안 입은 순)
- API: `GET /api/outfits/recommend`

#### 3.2 최근 착용 제외 필터
- ✅ **구현 완료**
- **기본값: ON (활성화)**
- **제외 기간: 최근 2일** (PRD 요구사항 반영)
- 파라미터:
  - `excludeRecent=true` (기본값)
  - `excludeDays=2` (기본값)
- 최근 N일 내 착용한 코디 추천에서 제외

#### 3.3 커스텀 추천
- ✅ **구현 완료**
- 사용자 정의 필터:
  - 최소 평점 조정 (1-5)
  - 최소 격식도 조정 (1-5)
  - 최근 착용 제외 ON/OFF
  - 특정 아이템 포함 (선택)
- 다중 추천 결과 제공 및 순환

#### 3.4 추천 결과 페이지
- ✅ **구현 완료**
- 추천된 코디 상세 정보 표시
- 아이템 목록, 평점, 격식도 표시
- 착용 횟수 및 마지막 착용일 표시
- "이 코디 선택" 기능 (착용 기록)
- "다시 추천받기" 기능

---

### 4. 착용 기록 (History Management)
#### 4.1 착용 기록 등록
- ✅ **구현 완료**
- 코디 착용 날짜 기록
- 자동 착용 횟수 증가
- "오늘 입기" 버튼 기능
- API: `POST /api/outfits/{id}/worn`

#### 4.2 착용 기록 조회
- ✅ **구현 완료**
- 전체 이력 조회
- 코디별 이력 조회
- 날짜 범위 조회
- 월별 조회 (캘린더 뷰)
- API: `GET /api/histories/month?year={year}&month={month}`

#### 4.3 캘린더 뷰
- ✅ **구현 완료**
- 월별 착용 내역 시각화
- 날짜별 코디 및 격식도 표시
- 오늘 날짜 하이라이트
- 클릭하여 코디 상세 페이지 이동
- 월간 통계:
  - 착용 일수
  - 고유 코디 수
  - 평균 격식도
  - 최다 착용 횟수

---

## 🗄️ 데이터 모델

### Item (아이템)
```java
{
  id: Long,
  category: String,  // TOPS, BOTTOMS, SHOES, OUTERWEAR, ACCESSORIES
  name: String,
  imageUrl: String,
  color: String,
  season: String,    // SPRING, SUMMER, FALL, WINTER, null (All)
  createdAt: LocalDateTime,
  updatedAt: LocalDateTime
}
```

### Outfit (코디)
```java
{
  id: Long,
  name: String,
  rating: Integer,          // 1-5
  formalityLevel: Integer,  // 1-5
  lastWornDate: LocalDate,
  wornCount: Integer,
  memo: String,
  outfitItems: List<OutfitItem>,
  createdAt: LocalDateTime,
  updatedAt: LocalDateTime
}
```

### OutfitItem (코디-아이템 연결)
```java
{
  id: Long,
  outfit: Outfit,
  item: Item
}
```

### History (착용 기록)
```java
{
  id: Long,
  outfit: Outfit,
  wornDate: LocalDate,
  createdAt: LocalDateTime
}
```

---

## 🔌 API 엔드포인트

### Items API
- `GET /api/items` - 전체 아이템 조회
- `GET /api/items/{id}` - 아이템 단건 조회
- `POST /api/items` - 아이템 등록
- `PUT /api/items/{id}` - 아이템 수정
- `DELETE /api/items/{id}` - 아이템 삭제
- `GET /api/items/category/{category}` - 카테고리별 조회
- `GET /api/items/season/{season}` - 시즌별 조회
- `GET /api/items/color/{color}` - 색상별 조회

### Outfits API
- `GET /api/outfits` - 전체 코디 조회
- `GET /api/outfits/{id}` - 코디 단건 조회
- `POST /api/outfits` - 코디 등록
- `PUT /api/outfits/{id}` - 코디 수정
- `DELETE /api/outfits/{id}` - 코디 삭제
- `GET /api/outfits/recommend` - 코디 추천
  - Query Parameters:
    - `minRating` (default: 3)
    - `minFormality` (default: 3)
    - `excludeRecent` (default: true)
    - `excludeDays` (default: 2)
- `GET /api/outfits/rating/{minRating}` - 평점 기준 조회
- `GET /api/outfits/formality` - 격식도 범위 조회
- `POST /api/outfits/{id}/worn` - 착용 기록

### Histories API
- `GET /api/histories` - 전체 착용 기록 조회
- `GET /api/histories/{id}` - 착용 기록 단건 조회
- `POST /api/histories` - 착용 기록 등록
- `DELETE /api/histories/{id}` - 착용 기록 삭제
- `GET /api/histories/outfit/{outfitId}` - 코디별 이력 조회
- `GET /api/histories/range` - 날짜 범위 조회
- `GET /api/histories/month` - 월별 조회

---

## 🎨 프론트엔드 페이지

### 구현 완료 페이지
1. ✅ **/ (Home)** - 메인 대시보드
2. ✅ **/wardrobe** - 옷장 아이템 목록
3. ✅ **/wardrobe/add** - 아이템 추가
4. ✅ **/wardrobe/[id]** - 아이템 상세/수정
5. ✅ **/outfits** - 코디 목록
6. ✅ **/outfits/create** - 코디 생성
7. ✅ **/outfits/[id]** - 코디 상세/수정
8. ✅ **/recommend** - 커스텀 추천 설정
9. ✅ **/recommend/result** - 추천 결과
10. ✅ **/calendar** - 착용 캘린더

---

## ✅ 구현 현황

### Backend (Spring Boot)
- ✅ 엔티티 및 데이터 모델 설계
- ✅ Repository 계층 구현
- ✅ Service 계층 비즈니스 로직 구현
- ✅ Controller 계층 RESTful API 구현
- ✅ DTO 패턴 적용 (Request/Response 분리)
- ✅ 추천 알고리즘 구현
- ✅ 착용 기록 자동 업데이트
- ✅ CORS 설정 (localhost:3000, localhost:3001)
- ✅ Swagger API 문서화
- ✅ 예외 처리 및 에러 응답
- ✅ PRD 요구사항 반영 (excludeDays=2, excludeRecent=true)

### Frontend (Next.js)
- ✅ 모든 페이지 백엔드 API 연동 완료
- ✅ TypeScript 타입 안전성 확보
- ✅ API 클라이언트 라이브러리 (`/lib/api.ts`)
- ✅ 로딩 상태 및 에러 처리
- ✅ Toast 알림 시스템
- ✅ 반응형 디자인 (모바일/태블릿/데스크톱)
- ✅ shadcn/ui 컴포넌트 활용
- ✅ 이미지 업로드 (Base64)
- ✅ 실시간 데이터 업데이트
- ✅ 사용자 친화적 UI/UX

---

## 🚀 실행 방법

### Backend
```bash
cd cody-back
./gradlew bootRun
# Server runs on http://localhost:8080
```

### Frontend
```bash
cd cody-front
npm install
npm run dev
# App runs on http://localhost:3000
```

### Environment Variables
```env
# Frontend (.env.local)
NEXT_PUBLIC_API_URL=http://localhost:8080/api
```

---

## 📊 주요 비즈니스 로직

### 추천 알고리즘
1. **필터링 단계**
   - 최소 평점 이상의 코디만 선택
   - 최소 격식도 이상의 코디만 선택
   - excludeRecent=true 시 최근 N일(기본 2일) 내 착용 코디 제외

2. **정렬 단계**
   - 착용한 적 없는 코디 우선
   - 마지막 착용일이 오래된 순서로 정렬

3. **결과 제공**
   - 조건에 맞는 모든 코디 반환
   - 프론트엔드에서 순환하며 여러 추천 제공

### 착용 기록 관리
1. **기록 시점**
   - "오늘 입기" 버튼 클릭
   - 추천 결과에서 "이 코디 선택" 클릭

2. **자동 업데이트**
   - `lastWornDate` 업데이트
   - `wornCount` 자동 증가
   - History 레코드 생성

3. **통계 계산**
   - 월별 착용 일수
   - 고유 코디 수
   - 평균 격식도
   - 최다 착용 횟수

---

## 🔜 향후 개선 방향

### Phase 3 (Future Enhancements)
- [ ] 사용자 인증 및 권한 관리
- [ ] 날씨 API 연동 (기상 조건 기반 추천)
- [ ] AI 이미지 인식 (자동 카테고리/색상 분류)
- [ ] 소셜 기능 (코디 공유, 좋아요)
- [ ] 통계 대시보드 (착용 트렌드 분석)
- [ ] 클라우드 이미지 스토리지 (AWS S3, Cloudinary)
- [ ] 모바일 앱 (React Native)
- [ ] 다크 모드
- [ ] 다국어 지원 (i18n)

### 기술 개선
- [ ] PostgreSQL 마이그레이션
- [ ] Redis 캐싱
- [ ] Docker 컨테이너화
- [ ] CI/CD 파이프라인
- [ ] 단위 테스트 및 통합 테스트
- [ ] 성능 최적화 (인덱싱, 쿼리 최적화)

---

## 📝 변경 이력

### v2.0 (2025-11-11)
- ✅ PRD 요구사항 100% 반영
- ✅ 최근 착용 제외 기본값 2일로 변경
- ✅ excludeRecent 기본값 true로 변경
- ✅ 모든 프론트엔드 페이지 백엔드 연동 완료
- ✅ 캘린더 뷰 실시간 데이터 연동
- ✅ 추천 시스템 완전 구현
- ✅ 착용 기록 자동화

### v1.0 (Initial)
- ✅ 기본 CRUD 기능 구현
- ✅ 백엔드 API 구조 설계
- ✅ 프론트엔드 UI 프로토타입

---

## 📞 문의 및 지원

**Project**: Cody - What to Wear Today
**Version**: 2.0
**Last Updated**: 2025-11-11
**Status**: ✅ Production Ready

---

**Built with ❤️ using Spring Boot & Next.js**
