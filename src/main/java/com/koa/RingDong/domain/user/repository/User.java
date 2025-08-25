package com.koa.RingDong.domain.user.repository;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
@Table(name = "user", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"oauthId", "oauthProvider"})
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String oauthId; // 각 플랫폼의 고유 id

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OAuthProvider oauthProvider;  // KAKAO, NAVER

    @Column(nullable = false)
    private String nickname;

    @Column
    private String email;

    @Column
    private Gender gender;

    @Column
    private AgeGroup ageGroup;

    @Column
    private String job;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Date createdAt;

    @Builder
    public User(String oauthId, OAuthProvider oauthProvider, String nickname, String email) {
        this.oauthId = oauthId;
        this.oauthProvider = oauthProvider;
        this.nickname = nickname;
        this.email = email;
        this.gender = null;
        this.ageGroup = null;
        this.job = null;
    }

    public void updateProfile(Gender gender, AgeGroup ageGroup, String job) {
        this.gender = gender;
        this.ageGroup = ageGroup;
        this.job = job;
    }
}