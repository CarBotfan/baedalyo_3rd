package com.green.beadalyo.lmy.order;

import com.green.beadalyo.lmy.order.model.OrderPostReq;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper {

    Object postOrderTable(OrderPostReq p);
    Object postOrderMenuTable(OrderPostReq p);
    Object postOrderMenuOptionTable(OrderPostReq p);
}
