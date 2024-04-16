package com.jjs.ClothingInventorySaleReformPlatform.domain.user.repository;

import com.jjs.ClothingInventorySaleReformPlatform.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);
}
