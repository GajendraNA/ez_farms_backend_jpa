package com.GNA.farms.dao;

import com.GNA.farms.model.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface DiscountDao extends JpaRepository<Discount,Long> {
}
