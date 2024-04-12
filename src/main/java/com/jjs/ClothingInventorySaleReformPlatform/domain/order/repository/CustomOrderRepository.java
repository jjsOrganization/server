package com.jjs.ClothingInventorySaleReformPlatform.domain.order.repository;

import com.jjs.ClothingInventorySaleReformPlatform.domain.order.dto.response.PurchaserOrderDTO;
import com.jjs.ClothingInventorySaleReformPlatform.domain.order.dto.response.SellerOrderDTO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomOrderRepository {
    List<SellerOrderDTO> findOrdersBySeller(String sellerEmail);
    List<PurchaserOrderDTO> findOrderByPurchaser(String purchaserEmail);

}
