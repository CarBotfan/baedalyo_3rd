package com.green.beadalyo.lmy.order.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OrderMenuReq
{
    @Schema(description = "메뉴 고유번호")
    private Long orderMenuPk ;
    @Schema(description = "메뉴 수량")
    private Integer menuCount ;
    @Schema(description = "메뉴 옵션 번호")
    private List<Long> MenuOptionPk ;
}
