package com.green.beadalyo.lmy.doneorder.model;

public class DailySalesDto {
    private String createdAt;
    private Long dailySales;

    public DailySalesDto(String createdAt, Long dailySales) {
        this.createdAt = createdAt;
        this.dailySales = dailySales;
    }
}
