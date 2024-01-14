package com.jjs.ClothingInventorySaleReformPlatform.repository;

import com.jjs.ClothingInventorySaleReformPlatform.domain.Purchaser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface PurchaserRepository extends JpaRepository<Purchaser, String> {
    Optional<Purchaser> findByEmail(String email);
    Optional<Purchaser> findByPhoneNumber(String phonenumber);
    Optional<Purchaser> findByNickname(String nickname);
}
