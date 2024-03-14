package com.jjs.ClothingInventorySaleReformPlatform.repository.order;

import com.jjs.ClothingInventorySaleReformPlatform.dto.order.response.PurchaserOrderDTO;
import com.jjs.ClothingInventorySaleReformPlatform.dto.order.response.SellerOrderDTO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomOrderRepository {
    List<SellerOrderDTO> findOrdersBySeller(String sellerEmail);
    List<PurchaserOrderDTO> findOrderByPurchaser(String purchaserEmail);

}
