package com.jjs.ClothingInventorySaleReformPlatform.domain.portfolio.repository;

import com.jjs.ClothingInventorySaleReformPlatform.domain.portfolio.entity.ReformOutput;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReformOutputRepository extends JpaRepository<ReformOutput, Long> {

    Optional<ReformOutput> findByProgress_id(Long progressNumber);
}
