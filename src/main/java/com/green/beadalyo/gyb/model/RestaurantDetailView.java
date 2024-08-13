package com.green.beadalyo.gyb.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Getter;
import org.hibernate.annotations.Immutable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "restaurant_detail_view")
@Immutable
@Getter
public class RestaurantDetailView
{
    @Id
    private Long restaurantPk;
    private String restaurantName;
    private Double reviewScore;
    private Integer reviewTotalElements;
    private String restaurantDesc;
    private String reviewDesc;
    private String regiNum;
    private Integer totalMenu;
    private LocalTime openTime;
    private LocalTime closeTime;
    private String restaurantAddr;
    private Integer restaurantState;
    private String restaurantPic;
    @Transient
    private Integer isFollow = 0; // Transient 필드로 설정
    private BigDecimal restaurantCoorX ;
    private BigDecimal restaurantCoorY ;
    private LocalDateTime createdAt ;

    public RestaurantDetailView(Long restaurantPk, String restaurantName, Double reviewScore, Integer reviewTotalElements, String reviewDesc, String restaurantDesc, String regiNum, Integer totalMenu, LocalTime closeTime, LocalTime openTime, String restaurantAddr, String restaurantPic, Integer restaurantState, BigDecimal restaurantCoorX, BigDecimal restaurantCoorY, LocalDateTime createdAt, Integer isFollow) {
        this.restaurantPk = restaurantPk;
        this.restaurantName = restaurantName;
        this.reviewScore = reviewScore;
        this.reviewTotalElements = reviewTotalElements;
        this.reviewDesc = reviewDesc;
        this.restaurantDesc = restaurantDesc;
        this.regiNum = regiNum;
        this.totalMenu = totalMenu;
        this.closeTime = closeTime;
        this.openTime = openTime;
        this.restaurantAddr = restaurantAddr;
        this.restaurantPic = restaurantPic;
        this.restaurantState = restaurantState;
        this.restaurantCoorX = restaurantCoorX;
        this.isFollow = isFollow;
        this.restaurantCoorY = restaurantCoorY;
        this.createdAt = createdAt;
    }

    public RestaurantDetailView() {

    }
}
