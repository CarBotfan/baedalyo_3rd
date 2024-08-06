package com.green.beadalyo.gyb.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;
import org.hibernate.annotations.View;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "restaurant_list_view")
@Immutable
@Getter
public class RestaurantListView
{
    @Id
    private Long restaurantPk ;

    private String restaurantName ;

    private Float reviewAvgScore ;

    private Integer reviewTotalElements ;

    private String restaurantAddr ;

    private Integer restaurantState ;

    private String restaurantPic ;

    private BigDecimal restaurantCoorX ;

    private BigDecimal restaurantCoorY ;

    private LocalDateTime createdAt ;
}
