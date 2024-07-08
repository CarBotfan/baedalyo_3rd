package com.green.beadalyo.lmy.order;

import com.green.beadalyo.common.model.ResultDto;
import com.green.beadalyo.lmy.order.model.OrderGetRes;
import com.green.beadalyo.lmy.order.model.OrderMiniGetRes;
import com.green.beadalyo.lmy.order.model.OrderPostReq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import java.util.List;


import static com.green.beadalyo.lmy.order.ExceptionMsgDataset.*;
import static com.green.beadalyo.lmy.order.ResponseDataSet.*;



@RestController
@RequestMapping("api/order/")
@RequiredArgsConstructor
@Slf4j
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    @Operation(summary = "주문하기")
    @ApiResponse(
            description =
                    "<p> 1 : 성공 </p>"+
                            "<p> -1 : 실패 </p>"+
                            "<p> -2 : 로그인 정보 획득 실패 </p>" +
                            "<p> -3 : 음식점 정보 획득 실패 </p>" +
                            "<p> -4 : 사업자 등록 번호 유효성 검사 실패 </p>" +
                            "<p> -5 : 상호 명 유효성 검사 실패 </p>"
    )
    public ResultDto<Integer> postOrder(@RequestBody OrderPostReq p){


        if (p.getPaymentMethod() == null) {
            return ResultDto.<Integer>builder().statusCode(-1).resultMsg(PAYMENT_METHOD_ERROR).build();
        }

        if (500 < p.getOrderRequest().length()) {
            return ResultDto.<Integer>builder().statusCode(-2).resultMsg(STRING_LENGTH_ERROR).build();
        }

        int result;
        try {
            result = orderService.postOrder(p);
        } catch (NullPointerException e) {
            return ResultDto.<Integer>builder().statusCode(-3).resultMsg(e.getMessage()).build();
        } catch (RuntimeException e) {
            return ResultDto.<Integer>builder().statusCode(-4).resultMsg(e.getMessage()).build();
        }

        return ResultDto.<Integer>builder().statusCode(SUCCESS_CODE).resultMsg(POST_ORDER_SUCCESS).resultData(result).build();
    }

    @PutMapping("cancel")
    public ResultDto<Integer> cancelOrder(@RequestParam Long orderPk) {
        int code = 1;
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
        int code = 1;
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
        int code = 1;
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
        int code = 1;
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
        int code = 1;
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
        int code = 1;
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
        int code = 1;
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

