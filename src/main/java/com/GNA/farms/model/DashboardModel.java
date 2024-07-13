package com.GNA.farms.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
@Data
public class DashboardModel {


    private Integer currmonth;
    private Integer lastmonth;

    private Integer user_count;
    private String user_type;

    private String order_status;

    private Integer order_status_count;


}
