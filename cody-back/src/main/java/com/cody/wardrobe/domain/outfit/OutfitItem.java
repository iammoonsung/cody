package com.cody.wardrobe.domain.outfit;

import com.cody.wardrobe.domain.item.Item;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "outfit_items",
    uniqueConstraints = @UniqueConstraint(name = "uk_outfit_item", columnNames = {"outfit_id", "item_id"}),
    indexes = {
        @Index(name = "idx_outfit", columnList = "outfit_id"),
        @Index(name = "idx_item", columnList = "item_id")
    }
)
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OutfitItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "outfit_id", nullable = false)
    private Outfit outfit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    // Static factory method
    public static OutfitItem create(Outfit outfit, Item item) {
        return OutfitItem.builder()
                .outfit(outfit)
                .item(item)
                .build();
    }
}
