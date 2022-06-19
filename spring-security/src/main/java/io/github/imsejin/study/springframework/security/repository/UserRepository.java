package io.github.imsejin.study.springframework.security.repository;

import io.github.imsejin.study.springframework.security.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @EntityGraph(attributePaths = "authorities")
    Optional<User> findWithAuthoritiesByUsername(String username);

}
