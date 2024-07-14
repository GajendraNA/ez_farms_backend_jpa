package com.GNA.farms.dto;

import lombok.Data;

@Data
public class InventoryRequestDto {
    private Long id;
    private String name;
    private Long farmerId;
    private Long itemId;
    private Double weight;
    private Double remaining_weight;
    private Double final_rate_per_kg;
}
