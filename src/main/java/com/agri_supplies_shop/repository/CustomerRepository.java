package com.agri_supplies_shop.repository;

import com.agri_supplies_shop.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByAccountId(Long id);

    @Override
    Page<Customer> findAll(Pageable pageable);

    void deleteByIdIn(List<Long> ids);
}
