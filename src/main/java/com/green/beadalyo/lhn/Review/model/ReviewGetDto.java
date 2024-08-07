package com.green.beadalyo.lhn.Review.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.green.beadalyo.gyb.model.Restaurant;
import com.green.beadalyo.jhw.user.entity.User;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ReviewGetDto {
    private Restaurant restaurant;
    private User user;
    private Integer page;

}

