package com.koa.RingDong.repository;

import com.koa.RingDong.entity.OAuthProvider;
import com.koa.RingDong.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByOauthIdAndOauthProvider(String oauthId, OAuthProvider oauthProvider);

}