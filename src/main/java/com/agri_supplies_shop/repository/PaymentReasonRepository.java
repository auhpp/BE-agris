package com.agri_supplies_shop.repository;

import com.agri_supplies_shop.entity.PaymentReason;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentReasonRepository extends JpaRepository<PaymentReason, Long> {
    PaymentReason findByName(String name);
}
