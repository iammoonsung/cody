package com.cody.wardrobe.domain.item;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "items", indexes = {
    @Index(name = "idx_category", columnList = "category"),
    @Index(name = "idx_season", columnList = "season")
})
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ItemCategory category;

    @Column(length = 200)
    private String name;

    @Column(name = "image_url", nullable = false, columnDefinition = "MEDIUMTEXT")
    private String imageUrl;

    @Column(length = 50)
    private String color;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Season season;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Static factory method
    public static Item create(ItemCategory category, String imageUrl) {
        return Item.builder()
                .category(category)
                .imageUrl(imageUrl)
                .build();
    }

    public static Item createWithDetails(ItemCategory category, String imageUrl,
                                         String name, String color, Season season) {
        return Item.builder()
                .category(category)
                .imageUrl(imageUrl)
                .name(name)
                .color(color)
                .season(season)
                .build();
    }

    // Update methods
    public void update(ItemCategory category, String imageUrl, String name, String color, Season season) {
        this.category = category;
        this.imageUrl = imageUrl;
        this.name = name;
        this.color = color;
        this.season = season;
    }
}
