package com.GNA.farms.dao;

import com.GNA.farms.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface InventoryDao extends JpaRepository<Inventory,Long> {
}
