package com.green.beadalyo.lmy.doneorder.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MonthSalesDto {
    private String createdAt;
    private Long monthSales;

    public MonthSalesDto(String createdAt, Long monthSales) {
        this.createdAt = createdAt;
        this.monthSales = monthSales;
    }
}