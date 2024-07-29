package com.green.beadalyo.lmy.order;

import com.green.beadalyo.common.model.ResultDto;
import com.green.beadalyo.jhw.security.AuthenticationFacade;
import com.green.beadalyo.lmy.dataset.ExceptionMsgDataSet;
import com.green.beadalyo.lmy.doneorder.model.DoneOrderMiniGetRes;
import com.green.beadalyo.lmy.order.model.OrderGetRes;
import com.green.beadalyo.lmy.order.model.OrderMiniGetRes;
import com.green.beadalyo.lmy.order.model.OrderPostReq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;



import static com.green.beadalyo.lmy.dataset.ResponseDataSet.*;



@RestController
@RequestMapping("api/order/")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "주문 관련 컨트롤러")
public class OrderController {
    private final OrderService orderService;
    private final OrderMapper orderMapper;
    private final AuthenticationFacade authenticationFacade;

    @PostMapping
    @Operation(summary = "주문하기")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @ApiResponse(
            description =
                    "<p> 1 : 주문하기 성공 </p>"+
                            "<p> -1 : 결제가 완료되지 않았습니다 </p>"+
                            "<p> -2 : 글 양식을 맞춰주세요 (글자 수) </p>" +
                            "<p> -3 : 메뉴를 찾을 수 없습니다 </p>" +
                            "<p> -4 : 데이터 리스트 생성 실패 </p>"
    )
    public ResultDto<Long> postOrder(@RequestBody OrderPostReq p){
        int statusCode = SUCCESS_CODE;
        String resultMsg = POST_ORDER_SUCCESS;
        Long resultData = p.getOrderPk();

        if (p.getPaymentMethod() == null) {
            statusCode = ExceptionMsgDataSet.PAYMENT_METHOD_ERROR.getCode();
            resultMsg = ExceptionMsgDataSet.PAYMENT_METHOD_ERROR.getMessage();
            resultData = null;
        }

        if (500 < p.getOrderRequest().length()) {
            statusCode = ExceptionMsgDataSet.STRING_LENGTH_ERROR.getCode();
            resultMsg = ExceptionMsgDataSet.STRING_LENGTH_ERROR.getMessage();
            resultData = null;
        }

        try {
            orderService.postOrder(p);
        } catch (NullPointerException e) {
            statusCode = ExceptionMsgDataSet.MENU_NOT_FOUND_ERROR.getCode();
            resultMsg = ExceptionMsgDataSet.MENU_NOT_FOUND_ERROR.getMessage();
            resultData = null;
        } catch (RuntimeException e) {
            statusCode = ExceptionMsgDataSet.DATALIST_FAIL_ERROR.getCode();
            resultMsg = ExceptionMsgDataSet.DATALIST_FAIL_ERROR.getMessage();
            resultData = null;
        }

        return ResultDto.<Long>builder()
                .statusCode(statusCode)
                .resultMsg(resultMsg)
                .resultData(resultData)
                .build();
    }

    @PutMapping("cancel/list/{order_pk}")
    @Operation(summary = "주문취소 하기")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN','OWNER')")
    @ApiResponse(
            description =
                    "<p> 1 : 주문 취소 성공 </p>"+
                            "<p> -9 : 접수전의 주문은 주문자, 상점주인만 취소 가능합니다 </p>" +
                            "<p> -10 : 접수중인 주문은 상점 주인만 취소 가능합니다 </p>" +
                            "<p> -5 : 주문 취소 실패 </p>"
    )
    public ResultDto<Integer> cancelOrder(@PathVariable("order_pk") Long orderPk) {
        int result = -1;

        long userPk = authenticationFacade.getLoginUserPk();
        if (orderMapper.getOrderState(orderPk) == 1){
            if (userPk != orderMapper.getResUserPkByOrderPk(orderPk) && userPk != orderMapper.getUserPkByOrderPk(orderPk)) {
                return ResultDto.<Integer>builder()
                        .statusCode(ExceptionMsgDataSet.NO_NON_CONFIRM_CANCEL_AUTHENTICATION.getCode())
                        .resultMsg(ExceptionMsgDataSet.NO_NON_CONFIRM_CANCEL_AUTHENTICATION.getMessage())
                        .build();
            }
        }

        if (orderMapper.getOrderState(orderPk) == 2){
            if (userPk != orderMapper.getResUserPkByOrderPk(orderPk)) {
                return ResultDto.<Integer>builder()
                        .statusCode(ExceptionMsgDataSet.NO_CONFIRM_CANCEL_AUTHENTICATION.getCode())
                        .resultMsg(ExceptionMsgDataSet.NO_CONFIRM_CANCEL_AUTHENTICATION.getMessage()).build();
            }
        }

        try {
            result = orderService.cancelOrder(orderPk);
        } catch (Exception e) {
            return ResultDto.<Integer>builder()
                    .statusCode(ExceptionMsgDataSet.ORDER_CANCEL_FAIL.getCode())
                    .resultMsg(ExceptionMsgDataSet.ORDER_CANCEL_FAIL.getMessage()).build();
        }

        return ResultDto.<Integer>builder()
                .statusCode(SUCCESS_CODE)
                .resultMsg(CANCEL_ORDER_SUCCESS)
                .resultData(result)
                .build();
    }

    @PutMapping("owner/done/{order_pk}")
    @Operation(summary = "주문완료 하기")
    @PreAuthorize("hasAnyRole('ADMIN','OWNER')")
    @ApiResponse(
            description =
                    "<p> 1 : 주문 완료 성공 </p>"+
                            "<p> -8 : 상점 주인의 접근이 아닙니다 </p>"
    )
    public ResultDto<Integer> completeOrder(@PathVariable("order_pk") Long orderPk) {
        int result = -1;

        try {
            result = orderService.completeOrder(orderPk);
        } catch (RuntimeException e) {
            return ResultDto.<Integer>builder()
                    .statusCode(ExceptionMsgDataSet.NO_AUTHENTICATION.getCode())
                    .resultMsg(ExceptionMsgDataSet.NO_AUTHENTICATION.getMessage()).build();
        }

        return ResultDto.<Integer>builder()
                .statusCode(SUCCESS_CODE)
                .resultMsg(COMPLETE_ORDER_SUCCESS)
                .resultData(result)
                .build();
    }

    @GetMapping("user/list")
    @Operation(summary = "유저의 진행중인 주문 불러오기")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @ApiResponse(
            description =
                    "<p> 1 : 유저의 진행중인 주문 불러오기 완료 </p>"+
                            "<p> -6 : 주문 정보 불러오기 실패 </p>"+
                            "<p> -7 : 불러올 주문 정보가 없음 </p>"
    )
    public ResultDto<List<OrderMiniGetRes>> getUserOrderList() {
        List<OrderMiniGetRes> result = null;

        try {
            result = orderService.getUserOrderList();
        } catch (RuntimeException e) {
            return ResultDto.<List<OrderMiniGetRes>>builder()
                    .statusCode(ExceptionMsgDataSet.GET_ORDER_LIST_FAIL.getCode())
                    .resultMsg(ExceptionMsgDataSet.GET_ORDER_LIST_FAIL.getMessage()).build();
        }

        if (result == null || result.isEmpty()) {
            return ResultDto.<List<OrderMiniGetRes>>builder()
                    .statusCode(ExceptionMsgDataSet.GET_ORDER_LIST_NON.getCode())
                    .resultMsg(ExceptionMsgDataSet.GET_ORDER_LIST_NON.getMessage()).build();
        }

        return ResultDto.<List<OrderMiniGetRes>>builder()
                .statusCode(SUCCESS_CODE)
                .resultMsg(USER_ORDER_LIST_SUCCESS)
                .resultData(result)
                .build();
    }

    @GetMapping("owner/noconfirm/list")
    @Operation(summary = "상점의 접수 전 주문정보 불러오기")
    @PreAuthorize("hasAnyRole('ADMIN','OWNER')")
    @ApiResponse(
            description =
                    "<p> 1 : 상점의 접수 전 주문정보 불러오기 완료 </p>"+
                            "<p> -8 : 상점 주인의 접근이 아닙니다 </p>"+
                            "<p> -7 : 불러올 주문 정보가 없음 </p>"
    )
    public ResultDto<List<OrderMiniGetRes>> getResNonConfirmOrderList() {
        List<OrderMiniGetRes> result = null;

        long userPk = authenticationFacade.getLoginUserPk();

        Long resPk = orderMapper.getResPkByUserPk(userPk);
        if (resPk == null) {
            return ResultDto.<List<OrderMiniGetRes>>builder()
                    .statusCode(ExceptionMsgDataSet.NO_AUTHENTICATION.getCode())
                    .resultMsg(ExceptionMsgDataSet.NO_AUTHENTICATION.getMessage())
                    .build();
        }

        try {
            result = orderService.getResNonConfirmOrderList(resPk);
        } catch (RuntimeException e) {
            return ResultDto.<List<OrderMiniGetRes>>builder()
                    .statusCode(ExceptionMsgDataSet.NO_AUTHENTICATION.getCode())
                    .resultMsg(ExceptionMsgDataSet.NO_AUTHENTICATION.getMessage())
                    .build();
        }

        if (result == null || result.isEmpty()) {
            return ResultDto.<List<OrderMiniGetRes>>builder()
                    .statusCode(ExceptionMsgDataSet.GET_ORDER_LIST_NON.getCode())
                    .resultMsg(ExceptionMsgDataSet.GET_ORDER_LIST_NON.getMessage())
                    .build();
        }

        return ResultDto.<List<OrderMiniGetRes>>builder()
                .statusCode(SUCCESS_CODE)
                .resultMsg(RES_ORDER_NO_CONFIRM_LIST_SUCCESS)
                .resultData(result)
                .build();
    }

    @GetMapping("owner/confirm/list")
    @Operation(summary = "상점의 접수 후 주문정보 불러오기")
    @PreAuthorize("hasAnyRole('ADMIN','OWNER')")
    @ApiResponse(
            description =
                    "<p> 1 : 상점의 접수 후 주문정보 불러오기 완료 </p>"+
                            "<p> -8 : 상점 주인의 접근이 아닙니다 </p>"+
                            "<p> -7 : 불러올 주문 정보가 없음 </p>"
    )
    public ResultDto<List<OrderMiniGetRes>> getResConfirmOrderList() {
        List<OrderMiniGetRes> result = null;

        long userPk = authenticationFacade.getLoginUserPk();

        Long resPk = orderMapper.getResPkByUserPk(userPk);
        if (resPk == null) {
            return ResultDto.<List<OrderMiniGetRes>>builder()
                    .statusCode(ExceptionMsgDataSet.NO_AUTHENTICATION.getCode())
                    .resultMsg(ExceptionMsgDataSet.NO_AUTHENTICATION.getMessage())
                    .build();
        }

        try {
            result = orderService.getResConfirmOrderList(resPk);
        } catch (Exception e) {
            return ResultDto.<List<OrderMiniGetRes>>builder()
                    .statusCode(ExceptionMsgDataSet.NO_AUTHENTICATION.getCode())
                    .resultMsg(ExceptionMsgDataSet.NO_AUTHENTICATION.getMessage())
                    .build();
        }

        if (result == null || result.isEmpty()) {
            return ResultDto.<List<OrderMiniGetRes>>builder()
                    .statusCode(ExceptionMsgDataSet.GET_ORDER_LIST_NON.getCode())
                    .resultMsg(ExceptionMsgDataSet.GET_ORDER_LIST_NON.getMessage())
                    .build();
        }

        return ResultDto.<List<OrderMiniGetRes>>builder()
                .statusCode(SUCCESS_CODE)
                .resultMsg(RES_ORDER_CONFIRM_LIST_SUCCESS)
                .resultData(result)
                .build();
    }

    @GetMapping("{order_pk}")
    @Operation(summary = "주문정보 상세보기")
    @ApiResponse(
            description =
                    "<p> 1 : 주문정보 상세보기 완료 </p>"+
                            "<p> -6 : 주문 정보 불러오기 실패 </p>"+
                            "<p> -7 : 불러올 주문 정보가 없음 </p>"
    )
    public ResultDto<OrderGetRes> getOrderInfo(@PathVariable("order_pk") Long orderPk) {
        OrderGetRes result = null;



        try {
            result = orderService.getOrderInfo(orderPk);
        } catch (IllegalArgumentException e) {
            return ResultDto.<OrderGetRes>builder()
                    .statusCode(ExceptionMsgDataSet.NO_AUTHENTICATION.getCode())
                    .resultMsg(ExceptionMsgDataSet.NO_AUTHENTICATION.getMessage())
                    .build();
        } catch (Exception e) {
            return ResultDto.<OrderGetRes>builder()
                    .statusCode(ExceptionMsgDataSet.GET_ORDER_LIST_FAIL.getCode())
                    .resultMsg(ExceptionMsgDataSet.GET_ORDER_LIST_FAIL.getMessage())
                    .build();
        }

        if (result == null) {
            return ResultDto.<OrderGetRes>builder()
                    .statusCode(ExceptionMsgDataSet.GET_ORDER_LIST_NON.getCode())
                    .resultMsg(ExceptionMsgDataSet.GET_ORDER_LIST_NON.getMessage())
                    .build();
        }

        return ResultDto.<OrderGetRes>builder()
                .statusCode(SUCCESS_CODE)
                .resultMsg(ORDER_INFO_SUCCESS)
                .resultData(result)
                .build();
    }

    @PatchMapping("owner/confirm/{order_pk}")
    @Operation(summary = "주문 접수하기")
    @PreAuthorize("hasAnyRole('ADMIN', 'OWNER')")
    @ApiResponse(
            description =
                    "<p> 1 : 주문 접수 완료 </p>"+
                            "<p> -8 : 상점 주인의 접근이 아닙니다 </p>"
    )
    public ResultDto<Integer> confirmOrder(@PathVariable("order_pk") Long orderPk){
        Integer result = -1;

        try {
            result = orderService.confirmOrder(orderPk);
        } catch (Exception e) {
            return ResultDto.<Integer>builder()
                    .statusCode(ExceptionMsgDataSet.NO_AUTHENTICATION.getCode())
                    .resultMsg(ExceptionMsgDataSet.NO_AUTHENTICATION.getMessage())
                    .build();
        }

        return ResultDto.<Integer>builder()
                .statusCode(SUCCESS_CODE)
                .resultMsg(CONFIRM_ORDER_SUCCESS)
                .resultData(result)
                .build();
    }
}

