package com.GNA.farms.dto;


import lombok.Data;

@Data
public class InventoryRequestDto {
    private Long id;
    private String name;
    private Long farmerId;
    private String itemName;
    private String description;
    private String category;


    private Double weight;
    private Double remaining_weight;
    private Double final_rate_per_kg;
}
