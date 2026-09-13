package com.huy.food.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(
    name = "owners",
    indexes = {
        @Index(name = "idx_owners_user", columnList = "user_id")
    }
)
@SQLRestriction("deleted_at IS NULL")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Owner extends BaseEntity {

    @Id
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    @MapsId
    private User user;
}