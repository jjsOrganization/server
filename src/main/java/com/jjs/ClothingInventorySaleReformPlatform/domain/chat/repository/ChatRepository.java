package com.jjs.ClothingInventorySaleReformPlatform.domain.chat.repository;

import com.jjs.ClothingInventorySaleReformPlatform.domain.chat.entity.Chat;
import com.jjs.ClothingInventorySaleReformPlatform.domain.user.entity.DesignerInfo;
import com.jjs.ClothingInventorySaleReformPlatform.domain.user.entity.PurchaserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat , Long> {
    List<Chat> findByPurchaserEmail(PurchaserInfo purchaserEmail);
    List<Chat> findByDesignerEmail(DesignerInfo designerEmail);

//    Optional<Chat> findByIdAndDesignerEmailAndPurchaserEmail(Long id, PurchaserInfo purchaserEmail);
}
