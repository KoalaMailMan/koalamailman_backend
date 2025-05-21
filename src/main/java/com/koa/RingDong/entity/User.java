package com.koa.RingDong.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "kakao_id", nullable = false)
    private String kakaoId;

    @Column(nullable = false)
    private String nickname;

    @Column
    private String email;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Date createdAt;

    @Builder
    public User(String kakaoId, String nickname, String email, Integer knockChance) {
        this.kakaoId = kakaoId;
        this.nickname = nickname;
        this.email = email;
    }
}