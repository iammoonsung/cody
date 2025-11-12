CREATE TABLE history (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    outfit_id BIGINT NOT NULL,
    worn_date DATE NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (outfit_id) REFERENCES outfits(id) ON DELETE CASCADE,
    INDEX idx_date (worn_date),
    INDEX idx_outfit (outfit_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
