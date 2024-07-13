package com.GNA.farms.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
@Entity
@Data
@Table(name = "payment")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "farmer_id")
    private Farmer farmer;

    @ManyToOne
    @JoinColumn(name = "buyer_id")
    private Buyer buyer;

    private String method;
    private LocalDate payment_date;
    private Double order_amount;
    private Double final_amount;
    private Double payment_amount;






}