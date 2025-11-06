package com.agri_supplies_shop.repository;

import com.agri_supplies_shop.entity.Supplier;
import com.agri_supplies_shop.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long>,
        JpaSpecificationExecutor<Supplier> {
    List<Supplier> findByNameContainingAndStatus(String name, Status status);

    Supplier findByEmailAndStatus(String email, Status status);

    Supplier findByPhoneNumberAndStatus(String phoneNumber, Status status);

    Supplier findByEmail(String email);

    Supplier findByPhoneNumber(String phoneNumber);
}
