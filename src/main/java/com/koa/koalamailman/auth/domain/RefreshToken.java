package com.koa.koalamailman.auth.domain;

import com.koa.koalamailman.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "token",
        indexes = {
                @Index(name = "idx_token_user_id", columnList = "userId")
        })
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class RefreshToken extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false, length = 32)
    private byte[] tokenHash;

    @Version
    @Column(nullable = false)
    private Integer version;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    public static RefreshToken create(Long userId, byte[] tokenHash, LocalDateTime expiresAt) {
        return RefreshToken.builder()
                .userId(userId)
                .tokenHash(tokenHash)
                .expiresAt(expiresAt)
                .build();
    }

    public void updateToken(byte[] tokenHash, LocalDateTime expiresAt) {
        this.tokenHash = tokenHash;
        this.expiresAt = expiresAt;
    }
}