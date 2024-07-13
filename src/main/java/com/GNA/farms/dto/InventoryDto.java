package com.GNA.farms.dto;

import lombok.Data;

@Data
public class InventoryDto {
    private String name;
    private Long id;


    private Long farmer_id;


    private Double weight;
    private Double remaining_weight;
    private Double final_rate_per_kg;
    private String farmer_name;
    private String item_name;
    private String item_description;
    private String item_category;



}
