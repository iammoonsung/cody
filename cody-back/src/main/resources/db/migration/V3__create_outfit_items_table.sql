CREATE TABLE outfit_items (
    id BIGSERIAL PRIMARY KEY,
    outfit_id BIGINT NOT NULL,
    item_id BIGINT NOT NULL,
    CONSTRAINT fk_outfit FOREIGN KEY (outfit_id) REFERENCES outfits(id) ON DELETE CASCADE,
    CONSTRAINT fk_item FOREIGN KEY (item_id) REFERENCES items(id) ON DELETE CASCADE,
    CONSTRAINT uk_outfit_item UNIQUE (outfit_id, item_id)
);

CREATE INDEX idx_outfit_items_outfit ON outfit_items(outfit_id);
CREATE INDEX idx_outfit_items_item ON outfit_items(item_id);
