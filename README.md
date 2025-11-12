# Cody - 스마트 옷장 관리 서비스 🎨👔

> 개인 옷장을 디지털화하고, AI 기반 추천으로 매일 최적의 코디를 제안하는 서비스

**신입사원 인수인계 문서**  
이 문서는 신입 개발자가 Cody 프로젝트를 빠르게 이해하고 개발에 참여할 수 있도록 작성되었습니다.

---

## 📚 목차

1. [프로젝트 개요](#-프로젝트-개요)
2. [시작하기](#-시작하기)
3. [환경변수 및 설정 관리](#️-환경변수-및-설정-관리)
4. [프로젝트 구조](#-프로젝트-구조)
5. [프론트엔드-백엔드 연동](#-프론트엔드-백엔드-연동)
6. [기능별 구현 가이드](#-기능별-구현-가이드)
7. [데이터베이스 스키마](#-데이터베이스-스키마)
8. [개발 도구 및 팁](#-개발-도구-및-팁)
9. [트러블슈팅](#-트러블슈팅)
10. [코드 읽는 법](#-코드-읽는-법)
11. [모바일 앱으로 변환하기 (PWA)](#-모바일-앱으로-변환하기-pwa)

---

## 🎯 프로젝트 개요

### 서비스 소개

Cody는 사용자가 보유한 옷을 디지털 옷장에 등록하고, 이를 조합하여 코디를 만들며, 매일 최적의 코디를 추천받을 수 있는 서비스입니다.

**핵심 기능:**
- 옷장 관리: 보유한 옷을 사진과 함께 등록 및 관리
- 코디 생성: 여러 옷을 조합하여 나만의 코디 저장
- 스마트 추천: 평점, 격식도, 착용 이력을 고려한 코디 추천
- 착용 기록: 언제 어떤 코디를 입었는지 캘린더로 관리

### 기술 스택

#### 백엔드 (cody-back)
```
언어/프레임워크: Java 21, Spring Boot 3.3.5
데이터베이스: MySQL 8.0
ORM: JPA (Hibernate)
마이그레이션: Flyway
보안: Spring Security, JWT
API 문서: Swagger/OpenAPI 3
빌드 도구: Gradle
서버: Undertow
포트: 8080
```

#### 프론트엔드 (cody-front)
```
프레임워크: Next.js 16.0 (App Router)
언어: TypeScript
UI 라이브러리: React 19
스타일링: TailwindCSS 4
컴포넌트: shadcn/ui
HTTP 클라이언트: Fetch API
포트: 3000
```

### 아키텍처

```
┌─────────────────────────────────────────────────────────────────┐
│                        사용자 (User)                              │
│                     브라우저 (Chrome, Safari 등)                   │
└─────────────────────────────────────────────────────────────────┘
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                  프론트엔드 (Next.js)                              │
│                    http://localhost:3000                         │
│                                                                   │
│  ┌─────────────┐    ┌─────────────┐    ┌─────────────┐         │
│  │    Pages    │───▶│    State    │───▶│  API Client │         │
│  │  (app/*.tsx)│    │  (useState) │    │ (lib/api.ts)│         │
│  └─────────────┘    └─────────────┘    └─────────────┘         │
│                                               │                   │
│                                               │ HTTP Request      │
│                                               ▼                   │
│                              {                                    │
│                                "category": "TOPS",               │
│                                "name": "흰색 셔츠",              │
│                                "imageUrl": "data:image...",      │
│                                ...                               │
│                              }                                    │
└─────────────────────────────────────────────────────────────────┘
                              │
                              │ REST API (JSON)
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                  백엔드 (Spring Boot)                             │
│                    http://localhost:8080                         │
│                                                                   │
│  ┌─────────────┐    ┌─────────────┐    ┌─────────────┐         │
│  │ Controller  │───▶│   Service   │───▶│ Repository  │         │
│  │  (API 계층)  │    │ (비즈니스 로직)│    │  (DB 접근)  │         │
│  └─────────────┘    └─────────────┘    └─────────────┘         │
│        │                   │                   │                 │
│     Swagger UI          Domain              JPA/JPQL            │
│                         Entities                                 │
└─────────────────────────────────────────────────────────────────┘
                              │
                              │ JDBC
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                    MySQL Database                                │
│                    localhost:3308                                │
│                    Database: cody_wardrobe                       │
│                                                                   │
│     Tables: items, outfits, outfit_items, histories             │
└─────────────────────────────────────────────────────────────────┘
```

**통신 방식:**
- REST API를 통한 JSON 기반 통신
- CORS 설정으로 크로스 오리진 요청 허용
- 모든 응답은 `ApiResponse<T>` 형식으로 통일

---

## 🚀 시작하기

### 필수 설치 항목

개발 환경을 구축하기 위해 다음 프로그램들이 필요합니다:

```bash
# 1. Java 21 (백엔드)
java -version
# openjdk version "21" 또는 그 이상

# 2. Node.js 18+ (프론트엔드)
node -v
# v18.0.0 또는 그 이상

# 3. MySQL 8.0
mysql --version
# mysql Ver 8.0 또는 그 이상

# 4. Git
git --version
```

### 데이터베이스 설정

MySQL을 3308 포트에서 실행하고 데이터베이스를 생성합니다:

```sql
-- MySQL 접속
mysql -u root -p -h localhost -P 3308

-- 데이터베이스 생성
CREATE DATABASE cody_wardrobe CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 확인
SHOW DATABASES;
USE cody_wardrobe;
```

**주의:** 포트가 3308인 이유는 기본 포트(3306) 충돌을 피하기 위함입니다.

### 백엔드 실행

```bash
# 1. 백엔드 디렉토리로 이동
cd /Users/moonsung/workspace/cody/cody-back

# 2. 의존성 설치 및 빌드 (최초 1회)
./gradlew build

# 3. 애플리케이션 실행 (개발 모드)
./gradlew bootRun

# 또는 프로파일을 명시적으로 지정
./gradlew bootRun --args='--spring.profiles.active=dev'

# ✅ 실행 확인
# 터미널에 "Started CodyWardrobeApplication" 메시지가 보이면 성공
# http://localhost:8080 으로 접속 가능
```

**Swagger UI 확인:**
```
http://localhost:8080/swagger-ui.html
```
여기서 모든 API를 테스트할 수 있습니다.

### 프론트엔드 실행

새 터미널을 열고:

```bash
# 1. 프론트엔드 디렉토리로 이동
cd /Users/moonsung/workspace/cody/cody-front

# 2. 의존성 설치 (최초 1회)
npm install

# 3. 환경 변수 설정 (최초 1회)
# .env.local 파일 생성 (로컬 개발 환경)
cat > .env.local << EOF
# Backend API URL
NEXT_PUBLIC_API_URL=http://localhost:8080/api
EOF

# 4. 개발 서버 실행
npm run dev

# ✅ 실행 확인
# 터미널에 "Ready on http://localhost:3000" 메시지가 보이면 성공
```

**브라우저에서 확인:**
```
http://localhost:3000
```

### 동시 실행 체크리스트

두 서버가 모두 실행 중이어야 정상 작동합니다:

- [ ] 백엔드: `http://localhost:8080` (Spring Boot)
- [ ] 프론트엔드: `http://localhost:3000` (Next.js)
- [ ] MySQL: `localhost:3308` (Database)

---

## ⚙️ 환경변수 및 설정 관리

### 프론트엔드 환경변수 (Next.js)

#### 개발 환경 (.env.local)

프론트엔드에서는 `.env.local` 파일을 사용하여 로컬 개발 환경 설정을 관리합니다:

```bash
# cody-front/.env.local
NEXT_PUBLIC_API_URL=http://localhost:8080/api
```

**중요:**
- `.env.local` 파일은 Git에 커밋되지 않습니다 (`.gitignore`에 포함)
- `NEXT_PUBLIC_` 접두사가 붙은 변수만 브라우저에서 접근 가능합니다
- Next.js는 **빌드 타임**에 환경변수를 읽어옵니다

#### 프로덕션 환경

프로덕션 배포 시에는 배포 플랫폼(Vercel, AWS Amplify 등)에서 환경변수를 직접 설정합니다:

**Vercel 예시:**
```
Settings → Environment Variables
- NEXT_PUBLIC_API_URL: https://api.yourdomain.com/api
```

**빌드 및 실행:**
```bash
# 프로덕션 빌드
npm run build

# 프로덕션 서버 실행
npm start
```

### 백엔드 환경변수 (Spring Boot)

#### 프로파일 기반 설정

백엔드는 Spring Profile을 사용하여 환경별 설정을 관리합니다:

**1. 개발 환경 (application-dev.yml)**
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3308/cody_wardrobe
    username: root
    password: root

server:
  port: 8080

cors:
  allowed-origins:
    - http://localhost:3000
    - http://localhost:3001
```

**2. 프로덕션 환경 (application-prod.yml)**
```yaml
spring:
  datasource:
    url: ${DATABASE_URL}
    username: ${DATABASE_USERNAME}
    password: ${DATABASE_PASSWORD}

server:
  port: ${SERVER_PORT:8080}

cors:
  allowed-origins:
    - ${FRONTEND_URL:https://yourdomain.com}
```

**3. 공통 설정 (application.yml)**
```yaml
spring:
  profiles:
    active: ${SPRING_PROFILES_ACTIVE:dev}  # 기본값: dev
  
  application:
    name: cody-wardrobe
  
  jpa:
    hibernate:
      ddl-auto: validate
    properties:
      hibernate:
        dialect: org.hibernate.dialect.MySQLDialect
  
  flyway:
    enabled: true
    baseline-on-migrate: true

jwt:
  secret: ${JWT_SECRET:default-secret-key}
  expiration: 86400000
```

#### 환경별 실행 방법

**개발 환경:**
```bash
# 방법 1: 기본값 사용 (dev 프로파일)
./gradlew bootRun

# 방법 2: 명시적 지정
./gradlew bootRun --args='--spring.profiles.active=dev'
```

**프로덕션 환경:**
```bash
# 시스템 환경변수 설정 (예시)
export DATABASE_URL="jdbc:mysql://prod-db.example.com:3306/cody_wardrobe"
export DATABASE_USERNAME="cody_user"
export DATABASE_PASSWORD="secure_password_here"
export FRONTEND_URL="https://cody.yourdomain.com"
export JWT_SECRET="your-production-jwt-secret-key"

# 프로덕션 프로파일로 실행
java -jar cody-back.jar --spring.profiles.active=prod

# 또는 환경변수로 지정
export SPRING_PROFILES_ACTIVE=prod
java -jar cody-back.jar
```

#### 환경변수 우선순위

Spring Boot는 다음 순서로 설정값을 적용합니다 (위가 더 높은 우선순위):

1. 시스템 환경변수 (`export DATABASE_URL=...`)
2. 커맨드 라인 인자 (`--spring.datasource.url=...`)
3. `application-{profile}.yml` (프로파일별 설정)
4. `application.yml` (공통 설정)

### 보안 주의사항

**절대 Git에 커밋하면 안 되는 것들:**
- 데이터베이스 비밀번호
- JWT Secret Key (프로덕션 환경)
- API 키 및 인증 토큰
- `.env.local` 파일

**Git에 포함해도 되는 것들:**
- `application.yml` (공통 설정, 민감 정보 제외)
- `application-dev.yml` (로컬 개발 설정만 있는 경우)
- `.env.production` (템플릿으로만, 실제 값 제외)
- `.env.local.example` (예시 파일)

### 환경변수 확인 방법

**프론트엔드 (브라우저 콘솔):**
```javascript
// 개발 도구 콘솔에서 확인 불가 (빌드 타임에 결정됨)
// 대신 네트워크 탭에서 API 호출 URL 확인
```

**백엔드 (로그):**
```bash
# 애플리케이션 시작 시 로그 확인
# "The following profiles are active: dev" 메시지 확인
```

---

## 📁 프로젝트 구조

### 전체 디렉토리 구조

```
cody/
├── cody-back/              # 백엔드 (Spring Boot)
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/cody/wardrobe/
│   │   │   │       ├── controller/      # API 엔드포인트
│   │   │   │       ├── domain/          # 도메인 로직
│   │   │   │       ├── common/          # 공통 유틸
│   │   │   │       ├── config/          # 설정 클래스
│   │   │   │       └── security/        # 보안 설정
│   │   │   └── resources/
│   │   │       ├── application.yml      # 설정 파일
│   │   │       └── db/migration/        # Flyway 스크립트
│   │   └── test/                        # 테스트 코드
│   ├── build.gradle                     # 빌드 설정
│   └── README.md                        # 백엔드 상세 문서
│
├── cody-front/                # 프론트엔드 (Next.js)
│   ├── app/                   # Next.js App Router
│   │   ├── page.tsx          # 홈페이지 (/)
│   │   ├── wardrobe/         # 옷장 관리
│   │   ├── outfits/          # 코디 관리
│   │   ├── recommend/        # 추천 기능
│   │   ├── calendar/         # 캘린더
│   │   └── layout.tsx        # 루트 레이아웃
│   ├── components/           # 재사용 컴포넌트
│   │   └── ui/              # shadcn/ui 컴포넌트
│   ├── lib/
│   │   ├── api.ts           # 🔥 API 클라이언트 (핵심!)
│   │   └── utils.ts         # 유틸리티 함수
│   ├── hooks/               # 커스텀 훅
│   ├── public/              # 정적 파일
│   ├── package.json         # 의존성 관리
│   └── tsconfig.json        # TypeScript 설정
│
└── README.md                 # 👈 이 문서 (통합 가이드)
```

### 백엔드 상세 구조

```
cody-back/src/main/java/com/cody/wardrobe/
│
├── CodyWardrobeApplication.java    # 메인 애플리케이션
│
├── controller/                      # 🌐 API 엔드포인트 계층
│   ├── ItemController.java         # /api/items
│   ├── OutfitController.java       # /api/outfits
│   ├── HistoryController.java      # /api/histories
│   ├── AdvancedRecommendationController.java  # /api/recommendations
│   └── dto/                        # 요청/응답 DTO
│       ├── item/
│       │   ├── ItemRequest.java
│       │   └── ItemResponse.java
│       ├── outfit/
│       │   ├── OutfitRequest.java
│       │   └── OutfitResponse.java
│       └── history/
│           ├── HistoryRequest.java
│           └── HistoryResponse.java
│
├── domain/                         # 💼 비즈니스 로직 계층
│   │
│   ├── item/                       # 아이템 도메인
│   │   ├── Item.java              # 엔티티
│   │   ├── ItemCategory.java     # Enum
│   │   ├── Season.java            # Enum
│   │   ├── service/
│   │   │   └── ItemService.java  # 비즈니스 로직
│   │   ├── repository/
│   │   │   └── ItemRepository.java  # DB 접근
│   │   └── dto/
│   │       ├── ItemDto.java
│   │       ├── ItemCreateDto.java
│   │       └── ItemUpdateDto.java
│   │
│   ├── outfit/                     # 코디 도메인
│   │   ├── Outfit.java
│   │   ├── OutfitItem.java        # 코디-아이템 연결 테이블
│   │   ├── service/
│   │   │   └── OutfitService.java
│   │   ├── repository/
│   │   │   ├── OutfitRepository.java
│   │   │   └── OutfitItemRepository.java
│   │   └── dto/
│   │       ├── OutfitDto.java
│   │       ├── OutfitCreateDto.java
│   │       └── OutfitUpdateDto.java
│   │
│   ├── history/                    # 착용 기록 도메인
│   │   ├── History.java
│   │   ├── service/
│   │   │   └── HistoryService.java
│   │   ├── repository/
│   │   │   └── HistoryRepository.java
│   │   └── dto/
│   │       ├── HistoryDto.java
│   │       └── HistoryCreateDto.java
│   │
│   └── recommendation/             # 추천 로직 도메인
│       ├── AdvancedRecommendationService.java
│       ├── filter/                # 필터링 로직
│       ├── scoring/               # 점수 계산 로직
│       └── dto/
│
├── common/                         # 공통 모듈
│   └── ApiResponse.java           # 통일된 API 응답 형식
│
├── config/                         # 설정 클래스
│   └── SecurityConfig.java        # CORS, 보안 설정
│
└── exception/                      # 예외 처리
    └── GlobalExceptionHandler.java
```

**계층별 역할:**

1. **Controller**: HTTP 요청을 받아서 Service로 전달하고 응답 반환
2. **Service**: 비즈니스 로직 수행 (여러 Repository 조합 가능)
3. **Repository**: JPA를 통한 데이터베이스 CRUD
4. **Entity**: 데이터베이스 테이블과 매핑되는 클래스
5. **DTO**: 계층 간 데이터 전송 객체

### 프론트엔드 상세 구조

```
cody-front/
│
├── app/                           # 📱 Next.js App Router (페이지)
│   │
│   ├── page.tsx                  # 홈페이지 (/)
│   ├── layout.tsx                # 루트 레이아웃
│   ├── globals.css               # 전역 스타일
│   │
│   ├── wardrobe/                 # 옷장 관리
│   │   ├── page.tsx             # 아이템 목록 (/wardrobe)
│   │   ├── add/
│   │   │   └── page.tsx         # 아이템 추가 (/wardrobe/add)
│   │   ├── [id]/
│   │   │   └── page.tsx         # 아이템 상세 (/wardrobe/123)
│   │   └── loading.tsx          # 로딩 상태
│   │
│   ├── outfits/                  # 코디 관리
│   │   ├── page.tsx             # 코디 목록 (/outfits)
│   │   ├── create/
│   │   │   └── page.tsx         # 코디 생성 (/outfits/create)
│   │   └── [id]/
│   │       └── page.tsx         # 코디 상세 (/outfits/123)
│   │
│   ├── recommend/                # 추천 기능
│   │   ├── page.tsx             # 추천 설정 (/recommend)
│   │   └── result/
│   │       └── page.tsx         # 추천 결과 (/recommend/result)
│   │
│   └── calendar/                 # 캘린더
│       └── page.tsx             # 착용 기록 캘린더 (/calendar)
│
├── components/                   # 재사용 컴포넌트
│   ├── theme-provider.tsx       # 다크모드 제공자
│   └── ui/                      # shadcn/ui 컴포넌트
│       ├── button.tsx
│       ├── card.tsx
│       ├── input.tsx
│       ├── dialog.tsx
│       └── ... (30+ 컴포넌트)
│
├── lib/                         # 유틸리티 라이브러리
│   ├── api.ts                   # 🔥 백엔드 API 클라이언트 (핵심!)
│   └── utils.ts                 # 공통 함수 (cn 등)
│
├── hooks/                       # 커스텀 React Hooks
│   ├── use-toast.ts            # 토스트 알림
│   └── use-mobile.ts           # 모바일 감지
│
├── public/                      # 정적 파일
│   └── *.png, *.jpg            # 이미지 리소스
│
└── 설정 파일들
    ├── package.json            # 의존성 관리
    ├── tsconfig.json           # TypeScript 설정
    ├── tailwind.config.ts      # TailwindCSS 설정
    └── next.config.mjs         # Next.js 설정
```

**Next.js App Router 특징:**
- 파일 시스템 기반 라우팅 (`app/` 폴더 구조가 URL이 됨)
- `page.tsx`: 해당 경로의 페이지 컴포넌트
- `layout.tsx`: 공통 레이아웃
- `[id]`: 동적 라우팅 (예: `/wardrobe/123`)

---

## 🔗 프론트엔드-백엔드 연동

### 핵심: lib/api.ts

`lib/api.ts`는 프론트엔드와 백엔드를 연결하는 **가장 중요한 파일**입니다.

#### 파일 구조

```typescript
// lib/api.ts

// 1. API 베이스 URL
const API_BASE_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080/api';

// 2. 응답 형식 타입
interface ApiResponse<T> {
  result: boolean;
  data: T;
  error?: string;
}

// 3. 도메인 타입 정의
export interface Item {
  id: number;
  category: string;
  name: string;
  imageUrl: string;
  color: string;
  season: string | null;
  createdAt: string;
  updatedAt: string;
}

export interface Outfit {
  id: number;
  name: string;
  rating: number;
  formalityLevel: number;
  lastWornDate: string | null;
  wornCount: number;
  outfitItems: OutfitItem[];
  createdAt: string;
  updatedAt: string;
}

// 4. API 클라이언트 클래스
class ApiClient {
  private baseUrl: string;

  // 공통 HTTP 요청 메서드
  private async request<T>(endpoint: string, options?: RequestInit): Promise<T> {
    // fetch 호출
    // 에러 처리
    // JSON 파싱
    // ApiResponse 언래핑
  }

  // Item API
  async getItems(): Promise<Item[]> { ... }
  async createItem(data: CreateItemRequest): Promise<Item> { ... }
  
  // Outfit API
  async getOutfits(): Promise<Outfit[]> { ... }
  async createOutfit(data: CreateOutfitRequest): Promise<Outfit> { ... }
  async recommendOutfits(params: {...}): Promise<Outfit[]> { ... }
  
  // History API
  async getHistoriesByMonth(year: number, month: number): Promise<History[]> { ... }
}

// 5. 싱글톤 인스턴스 export
export const api = new ApiClient(API_BASE_URL);
```

#### request() 메서드 - 모든 API 호출의 기반

```typescript
private async request<T>(endpoint: string, options?: RequestInit): Promise<T> {
  const url = `${this.baseUrl}${endpoint}`;

  // 1. fetch로 HTTP 요청
  const response = await fetch(url, {
    ...options,
    headers: {
      'Content-Type': 'application/json',
      ...options?.headers,
    },
  });

  // 2. HTTP 상태 코드 에러 체크
  if (!response.ok) {
    throw new Error(`API Error: ${response.status}`);
  }

  // 3. JSON 파싱
  const data: ApiResponse<T> = await response.json();

  // 4. 비즈니스 로직 에러 체크
  if (!data.result) {
    throw new Error(data.error || 'API request failed');
  }

  // 5. 실제 데이터만 반환 (data.data)
  return data.data;
}
```

**중요한 점:**
- `ApiResponse<T>`를 자동으로 언래핑하여 `T`만 반환
- 프론트엔드에서는 `api.getItems()`만 호출하면 `Item[]`를 바로 받음

#### 페이지에서 사용 예시

```typescript
// app/wardrobe/page.tsx

import { api, type Item } from "@/lib/api"

export default function WardrobePage() {
  const [items, setItems] = useState<Item[]>([])

  useEffect(() => {
    const fetchItems = async () => {
      try {
        // API 호출 - 매우 간단!
        const data = await api.getItems()
        setItems(data)
      } catch (err) {
        console.error('Failed to fetch items:', err)
      }
    }

    fetchItems()
  }, [])

  return (
    <div>
      {items.map(item => (
        <div key={item.id}>{item.name}</div>
      ))}
    </div>
  )
}
```

### API 응답 형식

백엔드의 모든 API는 통일된 형식으로 응답합니다:

#### 성공 응답

```json
{
  "result": true,
  "data": {
    "id": 1,
    "name": "흰색 셔츠",
    "category": "TOPS",
    "color": "White",
    ...
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

이 형식은 `common/ApiResponse.java`에 정의되어 있습니다.

### CORS 설정

프론트엔드(localhost:3000)에서 백엔드(localhost:8080)로 요청할 때 CORS 에러를 방지하기 위한 설정:

```java
// config/SecurityConfig.java

@Bean
public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    
    // 프론트엔드 URL 허용
    configuration.setAllowedOrigins(List.of(
        "http://localhost:3000",
        "http://localhost:3001"
    ));
    
    // HTTP 메서드 허용
    configuration.setAllowedMethods(List.of(
        "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"
    ));
    
    // 모든 헤더 허용
    configuration.setAllowedHeaders(List.of("*"));
    
    // 쿠키/인증 정보 허용
    configuration.setAllowCredentials(true);
    
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    
    return source;
}
```

---

## 🎨 기능별 구현 가이드

각 기능이 프론트엔드에서 백엔드까지 어떻게 연결되는지 상세히 설명합니다.

### 1️⃣ 옷장 관리 (Item Management)

#### 기능 개요

사용자가 보유한 옷(아이템)을 등록하고 관리하는 기능입니다.

#### 주요 기능
- 아이템 등록 (이미지, 카테고리, 이름, 색상, 시즌)
- 아이템 목록 조회 (필터링, 검색)
- 아이템 상세 조회
- 아이템 수정
- 아이템 삭제

#### 데이터 흐름: 아이템 등록

```
[사용자]
  ↓ 1. "/wardrobe/add" 페이지 접속
  ↓ 2. 이미지 업로드 + 정보 입력
  ↓ 3. "Save Item" 버튼 클릭

[프론트엔드: app/wardrobe/add/page.tsx]
  ↓ 4. handleSave() 함수 실행
  ↓ 5. 이미지를 Base64로 변환
  ↓ 6. CreateItemRequest 객체 생성
        {
          category: "TOPS",
          name: "흰색 셔츠",
          imageUrl: "data:image/png;base64,iVBORw0KG...",
          color: "White",
          season: "ALL"
        }
  ↓ 7. api.createItem(data) 호출

[lib/api.ts]
  ↓ 8. POST /api/items 요청
        Content-Type: application/json

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

[백엔드: controller/ItemController.java]
  ↓ 9. @PostMapping 메서드 진입
  ↓ 10. @Valid 검증 (필수 필드 체크)
  ↓ 11. ItemRequest → ItemCreateDto 변환

[domain/item/service/ItemService.java]
  ↓ 12. createItem(ItemCreateDto) 메서드
  ↓ 13. Item 엔티티 생성
  ↓ 14. itemRepository.save(item)

[domain/item/repository/ItemRepository.java]
  ↓ 15. JPA가 SQL INSERT 실행
        INSERT INTO items (category, name, image_url, color, season, ...)
        VALUES ('TOPS', '흰색 셔츠', 'data:image...', 'White', 'ALL', ...)

[MySQL Database]
  ↓ 16. 데이터 저장 완료 (Auto Increment ID 생성)

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

[백엔드 응답]
  ↓ 17. ItemDto 생성 (저장된 데이터)
  ↓ 18. ItemResponse 변환
  ↓ 19. ApiResponse.success(response) 반환
        {
          "result": true,
          "data": {
            "id": 1,
            "category": "TOPS",
            "name": "흰색 셔츠",
            ...
          }
        }

[프론트엔드]
  ↓ 20. 응답 받음
  ↓ 21. 성공 토스트 표시
  ↓ 22. router.push("/wardrobe") - 목록 페이지로 이동
```

#### 관련 파일

**프론트엔드:**
```
app/wardrobe/page.tsx           → 아이템 목록
app/wardrobe/add/page.tsx       → 아이템 추가
app/wardrobe/[id]/page.tsx      → 아이템 상세/수정
lib/api.ts                      → API 클라이언트
```

**백엔드:**
```
controller/ItemController.java          → API 엔드포인트
controller/dto/item/ItemRequest.java    → 요청 DTO
controller/dto/item/ItemResponse.java   → 응답 DTO
domain/item/Item.java                   → 엔티티
domain/item/service/ItemService.java    → 비즈니스 로직
domain/item/repository/ItemRepository.java → DB 접근
```

#### API 엔드포인트

```
POST   /api/items              → 아이템 생성
GET    /api/items              → 모든 아이템 조회
GET    /api/items/{id}         → 아이템 단건 조회
PUT    /api/items/{id}         → 아이템 수정
DELETE /api/items/{id}         → 아이템 삭제
GET    /api/items/category/{category}  → 카테고리별 조회
GET    /api/items/season/{season}      → 시즌별 조회
GET    /api/items/color/{color}        → 색상별 조회
```

#### 핵심 코드

**프론트엔드 - 아이템 목록 조회:**
```typescript
// app/wardrobe/page.tsx

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
```

**백엔드 - 아이템 생성:**
```java
// controller/ItemController.java

@PostMapping
public ResponseEntity<ApiResponse<ItemResponse>> createItem(
    @Valid @RequestBody ItemRequest request
) {
    ItemCreateDto createDto = ItemCreateDto.from(request);
    ItemDto itemDto = itemService.createItem(createDto);
    ItemResponse response = ItemResponse.from(itemDto);
    
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(ApiResponse.success(response));
}
```

---

### 2️⃣ 코디 관리 (Outfit Management)

#### 기능 개요

여러 아이템을 조합하여 코디를 만들고 관리하는 기능입니다.

#### 주요 기능
- 코디 생성 (아이템 선택, 평점 1-5, 격식도 1-5)
- 코디 목록 조회
- 코디 상세 조회
- 코디 수정
- 코디 삭제

#### 격식도 시스템

```
1 - Home         집에서 편하게 (트레이닝복, 편한 옷)
2 - Neighborhood 동네 외출 (청바지 + 티셔츠)
3 - Outing       일반 외출 (스마트 캐주얼)
4 - Work         업무/회의 (정장, 블라우스)
5 - Formal       공식 행사 (수트, 드레스)
```

#### 데이터 흐름: 코디 생성

```
[사용자]
  ↓ 1. "/outfits/create" 페이지 접속

[프론트엔드]
  ↓ 2. useEffect로 아이템 목록 조회
        await api.getItems()
  ↓ 3. 아이템 카드 렌더링

[사용자]
  ↓ 4. 아이템 3개 선택 (상의, 하의, 신발)
  ↓ 5. 코디 이름 입력: "출근룩"
  ↓ 6. 평점 선택: 5점
  ↓ 7. 격식도 선택: 4 (Work)
  ↓ 8. "Save Outfit" 버튼 클릭

[프론트엔드: app/outfits/create/page.tsx]
  ↓ 9. handleSave() 함수
  ↓ 10. CreateOutfitRequest 생성
         {
           name: "출근룩",
           rating: 5,
           formalityLevel: 4,
           itemIds: [1, 2, 3]
         }
  ↓ 11. api.createOutfit(data)

[백엔드: controller/OutfitController.java]
  ↓ 12. @PostMapping 진입
  ↓ 13. OutfitRequest → OutfitCreateDto

[domain/outfit/service/OutfitService.java]
  ↓ 14. createOutfit() 메서드
  ↓ 15. Outfit 엔티티 생성
         - name: "출근룩"
         - rating: 5
         - formalityLevel: 4
         - wornCount: 0
  
  ↓ 16. 각 itemId로 Item 엔티티 조회
         Item item1 = itemRepository.findById(1)
         Item item2 = itemRepository.findById(2)
         Item item3 = itemRepository.findById(3)
  
  ↓ 17. OutfitItem 생성 (Outfit ↔ Item 연결)
         outfit.addItem(item1)  // OutfitItem 생성
         outfit.addItem(item2)
         outfit.addItem(item3)
  
  ↓ 18. outfitRepository.save(outfit)

[Database]
  ↓ 19. INSERT INTO outfits (...)
  ↓ 20. INSERT INTO outfit_items (outfit_id, item_id) × 3

[응답]
  ↓ 21. OutfitDto 반환
         {
           id: 10,
           name: "출근룩",
           rating: 5,
           formalityLevel: 4,
           outfitItems: [
             { id: ..., item: { id: 1, name: "흰색 셔츠", ... } },
             { id: ..., item: { id: 2, name: "검정 바지", ... } },
             { id: ..., item: { id: 3, name: "구두", ... } }
           ]
         }

[프론트엔드]
  ↓ 22. 성공 토스트
  ↓ 23. router.push("/outfits")
```

#### 관련 파일

**프론트엔드:**
```
app/outfits/page.tsx            → 코디 목록
app/outfits/create/page.tsx     → 코디 생성
app/outfits/[id]/page.tsx       → 코디 상세/수정
```

**백엔드:**
```
controller/OutfitController.java
domain/outfit/Outfit.java           → 코디 엔티티
domain/outfit/OutfitItem.java       → 코디-아이템 연결 엔티티
domain/outfit/service/OutfitService.java
domain/outfit/repository/OutfitRepository.java
```

#### API 엔드포인트

```
POST   /api/outfits            → 코디 생성
GET    /api/outfits            → 모든 코디 조회
GET    /api/outfits/{id}       → 코디 단건 조회
PUT    /api/outfits/{id}       → 코디 수정
DELETE /api/outfits/{id}       → 코디 삭제
POST   /api/outfits/{id}/worn?date=YYYY-MM-DD  → 착용 기록
GET    /api/outfits/recommend  → 코디 추천 (다음 섹션)
```

---

### 3️⃣ 코디 추천 (Recommendation)

#### 기능 개요

사용자가 설정한 조건에 맞는 코디를 추천하는 핵심 기능입니다.

#### 추천 조건

```typescript
{
  minRating: number,         // 최소 평점 (1-5)
  minFormality: number,      // 최소 격식도 (1-5)
  excludeRecent: boolean,    // 최근 착용한 코디 제외 여부
  excludeDays: number        // 며칠 이내 착용 제외 (기본 2일)
}
```

#### 추천 알고리즘

```
1단계: 필터링
  ├─ outfit.rating >= minRating
  ├─ outfit.formalityLevel >= minFormality
  └─ excludeRecent == true 이면
      └─ lastWornDate IS NULL OR lastWornDate < (오늘 - excludeDays)

2단계: 정렬
  ├─ lastWornDate가 NULL인 것 우선 (한번도 안 입은 옷)
  └─ lastWornDate 오래된 순

3단계: 반환
  └─ 조건에 맞는 모든 코디 리스트
```

#### 데이터 흐름: 코디 추천

```
[사용자]
  ↓ 1. "/recommend" 페이지 접속

[프론트엔드: app/recommend/page.tsx]
  ↓ 2. 조건 설정 UI 렌더링
        - 슬라이더: 최소 평점 (기본값 3)
        - 슬라이더: 최소 격식도 (기본값 3)
        - 스위치: 최근 착용 제외 (기본값 ON)

[사용자]
  ↓ 3. 조건 설정
        minRating: 3
        minFormality: 3
        excludeRecent: true
  ↓ 4. "Get Recommendation" 버튼 클릭

[프론트엔드]
  ↓ 5. router.push with query params
        /recommend/result?minRating=3&minFormality=3&excludeRecent=true

[프론트엔드: app/recommend/result/page.tsx]
  ↓ 6. useSearchParams()로 파라미터 읽기
  ↓ 7. useEffect에서 API 호출
        api.recommendOutfits({
          minRating: 3,
          minFormality: 3,
          excludeRecent: true
        })

[lib/api.ts]
  ↓ 8. Query Parameter 생성
        /api/outfits/recommend?minRating=3&minFormality=3&excludeRecent=true&excludeDays=2

[백엔드: controller/OutfitController.java]
  ↓ 9. @GetMapping("/recommend") 진입
  ↓ 10. excludeRecent 확인
         if (excludeRecent) {
           outfitService.recommendOutfitsExcludingRecent(...)
         } else {
           outfitService.recommendOutfits(...)
         }

[domain/outfit/service/OutfitService.java]
  ↓ 11. recommendOutfitsExcludingRecent() 메서드
  ↓ 12. cutoffDate 계산
         LocalDate cutoffDate = LocalDate.now().minusDays(2);
         // 오늘이 2025-11-11이면 cutoffDate = 2025-11-09
  
  ↓ 13. outfitRepository.findRecommendedOutfitsExcludingRecent(
           minRating,
           minFormality,
           cutoffDate
        )

[domain/outfit/repository/OutfitRepository.java]
  ↓ 14. @Query JPQL 실행
         SELECT o FROM Outfit o
         WHERE o.rating >= :minRating
           AND o.formalityLevel >= :minFormality
           AND (o.lastWornDate IS NULL OR o.lastWornDate < :cutoffDate)
         ORDER BY o.lastWornDate ASC NULLS FIRST

[Database]
  ↓ 15. SQL 실행
         SELECT * FROM outfits
         WHERE rating >= 3
           AND formality_level >= 3
           AND (last_worn_date IS NULL OR last_worn_date < '2025-11-09')
         ORDER BY last_worn_date ASC NULLS FIRST

[응답]
  ↓ 16. 추천 결과 반환
         [
           { id: 5, name: "주말 브런치", rating: 4, formalityLevel: 3, lastWornDate: null, ... },
           { id: 3, name: "스마트 캐주얼", rating: 5, formalityLevel: 3, lastWornDate: "2025-11-01", ... },
           { id: 7, name: "편한 외출복", rating: 3, formalityLevel: 3, lastWornDate: "2025-11-05", ... }
         ]

[프론트엔드]
  ↓ 17. setRecommendedOutfits(outfits)
  ↓ 18. 첫 번째 코디 화면에 표시
         - 이름: "주말 브런치"
         - 아이템들 이미지 렌더링
         - 평점, 격식도 표시
  
  ↓ 19. "Recommend Again" 버튼
         → currentIndex++하여 다음 코디로 순환
  
  ↓ 20. "Select This Outfit" 버튼
         → api.recordOutfitWorn(outfitId, today)
         → /calendar 페이지로 이동
```

#### 관련 파일

**프론트엔드:**
```
app/recommend/page.tsx          → 추천 조건 설정
app/recommend/result/page.tsx   → 추천 결과 표시
```

**백엔드:**
```
controller/OutfitController.java
  └─ recommendOutfits() 메서드

domain/outfit/service/OutfitService.java
  ├─ recommendOutfits()
  └─ recommendOutfitsExcludingRecent()

domain/outfit/repository/OutfitRepository.java
  ├─ findRecommendedOutfits() - JPQL 쿼리
  └─ findRecommendedOutfitsExcludingRecent() - JPQL 쿼리
```

#### 핵심 코드

**백엔드 - Repository 쿼리:**
```java
// domain/outfit/repository/OutfitRepository.java

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
```

**프론트엔드 - 추천 결과 표시:**
```typescript
// app/recommend/result/page.tsx

const [recommendedOutfits, setRecommendedOutfits] = useState<Outfit[]>([])
const [currentIndex, setCurrentIndex] = useState(0)

useEffect(() => {
  const fetchRecommendations = async () => {
    const outfits = await api.recommendOutfits({
      minRating: Number(minRating),
      minFormality: Number(minFormality),
      excludeRecent,
    })
    
    if (outfits.length === 0) {
      toast({ title: "조건에 맞는 코디가 없습니다" })
      return
    }
    
    setRecommendedOutfits(outfits)
  }
  
  fetchRecommendations()
}, [minRating, minFormality, excludeRecent])

const handleRecommendAgain = () => {
  const nextIndex = (currentIndex + 1) % recommendedOutfits.length
  setCurrentIndex(nextIndex)
}
```

---

### 4️⃣ 착용 기록 (History)

#### 기능 개요

어떤 코디를 언제 입었는지 기록하고 캘린더로 확인하는 기능입니다.

#### 주요 기능
- 코디 착용 기록 ("Wear This Outfit Today" 버튼)
- 월별 착용 이력 조회
- 캘린더 뷰로 시각화
- 월간 통계 (착용 일수, 고유 코디 수)

#### 데이터 흐름: 착용 기록

```
[사용자]
  ↓ 1. 코디 상세 페이지 (/outfits/123)
  ↓ 2. "Wear This Outfit Today" 버튼 클릭

[프론트엔드: app/outfits/[id]/page.tsx]
  ↓ 3. handleWearToday() 함수
  ↓ 4. 오늘 날짜 계산
        const today = new Date().toISOString().split('T')[0]
        // "2025-11-11"
  ↓ 5. api.recordOutfitWorn(outfitId, today)

[lib/api.ts]
  ↓ 6. POST /api/outfits/123/worn?date=2025-11-11

[백엔드: controller/OutfitController.java]
  ↓ 7. @PostMapping("/{id}/worn") 진입
  ↓ 8. @RequestParam으로 date 받음
  ↓ 9. outfitService.recordWorn(id, date)

[domain/outfit/service/OutfitService.java]
  ↓ 10. recordWorn() 메서드
  ↓ 11. Outfit 엔티티 조회
         Outfit outfit = outfitRepository.findById(id)
  
  ↓ 12. Outfit 엔티티 업데이트
         outfit.recordWorn(wornDate)
         // 내부에서:
         //   this.lastWornDate = wornDate
         //   this.wornCount++
  
  ↓ 13. History 엔티티 생성
         History history = new History();
         history.setOutfit(outfit);
         history.setWornDate(wornDate);
  
  ↓ 14. historyRepository.save(history)

[Database]
  ↓ 15. UPDATE outfits
         SET last_worn_date = '2025-11-11',
             worn_count = worn_count + 1
         WHERE id = 123
  
  ↓ 16. INSERT INTO histories (outfit_id, worn_date, created_at)
         VALUES (123, '2025-11-11', NOW())

[응답]
  ↓ 17. 성공 응답

[프론트엔드]
  ↓ 18. 성공 토스트
  ↓ 19. 코디 정보 다시 조회 (업데이트된 wornCount 반영)
  ↓ 20. UI 업데이트
```

#### 캘린더 뷰

```
[사용자]
  ↓ 1. "/calendar" 페이지 접속

[프론트엔드: app/calendar/page.tsx]
  ↓ 2. 현재 년/월 계산
  ↓ 3. api.getHistoriesByMonth(year, month)

[백엔드: controller/HistoryController.java]
  ↓ 4. GET /api/histories/month?year=2025&month=11
  ↓ 5. historyService.getHistoriesByMonth(2025, 11)

[domain/history/service/HistoryService.java]
  ↓ 6. LocalDate 범위 계산
        startDate = 2025-11-01
        endDate = 2025-11-30
  ↓ 7. historyRepository.findByWornDateBetween(startDate, endDate)

[Database]
  ↓ 8. SELECT * FROM histories
        WHERE worn_date BETWEEN '2025-11-01' AND '2025-11-30'
        ORDER BY worn_date DESC

[응답]
  ↓ 9. History 리스트 반환 (Outfit 정보 포함)
        [
          { id: 10, outfit: {...}, wornDate: "2025-11-11" },
          { id: 9, outfit: {...}, wornDate: "2025-11-08" },
          ...
        ]

[프론트엔드]
  ↓ 10. 캘린더에 데이터 매핑
         - 날짜별로 그룹핑
         - 해당 날짜에 착용한 코디 표시
  ↓ 11. 통계 계산
         - 총 착용 일수
         - 고유 코디 수
```

#### 관련 파일

**프론트엔드:**
```
app/outfits/[id]/page.tsx       → "Wear This Outfit Today" 버튼
app/calendar/page.tsx           → 캘린더 뷰
```

**백엔드:**
```
controller/OutfitController.java
  └─ recordWorn() 메서드

controller/HistoryController.java
  └─ getHistoriesByMonth() 메서드

domain/outfit/Outfit.java
  └─ recordWorn() 메서드 (엔티티 자체 업데이트)

domain/history/History.java
domain/history/service/HistoryService.java
domain/history/repository/HistoryRepository.java
```

#### API 엔드포인트

```
POST   /api/outfits/{id}/worn?date=YYYY-MM-DD  → 착용 기록
GET    /api/histories                          → 모든 히스토리
GET    /api/histories/{id}                     → 히스토리 단건
GET    /api/histories/month?year=2025&month=11 → 월별 조회
GET    /api/histories/outfit/{outfitId}        → 코디별 히스토리
DELETE /api/histories/{id}                     → 히스토리 삭제
```

---

## 💾 데이터베이스 스키마

### Flyway 마이그레이션

데이터베이스 스키마는 Flyway를 통해 버전 관리됩니다.

**마이그레이션 파일 위치:**
```
cody-back/src/main/resources/db/migration/
├── V1__create_items_table.sql
├── V2__create_outfits_table.sql
├── V3__create_outfit_items_table.sql
└── V4__create_history_table.sql
```

### 테이블 구조

#### 1. items (아이템)

```sql
CREATE TABLE items (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    category VARCHAR(20) NOT NULL,        -- TOPS, BOTTOMS, SHOES, OUTERWEAR, ACCESSORIES
    name VARCHAR(200),
    image_url VARCHAR(500) NOT NULL,      -- Base64 이미지 또는 URL
    color VARCHAR(50),                     -- White, Black, Blue 등
    season VARCHAR(20),                    -- ALL, SPRING, SUMMER, FALL, WINTER
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_category (category),
    INDEX idx_season (season)
);
```

#### 2. outfits (코디)

```sql
CREATE TABLE outfits (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(200),
    rating INTEGER NOT NULL CHECK (rating BETWEEN 1 AND 5),
    formality_level INTEGER NOT NULL CHECK (formality_level BETWEEN 1 AND 5),
    memo TEXT,
    worn_count INTEGER NOT NULL DEFAULT 0,
    last_worn_date DATE,                  -- 마지막 착용 날짜
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_rating (rating),
    INDEX idx_formality (formality_level),
    INDEX idx_last_worn (last_worn_date)
);
```

#### 3. outfit_items (코디-아이템 연결)

```sql
CREATE TABLE outfit_items (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    outfit_id BIGINT NOT NULL,
    item_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (outfit_id) REFERENCES outfits(id) ON DELETE CASCADE,
    FOREIGN KEY (item_id) REFERENCES items(id) ON DELETE CASCADE,
    UNIQUE KEY uk_outfit_item (outfit_id, item_id)
);
```

#### 4. histories (착용 기록)

```sql
CREATE TABLE histories (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    outfit_id BIGINT NOT NULL,
    worn_date DATE NOT NULL,              -- 착용 날짜
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (outfit_id) REFERENCES outfits(id) ON DELETE CASCADE,
    INDEX idx_worn_date (worn_date),
    INDEX idx_outfit_id (outfit_id)
);
```

### 엔티티 관계도

```
┌─────────────┐
│    items    │
│             │
│ id (PK)     │
│ category    │
│ name        │
│ image_url   │
│ color       │
│ season      │
└─────────────┘
       △
       │
       │ Many-to-Many
       │
       ├────────────────┐
       │                │
┌──────┴──────┐  ┌──────┴──────┐
│outfit_items │  │   outfits   │
│             │  │             │
│ id (PK)     │  │ id (PK)     │
│ outfit_id(FK)  │ name        │
│ item_id (FK)│  │ rating      │
└─────────────┘  │ formality   │
                 │ worn_count  │
                 │ last_worn   │
                 └─────────────┘
                        △
                        │
                        │ One-to-Many
                        │
                 ┌──────┴──────┐
                 │  histories  │
                 │             │
                 │ id (PK)     │
                 │ outfit_id(FK)
                 │ worn_date   │
                 └─────────────┘
```

**관계 설명:**
- `items` ↔ `outfits`: Many-to-Many (중간 테이블: `outfit_items`)
- `outfits` ↔ `histories`: One-to-Many (한 코디는 여러 번 착용 가능)

---

## 🛠 개발 도구 및 팁

### Swagger UI (API 테스트)

백엔드 API를 브라우저에서 직접 테스트할 수 있습니다:

```
http://localhost:8080/swagger-ui.html
```

**사용법:**
1. API 엔드포인트 클릭
2. "Try it out" 버튼
3. 파라미터 입력
4. "Execute" 실행
5. 응답 확인

### MySQL 접속

```bash
# 터미널에서 접속
mysql -u root -p -h localhost -P 3308

# 데이터베이스 선택
USE cody_wardrobe;

# 테이블 확인
SHOW TABLES;

# 데이터 조회
SELECT * FROM items;
SELECT * FROM outfits;
SELECT * FROM histories;
```

### 브라우저 개발자 도구

**Network 탭:**
- API 요청/응답 확인
- HTTP 상태 코드 확인
- 요청 헤더, 본문 확인

**Console 탭:**
- 에러 메시지 확인
- `console.log()` 출력 확인

**React DevTools:**
- 컴포넌트 상태 확인
- Props 확인

### 디버깅 팁

#### 백엔드 디버깅

```java
// 1. 로그 출력
System.out.println("Debug: " + variable);

// 2. 로거 사용 (권장)
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ItemService {
    public void someMethod() {
        log.debug("Debug message: {}", value);
        log.info("Info message");
        log.error("Error occurred", exception);
    }
}

// 3. IntelliJ 디버거
// 좌측 라인 번호 클릭 → 브레이크포인트 설정
// Debug 모드로 실행
```

#### 프론트엔드 디버깅

```typescript
// 1. console.log
console.log("items:", items)

// 2. 객체 전체 출력
console.table(items)

// 3. 조건부 로그
if (items.length === 0) {
  console.warn("No items found!")
}

// 4. 에러 처리
try {
  await api.getItems()
} catch (err) {
  console.error("Error:", err)
}

// 5. 브라우저 디버거
debugger; // 이 줄에서 실행 멈춤
```

### 유용한 단축키

**IntelliJ IDEA (백엔드):**
```
Cmd + B         → 정의로 이동
Cmd + Alt + B   → 구현체로 이동
Cmd + Shift + F → 전체 검색
Cmd + /         → 주석 토글
```

**VS Code (프론트엔드):**
```
Cmd + P         → 파일 검색
Cmd + Shift + F → 전체 검색
Cmd + /         → 주석 토글
Cmd + D         → 같은 단어 선택
```

---

## 🔧 트러블슈팅

### ❌ 문제 1: CORS 에러

```
Access to fetch at 'http://localhost:8080/api/items' from origin
'http://localhost:3000' has been blocked by CORS policy
```

**원인:** 백엔드에서 프론트엔드 도메인을 허용하지 않음

**해결 방법:**

1. 백엔드 CORS 설정 확인:
```java
// config/SecurityConfig.java
configuration.setAllowedOrigins(List.of(
    "http://localhost:3000",  // 프론트엔드 URL 확인
    "http://localhost:3001"
));
```

2. 백엔드 재시작:
```bash
cd cody-back
./gradlew bootRun
```

---

### ❌ 문제 2: API 연결 실패

```
Error: API Error: 404
또는
Failed to fetch
```

**체크리스트:**

1. **백엔드 서버 확인:**
```bash
# 백엔드가 8080 포트에서 실행 중인지 확인
lsof -i :8080

# 또는 curl로 테스트
curl http://localhost:8080/api/items
```

2. **환경 변수 확인:**
```bash
# cody-front/.env.local
cat .env.local

# 내용이 정확한지 확인:
NEXT_PUBLIC_API_URL=http://localhost:8080/api
```

3. **프론트엔드 재시작:**
```bash
# .env.local 변경 후 반드시 재시작
cd cody-front
npm run dev
```

4. **MySQL 연결 확인:**
```bash
# MySQL이 3308 포트에서 실행 중인지
lsof -i :3308

# 백엔드 로그 확인
# "HikariPool" 관련 에러가 있다면 DB 연결 문제
```

---

### ❌ 문제 3: 데이터가 화면에 안 보임

**증상:** API 호출은 성공하는데 화면에 데이터가 안 뜸

**해결 방법:**

1. **브라우저 Console 확인:**
```javascript
// F12 → Console 탭
// 에러 메시지 확인
```

2. **Network 탭 확인:**
```
F12 → Network 탭
→ API 요청 클릭
→ Response 탭에서 실제 응답 확인
```

3. **데이터베이스 확인:**
```sql
-- 실제로 데이터가 있는지 확인
SELECT * FROM items;
SELECT * FROM outfits;
```

4. **API 응답 구조 확인:**
```typescript
// 올바른 접근
const data = await api.getItems()  // 이미 data.data 처리됨
setItems(data)

// 틀린 접근 (흔한 실수)
const response = await fetch('/api/items')
const json = await response.json()
const data = json.data.data  // ❌ 이중 접근
```

---

### ❌ 문제 4: 이미지가 안 보임

**원인:** Base64 문자열이 잘못되었거나 너무 큼

**해결 방법:**

1. **Base64 문자열 확인:**
```typescript
// 이미지 업로드 시 확인
console.log("imageUrl length:", imageUrl.length)
console.log("imageUrl start:", imageUrl.substring(0, 50))
// "data:image/png;base64,iVBORw0KGgoAAAANSUhEUg..." 이런 형식이어야 함
```

2. **이미지 크기 제한:**
```typescript
// 이미지 크기 줄이기 (옵션)
const MAX_WIDTH = 800
const MAX_HEIGHT = 800
// canvas로 리사이즈 로직 구현
```

3. **데이터베이스 확인:**
```sql
-- image_url이 제대로 저장되었는지
SELECT id, name, LEFT(image_url, 50) FROM items;
```

---

### ❌ 문제 5: 추천이 안 나옴

```
No outfits found
```

**원인:** 조건에 맞는 코디가 없음

**체크리스트:**

1. **DB에 코디가 있는지 확인:**
```sql
SELECT * FROM outfits;
```

2. **코디의 평점/격식도 확인:**
```sql
SELECT id, name, rating, formality_level, last_worn_date 
FROM outfits;
```

예를 들어:
- 모든 코디가 `rating=2`인데 `minRating=3`으로 검색하면 결과 없음
- 모든 코디가 `formality_level=1`인데 `minFormality=3`으로 검색하면 결과 없음

3. **최근 착용 제외 확인:**
```sql
-- 모든 코디를 오늘 입었다면?
SELECT id, name, last_worn_date FROM outfits;

-- 만약 모두 last_worn_date = '2025-11-11' (오늘) 이고
-- excludeRecent = true, excludeDays = 2 이면
-- 결과 없음 (2일 전인 2025-11-09보다 나중이므로)
```

4. **조건 완화해서 테스트:**
```
minRating: 1
minFormality: 1
excludeRecent: false
```

---

### ❌ 문제 6: 포트 충돌

```
Port 8080 is already in use
또는
Port 3000 is already in use
```

**해결 방법:**

```bash
# 1. 해당 포트 사용 중인 프로세스 찾기
lsof -i :8080
lsof -i :3000

# 2. 프로세스 종료
kill -9 <PID>

# 또는 다른 포트 사용
# 백엔드: application.yml의 server.port 변경
# 프론트엔드: npm run dev -- -p 3001
```

---

## 📖 코드 읽는 법

신입 개발자가 코드를 효율적으로 읽는 방법을 안내합니다.

### 1단계: 사용자 액션 찾기

화면에서 어떤 동작이 일어나는지 확인합니다.

```
예: "Save Item" 버튼 클릭
```

### 2단계: 이벤트 핸들러 찾기

해당 버튼의 `onClick` 핸들러를 찾습니다.

```typescript
// app/wardrobe/add/page.tsx

<Button onClick={handleSave}>
  Save Item
</Button>
```

### 3단계: 핸들러 함수 추적

```typescript
const handleSave = async () => {
  try {
    // 1. 데이터 검증
    if (!formData.category || !imagePreview) {
      toast({ title: "필수 항목을 입력하세요" })
      return
    }

    // 2. API 호출  👈 여기!
    const newItem = await api.createItem({
      category: formData.category,
      name: formData.name,
      imageUrl: imagePreview,
      color: formData.color,
      season: formData.season,
    })

    // 3. 성공 처리
    toast({ title: "아이템이 저장되었습니다" })
    router.push("/wardrobe")
  } catch (error) {
    toast({ title: "저장 실패", variant: "destructive" })
  }
}
```

### 4단계: API 클라이언트 확인

```typescript
// lib/api.ts

async createItem(data: CreateItemRequest): Promise<Item> {
  return this.request<Item>('/items', {
    method: 'POST',
    body: JSON.stringify(data),
  })
}
```

엔드포인트 확인: `POST /api/items`

### 5단계: 백엔드 Controller 찾기

```java
// controller/ItemController.java

@PostMapping  // 👈 POST /api/items
public ResponseEntity<ApiResponse<ItemResponse>> createItem(
    @Valid @RequestBody ItemRequest request
) {
    ItemCreateDto createDto = ItemCreateDto.from(request);
    ItemDto itemDto = itemService.createItem(createDto);  // 👈 Service 호출
    ItemResponse response = ItemResponse.from(itemDto);
    return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response));
}
```

### 6단계: Service 로직 확인

```java
// domain/item/service/ItemService.java

@Transactional
public ItemDto createItem(ItemCreateDto createDto) {
    // 1. DTO → Entity 변환
    Item item = Item.builder()
        .category(createDto.getCategory())
        .name(createDto.getName())
        .imageUrl(createDto.getImageUrl())
        .color(createDto.getColor())
        .season(createDto.getSeason())
        .build();
    
    // 2. Repository 저장  👈 DB 접근
    Item savedItem = itemRepository.save(item);
    
    // 3. Entity → DTO 변환
    return ItemDto.from(savedItem);
}
```

### 7단계: Repository 확인

```java
// domain/item/repository/ItemRepository.java

public interface ItemRepository extends JpaRepository<Item, Long> {
    // JpaRepository가 기본 CRUD 제공
    // save(), findById(), findAll(), delete() 등
}
```

### 요약: 전체 흐름

```
사용자 클릭
  ↓
이벤트 핸들러 (onClick)
  ↓
API 호출 (api.createItem)
  ↓
lib/api.ts (fetch)
  ↓
━━━━━━━━━━━━━━━━━━━━━━
백엔드 Controller
  ↓
Service (비즈니스 로직)
  ↓
Repository (DB 접근)
  ↓
Database (MySQL)
```

### 팁: 빠르게 찾는 법

1. **프론트엔드에서 API 호출 검색:**
```bash
cd cody-front
grep -r "api.createItem" .
```

2. **백엔드에서 엔드포인트 검색:**
```bash
cd cody-back
grep -r "@PostMapping" .
```

3. **특정 클래스 찾기:**
```bash
find . -name "ItemService.java"
```

---

## 📱 모바일 앱으로 변환하기 (PWA)

Cody 웹 애플리케이션을 Progressive Web App(PWA)로 변환하면 사용자가 앱스토어 없이 홈 화면에 추가하여 네이티브 앱처럼 사용할 수 있습니다.

### PWA란?

Progressive Web App은 웹 기술로 만들어진 애플리케이션이지만 다음과 같은 네이티브 앱의 기능을 제공합니다:

- 📱 홈 화면에 추가 가능
- 🖥️ 전체 화면 모드 (브라우저 UI 없음)
- 📴 오프라인 지원 (Service Worker)
- 🔔 푸시 알림 (선택적)
- ⚡ 빠른 로딩 속도

### PWA 구현 방법

#### 1. Manifest 파일 생성

`cody-front/public/manifest.json` 파일을 생성합니다:

```json
{
  "name": "오늘뭐입지? - Cody",
  "short_name": "Cody",
  "description": "스마트 옷장 관리 서비스",
  "start_url": "/",
  "display": "standalone",
  "background_color": "#ffffff",
  "theme_color": "#000000",
  "orientation": "portrait",
  "icons": [
    {
      "src": "/icon-192x192.png",
      "sizes": "192x192",
      "type": "image/png",
      "purpose": "any maskable"
    },
    {
      "src": "/icon-512x512.png",
      "sizes": "512x512",
      "type": "image/png",
      "purpose": "any maskable"
    }
  ]
}
```

#### 2. Next.js 메타데이터 설정

`cody-front/app/layout.tsx`에 메타데이터를 추가합니다:

```typescript
export const metadata: Metadata = {
  title: "오늘뭐입지? - What to Wear Today",
  description: "Smart outfit recommendations for your daily wardrobe",
  manifest: "/manifest.json",
  appleWebApp: {
    capable: true,
    statusBarStyle: "default",
    title: "Cody",
  },
  // ... 기존 설정
}
```

#### 3. Service Worker 설정 (오프라인 지원)

Next.js에서 Service Worker를 사용하려면 `next-pwa` 패키지를 설치합니다:

```bash
cd cody-front
npm install next-pwa
```

`next.config.js` 수정:

```javascript
const withPWA = require('next-pwa')({
  dest: 'public',
  register: true,
  skipWaiting: true,
  disable: process.env.NODE_ENV === 'development',
})

module.exports = withPWA({
  // 기존 Next.js 설정
})
```

#### 4. 아이콘 생성

PWA에 필요한 아이콘들을 `public/` 폴더에 추가합니다:

- `icon-192x192.png`: 192x192 픽셀
- `icon-512x512.png`: 512x512 픽셀
- `apple-touch-icon.png`: 180x180 픽셀 (iOS용)

**온라인 도구로 자동 생성:**
- [PWA Icon Generator](https://www.pwabuilder.com/imageGenerator)
- 기존 로고를 업로드하면 모든 크기의 아이콘을 자동 생성

#### 5. 오프라인 기능 구현 (선택사항)

캐시된 데이터로 오프라인에서도 코디 조회를 지원하려면:

**방법 1: Service Worker에서 API 응답 캐싱**

```javascript
// public/sw.js (Service Worker)
self.addEventListener('fetch', (event) => {
  if (event.request.url.includes('/api/outfits')) {
    event.respondWith(
      caches.match(event.request).then((response) => {
        return response || fetch(event.request).then((fetchResponse) => {
          return caches.open('api-cache').then((cache) => {
            cache.put(event.request, fetchResponse.clone())
            return fetchResponse
          })
        })
      })
    )
  }
})
```

**방법 2: IndexedDB로 로컬 저장**

```typescript
// lib/offline-storage.ts
import { openDB } from 'idb'

const DB_NAME = 'cody-offline-db'
const STORE_NAME = 'outfits'

export async function saveOutfitsOffline(outfits: Outfit[]) {
  const db = await openDB(DB_NAME, 1, {
    upgrade(db) {
      db.createObjectStore(STORE_NAME, { keyPath: 'id' })
    },
  })
  
  const tx = db.transaction(STORE_NAME, 'readwrite')
  for (const outfit of outfits) {
    await tx.store.put(outfit)
  }
  await tx.done
}

export async function getOfflineOutfits(): Promise<Outfit[]> {
  const db = await openDB(DB_NAME, 1)
  return await db.getAll(STORE_NAME)
}
```

### 사용자 설치 방법

#### iOS (Safari)
1. Safari로 웹사이트 접속
2. 공유 버튼 탭 (하단 중앙)
3. "홈 화면에 추가" 선택
4. 이름 확인 후 "추가" 탭

#### Android (Chrome)
1. Chrome으로 웹사이트 접속
2. 우측 상단 메뉴 (⋮) 탭
3. "홈 화면에 추가" 선택
4. 이름 확인 후 "추가" 탭

#### Desktop (Chrome)
1. Chrome으로 웹사이트 접속
2. 주소창 우측의 설치 아이콘 클릭
3. "설치" 버튼 클릭

### 배포 및 테스트

#### 로컬 테스트

```bash
# 프로덕션 빌드 생성
cd cody-front
npm run build

# 프로덕션 서버 실행
npm start

# 브라우저에서 접속
# Chrome DevTools → Application → Manifest 확인
# Chrome DevTools → Application → Service Workers 확인
```

#### HTTPS 필수

PWA는 보안상의 이유로 HTTPS 환경에서만 작동합니다:
- **로컬 개발**: `localhost`는 예외로 HTTP 허용
- **프로덕션**: 반드시 HTTPS 도메인 필요

#### 배포 플랫폼

**Vercel (추천):**
```bash
npm install -g vercel
vercel login
vercel --prod
```
- 자동 HTTPS 지원
- 환경변수 설정 UI 제공

**Netlify:**
```bash
npm install -g netlify-cli
netlify login
netlify deploy --prod
```

### PWA vs 네이티브 앱

| 특징 | PWA | 네이티브 앱 |
|------|-----|------------|
| 배포 | URL만 공유 | 앱스토어 심사 필요 |
| 설치 | 브라우저에서 즉시 | 스토어에서 다운로드 |
| 업데이트 | 자동 (웹페이지처럼) | 사용자가 직접 업데이트 |
| 크기 | 가볍고 빠름 | 상대적으로 무거움 |
| 접근성 | 뛰어남 | 스토어 검색 의존 |
| 네이티브 기능 | 제한적 | 완전 지원 |
| 개발 비용 | 낮음 (웹 기술) | 높음 (플랫폼별 개발) |

### MVP에서 PWA를 선택하는 이유

1. **빠른 시장 진입**: 앱스토어 심사 없이 즉시 배포
2. **낮은 진입 장벽**: URL만 공유하면 누구나 사용 가능
3. **유지보수 간편**: 한 번 업데이트하면 모든 사용자에게 적용
4. **크로스 플랫폼**: iOS, Android, Desktop 모두 지원
5. **비용 절감**: 별도의 네이티브 앱 개발 불필요

### 향후 계획

PWA로 시작한 후, 사용자 피드백을 받아 다음 단계를 결정할 수 있습니다:

1. **PWA 유지**: 대부분의 기능이 충분하다면 계속 PWA 사용
2. **하이브리드 앱**: React Native나 Flutter로 전환
3. **네이티브 앱**: 필요한 경우 iOS/Android 각각 개발

---

## 📚 추가 참고 자료

### 프로젝트 문서

- `cody-back/README.md` - 백엔드 상세 문서 (기존)
- `cody-back/PRD.md` - 제품 요구사항 정의서
- `FRONTEND_BACKEND_INTEGRATION.md` - API 연동 완료 문서

### 공식 문서

- [Spring Boot 공식 문서](https://spring.io/projects/spring-boot)
- [Next.js 공식 문서](https://nextjs.org/docs)
- [TailwindCSS 문서](https://tailwindcss.com/docs)
- [shadcn/ui 컴포넌트](https://ui.shadcn.com/)

### 학습 순서 (신입 개발자용)

#### 1주차: 프로젝트 전체 이해
- [ ] 프로젝트 실행 환경 구축
- [ ] 백엔드 실행 및 Swagger UI 탐색
- [ ] 프론트엔드 실행 및 모든 페이지 탐색
- [ ] 데이터베이스 테이블 구조 확인
- [ ] 한 가지 기능(아이템 조회)의 전체 흐름 추적

#### 2주차: 백엔드 이해
- [ ] Entity, Repository, Service, Controller 역할 이해
- [ ] JPA 쿼리 메서드 학습
- [ ] DTO 변환 패턴 이해
- [ ] 디버거로 코드 실행 흐름 확인

#### 3주차: 프론트엔드 이해
- [ ] Next.js App Router 구조 이해
- [ ] lib/api.ts 파일 완전히 이해하기
- [ ] useState, useEffect 활용법 학습
- [ ] shadcn/ui 컴포넌트 사용법

#### 4주차: 실전 투입
- [ ] 간단한 버그 수정
- [ ] 새로운 필터 기능 추가
- [ ] 테스트 코드 작성
- [ ] 코드 리뷰 참여

---

## 🎉 마무리

이 문서는 Cody 프로젝트에 새로 합류하는 개발자를 위한 인수인계 자료입니다.

### 다음 단계

1. **환경 설정**: [시작하기](#-시작하기) 섹션을 따라 개발 환경을 구축하세요.
2. **구조 파악**: [프로젝트 구조](#-프로젝트-구조)를 읽고 전체 구조를 이해하세요.
3. **실습**: 한 가지 기능을 선택해서 [기능별 구현 가이드](#-기능별-구현-가이드)를 따라가며 코드를 추적하세요.
4. **질문**: 막히는 부분이 있으면 [트러블슈팅](#-트러블슈팅)을 참고하거나 팀에 질문하세요.

### 연락처

- 기술 질문: 팀 Slack 채널
- 긴급 이슈: 팀장에게 연락

**환영합니다! 함께 멋진 서비스를 만들어갑시다! 🚀**

---

**문서 버전**: 1.0  
**최종 업데이트**: 2025-11-11  
**작성자**: Cody Development Team

