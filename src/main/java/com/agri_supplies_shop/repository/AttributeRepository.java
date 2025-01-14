package com.agri_supplies_shop.repository;

import com.agri_supplies_shop.entity.Attribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttributeRepository extends JpaRepository<Attribute, Long> {
    Attribute findByName(String name);
}
