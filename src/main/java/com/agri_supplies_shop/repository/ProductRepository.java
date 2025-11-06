package com.agri_supplies_shop.repository;

import com.agri_supplies_shop.entity.Product;
import com.agri_supplies_shop.enums.Status;
import com.agri_supplies_shop.repository.custom.ProductRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryCustom {
    List<Product> findByNameContaining(String name);

    Optional<Product> findByIdAndStatus(Long id, Status status);

}
