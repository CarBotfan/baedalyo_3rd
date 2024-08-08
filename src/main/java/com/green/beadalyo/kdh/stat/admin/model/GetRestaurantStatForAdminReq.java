package com.green.beadalyo.kdh.stat.admin.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GetRestaurantStatForAdminReq {
    private String date;

    private Long resPk;

}
