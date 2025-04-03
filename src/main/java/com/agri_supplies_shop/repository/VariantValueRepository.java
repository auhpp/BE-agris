package com.agri_supplies_shop.repository;

import com.agri_supplies_shop.entity.VariantValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VariantValueRepository extends JpaRepository<VariantValue, Long> {
    VariantValue findByValue(String value);

}
