package com.huy.food.entities;

import com.huy.food.enums.RestaurantStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(
    name = "restaurants",
    indexes = {
        @Index(name = "idx_restaurants_owner", columnList = "owner_id"),
        @Index(name = "idx_restaurants_status", columnList = "status"),
        @Index(
            name = "idx_restaurants_location",
            columnList = "latitude, longitude"
        )
    }
)
@SQLRestriction("deleted_at IS NULL")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Restaurant extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(nullable = false, unique = true, length = 20)
    private String phone;

    @Column(nullable = false, length = 300)
    private String description;

    @Column(nullable = false, length = 100)
    private String address;

    @Column(nullable = false, length = 100)
    private String district;

    @Column(nullable = false, length = 100)
    private String city;

    @Column(nullable = false, precision = 9, scale = 6)
    private BigDecimal latitude;

    @Column(nullable = false, precision = 9, scale = 6)
    private BigDecimal longitude;

    @Column(name = "open_hour", nullable = false)
    private LocalTime openHour;

    @Column(name = "close_hour", nullable = false)
    private LocalTime closeHour;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private RestaurantStatus status = RestaurantStatus.ACTIVE;
}