# Cody - What to Wear Today 🎨👔

> 개인 옷장을 디지털화하고 AI 기반 추천으로 매일 최적의 코디를 제안하는 스타일링 도우미

<br/>

## 📚 목차
1. [프로젝트 개요](#프로젝트-개요)
2. [시작하기](#시작하기)
3. [프로젝트 구조](#프로젝트-구조)
4. [기능별 상세 설명](#기능별-상세-설명)
5. [프론트엔드-백엔드 연동](#프론트엔드-백엔드-연동)
6. [데이터 흐름](#데이터-흐름)
7. [주요 코드 설명](#주요-코드-설명)
8. [트러블슈팅](#트러블슈팅)

<br/>

---

## 프로젝트 개요

### 🎯 서비스 목표
사용자가 가진 옷들을 등록하고, 조합하여 코디를 만들고, 매일 입을 옷을 추천받는 서비스입니다.

### 🛠 기술 스택

#### 백엔드
```
Spring Boot 3.3.5
Java 17
JPA / Hibernate
H2 Database (Dev)
Gradle
```

#### 프론트엔드
```
Next.js 16.0.0
TypeScript
TailwindCSS
shadcn/ui
```

### 🌐 아키텍처
```
┌─────────────────┐         HTTP API          ┌─────────────────┐
│   Next.js       │ ←───────────────────────→ │  Spring Boot    │
│   (Frontend)    │    JSON Request/Response   │   (Backend)     │
│   Port: 3000    │                            │   Port: 8080    │
└─────────────────┘                            └─────────────────┘
        │                                               │
        │                                               │
        ↓                                               ↓
   UI Components                                   H2 Database
   State Management                                JPA Entities
```

<br/>

---

## 시작하기

### 1️⃣ 사전 준비
```bash
# 필수 설치 항목
- Java 17 이상
- Node.js 18 이상
- npm 또는 yarn
```

### 2️⃣ 백엔드 실행
```bash
# 1. 백엔드 디렉토리로 이동
cd /Users/moonsung/workspace/cody/cody-back

# 2. 애플리케이션 실행
./gradlew bootRun

# ✅ 실행 확인: http://localhost:8080/swagger-ui/index.html
```

### 3️⃣ 프론트엔드 실행
```bash
# 1. 프론트엔드 디렉토리로 이동
cd /Users/moonsung/workspace/cody/cody-front

# 2. 패키지 설치 (최초 1회)
npm install

# 3. 개발 서버 실행
npm run dev

# ✅ 실행 확인: http://localhost:3000
```

### 4️⃣ 환경 변수 설정
```bash
# cody-front/.env.local 파일 생성
NEXT_PUBLIC_API_URL=http://localhost:8080/api
```

<br/>

---

## 프로젝트 구조

### 📁 백엔드 구조
```
cody-back/src/main/java/com/cody/wardrobe/
│
├── domain/                          # 도메인 계층 (비즈니스 로직)
│   ├── item/                        # 아이템(옷) 도메인
│   │   ├── Item.java               # 엔티티
│   │   ├── service/
│   │   │   └── ItemService.java   # 비즈니스 로직
│   │   ├── repository/
│   │   │   └── ItemRepository.java # DB 접근
│   │   └── dto/                     # 데이터 전송 객체
│   │
│   ├── outfit/                      # 코디 도메인
│   │   ├── Outfit.java
│   │   ├── OutfitItem.java         # 코디-아이템 연결
│   │   ├── service/
│   │   ├── repository/
│   │   └── dto/
│   │
│   └── history/                     # 착용 기록 도메인
│       ├── History.java
│       ├── service/
│       ├── repository/
│       └── dto/
│
├── controller/                      # API 컨트롤러 계층
│   ├── ItemController.java         # /api/items
│   ├── OutfitController.java       # /api/outfits
│   ├── HistoryController.java      # /api/histories
│   └── dto/                         # 요청/응답 DTO
│       ├── item/
│       ├── outfit/
│       └── history/
│
├── common/                          # 공통 설정
│   ├── ApiResponse.java            # 통일된 API 응답 형식
│   └── config/
│       └── WebConfig.java          # CORS 설정
│
└── WardrobeApplication.java        # 메인 애플리케이션
```

### 📁 프론트엔드 구조
```
cody-front/
│
├── app/                             # Next.js 13+ App Router
│   ├── page.tsx                    # 홈 페이지 (/)
│   │
│   ├── wardrobe/                   # 옷장 관리
│   │   ├── page.tsx               # 아이템 목록 (/wardrobe)
│   │   ├── add/
│   │   │   └── page.tsx           # 아이템 추가 (/wardrobe/add)
│   │   └── [id]/
│   │       └── page.tsx           # 아이템 상세 (/wardrobe/123)
│   │
│   ├── outfits/                    # 코디 관리
│   │   ├── page.tsx               # 코디 목록 (/outfits)
│   │   ├── create/
│   │   │   └── page.tsx           # 코디 생성 (/outfits/create)
│   │   └── [id]/
│   │       └── page.tsx           # 코디 상세 (/outfits/123)
│   │
│   ├── recommend/                  # 추천 기능
│   │   ├── page.tsx               # 추천 설정 (/recommend)
│   │   └── result/
│   │       └── page.tsx           # 추천 결과 (/recommend/result)
│   │
│   └── calendar/                   # 캘린더 뷰
│       └── page.tsx               # 착용 캘린더 (/calendar)
│
├── components/                      # 재사용 가능한 UI 컴포넌트
│   └── ui/                         # shadcn/ui 컴포넌트
│
├── lib/                            # 유틸리티 및 설정
│   └── api.ts                     # 🔥 백엔드 API 클라이언트 (중요!)
│
└── hooks/                          # 커스텀 React Hooks
    └── use-toast.ts               # 토스트 알림
```

<br/>

---

## 기능별 상세 설명

### 1️⃣ 옷장 관리 (Item Management)

#### 📌 개념
사용자가 가진 옷(아이템)을 등록하고 관리하는 기능입니다.

#### 🎯 주요 기능
- 아이템 등록 (사진, 카테고리, 이름, 색상, 시즌)
- 아이템 목록 조회
- 아이템 수정/삭제
- 카테고리별, 색상별, 시즌별 필터링

#### 🔄 데이터 흐름 (아이템 등록 예시)

```
[프론트엔드]                              [백엔드]

1. 사용자가 이미지 업로드
   ↓
2. 이미지를 Base64로 변환
   ↓
3. FormData 생성
   {
     category: "TOPS",
     name: "흰색 셔츠",
     imageUrl: "data:image/png;base64...",
     color: "White",
     season: "All"
   }
   ↓
4. API 호출
   api.createItem(data)
   ↓
   ────────────────────────────────────→  POST /api/items
                                           ↓
                                        5. ItemController 진입
                                           @PostMapping
                                           ↓
                                        6. Request → DTO 변환
                                           ItemCreateDto
                                           ↓
                                        7. ItemService 호출
                                           createItem()
                                           ↓
                                        8. Item 엔티티 생성
                                           ↓
                                        9. ItemRepository 저장
                                           save()
                                           ↓
                                        10. DB에 INSERT
                                           ↓
                                        11. ItemDto 반환
   ←────────────────────────────────────
   12. 응답 받음
   {
     result: true,
     data: { id: 1, name: "흰색 셔츠", ... }
   }
   ↓
13. UI 업데이트 & 토스트 알림
```

#### 💻 코드 위치

**백엔드**
```java
// 1. 엔티티 (DB 테이블)
domain/item/Item.java

// 2. 비즈니스 로직
domain/item/service/ItemService.java
→ createItem(), getItems(), updateItem(), deleteItem()

// 3. API 엔드포인트
controller/ItemController.java
→ POST /api/items
→ GET /api/items
→ GET /api/items/{id}
→ PUT /api/items/{id}
→ DELETE /api/items/{id}
```

**프론트엔드**
```typescript
// 1. API 클라이언트
lib/api.ts
→ createItem(), getItems(), getItem(), updateItem(), deleteItem()

// 2. 페이지 컴포넌트
app/wardrobe/page.tsx          // 목록
app/wardrobe/add/page.tsx      // 등록
app/wardrobe/[id]/page.tsx     // 상세/수정
```

---

### 2️⃣ 코디 관리 (Outfit Management)

#### 📌 개념
여러 아이템을 조합하여 하나의 코디를 만들고 관리하는 기능입니다.

#### 🎯 주요 기능
- 코디 생성 (아이템 선택, 평점, 격식도 설정)
- 코디 목록 조회
- 코디 수정/삭제
- 평점 및 격식도 기반 필터링

#### 🔄 데이터 흐름 (코디 생성 예시)

```
[프론트엔드]                              [백엔드]

1. 사용자가 아이템 선택
   - 상의: 흰색 셔츠 (id: 1)
   - 하의: 검정 바지 (id: 2)
   - 신발: 구두 (id: 3)
   ↓
2. 평점(5점), 격식도(4) 설정
   ↓
3. API 호출
   api.createOutfit({
     name: "출근룩",
     rating: 5,
     formalityLevel: 4,
     itemIds: [1, 2, 3]
   })
   ↓
   ────────────────────────────────────→  POST /api/outfits
                                           ↓
                                        4. OutfitController 진입
                                           ↓
                                        5. OutfitService 호출
                                           createOutfit()
                                           ↓
                                        6. Outfit 엔티티 생성
                                           ↓
                                        7. 각 아이템을 조회
                                           itemRepository.findById(1)
                                           itemRepository.findById(2)
                                           itemRepository.findById(3)
                                           ↓
                                        8. OutfitItem 생성
                                           (Outfit - Item 연결)
                                           ↓
                                        9. DB 저장
                                           outfitRepository.save()
   ←────────────────────────────────────
   10. 응답
   {
     result: true,
     data: {
       id: 10,
       name: "출근룩",
       rating: 5,
       formalityLevel: 4,
       outfitItems: [...]
     }
   }
   ↓
11. /outfits 페이지로 리다이렉트
```

#### 💻 코드 위치

**백엔드**
```java
// 1. 엔티티
domain/outfit/Outfit.java          // 코디
domain/outfit/OutfitItem.java      // 코디-아이템 연결

// 2. 비즈니스 로직
domain/outfit/service/OutfitService.java
→ createOutfit(), updateOutfit(), deleteOutfit()
→ recommendOutfits()               // 추천 로직

// 3. API 엔드포인트
controller/OutfitController.java
→ POST /api/outfits
→ GET /api/outfits
→ PUT /api/outfits/{id}
→ DELETE /api/outfits/{id}
```

**프론트엔드**
```typescript
// 1. API 클라이언트
lib/api.ts
→ createOutfit(), getOutfits(), updateOutfit(), deleteOutfit()

// 2. 페이지 컴포넌트
app/outfits/page.tsx              // 목록
app/outfits/create/page.tsx       // 생성
app/outfits/[id]/page.tsx         // 상세/수정
```

---

### 3️⃣ 코디 추천 (Recommendation)

#### 📌 개념
사용자의 조건(평점, 격식도)에 맞는 코디를 추천하는 핵심 기능입니다.

#### 🎯 추천 알고리즘
```java
1. 필터링 단계
   - 최소 평점 이상만 선택 (예: 3점 이상)
   - 최소 격식도 이상만 선택 (예: 격식도 3 이상)
   - 최근 N일(2일) 내 착용한 코디 제외 (기본 ON)

2. 정렬 단계
   - 착용한 적 없는 코디 우선
   - 마지막 착용일이 오래된 순으로 정렬

3. 결과 반환
   - 조건에 맞는 모든 코디 반환
```

#### 🔄 데이터 흐름 (추천 받기)

```
[프론트엔드]                              [백엔드]

1. 사용자가 추천 조건 설정
   - 최소 평점: 3
   - 최소 격식도: 3
   - 최근 착용 제외: ON
   ↓
2. /recommend/result로 이동
   (쿼리 파라미터 전달)
   ↓
3. useEffect에서 API 호출
   api.recommendOutfits({
     minRating: 3,
     minFormality: 3,
     excludeRecent: true
   })
   ↓
   ────────────────────────────────────→  GET /api/outfits/recommend
                                           ?minRating=3
                                           &minFormality=3
                                           &excludeRecent=true
                                           &excludeDays=2
                                           ↓
                                        4. OutfitController 진입
                                           recommendOutfits()
                                           ↓
                                        5. excludeRecent 확인
                                           true → recommendOutfitsExcludingRecent()
                                           false → recommendOutfits()
                                           ↓
                                        6. 추천 로직 실행
                                           // OutfitService.java

                                           LocalDate cutoffDate =
                                             LocalDate.now().minusDays(2);

                                           outfitRepository
                                             .findRecommendedOutfitsExcludingRecent(
                                               minRating,
                                               minFormality,
                                               cutoffDate
                                             );
                                           ↓
                                        7. SQL 쿼리 실행
                                           SELECT * FROM outfit o
                                           WHERE o.rating >= 3
                                           AND o.formality_level >= 3
                                           AND (
                                             o.last_worn_date IS NULL
                                             OR o.last_worn_date < '2025-11-09'
                                           )
                                           ORDER BY
                                             o.last_worn_date ASC NULLS FIRST
   ←────────────────────────────────────
   8. 추천 결과 받음
   [
     { id: 5, name: "주말 브런치", rating: 4, ... },
     { id: 3, name: "스마트 캐주얼", rating: 5, ... }
   ]
   ↓
9. 첫 번째 코디 화면에 표시
   ↓
10. "다시 추천받기" 버튼
    → 다음 코디로 순환
```

#### 💻 코드 위치

**백엔드**
```java
// 1. Repository (SQL 쿼리)
domain/outfit/repository/OutfitRepository.java
→ findRecommendedOutfits()
→ findRecommendedOutfitsExcludingRecent()

// 2. 비즈니스 로직
domain/outfit/service/OutfitService.java
→ recommendOutfits()
→ recommendOutfitsExcludingRecent()

// 3. API 엔드포인트
controller/OutfitController.java
→ GET /api/outfits/recommend
```

**프론트엔드**
```typescript
// 1. API 클라이언트
lib/api.ts
→ recommendOutfits(params)

// 2. 페이지 컴포넌트
app/recommend/page.tsx            // 추천 설정
app/recommend/result/page.tsx     // 추천 결과
```

---

### 4️⃣ 착용 기록 (History)

#### 📌 개념
어떤 코디를 언제 입었는지 기록하고 캘린더로 확인하는 기능입니다.

#### 🎯 주요 기능
- 코디 착용 기록 ("오늘 입기" 버튼)
- 월별 착용 이력 조회
- 캘린더 뷰로 시각화
- 월간 통계 (착용 일수, 고유 코디 수, 평균 격식도)

#### 🔄 데이터 흐름 (착용 기록)

```
[프론트엔드]                              [백엔드]

1. 사용자가 "Wear This Outfit Today" 클릭
   (/outfits/123 페이지)
   ↓
2. 오늘 날짜 계산
   const today = new Date()
     .toISOString()
     .split('T')[0];
   // "2025-11-11"
   ↓
3. API 호출
   api.recordOutfitWorn(123, today)
   ↓
   ────────────────────────────────────→  POST /api/outfits/123/worn
                                           ?date=2025-11-11
                                           ↓
                                        4. OutfitController 진입
                                           recordWorn(id, date)
                                           ↓
                                        5. OutfitService 호출
                                           recordWorn(123, "2025-11-11")
                                           ↓
                                        6. Outfit 엔티티 조회
                                           ↓
                                        7. Outfit 업데이트
                                           outfit.recordWorn(date);
                                           // lastWornDate = 2025-11-11
                                           // wornCount++
                                           ↓
                                        8. History 엔티티 생성
                                           History history = new History();
                                           history.setOutfit(outfit);
                                           history.setWornDate(date);
                                           ↓
                                        9. DB 저장
                                           historyRepository.save(history);
   ←────────────────────────────────────
   10. 성공 응답
   ↓
11. 코디 정보 다시 조회
    (업데이트된 wornCount, lastWornDate)
    ↓
12. UI 업데이트 & 토스트 알림
```

#### 💻 코드 위치

**백엔드**
```java
// 1. 엔티티
domain/history/History.java
domain/outfit/Outfit.java
→ recordWorn(LocalDate date) 메서드

// 2. 비즈니스 로직
domain/outfit/service/OutfitService.java
→ recordWorn()

domain/history/service/HistoryService.java
→ getHistoriesByMonth()

// 3. API 엔드포인트
controller/OutfitController.java
→ POST /api/outfits/{id}/worn

controller/HistoryController.java
→ GET /api/histories/month
```

**프론트엔드**
```typescript
// 1. API 클라이언트
lib/api.ts
→ recordOutfitWorn()
→ getHistoriesByMonth()

// 2. 페이지 컴포넌트
app/outfits/[id]/page.tsx         // "Wear This Outfit Today"
app/calendar/page.tsx             // 캘린더 뷰
```

---

### 5️⃣ 격식도 시스템 (Formality Level)

#### 📌 개념
코디의 정형성/격식을 1-5단계로 구분하는 시스템입니다.

#### 🎯 격식도 단계
```
1단계 (Home)         - 집에서 편하게 (트레이닝복, 편한 옷)
2단계 (Neighborhood) - 동네 외출 (청바지 + 티셔츠)
3단계 (Outing)       - 일반 외출 (스마트 캐주얼)
4단계 (Work)         - 업무/회의 (정장, 블라우스)
5단계 (Formal)       - 공식 행사 (수트, 드레스)
```

#### 💻 코드에서 확인
```typescript
// 프론트엔드 - 격식도 라벨
const FORMALITY_LABELS = ["Home", "Neighborhood", "Outing", "Work", "Formal"]

// 프론트엔드 - 격식도 색상
const FORMALITY_COLORS = [
  "bg-[oklch(var(--formality-1))]",  // 1단계 색상
  "bg-[oklch(var(--formality-2))]",
  "bg-[oklch(var(--formality-3))]",
  "bg-[oklch(var(--formality-4))]",
  "bg-[oklch(var(--formality-5))]"   // 5단계 색상
]

// 백엔드 - DB 컬럼
@Column(nullable = false)
private Integer formalityLevel; // 1-5 저장
```

<br/>

---

## 프론트엔드-백엔드 연동

### 🔗 API 클라이언트 (`lib/api.ts`)

이 파일이 **프론트엔드와 백엔드를 연결하는 핵심**입니다!

#### 📍 구조
```typescript
// 1. API 베이스 URL 설정
const API_BASE_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080/api';

// 2. ApiClient 클래스
class ApiClient {
  // 모든 HTTP 요청을 처리하는 기본 메서드
  private async request<T>(endpoint: string, options?: RequestInit): Promise<T>

  // Item API
  async getItems(): Promise<Item[]>
  async createItem(data: CreateItemRequest): Promise<Item>

  // Outfit API
  async getOutfits(): Promise<Outfit[]>
  async createOutfit(data: CreateOutfitRequest): Promise<Outfit>
  async recommendOutfits(params): Promise<Outfit[]>

  // History API
  async getHistoriesByMonth(year: number, month: number): Promise<History[]>
  async recordOutfitWorn(outfitId: number, wornDate: string): Promise<void>
}

// 3. 싱글톤 인스턴스 export
export const api = new ApiClient(API_BASE_URL);
```

#### 🔥 사용 예시

**페이지 컴포넌트에서 API 호출**
```typescript
// app/wardrobe/page.tsx
import { api } from "@/lib/api"

export default function WardrobePage() {
  const [items, setItems] = useState<Item[]>([])

  useEffect(() => {
    const fetchItems = async () => {
      try {
        const data = await api.getItems()  // 👈 API 호출
        setItems(data)
      } catch (err) {
        console.error('Failed to fetch items:', err)
      }
    }

    fetchItems()
  }, [])

  return (
    // UI 렌더링
  )
}
```

### 📡 HTTP 요청/응답 형식

#### 요청 (Request)
```http
POST /api/items HTTP/1.1
Host: localhost:8080
Content-Type: application/json

{
  "category": "TOPS",
  "name": "흰색 셔츠",
  "imageUrl": "data:image/png;base64,...",
  "color": "White",
  "season": "All"
}
```

#### 응답 (Response)
```json
{
  "result": true,
  "data": {
    "id": 1,
    "category": "TOPS",
    "name": "흰색 셔츠",
    "imageUrl": "data:image/png;base64,...",
    "color": "White",
    "season": "All",
    "createdAt": "2025-11-11T10:30:00",
    "updatedAt": "2025-11-11T10:30:00"
  },
  "error": null
}
```

#### 에러 응답
```json
{
  "result": false,
  "data": null,
  "error": "Item not found with id: 999"
}
```

### 🔄 CORS 설정

백엔드에서 프론트엔드 요청을 허용하는 설정입니다.

```java
// common/config/WebConfig.java
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins(
                    "http://localhost:3000",   // 개발 환경
                    "http://localhost:3001"
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
```

<br/>

---

## 데이터 흐름

### 📊 전체 플로우 맵

```
┌─────────────────────────────────────────────────────────────┐
│                        사용자 (User)                          │
└─────────────────────────────────────────────────────────────┘
                              │
                              ↓
┌─────────────────────────────────────────────────────────────┐
│                  프론트엔드 (Next.js)                         │
│                                                               │
│  ┌──────────────┐    ┌──────────────┐    ┌──────────────┐  │
│  │  UI 컴포넌트  │ ←→ │  State 관리   │ ←→ │  API Client  │  │
│  │  (page.tsx)  │    │  (useState)  │    │  (lib/api)   │  │
│  └──────────────┘    └──────────────┘    └──────────────┘  │
│                                                 ↓             │
│                                          HTTP Request         │
└─────────────────────────────────────────────────────────────┘
                              ↓
                    ┌──────────────────┐
                    │   CORS Filter    │
                    └──────────────────┘
                              ↓
┌─────────────────────────────────────────────────────────────┐
│                   백엔드 (Spring Boot)                        │
│                                                               │
│  ┌──────────────┐    ┌──────────────┐    ┌──────────────┐  │
│  │  Controller  │ ←→ │   Service    │ ←→ │  Repository  │  │
│  │  (API 진입)   │    │ (비즈니스 로직)│    │  (DB 접근)   │  │
│  └──────────────┘    └──────────────┘    └──────────────┘  │
│                                                 ↓             │
│                                            JPA/Hibernate      │
└─────────────────────────────────────────────────────────────┘
                              ↓
                    ┌──────────────────┐
                    │   H2 Database    │
                    │   (파일/메모리)    │
                    └──────────────────┘
```

### 🎬 시나리오: 코디 생성 전체 과정

```
1. [사용자] "Create Outfit" 버튼 클릭
   ↓
2. [프론트] /outfits/create 페이지 이동
   ↓
3. [프론트] useEffect로 아이템 목록 조회
   api.getItems() 호출
   ↓
4. [백엔드] GET /api/items 처리
   → ItemController → ItemService → ItemRepository → DB
   ↓
5. [프론트] 아이템 목록 화면에 표시
   ↓
6. [사용자] 아이템 3개 선택 + 이름/평점/격식도 입력
   ↓
7. [사용자] "Save Outfit" 버튼 클릭
   ↓
8. [프론트] handleSave() 함수 실행
   api.createOutfit({
     name: "주말룩",
     rating: 4,
     formalityLevel: 2,
     itemIds: [1, 5, 7]
   })
   ↓
9. [백엔드] POST /api/outfits 처리
   → OutfitController
   → OutfitCreateDto 변환
   → OutfitService.createOutfit()
   → Outfit 엔티티 생성
   → 각 itemId로 Item 조회 (findById)
   → OutfitItem 3개 생성
   → outfitRepository.save()
   → DB에 INSERT
   ↓
10. [백엔드] 생성된 Outfit 반환
    OutfitDto → OutfitResponse
    ↓
11. [프론트] 응답 받음
    ↓
12. [프론트] 성공 토스트 표시
    router.push("/outfits") // 목록으로 이동
    ↓
13. [프론트] /outfits 페이지에서 새로 생성된 코디 확인
```

<br/>

---

## 주요 코드 설명

### 1️⃣ 백엔드: Repository 쿼리 메서드

```java
// domain/outfit/repository/OutfitRepository.java

public interface OutfitRepository extends JpaRepository<Outfit, Long> {

    /**
     * 🔥 추천 로직 - 최근 착용 제외
     *
     * @param minRating 최소 평점
     * @param minFormality 최소 격식도
     * @param cutoffDate 제외 기준일 (예: 2일 전)
     * @return 조건에 맞는 코디 리스트 (오래된 순)
     */
    @Query("""
        SELECT o FROM Outfit o
        WHERE o.rating >= :minRating
        AND o.formalityLevel >= :minFormality
        AND (o.lastWornDate IS NULL OR o.lastWornDate < :cutoffDate)
        ORDER BY o.lastWornDate ASC NULLS FIRST
    """)
    List<Outfit> findRecommendedOutfitsExcludingRecent(
        @Param("minRating") Integer minRating,
        @Param("minFormality") Integer minFormality,
        @Param("cutoffDate") LocalDate cutoffDate
    );
}
```

**설명:**
- `@Query`: JPQL로 직접 쿼리 작성
- `NULLS FIRST`: 착용 이력 없는 코디 우선
- `cutoffDate`: `LocalDate.now().minusDays(2)` 로 2일 전 계산

---

### 2️⃣ 백엔드: Service 비즈니스 로직

```java
// domain/outfit/service/OutfitService.java

@Service
@RequiredArgsConstructor
public class OutfitService {

    private final OutfitRepository outfitRepository;
    private final ItemRepository itemRepository;

    /**
     * 코디 생성
     */
    @Transactional
    public OutfitDto createOutfit(OutfitCreateDto createDto) {
        // 1. Outfit 엔티티 생성
        Outfit outfit = Outfit.builder()
                .name(createDto.getName())
                .rating(createDto.getRating())
                .formalityLevel(createDto.getFormalityLevel())
                .wornCount(0)
                .build();

        // 2. 각 아이템 조회 후 OutfitItem 생성
        for (Long itemId : createDto.getItemIds()) {
            Item item = itemRepository.findById(itemId)
                    .orElseThrow(() -> new IllegalArgumentException("Item not found"));

            outfit.addItem(item);  // OutfitItem 자동 생성
        }

        // 3. DB 저장
        Outfit savedOutfit = outfitRepository.save(outfit);

        // 4. DTO로 변환하여 반환
        return OutfitDto.from(savedOutfit);
    }

    /**
     * 착용 기록
     */
    @Transactional
    public void recordWorn(Long outfitId, LocalDate wornDate) {
        Outfit outfit = outfitRepository.findById(outfitId)
                .orElseThrow(() -> new IllegalArgumentException("Outfit not found"));

        // Outfit 엔티티의 메서드 호출
        outfit.recordWorn(wornDate);  // lastWornDate, wornCount 자동 업데이트

        // JPA의 변경 감지(Dirty Checking)로 자동 UPDATE
    }
}
```

---

### 3️⃣ 프론트엔드: API 클라이언트

```typescript
// lib/api.ts

class ApiClient {

  /**
   * 기본 HTTP 요청 메서드
   */
  private async request<T>(endpoint: string, options?: RequestInit): Promise<T> {
    const url = `${this.baseUrl}${endpoint}`;

    // 1. fetch 호출
    const response = await fetch(url, {
      ...options,
      headers: {
        'Content-Type': 'application/json',
        ...options?.headers,
      },
    });

    // 2. HTTP 에러 체크
    if (!response.ok) {
      throw new Error(`API Error: ${response.status}`);
    }

    // 3. JSON 파싱
    const data: ApiResponse<T> = await response.json();

    // 4. 비즈니스 로직 에러 체크
    if (!data.result) {
      throw new Error(data.error || 'API request failed');
    }

    // 5. 실제 데이터만 반환
    return data.data;
  }

  /**
   * 코디 추천 API
   */
  async recommendOutfits(params: {
    minRating?: number;
    minFormality?: number;
    excludeRecent?: boolean;
    excludeDays?: number;
  }): Promise<Outfit[]> {
    // Query Parameter 생성
    const queryParams = new URLSearchParams();
    if (params.minRating !== undefined)
      queryParams.append('minRating', params.minRating.toString());
    if (params.minFormality !== undefined)
      queryParams.append('minFormality', params.minFormality.toString());
    if (params.excludeRecent !== undefined)
      queryParams.append('excludeRecent', params.excludeRecent.toString());
    if (params.excludeDays !== undefined)
      queryParams.append('excludeDays', params.excludeDays.toString());

    // GET 요청
    return this.request<Outfit[]>(`/outfits/recommend?${queryParams.toString()}`);
  }
}
```

---

### 4️⃣ 프론트엔드: 페이지 컴포넌트

```typescript
// app/recommend/result/page.tsx

function RecommendResultContent() {
  const [recommendedOutfits, setRecommendedOutfits] = useState<Outfit[]>([])
  const [currentIndex, setCurrentIndex] = useState(0)
  const [isLoading, setIsLoading] = useState(true)

  // 1. URL에서 파라미터 읽기
  const searchParams = useSearchParams()
  const minRating = searchParams.get("minRating") || "3"
  const minFormality = searchParams.get("minFormality") || "3"
  const excludeRecent = searchParams.get("excludeRecent") === "true"

  // 2. 컴포넌트 마운트 시 추천 API 호출
  useEffect(() => {
    const fetchRecommendations = async () => {
      try {
        setIsLoading(true)

        // API 호출
        const outfits = await api.recommendOutfits({
          minRating: Number(minRating),
          minFormality: Number(minFormality),
          excludeRecent,
        })

        // 결과 없으면 에러
        if (outfits.length === 0) {
          toast({ title: "No outfits found" })
          router.push("/recommend")
          return
        }

        // State 업데이트
        setRecommendedOutfits(outfits)
      } catch (err) {
        console.error('Failed to fetch recommendations:', err)
        toast({ title: "Error", variant: "destructive" })
      } finally {
        setIsLoading(false)
      }
    }

    fetchRecommendations()
  }, [minRating, minFormality, excludeRecent])

  // 3. "다시 추천받기" 핸들러
  const handleRecommendAgain = () => {
    const nextIndex = (currentIndex + 1) % recommendedOutfits.length
    setCurrentIndex(nextIndex)
  }

  // 4. "이 코디 선택" 핸들러
  const handleSelectOutfit = async () => {
    const outfit = recommendedOutfits[currentIndex]
    const today = new Date().toISOString().split('T')[0]

    await api.recordOutfitWorn(outfit.id, today)

    toast({ title: "Success!" })
    router.push("/calendar")
  }

  // 5. UI 렌더링
  return (
    <div>
      {/* 추천된 코디 표시 */}
      <Button onClick={handleSelectOutfit}>Select This Outfit</Button>
      <Button onClick={handleRecommendAgain}>Recommend Again</Button>
    </div>
  )
}
```

<br/>

---

## 트러블슈팅

### ❌ 문제 1: CORS 에러
```
Access to fetch at 'http://localhost:8080/api/items' from origin
'http://localhost:3000' has been blocked by CORS policy
```

**해결:**
```java
// 백엔드: common/config/WebConfig.java 확인
.allowedOrigins("http://localhost:3000")
```

---

### ❌ 문제 2: API 호출 후 데이터가 안 보임
```typescript
// 콘솔에 에러 없는데 화면에 데이터가 안 뜰 때
```

**체크리스트:**
1. 백엔드 서버가 켜져있는지 확인 (`http://localhost:8080`)
2. 프론트 환경변수 확인 (`.env.local`)
3. API 응답 구조 확인
   ```typescript
   // 올바른 접근
   const data = await api.getItems()  // data에 바로 Item[] 들어옴

   // 틀린 접근
   const response = await fetch('/api/items')
   const json = await response.json()
   const data = json.data  // ❌ api.ts에서 이미 처리함
   ```

---

### ❌ 문제 3: 이미지가 안 보임
```typescript
// 이미지 업로드했는데 화면에 안 뜰 때
```

**원인:** Base64 문자열이 너무 길어서 로그에 보이지 않음

**확인 방법:**
```typescript
console.log(item.imageUrl.substring(0, 50))  // 앞 50자만 확인
// "data:image/png;base64,iVBORw0KGgoAAAANSUhEUg..."
```

---

### ❌ 문제 4: 추천이 안 나옴
```
No outfits found
```

**체크리스트:**
1. DB에 코디가 있는지 확인
   - `http://localhost:8080/h2-console` 접속
   - URL: `jdbc:h2:~/wardrobe`
   - `SELECT * FROM outfit` 실행

2. 필터 조건 확인
   ```java
   // 모든 코디가 rating=2, formality=1 인데
   // minRating=3, minFormality=3 으로 검색하면 결과 없음
   ```

3. 최근 착용 제외 확인
   ```java
   // 모든 코디를 오늘 입었다면
   // excludeRecent=true, excludeDays=2 일 때 결과 없음
   ```

---

### ❌ 문제 5: H2 콘솔 접속 안됨

**설정 확인:**
```yaml
# application.yml
spring:
  h2:
    console:
      enabled: true
      path: /h2-console
```

**접속 정보:**
- URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:~/wardrobe`
- Username: `sa`
- Password: (비워두기)

<br/>

---

## 🎓 신입 개발자를 위한 팁

### 📚 학습 순서
1. **먼저 백엔드부터 이해하기**
   - Entity → Repository → Service → Controller 순서
   - Swagger로 API 테스트해보기

2. **프론트엔드 따라가기**
   - lib/api.ts 먼저 읽기
   - 한 페이지씩 흐름 따라가기

3. **디버깅 연습**
   - 백엔드: `System.out.println()` 또는 디버거
   - 프론트: `console.log()`

### 🔍 코드 읽는 법
```
1. 사용자 액션 찾기 (예: 버튼 클릭)
   ↓
2. 핸들러 함수 찾기 (onClick, handleSubmit 등)
   ↓
3. API 호출 찾기 (api.XXX)
   ↓
4. lib/api.ts에서 메서드 확인
   ↓
5. 백엔드 Controller 찾기
   ↓
6. Service → Repository 순서로 따라가기
```

### 💡 추천 개발 도구
- **백엔드 테스트**: Postman, Swagger UI
- **프론트 디버깅**: React DevTools, Network Tab
- **DB 확인**: H2 Console
- **코드 에디터**: IntelliJ IDEA (백), VSCode (프론트)

<br/>

---

## 📞 도움이 필요할 때

### 📋 이슈 보고 템플릿
```
## 문제 상황
- 무엇을 하려고 했나요?
- 어떤 에러가 발생했나요?

## 환경
- 브라우저: Chrome 버전 XXX
- 백엔드 실행 상태: 실행 중 / 중지
- 프론트 실행 상태: 실행 중 / 중지

## 재현 방법
1. XXX 페이지 접속
2. XXX 버튼 클릭
3. 에러 발생

## 에러 메시지
(콘솔 로그 붙여넣기)
```

### 🔗 유용한 링크
- [Spring Boot 공식 문서](https://spring.io/projects/spring-boot)
- [Next.js 공식 문서](https://nextjs.org/docs)
- [shadcn/ui 컴포넌트](https://ui.shadcn.com/)
- [TailwindCSS 문서](https://tailwindcss.com/docs)

<br/>

---

**🎉 환영합니다! 궁금한 점이 있으면 언제든 물어보세요!**

---

**작성일**: 2025-11-11
**작성자**: Cody Development Team
**버전**: 2.0
