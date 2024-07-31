package com.green.beadalyo.gyb.dto;

import lombok.Data;

import java.time.LocalTime;

@Data
public class RestaurantInsertDto
{

    //유저 정보
    private Long user ;
    //가게 이름
    private String name ;
    //사업자 번호
    private String regiNum ;
    //사업장 주소
    private String resAddr ;
    //사업장 설명
    private String desc1 ;
    //리뷰 설명
    private String desc2 ;
    //위도(X)
    private Double resCoorX ;
    //경도(Y)
    private Double resCoorY ;
    //개점 시간
    private LocalTime openTime ;
    //폐점 시간
    private LocalTime closeTime ;
    //음식점 사진
    private String resPic ;

}
