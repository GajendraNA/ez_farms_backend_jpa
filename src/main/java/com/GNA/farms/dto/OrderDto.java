package com.GNA.farms.dto;

import com.GNA.farms.model.Buyer;
import com.GNA.farms.model.Farmer;
import com.GNA.farms.model.Inventory;
import com.GNA.farms.model.Items;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.time.LocalDate;
@Data
public class OrderDto {

    private Long inventory_id;
    private Long id;

    private Long farmer_id;

    private Long buyer_id;
    private Long item_id;
    private Double weight;
    private LocalDate ordered_date;
    private LocalDate est_delivery_date;
    private Double order_amount;
    private Double discount_applied;
    private Double amount_pending;
    private Double final_amount;
    private String status;


    private String farmer_name;
    private String inventory_name;
    private String buyer_name;
    private String item_name;






}
