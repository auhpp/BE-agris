package com.agri_supplies_shop.repository;

import com.agri_supplies_shop.entity.Staff;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {
    Staff findByEmail(String email);

    Page<Staff> findByFullNameContaining(String fullName, Pageable pageable);

    Staff findByPhoneNumber(String phoneNumber);

    Optional<Staff> findByAccountId(Long accountId);
}
