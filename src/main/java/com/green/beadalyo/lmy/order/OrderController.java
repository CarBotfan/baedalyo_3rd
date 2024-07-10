package com.green.beadalyo.lmy.order;

import com.green.beadalyo.common.model.ResultDto;
import com.green.beadalyo.lmy.order.model.OrderGetRes;
import com.green.beadalyo.lmy.order.model.OrderMiniGetRes;
import com.green.beadalyo.lmy.order.model.OrderPostReq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;


import static com.green.beadalyo.lmy.order.dataset.ExceptionMsgDataset.*;
import static com.green.beadalyo.lmy.order.dataset.ResponseDataSet.*;



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
                            "<p> -1 : 결제가 완료되지 않았습니다 </p>"+
                            "<p> -2 : 글 양식을 맞춰주세요 (글자 수) </p>" +
                            "<p> -3 : 메뉴를 찾을 수 없습니다 </p>" +
                            "<p> -4 : 데이터 리스트 생성 실패 </p>"
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
    @Operation(summary = "주문취소 하기")
    @ApiResponse(
            description =
                    "<p> 1 : 성공 </p>"+
                            "<p> -1 : 주문 완료 실패 </p>"  +
                            "<p> -3 : 메뉴 찾기 실패 </p>"
    )
    public ResultDto<Integer> cancelOrder(@RequestParam Long orderPk) {
        int result = -1;

        try {
            result = orderService.cancelOrder(orderPk);
        } catch (Exception e) {

        }

        return ResultDto.<Integer>builder()
                .statusCode(SUCCESS_CODE)
                .resultMsg(CANCEL_ORDER_SUCCESS)
                .resultData(result)
                .build();
    }

    @PutMapping("done")
    @Operation(summary = "주문완료 하기")
    @ApiResponse(
            description =
                    "<p> 1 : 성공 </p>"+
                            "<p> -1 : 상점 주인의 접근이 아닙니다 </p>"
    )
    public ResultDto<Integer> completeOrder(@RequestParam Long orderPk) {
        int result = -1;

        try {
            result = orderService.completeOrder(orderPk);
        } catch (RuntimeException e) {
            return ResultDto.<Integer>builder().statusCode(-1).resultMsg(e.getMessage()).build();
        }

        return ResultDto.<Integer>builder()
                .statusCode(SUCCESS_CODE)
                .resultMsg(COMPLETE_ORDER_SUCCESS)
                .resultData(result)
                .build();
    }

    @GetMapping("user")
    @Operation(summary = "유저의 진행중인 주문 불러오기")
    @ApiResponse(
            description =
                    "<p> 1 : 성공 </p>"+
                            "<p> -1 : 주문 정보 불러오기 실패 </p>"+
                            "<p> -2 : 불러올 주문 정보가 없음 </p>"
    )
    public ResultDto<List<OrderMiniGetRes>> getUserOrderList() {
        List<OrderMiniGetRes> result = null;

        try {
            result = orderService.getUserOrderList();
        } catch (RuntimeException e) {
            return ResultDto.<List<OrderMiniGetRes>>builder().statusCode(-1).resultMsg(GET_ORDER_LIST_FAIL).build();
        }

        if (result == null || result.isEmpty()) {
            return ResultDto.<List<OrderMiniGetRes>>builder().statusCode(-2).resultMsg(GET_ORDER_LIST_NON).build();
        }

        return ResultDto.<List<OrderMiniGetRes>>builder()
                .statusCode(SUCCESS_CODE)
                .resultMsg(USER_ORDER_LIST_SUCCESS)
                .resultData(result)
                .build();
    }

    @GetMapping("res/noconfirm")
    @Operation(summary = "상점의 접수 전 주문정보 불러오기")
    @ApiResponse(
            description =
                    "<p> 1 : 성공 </p>"+
                            "<p> -1 : 상점 주인의 접근이 아닙니다 </p>"+
                            "<p> -2 : 불러올 주문 정보가 없음 </p>"
    )
    public ResultDto<List<OrderMiniGetRes>> getResNonConfirmOrderList(@RequestParam Long resPk) {
        List<OrderMiniGetRes> result = null;

        try {
            result = orderService.getResNonConfirmOrderList(resPk);
        } catch (RuntimeException e) {
            return ResultDto.<List<OrderMiniGetRes>>builder().statusCode(-1).resultMsg(e.getMessage()).build();
        }

        if (result == null || result.isEmpty()) {
            return ResultDto.<List<OrderMiniGetRes>>builder().statusCode(-2).resultMsg(GET_ORDER_LIST_NON).build();
        }

        return ResultDto.<List<OrderMiniGetRes>>builder()
                .statusCode(SUCCESS_CODE)
                .resultMsg(RES_ORDER_NO_CONFIRM_LIST_SUCCESS)
                .resultData(result)
                .build();
    }

    @GetMapping("res/confirm")
    @Operation(summary = "상점의 접수 후 주문정보 불러오기")
    @ApiResponse(
            description =
                    "<p> 1 : 성공 </p>"+
                            "<p> -1 : 상점 주인의 접근이 아닙니다 </p>"+
                            "<p> -2 : 불러올 주문 정보가 없음 </p>"
    )
    public ResultDto<List<OrderMiniGetRes>> getResConfirmOrderList(@RequestParam Long resPk) {
        List<OrderMiniGetRes> result = null;

        try {
            result = orderService.getResConfirmOrderList(resPk);
        } catch (Exception e) {
            return ResultDto.<List<OrderMiniGetRes>>builder().statusCode(-1).resultMsg(e.getMessage()).build();
        }

        if (result == null || result.isEmpty()) {
            return ResultDto.<List<OrderMiniGetRes>>builder().statusCode(-2).resultMsg(GET_ORDER_LIST_NON).build();
        }

        return ResultDto.<List<OrderMiniGetRes>>builder()
                .statusCode(SUCCESS_CODE)
                .resultMsg(RES_ORDER_CONFIRM_LIST_SUCCESS)
                .resultData(result)
                .build();
    }

    @GetMapping
    @Operation(summary = "주문정보 상세보기")
    @ApiResponse(
            description =
                    "<p> 1 : 성공 </p>"+
                            "<p> -1 : 주문 정보 불러오기 실패 </p>"+
                            "<p> -2 : 불러올 주문 정보가 없음 </p>"
    )
    public ResultDto<OrderGetRes> getOrderInfo(@RequestParam Long orderPk) {
        OrderGetRes result = null;

        try {
            result = orderService.getOrderInfo(orderPk);
        } catch (Exception e) {
            return ResultDto.<OrderGetRes>builder().statusCode(-1).resultMsg(GET_ORDER_LIST_FAIL).build();
        }

        if (result == null) {
            return ResultDto.<OrderGetRes>builder().statusCode(-2).resultMsg(GET_ORDER_LIST_NON).build();
        }

        return ResultDto.<OrderGetRes>builder()
                .statusCode(SUCCESS_CODE)
                .resultMsg(ORDER_INFO_SUCCESS)
                .resultData(result)
                .build();
    }

    @PatchMapping
    @Operation(summary = "주문 접수하기")
    @ApiResponse(
            description =
                    "<p> 1 : 성공 </p>"+
                            "<p> -1 : 상점 주인의 접근이 아닙니다 </p>"
    )
    public ResultDto<Integer> confirmOrder(@RequestParam Long orderPk){
        Integer result = -1;

        try {
            result = orderService.confirmOrder(orderPk);
        } catch (Exception e) {
            return ResultDto.<Integer>builder().statusCode(-1).resultMsg(e.getMessage()).build();
        }

        return ResultDto.<Integer>builder()
                .statusCode(SUCCESS_CODE)
                .resultMsg(CONFIRM_ORDER_SUCCESS)
                .resultData(result)
                .build();
    }
}

