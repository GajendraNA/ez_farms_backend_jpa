package com.GNA.farms.dto;


import com.GNA.farms.model.Buyer;
import com.GNA.farms.model.Farmer;
import com.GNA.farms.model.Order;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.time.LocalDate;
@Data
public class PaymentDto {

    private Long order_id;

    private Long farmer_id;

    private Long buyer_id;

    private String method;
    private LocalDate payment_date;
    private Double order_amount;


    private String farmerName;
    private String buyerName;





    private Double final_amount;
    private Double payment_amount;


}
