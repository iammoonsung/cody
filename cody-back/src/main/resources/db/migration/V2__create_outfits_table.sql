CREATE TABLE outfits (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(200),
    rating INTEGER NOT NULL CHECK (rating BETWEEN 1 AND 5),
    formality_level INTEGER NOT NULL CHECK (formality_level BETWEEN 1 AND 5),
    memo TEXT,
    worn_count INTEGER NOT NULL DEFAULT 0,
    last_worn_date DATE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_rating (rating),
    INDEX idx_formality (formality_level),
    INDEX idx_last_worn (last_worn_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
