package com.agri_supplies_shop.repository;

import com.agri_supplies_shop.entity.ProductAttributeValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttributeValueRepository extends JpaRepository<ProductAttributeValue, Long> {
    void deleteByIdIn(List<Long> ids);
}
