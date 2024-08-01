package com.green.beadalyo.lmy.doneorder.model;

public class DailyOrderCountDto {
    private String createdAt;
    private Long dailyOrderCountDto;

    public DailyOrderCountDto(String createdAt, Long dailyOrderCountDto) {
        this.createdAt = createdAt;
        this.dailyOrderCountDto = dailyOrderCountDto;
    }
}
