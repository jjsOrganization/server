package com.jjs.ClothingInventorySaleReformPlatform.domain.order.repository;

import com.jjs.ClothingInventorySaleReformPlatform.domain.order.dto.response.PurchaserOrderDTO;
import com.jjs.ClothingInventorySaleReformPlatform.domain.order.dto.response.SellerOrderDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class CustomOrderRepositoryImpl implements CustomOrderRepository{

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<SellerOrderDTO> findOrdersBySeller(String sellerEmail) {

        String jpql = "select new com.jjs.ClothingInventorySaleReformPlatform.domain.order.dto.response.SellerOrderDTO(o, od, p, d) " +
                "FROM OrderDetail od " +
                "join od.product p " +
                "join od.order o " +
                "join Delivery d on o.id = d.order.id " +
                "where p.createBy = :sellerEmail AND o.orderStatus = com.jjs.ClothingInventorySaleReformPlatform.domain.order.entity.OrderStatus.ORDER_COMPLETE";

        return em.createQuery(jpql, SellerOrderDTO.class)
                .setParameter("sellerEmail", sellerEmail)
                .getResultList();
    }

    @Override
    public List<PurchaserOrderDTO> findOrderByPurchaser(String purchaserEmail) {

        String jpql = "select new com.jjs.ClothingInventorySaleReformPlatform.domain.order.dto.response.PurchaserOrderDTO(o, od, p, d) " +
                "FROM OrderDetail od " +
                "join od.product p " +
                "join od.order o " +
                "join Delivery d on o.id = d.order.id " +
                "where o.purchaserInfo.email = :purchaserEmail AND o.orderStatus = com.jjs.ClothingInventorySaleReformPlatform.domain.order.entity.OrderStatus.ORDER_COMPLETE";

        return em.createQuery(jpql, PurchaserOrderDTO.class)
                .setParameter("purchaserEmail", purchaserEmail)
                .getResultList();
    }
}
