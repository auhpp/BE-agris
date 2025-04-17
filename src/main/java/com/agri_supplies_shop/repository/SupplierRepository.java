package com.agri_supplies_shop.repository;

import com.agri_supplies_shop.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long>,
        JpaSpecificationExecutor<Supplier> {
    List<Supplier> findByNameContaining(String name);

    Supplier findByEmail(String email);

    Supplier findByPhoneNumber(String phoneNumber);
}
