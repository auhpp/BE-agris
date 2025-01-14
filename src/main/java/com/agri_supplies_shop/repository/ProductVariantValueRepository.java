package com.agri_supplies_shop.repository;

import com.agri_supplies_shop.entity.ProductVariantValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductVariantValueRepository extends JpaRepository<ProductVariantValue, Long> {
}
