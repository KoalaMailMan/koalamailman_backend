package com.koa.koalamailman.user.infrastructure;

import com.koa.koalamailman.user.domain.OAuthProvider;
import com.koa.koalamailman.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByOauthIdAndOauthProvider(String oauthId, OAuthProvider oauthProvider);
}