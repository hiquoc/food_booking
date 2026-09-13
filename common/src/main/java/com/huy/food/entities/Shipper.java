package com.huy.food.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(
    name = "shippers",
    indexes = {
        @Index(name = "idx_shippers_user", columnList = "user_id")
    }
)
@SQLRestriction("deleted_at IS NULL")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Shipper extends BaseEntity {

    @Id
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    @MapsId
    private User user;
}