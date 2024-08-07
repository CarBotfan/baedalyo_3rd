package com.green.beadalyo.gyb.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
    private Long restaurantPk ;
    private String restaurantName ;
    private Double reviewScore ;
    private Integer reviewTotalElements ;
    private String restaurantDesc ;
    private String reviewDesc ;
    private String regiNum ;
    private Integer totalMenu ;
    private LocalTime openTime ;
    private LocalTime closeTime ;
    private String restaurantAddr ;
    private Integer restaurantState ;
    private String restaurantPic ;
    private BigDecimal restaurantCoorX ;
    private BigDecimal restaurantCoorY ;
    private LocalDateTime createdAt ;
}
