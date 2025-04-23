package com.agri_supplies_shop.repository;

import com.agri_supplies_shop.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>,
        JpaSpecificationExecutor<Customer> {

    Optional<Customer> findByAccountId(Long id);

    void deleteByIdIn(List<Long> ids);

    Optional<Customer> findByEmail(String email);
}
