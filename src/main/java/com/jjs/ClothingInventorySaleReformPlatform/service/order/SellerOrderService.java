package com.jjs.ClothingInventorySaleReformPlatform.service.order;

import com.jjs.ClothingInventorySaleReformPlatform.dto.order.response.SellerOrderDTO;
import com.jjs.ClothingInventorySaleReformPlatform.repository.order.CustomOrderRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class SellerOrderService {

    private final CustomOrderRepository customOrderRepository;

    public List<SellerOrderDTO> getSellerOrders(String sellerEmail) {
        return customOrderRepository.findOrdersBySeller(sellerEmail);
    }
}
