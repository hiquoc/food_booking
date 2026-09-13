package com.huy.food.entities;

import com.huy.food.enums.AssignmentStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(
    name = "delivery_assignments",
    indexes = {
        @Index(
            name = "idx_assignments_shipper_status",
            columnList = "shipper_id, status"
        ),
        @Index(
            name = "idx_assignments_delivery_sequence",
            columnList = "delivery_id, sequence_number"
        ),
        @Index(
            name = "idx_assignments_expiration",
            columnList = "expires_at"
        )
    }
)
@SQLRestriction("deleted_at IS NULL")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeliveryAssignment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "delivery_id", nullable = false)
    private Delivery delivery;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "shipper_id", nullable = false)
    private Shipper shipper;

    @Column(name = "sequence_number")
    private Integer sequenceNumber;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private AssignmentStatus status = AssignmentStatus.OFFERED;

    @Column(name = "expires_at")
    private Instant expiresAt;
}