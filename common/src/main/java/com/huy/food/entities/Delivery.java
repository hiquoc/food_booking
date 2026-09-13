package com.huy.food.entities;

import com.huy.food.enums.DeliveryStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(
    name = "deliveries",
    indexes = {
        @Index(name = "idx_deliveries_user", columnList = "user_id"),
        @Index(name = "idx_deliveries_status", columnList = "status"),
        @Index(
            name = "idx_deliveries_user_status",
            columnList = "user_id, status"
        ),
        @Index(
            name = "idx_deliveries_expected_delivery",
            columnList = "expected_delivery_at"
        )
    }
)
@SQLRestriction("deleted_at IS NULL")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Delivery extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true, length = 30)
    private String code;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private DeliveryStatus status = DeliveryStatus.CREATED;

    @Builder.Default
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal subtotal = BigDecimal.ZERO;

    @Builder.Default
    @Column(name = "discounted_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal discountedAmount = BigDecimal.ZERO;

    @Builder.Default
    @Column(name = "shipping_fee", nullable = false, precision = 12, scale = 2)
    private BigDecimal shippingFee = BigDecimal.ZERO;

    @Builder.Default
    @Column(name = "total_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @Column(precision = 10, scale = 2)
    private BigDecimal distance;

    @Column(name = "shipper_revenue", precision = 12, scale = 2)
    private BigDecimal shipperRevenue;

    @Column(name = "expected_delivery_at")
    private Instant expectedDeliveryAt;

    @Column(name = "delivered_at")
    private Instant deliveredAt;

    @Builder.Default
    @OneToMany(mappedBy = "delivery", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<DeliveryItem> items=new ArrayList<>();

}