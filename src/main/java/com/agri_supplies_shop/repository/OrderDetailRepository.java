package com.agri_supplies_shop.repository;

import com.agri_supplies_shop.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
    OrderDetail findByOrdersIdAndProductVariantValueId(Long ordersId, Long productVariantValueId);
}

