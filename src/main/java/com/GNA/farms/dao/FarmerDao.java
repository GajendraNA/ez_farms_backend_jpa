package com.GNA.farms.dao;

import com.GNA.farms.model.Farmer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FarmerDao extends JpaRepository<Farmer,Long> {
    Optional<Farmer> findByEmailAndPassword(String email, String password);
}
