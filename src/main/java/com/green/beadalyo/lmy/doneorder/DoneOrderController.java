package com.green.beadalyo.lmy.doneorder;

import com.green.beadalyo.common.model.ResultDto;
import com.green.beadalyo.jhw.security.AuthenticationFacade;
import com.green.beadalyo.lmy.dataset.ExceptionMsgDataSet;
import com.green.beadalyo.lmy.doneorder.model.DoneOrderGetRes;
import com.green.beadalyo.lmy.doneorder.model.DoneOrderMiniGetRes;
import com.green.beadalyo.lmy.order.OrderMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    private final DoneOrderMapper doneOrderMapper;

    @GetMapping("user/list")
    @Operation(summary = "유저 끝난주문기록 불러오기(완료 = 1, 취소 = 2)")
    @ApiResponse(
            description =
                    "<p> 1 : 유저 끝난주문기록 불러오기 완료 </p>"+
                            "<p> -6 : 주문 정보 불러오기 실패 </p>"+
                            "<p> -7 : 불러올 주문 정보가 없음 </p>"
    )
    public ResultDto<List<DoneOrderMiniGetRes>> getDoneOrderByUserPk() {
        List<DoneOrderMiniGetRes> result = null;

        try {
            result = doneOrderService.getDoneOrderByUserPk();
        } catch (Exception e) {
            return ResultDto.<List<DoneOrderMiniGetRes>>builder()
                    .statusCode(ExceptionMsgDataSet.GET_ORDER_LIST_FAIL.getCode())
                    .resultMsg(ExceptionMsgDataSet.GET_ORDER_LIST_FAIL.getMessage())
                    .build();
        }

        if (result == null || result.isEmpty()) {
            return ResultDto.<List<DoneOrderMiniGetRes>>builder()
                    .statusCode(ExceptionMsgDataSet.GET_ORDER_LIST_NON.getCode())
                    .resultMsg(ExceptionMsgDataSet.GET_ORDER_LIST_NON.getMessage())
                    .build();
        }

        return ResultDto.<List<DoneOrderMiniGetRes>>builder().statusCode(SUCCESS_CODE).resultMsg(GET_DONE_ORDER_BY_USER_PK_SUCCESS).resultData(result).build();
    }

//    @GetMapping("user/cancel/list")
//    @Operation(summary = "유저 취소 주문기록 불러오기")
//    @ApiResponse(
//            description =
//                    "<p> 1 : 유저 취소주문기록 불러오기 완료 </p>"+
//                            "<p> -6 : 주문 정보 불러오기 실패 </p>"+
//                            "<p> -7 : 불러올 주문 정보가 없음 </p>"
//    )
//    public ResultDto<List<DoneOrderMiniGetRes>> getCancelOrderByUserPk() {
//        List<DoneOrderMiniGetRes> result = null;
//
//        try {
//            result = doneOrderService.getCancelOrderByUserPk();
//        } catch (Exception e) {
//            return ResultDto.<List<DoneOrderMiniGetRes>>builder()
//                    .statusCode(ExceptionMsgDataSet.GET_ORDER_LIST_FAIL.getCode())
//                    .resultMsg(ExceptionMsgDataSet.GET_ORDER_LIST_FAIL.getMessage())
//                    .build();
//        }
//
//        if (result == null || result.isEmpty()) {
//            return ResultDto.<List<DoneOrderMiniGetRes>>builder()
//                    .statusCode(ExceptionMsgDataSet.GET_ORDER_LIST_NON.getCode())
//                    .resultMsg(ExceptionMsgDataSet.GET_ORDER_LIST_NON.getMessage())
//                    .build();
//        }
//
//        return ResultDto.<List<DoneOrderMiniGetRes>>builder()
//                .statusCode(SUCCESS_CODE)
//                .resultMsg(GET_CANCEL_ORDER_BY_USER_PK_SUCCESS)
//                .resultData(result)
//                .build();
//    }

    @GetMapping("owner/done/list")
    @Operation(summary = "상점 완료주문기록 불러오기")
    @ApiResponse(
            description =
                    "<p> 1 : 상점 완료주문기록 불러오기 완료 </p>"+
                            "<p> -8 : 상점 주인의 접근이 아닙니다 </p>" +
                            "<p> -6 : 주문 정보 불러오기 실패 </p>"+
                            "<p> -7 : 불러올 주문 정보가 없음 </p>"
    )
    public ResultDto<List<DoneOrderMiniGetRes>> getDoneOrderByResPk() {
        List<DoneOrderMiniGetRes> result = null;

        long userPk = authenticationFacade.getLoginUserPk();

        try {
            result = doneOrderService.getDoneOrderByResPk(userPk);
        } catch (Exception e) {
            return ResultDto.<List<DoneOrderMiniGetRes>>builder()
                    .statusCode(ExceptionMsgDataSet.GET_ORDER_LIST_FAIL.getCode())
                    .resultMsg(ExceptionMsgDataSet.GET_ORDER_LIST_FAIL.getMessage())
                    .build();
        }

        if (result == null || result.isEmpty()) {
            return ResultDto.<List<DoneOrderMiniGetRes>>builder()
                    .statusCode(ExceptionMsgDataSet.GET_ORDER_LIST_NON.getCode())
                    .resultMsg(ExceptionMsgDataSet.GET_ORDER_LIST_NON.getMessage())
                    .build();
        }

        return ResultDto.<List<DoneOrderMiniGetRes>>builder()
                .statusCode(SUCCESS_CODE)
                .resultMsg(GET_DONE_ORDER_BY_RES_PK_SUCCESS)
                .resultData(result)
                .build();
    }

    @GetMapping("owner/cancel/list")
    @Operation(summary = "상점 취소주문기록 불러오기")
    @ApiResponse(
            description =
                    "<p> 1 : 상점 취소주문기록 불러오기 완료 </p>"+
                            "<p> -8 : 상점 주인의 접근이 아닙니다 </p>" +
                            "<p> -6 : 주문 정보 불러오기 실패 </p>"+
                            "<p> -7 : 불러올 주문 정보가 없음 </p>"
    )
    public ResultDto<List<DoneOrderMiniGetRes>> getCancelOrderByResPk() {
        List<DoneOrderMiniGetRes> result = null;

        long userPk = authenticationFacade.getLoginUserPk();

        Long resPk = orderMapper.getResPkByUserPk(userPk);
        if (resPk == null) {
            return ResultDto.<List<DoneOrderMiniGetRes>>builder()
                    .statusCode(ExceptionMsgDataSet.NO_AUTHENTICATION.getCode())
                    .resultMsg(ExceptionMsgDataSet.NO_AUTHENTICATION.getMessage())
                    .build();
        }

        try {
            result = doneOrderService.getCancelOrderByResPk(resPk);
        } catch (Exception e) {
            return ResultDto.<List<DoneOrderMiniGetRes>>builder()
                    .statusCode(ExceptionMsgDataSet.GET_ORDER_LIST_FAIL.getCode())
                    .resultMsg(ExceptionMsgDataSet.GET_ORDER_LIST_FAIL.getMessage())
                    .build();
        }

        if (result == null || result.isEmpty()) {
            return ResultDto.<List<DoneOrderMiniGetRes>>builder()
                    .statusCode(ExceptionMsgDataSet.GET_ORDER_LIST_NON.getCode())
                    .resultMsg(ExceptionMsgDataSet.GET_ORDER_LIST_NON.getMessage())
                    .build();
        }

        return ResultDto.<List<DoneOrderMiniGetRes>>builder()
                .statusCode(SUCCESS_CODE)
                .resultMsg(GET_CANCEL_ORDER_BY_RES_PK_SUCCESS)
                .resultData(result)
                .build();
    }

    @GetMapping("{done_order_pk}")
    @Operation(summary = "끝난 주문 상세보기")
    @ApiResponse(
            description =
                    "<p> 1 : 끝난 주문 상세보기 완료 </p>"+
                            "<p> -6 : 주문 정보 불러오기 실패 </p>"
    )
    public ResultDto<DoneOrderGetRes> getDoneOrderInfo(@PathVariable("done_order_pk") Long doneOrderPk) {
        DoneOrderGetRes result = null;

        try {
            result = doneOrderService.getDoneOrderInfo(doneOrderPk);
        } catch (Exception e) {
            return ResultDto.<DoneOrderGetRes>builder()
                    .statusCode(ExceptionMsgDataSet.GET_ORDER_LIST_FAIL.getCode())
                    .resultMsg(ExceptionMsgDataSet.GET_ORDER_LIST_FAIL.getMessage())
                    .build();
        }

        return ResultDto.<DoneOrderGetRes>builder()
                .statusCode(SUCCESS_CODE)
                .resultMsg(GET_DONE_ORDER_INFO_SUCCESS)
                .resultData(result).build();
    }


}
