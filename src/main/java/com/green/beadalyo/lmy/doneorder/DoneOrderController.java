package com.green.beadalyo.lmy.doneorder;

import com.green.beadalyo.common.model.ResultDto;
import com.green.beadalyo.jhw.security.AuthenticationFacade;
import com.green.beadalyo.lmy.doneorder.model.DoneOrderGetRes;
import com.green.beadalyo.lmy.doneorder.model.DoneOrderMiniGetRes;
import com.green.beadalyo.lmy.order.OrderMapper;
import com.green.beadalyo.lmy.order.model.OrderMiniGetRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.green.beadalyo.lmy.dataset.ExceptionMsgDataset.*;
import static com.green.beadalyo.lmy.dataset.ResponseDataSet.*;


@RestController
@RequestMapping("api/done/")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "끝난 주문 관련 컨트롤러")
public class DoneOrderController {
    private final DoneOrderService doneOrderService;
    private final OrderMapper orderMapper;
    private final AuthenticationFacade authenticationFacade;

    @GetMapping("user/done")
    @Operation(summary = "유저 완료주문기록 불러오기")
    @ApiResponse(
            description =
                    "<p> 1 : 유저 완료주문기록 불러오기 완료 </p>"+
                            "<p> -1 : 주문 정보 불러오기 실패 </p>"+
                            "<p> -2 : 불러올 주문 정보가 없음 </p>"
    )
    public ResultDto<List<DoneOrderMiniGetRes>> getDoneOrderByUserPk() {
        List<DoneOrderMiniGetRes> result = null;

        try {
            result = doneOrderService.getDoneOrderByUserPk();
        } catch (Exception e) {
            return ResultDto.<List<DoneOrderMiniGetRes>>builder().statusCode(-1).resultMsg(GET_ORDER_LIST_FAIL).build();
        }

        if (result == null || result.isEmpty()) {
            return ResultDto.<List<DoneOrderMiniGetRes>>builder().statusCode(-2).resultMsg(GET_ORDER_LIST_NON).build();
        }

        return ResultDto.<List<DoneOrderMiniGetRes>>builder().statusCode(SUCCESS_CODE).resultMsg(GET_DONE_ORDER_BY_USER_PK_SUCCESS).resultData(result).build();
    }

    @GetMapping("user/cancel")
    @Operation(summary = "유저 취소 주문기록 불러오기")
    @ApiResponse(
            description =
                    "<p> 1 : 유저 취소주문기록 불러오기 완료 </p>"+
                            "<p> -1 : 주문 정보 불러오기 실패 </p>"+
                            "<p> -2 : 불러올 주문 정보가 없음 </p>"
    )
    public ResultDto<List<DoneOrderMiniGetRes>> getCancelOrderByUserPk() {
        List<DoneOrderMiniGetRes> result = null;

        try {
            result = doneOrderService.getCancelOrderByUserPk();
        } catch (Exception e) {
            return ResultDto.<List<DoneOrderMiniGetRes>>builder().statusCode(-1).resultMsg(GET_ORDER_LIST_FAIL).build();
        }

        if (result == null || result.isEmpty()) {
            return ResultDto.<List<DoneOrderMiniGetRes>>builder().statusCode(-2).resultMsg(GET_ORDER_LIST_NON).build();
        }

        return ResultDto.<List<DoneOrderMiniGetRes>>builder().statusCode(SUCCESS_CODE).resultMsg(GET_CANCEL_ORDER_BY_USER_PK_SUCCESS).resultData(result).build();
    }

    @GetMapping("res/done")
    @Operation(summary = "상점 완료주문기록 불러오기")
    @ApiResponse(
            description =
                    "<p> 1 : 상점 완료주문기록 불러오기 완료 </p>"+
                            "<p> -1 : 상점 주인의 접근이 아닙니다 </p>" +
                            "<p> -2 : 주문 정보 불러오기 실패 </p>"+
                            "<p> -3 : 불러올 주문 정보가 없음 </p>"
    )
    public ResultDto<List<DoneOrderMiniGetRes>> getDoneOrderByResPk(@RequestParam("res_pk") Long resPk) {
        List<DoneOrderMiniGetRes> result = null;

        long userPk = authenticationFacade.getLoginUserPk();
        if (userPk != orderMapper.getResUserPkByResPk(resPk)) {
            return ResultDto.<List<DoneOrderMiniGetRes>>builder().statusCode(-1).resultMsg(NO_AUTHENTICATION).build();
        }

        try {
            result = doneOrderService.getDoneOrderByResPk(resPk);
        } catch (Exception e) {
            return ResultDto.<List<DoneOrderMiniGetRes>>builder().statusCode(-2).resultMsg(GET_ORDER_LIST_FAIL).build();
        }

        if (result == null || result.isEmpty()) {
            return ResultDto.<List<DoneOrderMiniGetRes>>builder().statusCode(-3).resultMsg(GET_ORDER_LIST_NON).build();
        }

        return ResultDto.<List<DoneOrderMiniGetRes>>builder().statusCode(SUCCESS_CODE).resultMsg(GET_DONE_ORDER_BY_RES_PK_SUCCESS).resultData(result).build();
    }

    @GetMapping("res/cancel")
    @Operation(summary = "상점 취소주문기록 불러오기")
    @ApiResponse(
            description =
                    "<p> 1 : 상점 취소주문기록 불러오기 완료 </p>"+
                            "<p> -1 : 상점 주인의 접근이 아닙니다 </p>" +
                            "<p> -2 : 주문 정보 불러오기 실패 </p>"+
                            "<p> -3 : 불러올 주문 정보가 없음 </p>"
    )
    public ResultDto<List<DoneOrderMiniGetRes>> getCancelOrderByResPk(@RequestParam("res_pk") Long resPk) {
        List<DoneOrderMiniGetRes> result = null;

        long userPk = authenticationFacade.getLoginUserPk();
        if (userPk != orderMapper.getResUserPkByResPk(resPk)) {
            return ResultDto.<List<DoneOrderMiniGetRes>>builder().statusCode(-1).resultMsg(NO_AUTHENTICATION).build();
        }

        try {
            result = doneOrderService.getCancelOrderByResPk(resPk);
        } catch (Exception e) {
            return ResultDto.<List<DoneOrderMiniGetRes>>builder().statusCode(-2).resultMsg(GET_ORDER_LIST_FAIL).build();
        }

        if (result == null || result.isEmpty()) {
            return ResultDto.<List<DoneOrderMiniGetRes>>builder().statusCode(-3).resultMsg(GET_ORDER_LIST_NON).build();
        }

        return ResultDto.<List<DoneOrderMiniGetRes>>builder().statusCode(SUCCESS_CODE).resultMsg(GET_CANCEL_ORDER_BY_RES_PK_SUCCESS).resultData(result).build();
    }

    @GetMapping
    @Operation(summary = "끝난 주문 상세보기")
    @ApiResponse(
            description =
                    "<p> 1 : 끝난 주문 상세보기 완료 </p>"+
                            "<p> -1 : 주문 정보 불러오기 실패 </p>"
    )
    public ResultDto<DoneOrderGetRes> getDoneOrderInfo(@RequestParam("done_order_pk") Long doneOrderPk) {
        DoneOrderGetRes result = null;

        try {
            result = doneOrderService.getDoneOrderInfo(doneOrderPk);
        } catch (Exception e) {
            return ResultDto.<DoneOrderGetRes>builder().statusCode(-1).resultMsg(GET_ORDER_LIST_FAIL).build();
        }

        return ResultDto.<DoneOrderGetRes>builder().statusCode(SUCCESS_CODE).resultMsg(GET_DONE_ORDER_INFO_SUCCESS).resultData(result).build();
    }


}
