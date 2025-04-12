package com.agri_supplies_shop.repository;

import com.agri_supplies_shop.entity.Cart;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Page<Cart> findAllByCustomerId(Long userId, Pageable pageable);

    Cart findByProductVariantValueIdAndCustomerId(Long productVariantValueId, Long userId);
}
