package com.jjs.ClothingInventorySaleReformPlatform.repository;

import com.jjs.ClothingInventorySaleReformPlatform.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaserRepository extends JpaRepository<User, String> {

    Boolean existsByEmail(String email);

    // email을 받아 DB 테이블에서 회원을 조회하는 메소드
    User findByEmail(String email);


    /*
    Optional<Purchaser> findByEmail(String email);
    Optional<Purchaser> findByPhoneNumber(String phonenumber);
    Optional<Purchaser> findByNickname(String nickname);

     */
}
