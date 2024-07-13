package com.GNA.farms.dao;

import com.GNA.farms.model.Inventory;
import com.GNA.farms.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface InventoryDao extends JpaRepository<Inventory,Long> {
    List<Inventory> findAllByFarmerId(@Param("farmer_id") Long id);
}
