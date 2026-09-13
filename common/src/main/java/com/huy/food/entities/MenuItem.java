package com.huy.food.entities;

import com.huy.food.enums.MenuItemStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(
    name = "menu_items",
    indexes = {
        @Index(
            name = "idx_menu_items_restaurant",
            columnList = "restaurant_id"
        ),
        @Index(
            name = "idx_menu_items_section",
            columnList = "restaurant_section_id"
        ),
        @Index(
            name = "idx_menu_items_restaurant_status",
            columnList = "restaurant_id, status"
        )
    }
)
@SQLRestriction("deleted_at IS NULL")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MenuItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "restaurant_section_id", nullable = false)
    private RestaurantSection section;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(length = 500)
    private String image;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    @Column(name = "discount_rate", precision = 5, scale = 2)
    private BigDecimal discountRate;

    @Column(name="display_order",nullable = false)
    private long displayOrder;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MenuItemStatus status = MenuItemStatus.AVAILABLE;

    @Version
    @Column(nullable = false)
    private long version;
}