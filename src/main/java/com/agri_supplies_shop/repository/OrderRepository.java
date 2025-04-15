package com.agri_supplies_shop.repository;

import com.agri_supplies_shop.entity.Orders;
import com.agri_supplies_shop.repository.specification.BaseSpecification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Long>, JpaSpecificationExecutor<Orders> {
}
