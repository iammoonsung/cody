package com.cody.wardrobe.domain.outfit;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "outfits", indexes = {
    @Index(name = "idx_rating", columnList = "rating"),
    @Index(name = "idx_formality", columnList = "formality_level"),
    @Index(name = "idx_last_worn", columnList = "last_worn_date")
})
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Outfit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 200)
    private String name;

    @Column(nullable = false)
    private Integer rating;

    @Column(name = "formality_level", nullable = false)
    private Integer formalityLevel;

    @Column(columnDefinition = "TEXT")
    private String memo;

    @Builder.Default
    @Column(name = "worn_count", nullable = false)
    private Integer wornCount = 0;

    @Column(name = "last_worn_date")
    private LocalDate lastWornDate;

    @Builder.Default
    @OneToMany(mappedBy = "outfit", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OutfitItem> outfitItems = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Static factory method
    public static Outfit create(Integer rating, Integer formalityLevel) {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }
        if (formalityLevel < 1 || formalityLevel > 5) {
            throw new IllegalArgumentException("Formality level must be between 1 and 5");
        }

        return Outfit.builder()
                .rating(rating)
                .formalityLevel(formalityLevel)
                .build();
    }

    public static Outfit createWithDetails(String name, Integer rating,
                                           Integer formalityLevel, String memo) {
        Outfit outfit = create(rating, formalityLevel);
        outfit.name = name;
        outfit.memo = memo;
        return outfit;
    }

    // Update methods
    public void update(String name, Integer rating, Integer formalityLevel, String memo) {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }
        if (formalityLevel < 1 || formalityLevel > 5) {
            throw new IllegalArgumentException("Formality level must be between 1 and 5");
        }

        this.name = name;
        this.rating = rating;
        this.formalityLevel = formalityLevel;
        this.memo = memo;
    }

    // Business methods
    public void recordWorn(LocalDate date) {
        this.lastWornDate = date;
        this.wornCount++;
    }

    public void decrementWornCount() {
        if (this.wornCount > 0) {
            this.wornCount--;
        }
    }

    public void updateLastWornDate(LocalDate date) {
        this.lastWornDate = date;
    }

    public void addItem(OutfitItem outfitItem) {
        this.outfitItems.add(outfitItem);
        outfitItem.setOutfit(this);
    }

    public void removeItem(OutfitItem outfitItem) {
        this.outfitItems.remove(outfitItem);
        outfitItem.setOutfit(null);
    }

    public void clearItems() {
        this.outfitItems.forEach(item -> item.setOutfit(null));
        this.outfitItems.clear();
    }
}
