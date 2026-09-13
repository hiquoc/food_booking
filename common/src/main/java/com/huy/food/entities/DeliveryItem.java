package com.huy.food.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(
    name = "delivery_items",
    indexes = {
        @Index(
            name = "idx_delivery_items_delivery",
            columnList = "delivery_id"
        ),
        @Index(
            name = "idx_delivery_items_menu_item",
            columnList = "menu_item_id"
        )
    }
)
@SQLRestriction("deleted_at IS NULL")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeliveryItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "delivery_id", nullable = false)
    private Delivery delivery;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_item_id")
    private MenuItem menuItem;

    // Snapshot at purchase time
    @Column(name = "item_name", nullable = false, length = 200)
    private String itemName;

    @Column(name = "unit_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "line_total", nullable = false, precision = 12, scale = 2)
    private BigDecimal lineTotal;

    @Column(name = "discount_rate", precision = 5, scale = 2)
    private BigDecimal discountRate;

    @Column(nullable = false)
    private Integer quantity;
}