package com.GNA.farms.dao;

import com.GNA.farms.model.Buyer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository

public interface BuyerDao extends JpaRepository<Buyer,Long> {
    Optional<Buyer> findByEmailAndPassword(String email, String password);

    boolean existsByEmail(String email);
}
