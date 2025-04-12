package com.agri_supplies_shop.repository;

import com.agri_supplies_shop.entity.Warehouse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {
    @Query(value = "select count(*) from " +
            "(select s.product_variant_value_id " +
            " from warehouse_detail wd inner join shipment s on wd.shipment_id = s.id " +
            "where wd.warehouse_id = :id " +
            " group by s.product_variant_value_id) ", nativeQuery = true
    )
    Long countProductQuantity(@Param("id") Long id);

    Page<Warehouse> findByNameContaining(String name, Pageable pageable);
}
