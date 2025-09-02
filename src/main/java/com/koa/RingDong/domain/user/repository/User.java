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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OAuthProvider oauthProvider;

    @Column(nullable = false)
    private String oauthId;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
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
    public User(OAuthProvider oauthProvider, String oauthId, String nickname, String email) {
        this.oauthProvider = oauthProvider;
        this.oauthId = oauthId;
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