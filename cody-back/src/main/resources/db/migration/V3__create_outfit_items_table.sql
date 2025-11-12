CREATE TABLE outfit_items (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    outfit_id BIGINT NOT NULL,
    item_id BIGINT NOT NULL,
    FOREIGN KEY (outfit_id) REFERENCES outfits(id) ON DELETE CASCADE,
    FOREIGN KEY (item_id) REFERENCES items(id) ON DELETE CASCADE,
    UNIQUE KEY uk_outfit_item (outfit_id, item_id),
    INDEX idx_outfit (outfit_id),
    INDEX idx_item (item_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
