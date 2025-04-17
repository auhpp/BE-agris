package com.agri_supplies_shop.repository;

import com.agri_supplies_shop.entity.PayeeType;
import com.agri_supplies_shop.enums.PayeeTypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PayeeTypeRepository extends JpaRepository<PayeeType, Long> {
    PayeeType findByName(PayeeTypeEnum name);
}
