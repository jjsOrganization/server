package com.jjs.ClothingInventorySaleReformPlatform.domain.order.service;

import com.jjs.ClothingInventorySaleReformPlatform.domain.order.dto.response.SellerOrderDTO;
import com.jjs.ClothingInventorySaleReformPlatform.domain.order.repository.CustomOrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
