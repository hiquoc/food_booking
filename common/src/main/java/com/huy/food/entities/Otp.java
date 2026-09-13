package com.huy.food.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Setter
@Entity
@Table(name = "otp",
        indexes = {
                @Index(name = "idx_otp", columnList = "otp")
        })
@AllArgsConstructor
@NoArgsConstructor
@Builder
@SQLRestriction("deleted_at IS NULL")
public class Otp extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String phone;
    private String otp;
}
