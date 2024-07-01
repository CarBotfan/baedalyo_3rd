package com.green.beadalyo.lmy.order;

import com.green.beadalyo.lmy.order.model.OrderPostReq;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface OrderMapper {
    List<Map<String, Object>> selectMenus(List<Long> menuPkList);
    void postOrderTable(OrderPostReq orderPostReq);
    void insertOrderMenuBatch(List<Map<String, Object>> orderMenuList);
}
