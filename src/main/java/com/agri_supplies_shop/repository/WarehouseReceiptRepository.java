package com.agri_supplies_shop.repository;

import com.agri_supplies_shop.entity.WarehouseReceipt;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface WarehouseReceiptRepository extends JpaRepository<WarehouseReceipt, Long>,
        JpaSpecificationExecutor<WarehouseReceipt> {
    @Override
    Page<WarehouseReceipt> findAll(Pageable pageable);
}
