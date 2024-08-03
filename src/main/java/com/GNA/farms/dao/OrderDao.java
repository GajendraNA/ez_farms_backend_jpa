package com.GNA.farms.dao;

import com.GNA.farms.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface OrderDao extends JpaRepository<Order,Long> {
    @Query("SELECT o FROM Order o " +
            "JOIN FETCH o.inventory " +
            "JOIN FETCH o.farmer " +
            "JOIN FETCH o.buyer " +
            "JOIN FETCH o.items")
    List<Order> findAllWithDetails();

    List<Order> findAllByFarmerId(@Param("farmer_id") Long id);
    List<Order> findAllByBuyerId(@Param("buyer_id") Long id);


}
