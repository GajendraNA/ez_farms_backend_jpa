package com.GNA.farms.dto;

import com.GNA.farms.model.Buyer;
import lombok.Data;

@Data
public class DiscountDto {
    private Long buyer_id;

    private Long order_count;
    private Double discount_percent;
}
