package com.green.beadalyo.lmy.order;

import com.green.beadalyo.common.model.ResultDto;
import com.green.beadalyo.gyb.restaurant.repository.RestaurantRepository;
import com.green.beadalyo.jhw.security.AuthenticationFacade;
import com.green.beadalyo.lmy.dataset.ExceptionMsgDataSet;
import com.green.beadalyo.lmy.doneorder.entity.DoneOrder;
import com.green.beadalyo.lmy.order.entity.Order;
import com.green.beadalyo.lmy.order.entity.OrderMenu;
import com.green.beadalyo.lmy.order.model.OrderGetRes;
import com.green.beadalyo.lmy.order.model.OrderMiniGetRes;
import com.green.beadalyo.lmy.order.model.OrderPostReq;
import com.green.beadalyo.lmy.order.repository.OrderRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


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
    private final OrderRepository orderRepository;
    private final RestaurantRepository restaurantRepository;

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
    @Transactional
    public ResultDto<Long> postOrder(@RequestBody OrderPostReq p){

        if (p.getPaymentMethod() == null) {
            return ResultDto.<Long>builder()
                    .statusCode(ExceptionMsgDataSet.PAYMENT_METHOD_ERROR.getCode())
                    .resultMsg(ExceptionMsgDataSet.PAYMENT_METHOD_ERROR.getMessage())
                    .build();
        }

        if (500 < p.getOrderRequest().length()) {
            return ResultDto.<Long>builder()
                    .statusCode(ExceptionMsgDataSet.STRING_LENGTH_ERROR.getCode())
                    .resultMsg(ExceptionMsgDataSet.STRING_LENGTH_ERROR.getMessage())
                    .build();
        }

        List<Map<String, Object>> menuList = orderService.getMenuDetails(p.getMenuPk());
        p.setOrderUserPk(authenticationFacade.getLoginUserPk());
        int totalPrice = orderService.calculateTotalPrice(p.getMenuPk());
        p.setOrderPrice(totalPrice);

        Order order = orderService.saveOrder(p);

        List<Map<String, Object>> orderMenuList = orderService.createOrderMenuList(p, menuList);

        orderService.saveOrderMenuBatch(orderMenuList, order);

        return ResultDto.<Long>builder()
                .statusCode(SUCCESS_CODE)
                .resultMsg(POST_ORDER_SUCCESS)
                .resultData(order.getOrderPk())
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
    @Transactional
    public ResultDto<Integer> cancelOrder(@PathVariable("order_pk") Long orderPk) {

        long userPk = authenticationFacade.getLoginUserPk();

        if (orderRepository.getReferenceById(orderPk).getOrderState() == 1){
            if (userPk != orderRepository.getReferenceById(orderPk).getOrderResPk().getUser() && userPk != orderRepository.getReferenceById(orderPk).getOrderUserPk().getUserPk()) {
                return ResultDto.<Integer>builder()
                        .statusCode(ExceptionMsgDataSet.NO_NON_CONFIRM_CANCEL_AUTHENTICATION.getCode())
                        .resultMsg(ExceptionMsgDataSet.NO_NON_CONFIRM_CANCEL_AUTHENTICATION.getMessage())
                        .build();
            }
        }

        if (orderRepository.getReferenceById(orderPk).getOrderState() == 2){
            if (userPk != orderRepository.getReferenceById(orderPk).getOrderResPk().getUser()) {
                return ResultDto.<Integer>builder()
                        .statusCode(ExceptionMsgDataSet.NO_CONFIRM_CANCEL_AUTHENTICATION.getCode())
                        .resultMsg(ExceptionMsgDataSet.NO_CONFIRM_CANCEL_AUTHENTICATION.getMessage()).build();
            }
        }

        Order order = orderService.getOrderEntity(orderPk);
        DoneOrder doneOrder = orderService.saveDoneOrder(order, userPk,2);

        List<OrderMenu> orderMenuList = orderService.getOrderMenuEntities(orderPk);
        orderService.saveDoneOrderMenuBatch(orderMenuList, doneOrder);

        orderService.deleteOrder(orderPk);

        return ResultDto.<Integer>builder()
                .statusCode(SUCCESS_CODE)
                .resultMsg(CANCEL_ORDER_SUCCESS)
                .resultData(1)
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

        long userPk = authenticationFacade.getLoginUserPk();
        if (userPk != orderRepository.getReferenceById(orderPk).getOrderResPk().getUser()) {
            return ResultDto.<Integer>builder()
                    .statusCode(ExceptionMsgDataSet.NO_AUTHENTICATION.getCode())
                    .resultMsg(ExceptionMsgDataSet.NO_AUTHENTICATION.getMessage()).build();
        }

        Order order = orderService.getOrderEntity(orderPk);
        DoneOrder doneOrder = orderService.saveDoneOrder(order, userPk,1);

        List<OrderMenu> orderMenuList = orderService.getOrderMenuEntities(orderPk);
        orderService.saveDoneOrderMenuBatch(orderMenuList, doneOrder);

        orderService.deleteOrder(orderPk);


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

        if (userPk != orderMapper.getResUserPkByResPk(resPk)){
            return ResultDto.<List<OrderMiniGetRes>>builder()
                    .statusCode(ExceptionMsgDataSet.NO_AUTHENTICATION.getCode())
                    .resultMsg(ExceptionMsgDataSet.NO_AUTHENTICATION.getMessage())
                    .build();
        }

        result = orderService.getResNonConfirmOrderList(resPk);


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

        if (userPk != orderMapper.getResUserPkByResPk(resPk)){
            return ResultDto.<List<OrderMiniGetRes>>builder()
                    .statusCode(ExceptionMsgDataSet.NO_AUTHENTICATION.getCode())
                    .resultMsg(ExceptionMsgDataSet.NO_AUTHENTICATION.getMessage())
                    .build();
        }

            result = orderService.getResConfirmOrderList(resPk);

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

        long userPk = authenticationFacade.getLoginUserPk();
        if (userPk != orderMapper.getOrderResUser(orderPk)
                && userPk != orderMapper.getOrderUser(orderPk)) {
            return ResultDto.<OrderGetRes>builder()
                    .statusCode(ExceptionMsgDataSet.NO_AUTHENTICATION.getCode())
                    .resultMsg(ExceptionMsgDataSet.NO_AUTHENTICATION.getMessage())
                    .build();
        }

        result = orderService.getOrderInfo(orderPk);

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

        long resUserPk = authenticationFacade.getLoginUserPk();
        if (resUserPk != orderMapper.getResUserPkByOrderPk(orderPk)) {
            return ResultDto.<Integer>builder()
                    .statusCode(ExceptionMsgDataSet.NO_AUTHENTICATION.getCode())
                    .resultMsg(ExceptionMsgDataSet.NO_AUTHENTICATION.getMessage())
                    .build();
        }

        result = orderRepository.confirmOrder(orderPk);

        return ResultDto.<Integer>builder()
                .statusCode(SUCCESS_CODE)
                .resultMsg(CONFIRM_ORDER_SUCCESS)
                .resultData(result)
                .build();
    }
}

