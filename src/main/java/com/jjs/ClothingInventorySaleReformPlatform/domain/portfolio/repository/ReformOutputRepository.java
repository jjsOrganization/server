package com.jjs.ClothingInventorySaleReformPlatform.domain.portfolio.repository;

import com.jjs.ClothingInventorySaleReformPlatform.domain.portfolio.entity.ReformOutput;
import com.jjs.ClothingInventorySaleReformPlatform.domain.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReformOutputRepository extends JpaRepository<ReformOutput, Long> {

    Optional<ReformOutput> findByProgress_id(Long progressNumber);
    List<ReformOutput> findAll();
    List<ReformOutput> findByCreateBy(String createBy);
}
