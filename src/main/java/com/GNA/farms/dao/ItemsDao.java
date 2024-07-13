package com.GNA.farms.dao;

import com.GNA.farms.model.Items;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface ItemsDao extends JpaRepository<Items,Long> {
}
