package com.koa.koalamailman.mandalart.infrastructure;

import com.koa.koalamailman.mandalart.domain.Mandalart;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface MandalartRepository extends JpaRepository<Mandalart, Long> {
    Optional<Mandalart> findByUserId(Long userId);
    Boolean existsByUserId(Long userId);
}
