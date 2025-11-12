CREATE TABLE outfits (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(200),
    rating INTEGER NOT NULL CHECK (rating BETWEEN 1 AND 5),
    formality_level INTEGER NOT NULL CHECK (formality_level BETWEEN 1 AND 5),
    memo TEXT,
    worn_count INTEGER NOT NULL DEFAULT 0,
    last_worn_date DATE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_outfits_rating ON outfits(rating);
CREATE INDEX idx_outfits_formality ON outfits(formality_level);
CREATE INDEX idx_outfits_last_worn ON outfits(last_worn_date);

-- outfits 테이블에 트리거 적용
CREATE TRIGGER update_outfits_updated_at
    BEFORE UPDATE ON outfits
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();
