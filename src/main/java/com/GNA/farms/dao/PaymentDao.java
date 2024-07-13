package com.GNA.farms.dao;


import com.GNA.farms.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentDao extends JpaRepository<Payment,Long> {
    List<Payment> findAllByFarmerId(@Param("farmer_id") Long id);
    List<Payment> findAllByBuyerId(@Param("buyer_id") Long id);
    List<Payment> findAllByOrderId(@Param("order_id") Long id);

}
