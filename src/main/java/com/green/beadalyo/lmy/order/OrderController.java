package com.green.beadalyo.lmy.order;

import com.green.beadalyo.common.model.ResultDto;
import com.green.beadalyo.lmy.order.model.OrderGetRes;
import com.green.beadalyo.lmy.order.model.OrderPostReq;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("api/order/")
@RequiredArgsConstructor
@Slf4j
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResultDto<Integer> postOrder(@RequestBody OrderPostReq p){
        int code = 2;
        String msg = "order 업로드 완료";
        int result = -1;

        try {
            result = orderService.postOrder(p);
        } catch (Exception e) {
            code = 4;
            msg = e.getMessage();
        }

        return ResultDto.<Integer>builder()
                .statusCode(code)
                .resultMsg(msg)
                .resultData(result)
                .build();
    }

    @PutMapping("api/order/cancel")
    public ResultDto<Integer> cancelOrder(@RequestParam Long orderPk) {
        int code = 2;
        String msg = "order 취소 완료";
        int result = -1;

        try {
            result = orderService.cancelOrder(orderPk);
        } catch (Exception e) {
            code = 4;
            msg = e.getMessage();
        }

        return ResultDto.<Integer>builder()
                .statusCode(code)
                .resultMsg(msg)
                .resultData(result)
                .build();
    }

    @PutMapping("api/order/done")
    public ResultDto<Integer> completeOrder(@RequestParam Long orderPk) {
        int code = 2;
        String msg = "order 취소 완료";
        int result = -1;

        try {
            result = orderService.completeOrder(orderPk);
        } catch (Exception e) {
            code = 4;
            msg = e.getMessage();
        }

        return ResultDto.<Integer>builder()
                .statusCode(code)
                .resultMsg(msg)
                .resultData(result)
                .build();
    }

    @GetMapping("api/order/user")
    public ResultDto<List<OrderGetRes>> getUserOrderList(@RequestParam Long userPk) {
        int code = 2;
        String msg = "order 취소 완료";
        List<OrderGetRes> result = null;

        try {
            result = orderService.getUserOrderList(userPk);
        } catch (Exception e) {
            code = 4;
            msg = e.getMessage();
        }

        return ResultDto.<List<OrderGetRes>>builder()
                .statusCode(code)
                .resultMsg(msg)
                .resultData(result)
                .build();
    }

    @GetMapping("api/order/res")
    public ResultDto<List<OrderGetRes>> getResOrderList(@RequestParam Long resPk) {
        int code = 2;
        String msg = "order 취소 완료";
        List<OrderGetRes> result = null;

        try {
            result = orderService.getResOrderList(resPk);
        } catch (Exception e) {
            code = 4;
            msg = e.getMessage();
        }

        return ResultDto.<List<OrderGetRes>>builder()
                .statusCode(code)
                .resultMsg(msg)
                .resultData(result)
                .build();
    }
}
