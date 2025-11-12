-- PostgreSQL: updated_at 자동 업데이트를 위한 트리거 함수 생성
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TABLE items (
    id BIGSERIAL PRIMARY KEY,
    category VARCHAR(20) NOT NULL,
    name VARCHAR(200),
    image_url VARCHAR(500) NOT NULL,
    color VARCHAR(50),
    season VARCHAR(20),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_items_category ON items(category);
CREATE INDEX idx_items_season ON items(season);

-- items 테이블에 트리거 적용
CREATE TRIGGER update_items_updated_at
    BEFORE UPDATE ON items
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();
