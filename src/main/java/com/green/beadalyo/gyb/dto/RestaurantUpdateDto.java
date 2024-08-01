package com.green.beadalyo.gyb.dto;

import lombok.Data;

import java.time.LocalTime;

@Data
public class RestaurantUpdateDto
{
    //가게 이름
    private String name ;
    //사업자 번호
    private String regiNum ;
    //사업장 주소
    private String resAddr ;
    //위도(X)
    private Double resCoorX ;
    //경도(Y)
    private Double resCoorY ;
    //개점 시간
    private LocalTime openTime ;
    //폐점 시간
    private LocalTime closeTime ;
    //상태창
    private Integer state ;



}
