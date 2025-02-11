package com.agri_supplies_shop.repository;

import com.agri_supplies_shop.entity.CartItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<CartItem, Long> {
    Page<CartItem> findAllByUserId(Long userId, Pageable pageable);
}
