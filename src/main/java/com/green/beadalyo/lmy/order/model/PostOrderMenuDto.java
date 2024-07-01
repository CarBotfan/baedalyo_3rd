package com.green.beadalyo.lmy.order.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostOrderMenuDto {
    private Long orderPk;
    private Long menuPk;
}
