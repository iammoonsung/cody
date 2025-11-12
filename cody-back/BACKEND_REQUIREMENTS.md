# Backend Requirements for "오늘뭐입지?" (What to Wear Today)

## 1. Overview

Spring Boot 백엔드 API for wardrobe management, outfit creation, and smart recommendation system.

## 2. Tech Stack (Based on Reference Repository)

- **Java 21** with Spring Boot 3.3.5
- **Spring Data JPA** (Hibernate) with QueryDSL 5.0.0
- **MySQL 8.0**
- **Flyway** for database migrations
- **Lombok** and **MapStruct** for code generation
- **Undertow** as web server
- **Swagger/OpenAPI** for API documentation
- **Spring Security** with JWT for authentication

## 3. Functional Requirements from PRD

### 3.1 User Management
- User registration (email, password)
- User login (JWT token generation)
- User profile management
- Password encryption with BCrypt

### 3.2 Item Management (Wardrobe)
- Create item with image URL (S3 or local storage path)
- Read all items for user
- Read single item by ID
- Update item details
- Delete item
- Filter items by category, season, color

**Item Attributes:**
- ID (Long)
- User ID (foreign key)
- Category (TOPS, BOTTOMS, SHOES, OUTERWEAR, ACCESSORIES)
- Name (optional)
- Image URL (required)
- Color (optional)
- Season (SPRING, SUMMER, FALL, WINTER, ALL)
- Created at, Updated at

### 3.3 Outfit Management
- Create outfit (select multiple items)
- Read all outfits for user
- Read single outfit by ID
- Update outfit (rating, formality, memo)
- Delete outfit
- Filter outfits by rating, formality level
- Track worn count and last worn date

**Outfit Attributes:**
- ID (Long)
- User ID (foreign key)
- Name (optional)
- Rating (1-5 stars)
- Formality Level (1-5: Home, Neighborhood, Outing, Work, Formal)
- Memo (optional)
- Worn Count (default: 0)
- Last Worn Date (nullable)
- Created at, Updated at

**Outfit-Item Relationship:**
- Junction table: OutfitItem
- Many-to-Many relationship between Outfit and Item
- Each outfit can have multiple items
- Each item can be in multiple outfits

### 3.4 Recommendation System
- Get outfit recommendation based on criteria:
  - Minimum rating (default: 3)
  - Minimum formality level (default: 3)
  - Optional: Required item ID (must include this item)
  - Optional: Exclude recently worn (last 2 days)
- Recommendation algorithm:
  1. Filter outfits by user ID
  2. Apply rating filter (rating >= minRating)
  3. Apply formality filter (formalityLevel >= minFormality)
  4. If requiredItemId: filter outfits containing that item
  5. If excludeRecent: filter out outfits worn in last 2 days
  6. Randomly select one outfit from filtered results
  7. Return null if no outfits match

### 3.5 History Management (Calendar)
- Record outfit worn on specific date
- Get outfit history for a date range (month view)
- Get statistics:
  - Number of outfits worn this month
  - Most worn outfits (top 5)
  - Most used items (top 5)
  - Average formality level

**History Attributes:**
- ID (Long)
- User ID (foreign key)
- Outfit ID (foreign key)
- Worn Date (LocalDate, YYYY-MM-DD)
- Created at

**Business Logic:**
- When recording outfit worn:
  - Create new History record
  - Update Outfit: lastWornDate = today, wornCount++

## 4. Database Schema Design

### 4.1 Users Table
```sql
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    name VARCHAR(50),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_email (email)
);
```

### 4.2 Items Table
```sql
CREATE TABLE items (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    category VARCHAR(20) NOT NULL, -- TOPS, BOTTOMS, SHOES, OUTERWEAR, ACCESSORIES
    name VARCHAR(200),
    image_url VARCHAR(500) NOT NULL,
    color VARCHAR(50),
    season VARCHAR(20), -- SPRING, SUMMER, FALL, WINTER, ALL
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_user_category (user_id, category),
    INDEX idx_user_season (user_id, season)
);
```

### 4.3 Outfits Table
```sql
CREATE TABLE outfits (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    name VARCHAR(200),
    rating INTEGER NOT NULL CHECK (rating BETWEEN 1 AND 5),
    formality_level INTEGER NOT NULL CHECK (formality_level BETWEEN 1 AND 5),
    memo TEXT,
    worn_count INTEGER NOT NULL DEFAULT 0,
    last_worn_date DATE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_user_rating (user_id, rating),
    INDEX idx_user_formality (user_id, formality_level),
    INDEX idx_last_worn (user_id, last_worn_date)
);
```

### 4.4 Outfit_Items Table (Junction)
```sql
CREATE TABLE outfit_items (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    outfit_id BIGINT NOT NULL,
    item_id BIGINT NOT NULL,
    FOREIGN KEY (outfit_id) REFERENCES outfits(id) ON DELETE CASCADE,
    FOREIGN KEY (item_id) REFERENCES items(id) ON DELETE CASCADE,
    UNIQUE KEY uk_outfit_item (outfit_id, item_id),
    INDEX idx_outfit (outfit_id),
    INDEX idx_item (item_id)
);
```

### 4.5 History Table
```sql
CREATE TABLE history (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    outfit_id BIGINT NOT NULL,
    worn_date DATE NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (outfit_id) REFERENCES outfits(id) ON DELETE CASCADE,
    INDEX idx_user_date (user_id, worn_date),
    INDEX idx_outfit (outfit_id)
);
```

## 5. API Endpoints (RESTful)

### 5.1 Authentication
- `POST /api/auth/register` - User registration
- `POST /api/auth/login` - User login (returns JWT token)

### 5.2 Items
- `GET /api/items` - Get all items for user (with filters)
- `GET /api/items/{id}` - Get item by ID
- `POST /api/items` - Create new item
- `PUT /api/items/{id}` - Update item
- `DELETE /api/items/{id}` - Delete item

### 5.3 Outfits
- `GET /api/outfits` - Get all outfits for user (with filters)
- `GET /api/outfits/{id}` - Get outfit by ID (with items)
- `POST /api/outfits` - Create new outfit
- `PUT /api/outfits/{id}` - Update outfit
- `DELETE /api/outfits/{id}` - Delete outfit

### 5.4 Recommendations
- `POST /api/recommendations` - Get outfit recommendation
  - Request body: `{ minRating, minFormality, requiredItemId?, excludeRecent }`
  - Response: Outfit object or 404 if none found

### 5.5 History
- `GET /api/history` - Get all history for user
- `GET /api/history/calendar?year={year}&month={month}` - Get calendar data
- `POST /api/history` - Record outfit worn (updates outfit stats)
- `GET /api/history/stats?year={year}&month={month}` - Get statistics

## 6. Package Structure (Following Reference Style)

```
com.cody.wardrobe/
├── controller/              # REST Controllers
│   ├── dto/                # Controller DTOs (Request/Response)
│   │   ├── auth/
│   │   ├── item/
│   │   ├── outfit/
│   │   ├── recommendation/
│   │   └── history/
│   └── mapper/             # MapStruct mappers (Controller DTO ↔ Service DTO)
├── service/                # Business logic
│   ├── dto/               # Service layer DTOs
│   │   ├── auth/
│   │   ├── item/
│   │   ├── outfit/
│   │   ├── recommendation/
│   │   └── history/
│   └── mapper/            # Service layer mappers
├── repository/             # Data access layer
│   ├── ItemRepository
│   ├── ItemQueryRepository (QueryDSL)
│   ├── OutfitRepository
│   ├── OutfitQueryRepository
│   └── HistoryRepository, etc.
├── domain/                # JPA Entities
│   ├── user/
│   │   └── User.java
│   ├── item/
│   │   ├── Item.java
│   │   ├── ItemCategory.java (enum)
│   │   └── Season.java (enum)
│   ├── outfit/
│   │   ├── Outfit.java
│   │   ├── OutfitItem.java
│   │   └── FormalityLevel.java (enum)
│   └── history/
│       └── History.java
├── security/              # JWT, Security config
├── exception/             # Custom exceptions
├── config/                # Spring configuration
└── common/                # Shared utilities (ApiResponse, etc.)
```

## 7. Key Implementation Details

### 7.1 Entity Design Patterns (From Reference)
- Use `@Builder` for object construction
- Use `@NoArgsConstructor(access = AccessLevel.PROTECTED)` for JPA
- Use `@AllArgsConstructor(access = AccessLevel.PRIVATE)` with Builder
- Static factory methods for entity creation (e.g., `User.create()`)
- Update methods for modifying entities (e.g., `updateProfile()`)
- `@CreationTimestamp` and `@UpdateTimestamp` for audit fields

### 7.2 Repository Patterns
- Standard Spring Data JPA repositories for basic CRUD
- QueryDSL repositories for complex queries (filtering, statistics)
- Example: `ItemQueryRepository` for filtering by category/season

### 7.3 Service Layer
- `@Transactional` for write operations
- Separate DTOs for service layer (internal contracts)
- MapStruct for DTO conversions
- Business logic validation

### 7.4 Controller Layer
- `@RestController` with `@RequestMapping`
- `@Valid` for input validation
- Return `ApiResponse<T>` wrapper for consistent responses
- Exception handling with `@RestControllerAdvice`

### 7.5 Security
- JWT token generation on login
- Token validation filter
- User authentication from token
- Authorization checks (user can only access their own data)

## 8. Development Phases

### Phase 1: Core Setup
1. Initialize Spring Boot project with Gradle
2. Configure MySQL connection
3. Setup Flyway migrations
4. Create base entity classes and enums
5. Configure Lombok, MapStruct, QueryDSL

### Phase 2: User & Authentication
1. User entity and repository
2. Authentication service (register, login, JWT)
3. Security configuration
4. Auth endpoints

### Phase 3: Item Management
1. Item entity, enums, repository
2. ItemService with CRUD operations
3. ItemQueryRepository for filtering
4. ItemController with full CRUD endpoints

### Phase 4: Outfit Management
1. Outfit entity, OutfitItem junction entity
2. OutfitService with CRUD + outfit-item management
3. OutfitQueryRepository for filtering
4. OutfitController endpoints

### Phase 5: Recommendation System
1. RecommendationService with algorithm
2. QueryDSL queries for complex filtering
3. Recommendation endpoint

### Phase 6: History & Calendar
1. History entity and repository
2. HistoryService with recording and statistics
3. QueryDSL for date-based queries and aggregations
4. History endpoints

### Phase 7: Testing & Documentation
1. Unit tests for services
2. Integration tests for endpoints
3. Swagger/OpenAPI documentation
4. CLAUDE.md for project guidance

## 9. Non-Functional Requirements

- **Performance**: Paginated responses for list endpoints
- **Security**: All endpoints (except auth) require JWT token
- **Validation**: Bean Validation on all request DTOs
- **Error Handling**: Consistent error response format
- **Logging**: Request/response logging for debugging
- **Database**: Use Flyway for all schema changes (no Hibernate DDL auto)

## 10. Future Enhancements (Not in MVP)

- Weather integration API
- ML-based recommendations
- Image upload to S3
- Social features (sharing outfits)
- Multi-language support
