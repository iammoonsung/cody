# 프론트엔드-백엔드 연동 완료

## 📋 개요

Cody Wardrobe 프로젝트의 프론트엔드(Next.js)와 백엔드(Spring Boot)가 성공적으로 연동되었습니다.

## 🔧 설정 내용

### 1. 백엔드 CORS 설정

**파일**: `cody-back/src/main/java/com/cody/wardrobe/config/SecurityConfig.java`

```java
@Bean
public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();

    // 프론트엔드 URL 허용
    configuration.setAllowedOrigins(List.of(
        "http://localhost:3000",
        "http://localhost:3001"
    ));

    // 모든 HTTP 메서드 허용
    configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));

    // 모든 헤더 허용
    configuration.setAllowedHeaders(List.of("*"));

    // 인증 정보 포함 허용
    configuration.setAllowCredentials(true);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);

    return source;
}
```

### 2. 프론트엔드 API 클라이언트

**파일**: `cody-front/lib/api.ts`

- TypeScript로 작성된 타입 안전 API 클라이언트
- 모든 백엔드 엔드포인트에 대한 함수 제공
- 자동 에러 처리 및 응답 파싱

**환경 변수**: `cody-front/.env.local`
```
NEXT_PUBLIC_API_URL=http://localhost:8080/api
```

### 3. Wardrobe 페이지 연동

**파일**: `cody-front/app/wardrobe/page.tsx`

- 백엔드에서 아이템 데이터 실시간 로딩
- 로딩 상태 표시
- 에러 처리
- 필터링 기능 (카테고리, 검색)

## 🚀 실행 방법

### 백엔드 시작 (포트 8080)

```bash
cd cody-back
./gradlew bootRun
```

### 프론트엔드 시작 (포트 3001)

```bash
cd cody-front
npm run dev
```

## 📡 사용 가능한 API

### Item API
- `GET /api/items` - 모든 아이템 조회
- `GET /api/items/{id}` - 특정 아이템 조회
- `POST /api/items` - 아이템 생성
- `PUT /api/items/{id}` - 아이템 수정
- `DELETE /api/items/{id}` - 아이템 삭제
- `GET /api/items/category/{category}` - 카테고리별 조회
- `GET /api/items/season/{season}` - 계절별 조회
- `GET /api/items/color/{color}` - 색상별 조회

### Outfit API
- `GET /api/outfits` - 모든 코디 조회
- `GET /api/outfits/{id}` - 특정 코디 조회
- `POST /api/outfits` - 코디 생성
- `PUT /api/outfits/{id}` - 코디 수정
- `DELETE /api/outfits/{id}` - 코디 삭제
- `GET /api/outfits/rating/{minRating}` - 평점별 조회
- `GET /api/outfits/formality` - 격식도별 조회
- `POST /api/outfits/{outfitId}/items/{itemId}` - 아이템 추가
- `DELETE /api/outfits/{outfitId}/items/{itemId}` - 아이템 제거
- `POST /api/outfits/{outfitId}/worn` - 착용 기록

### Recommendation API
- `GET /api/recommendations` - 코디 추천

### History API
- `GET /api/histories` - 모든 히스토리 조회
- `GET /api/histories/{id}` - 특정 히스토리 조회
- `POST /api/histories` - 히스토리 생성
- `DELETE /api/histories/{id}` - 히스토리 삭제
- `GET /api/histories/outfit/{outfitId}` - 코디별 히스토리
- `GET /api/histories/date-range` - 날짜 범위별 조회
- `GET /api/histories/month` - 월별 조회

## 🧪 테스트 데이터

현재 데이터베이스에 있는 테스트 데이터:

1. **Winter Jacket** (OUTERWEAR, Black, WINTER)
2. **White Shirt** (TOPS, White, SPRING)
3. **White Sneakers** (SHOES, White)
4. **Blue Jeans** (BOTTOMS, Blue)

## 🎯 프론트엔드에서 API 사용 예시

```typescript
import { api } from '@/lib/api'

// 아이템 조회
const items = await api.getItems()

// 아이템 생성
const newItem = await api.createItem({
  category: 'TOPS',
  name: 'Blue T-Shirt',
  imageUrl: 'https://example.com/tshirt.jpg',
  color: 'Blue',
  season: 'SUMMER'
})

// 코디 생성
const newOutfit = await api.createOutfit({
  name: 'Casual Look',
  rating: 4,
  formalityLevel: 2,
  itemIds: [1, 3, 4]
})

// 히스토리 생성
const history = await api.createHistory({
  outfitId: 1,
  wornDate: '2025-11-11'
})
```

## ✅ 연동 확인 방법

### 1. 브라우저에서 확인
```
http://localhost:3001/wardrobe
```

실제 백엔드 데이터가 표시되는지 확인

### 2. API 직접 호출
```bash
curl http://localhost:8080/api/items
```

### 3. 브라우저 개발자 도구
- Network 탭에서 API 호출 확인
- Console에서 에러가 없는지 확인

## 🔍 트러블슈팅

### CORS 에러 발생 시
- 백엔드 SecurityConfig에서 프론트엔드 URL이 정확한지 확인
- 백엔드 재시작

### API 연결 실패 시
- `.env.local` 파일의 `NEXT_PUBLIC_API_URL` 확인
- 백엔드가 8080 포트에서 실행 중인지 확인
- MySQL이 3308 포트에서 실행 중인지 확인

### 데이터가 표시되지 않을 시
- 브라우저 개발자 도구 Console 확인
- 백엔드 로그 확인
- 데이터베이스에 데이터가 있는지 확인

## 📝 다음 단계

1. 다른 페이지들도 API 연동 (outfits, recommend, calendar)
2. 아이템 생성/수정 페이지 연동
3. 코디 생성/수정 페이지 연동
4. 이미지 업로드 기능 구현
5. 추천 알고리즘 활성화

## 🎉 완료!

프론트엔드와 백엔드가 성공적으로 연결되었습니다.
이제 프론트엔드에서 실제 데이터를 확인하고 CRUD 작업을 수행할 수 있습니다!
