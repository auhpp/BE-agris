package com.agri_supplies_shop.repository;

import com.agri_supplies_shop.entity.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ShipmentRepository extends JpaRepository<Shipment, Long>,
        JpaSpecificationExecutor<Shipment> {

}
