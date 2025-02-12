package com.agri_supplies_shop.repository;

import com.agri_supplies_shop.entity.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByUserName(String username);

    @Override
    Page<Users> findAll(Pageable pageable);

    void deleteByIdIn(List<Long> ids);
}
