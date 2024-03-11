package com.jjs.ClothingInventorySaleReformPlatform.repository.order;

import com.jjs.ClothingInventorySaleReformPlatform.dto.order.response.SellerOrderDTO;
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

        String jpql = "select new com.jjs.ClothingInventorySaleReformPlatform.dto.order.response.SellerOrderDTO(o, od, p, d) " +
                "FROM OrderDetail od " +
                "join od.product p " +
                "join od.order o " +
                "join Delivery d on o.id = d.order.id " +
                "where p.createBy = :sellerEmail AND o.orderStatus = com.jjs.ClothingInventorySaleReformPlatform.domain.order.OrderStatus.ORDER_COMPLETE";

        return em.createQuery(jpql, SellerOrderDTO.class)
                .setParameter("sellerEmail", sellerEmail)
                .getResultList();
    }
}
