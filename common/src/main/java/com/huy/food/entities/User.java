package com.huy.food.entities;

import com.huy.food.enums.AccountRole;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(
    name = "users",
    indexes = {
        @Index(name = "idx_users_banned", columnList = "banned")
    }
)
@SQLRestriction("deleted_at IS NULL")
@SQLDelete(sql = "UPDATE users SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User extends BaseEntity {

    @Id
    private UUID id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, unique = true, length = 20)
    private String phone;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountRole role=AccountRole.USER;

    @Builder.Default
    @Column(nullable = false)
    private boolean banned = false;
}