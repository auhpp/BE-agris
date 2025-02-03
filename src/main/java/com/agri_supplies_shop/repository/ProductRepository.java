package com.agri_supplies_shop.repository;

import com.agri_supplies_shop.entity.Product;
import com.agri_supplies_shop.repository.custom.ProductRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryCustom {
}
