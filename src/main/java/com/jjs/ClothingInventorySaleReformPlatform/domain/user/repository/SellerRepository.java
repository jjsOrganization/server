package com.jjs.ClothingInventorySaleReformPlatform.domain.user.repository;

import com.jjs.ClothingInventorySaleReformPlatform.domain.user.entity.SellerInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SellerRepository extends JpaRepository<SellerInfo, String> {

    //SellerInfo findByEmail(String email);
    Optional<SellerInfo> findByEmail (String email);
}
