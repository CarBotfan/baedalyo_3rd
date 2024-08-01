package com.green.beadalyo.lmy.doneorder.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MonthOrderCountDto {
    private String createdAt;
    private Long monthOrderCount;

    public MonthOrderCountDto(String createdAt, Long monthOrderCount) {
        this.createdAt = createdAt;
        this.monthOrderCount = monthOrderCount;
    }


}