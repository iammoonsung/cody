package com.cody.wardrobe.domain.history;

import com.cody.wardrobe.domain.outfit.Outfit;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "history", indexes = {
    @Index(name = "idx_date", columnList = "worn_date"),
    @Index(name = "idx_outfit", columnList = "outfit_id")
})
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class History {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "outfit_id", nullable = false)
    private Outfit outfit;

    @Column(name = "worn_date", nullable = false)
    private LocalDate wornDate;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Static factory method
    public static History create(Outfit outfit, LocalDate wornDate) {
        return History.builder()
                .outfit(outfit)
                .wornDate(wornDate)
                .build();
    }
}
