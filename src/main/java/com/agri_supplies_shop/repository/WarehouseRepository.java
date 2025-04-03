package com.agri_supplies_shop.repository;

import com.agri_supplies_shop.entity.Warehouse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {
    @Query(value = " select count(s.productVariantValue.id) " +
            " from WarehouseDetail wd inner join Shipment s on wd.shipment.id = s.id" +
            " group by s.productVariantValue.id"
    )
    Integer countProductQuantity(Long warehouseId);

    Page<Warehouse> findByNameContaining(String name, Pageable pageable);
}
