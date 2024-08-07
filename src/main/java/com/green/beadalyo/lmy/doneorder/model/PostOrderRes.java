package com.green.beadalyo.lmy.doneorder.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PostOrderRes
{
    @Schema(description = "할인 전 최종 금액")
    private Integer orderPrice ;

    @Schema(description = "할인 후 최종 금액")
    private Integer totalPrice ;

    @Schema(description = "주문 고유번호")
    private Long orderPk ;

}
