package com.green.beadalyo.lmy.order;

import com.green.beadalyo.lmy.order.model.OrderPostReq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderMapper orderMapper;

    @Transactional
    public Object postOrder(OrderPostReq p) {


    }
}
