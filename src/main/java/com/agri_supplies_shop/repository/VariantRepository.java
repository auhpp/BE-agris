package com.agri_supplies_shop.repository;

import com.agri_supplies_shop.entity.Variant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VariantRepository extends JpaRepository<Variant, Long> {
    Variant findByName(String name);
}
