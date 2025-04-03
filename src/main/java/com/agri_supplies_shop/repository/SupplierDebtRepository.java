package com.agri_supplies_shop.repository;

import com.agri_supplies_shop.entity.PaymentSlip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupplierDebtRepository extends JpaRepository<PaymentSlip, Long> {
}
