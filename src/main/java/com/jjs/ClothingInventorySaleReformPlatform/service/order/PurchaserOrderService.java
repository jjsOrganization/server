package com.jjs.ClothingInventorySaleReformPlatform.service.order;

import com.jjs.ClothingInventorySaleReformPlatform.dto.order.response.PurchaserOrderDTO;
import com.jjs.ClothingInventorySaleReformPlatform.dto.order.response.SellerOrderDTO;
import com.jjs.ClothingInventorySaleReformPlatform.repository.order.CustomOrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PurchaserOrderService {

    private final CustomOrderRepository customOrderRepository;

    public List<PurchaserOrderDTO> getPurchaserOrders(String purchaserEmail) {
        return customOrderRepository.findOrderByPurchaser(purchaserEmail);
    }

}
