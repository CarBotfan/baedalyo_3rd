package com.green.beadalyo.gyb.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Getter;
import net.jcip.annotations.Immutable;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "restaurant_list_view")
@Immutable
@Getter
public class RestaurantListView {
    @Id
    private Long restaurantPk;

    private String restaurantName;

    private Float reviewAvgScore;

    private Integer reviewTotalElements;

    private String restaurantAddr;

    private Integer restaurantState;

    private String restaurantPic;

    @Transient
    private Integer isFollow = 0; // Transient 필드로 설정
    @Transient
    private Integer isCoupon = 0;
    @Transient
    private Integer maxPrice = null;

    private BigDecimal restaurantCoorX;

    private BigDecimal restaurantCoorY;

    private LocalDateTime createdAt;

    public RestaurantListView(Long restaurantPk, String restaurantName, Float reviewAvgScore, Integer reviewTotalElements,
                              String restaurantAddr, Integer restaurantState, String restaurantPic,
                              BigDecimal restaurantCoorX, BigDecimal restaurantCoorY, LocalDateTime createdAt, Integer isFollow, Integer isCoupon, Integer maxPrice) {
        this.restaurantPk = restaurantPk;
        this.restaurantName = restaurantName;
        this.reviewAvgScore = reviewAvgScore;
        this.reviewTotalElements = reviewTotalElements;
        this.restaurantAddr = restaurantAddr;
        this.restaurantState = restaurantState;
        this.restaurantPic = restaurantPic;
        this.restaurantCoorX = restaurantCoorX;
        this.restaurantCoorY = restaurantCoorY;
        this.createdAt = createdAt;
        this.isFollow = isFollow;
        this.isCoupon = isCoupon;
        this.maxPrice = maxPrice;

    }

    public RestaurantListView() {
    }
}