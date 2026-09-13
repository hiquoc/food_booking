package com.huy.food.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Setter
@Entity
@Table(
    name = "restaurant_sections",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uq_section_restaurant_name",
            columnNames = {"restaurant_id", "name"}
        )
    },
    indexes = {
        @Index(
            name = "idx_sections_restaurant",
            columnList = "restaurant_id"
        )
    }
)
@SQLRestriction("deleted_at IS NULL")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RestaurantSection extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name="display_order",nullable = false)
    private long displayOrder;
}