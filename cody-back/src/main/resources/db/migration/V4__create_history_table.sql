CREATE TABLE history (
    id BIGSERIAL PRIMARY KEY,
    outfit_id BIGINT NOT NULL,
    worn_date DATE NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_history_outfit FOREIGN KEY (outfit_id) REFERENCES outfits(id) ON DELETE CASCADE
);

CREATE INDEX idx_history_worn_date ON history(worn_date);
CREATE INDEX idx_history_outfit ON history(outfit_id);
