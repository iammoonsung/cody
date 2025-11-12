-- Base64 이미지를 저장할 수 있도록 image_url 크기 증가
-- VARCHAR(500) → TEXT (PostgreSQL에서는 TEXT 타입이 자동으로 대용량 지원)
ALTER TABLE items ALTER COLUMN image_url TYPE TEXT;
