package com.green.beadalyo.lmy.order;

import com.green.beadalyo.common.model.ResultDto;
import com.green.beadalyo.lmy.order.model.OrderGetRes;
import com.green.beadalyo.lmy.order.model.OrderMiniGetRes;
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

    @PutMapping("cancel")
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

    @PutMapping("done")
    public ResultDto<Integer> completeOrder(@RequestParam Long orderPk) {
        int code = 2;
        String msg = "order complete 완료";
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

    @GetMapping("user")
    public ResultDto<List<OrderMiniGetRes>> getUserOrderList(@RequestParam Long userPk) {
        int code = 2;
        String msg = "유저의 진행중인 주문정보 불러오기 완료";
        List<OrderMiniGetRes> result = null;

        try {
            result = orderService.getUserOrderList(userPk);
        } catch (Exception e) {
            code = 4;
            msg = e.getMessage();
        }

        return ResultDto.<List<OrderMiniGetRes>>builder()
                .statusCode(code)
                .resultMsg(msg)
                .resultData(result)
                .build();
    }

    @GetMapping("res/noconfirm")
    public ResultDto<List<OrderMiniGetRes>> getResNonConfirmOrderList(@RequestParam Long resPk) {
        int code = 2;
        String msg = "상점의 진행중인 주문정보 불러오기 완료";
        List<OrderMiniGetRes> result = null;

        try {
            result = orderService.getResNonConfirmOrderList(resPk);
        } catch (Exception e) {
            code = 4;
            msg = e.getMessage();
        }

        return ResultDto.<List<OrderMiniGetRes>>builder()
                .statusCode(code)
                .resultMsg(msg)
                .resultData(result)
                .build();
    }

    @GetMapping("res/confirm")
    public ResultDto<List<OrderMiniGetRes>> getResConfirmOrderList(@RequestParam Long resPk) {
        int code = 2;
        String msg = "상점의 진행중인 주문정보 불러오기 완료";
        List<OrderMiniGetRes> result = null;

        try {
            result = orderService.getResConfirmOrderList(resPk);
        } catch (Exception e) {
            code = 4;
            msg = e.getMessage();
        }

        return ResultDto.<List<OrderMiniGetRes>>builder()
                .statusCode(code)
                .resultMsg(msg)
                .resultData(result)
                .build();
    }

    @GetMapping
    public ResultDto<OrderGetRes> getOrderInfo(@RequestParam Long orderPk) {
        int code = 2;
        String msg = "주문 상세보기 완료";
        OrderGetRes result = null;

        try {
            result = orderService.getOrderInfo(orderPk);
        } catch (Exception e) {
            code = 4;
            msg = e.getMessage();
        }

        return ResultDto.<OrderGetRes>builder()
                .statusCode(code)
                .resultMsg(msg)
                .resultData(result)
                .build();
    }

    @PatchMapping
    public ResultDto<Integer> confirmOrder(@RequestParam Long orderPk){
        int code = 2;
        String msg = "주문 접수 완료";
        Integer result = -1;

        try {
            result = orderService.confirmOrder(orderPk);
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
}

