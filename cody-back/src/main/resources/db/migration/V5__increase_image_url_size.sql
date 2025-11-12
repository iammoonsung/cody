-- Base64 이미지를 저장할 수 있도록 image_url 크기 증가
-- VARCHAR(500) → MEDIUMTEXT (최대 16MB)
ALTER TABLE items MODIFY image_url MEDIUMTEXT NOT NULL;

