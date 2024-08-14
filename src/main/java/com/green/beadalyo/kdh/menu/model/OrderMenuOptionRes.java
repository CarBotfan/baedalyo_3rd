package com.green.beadalyo.kdh.menu.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.green.beadalyo.lmy.order.entity.OrderMenuOption;
import lombok.Data;

@Data
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OrderMenuOptionRes
{
    private Long optionPk ;
    private Long optionMenuPk ;
    private String optionName ;
    private Integer optionPrice ;

    public OrderMenuOptionRes(OrderMenuOption data)
    {
        this.optionPk = data.getSeq() ;
        this.optionName = data.getOptionName() ;
        this.optionPrice = data.getOptionPrice() ;
    }

}
