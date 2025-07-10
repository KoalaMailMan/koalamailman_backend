package com.koa.RingDong.repository;

import com.koa.RingDong.entity.OAuthProvider;
import com.koa.RingDong.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByOauthIdAndOauthProvider(String oauthId, OAuthProvider oauthProvider);

}