package com.green.beadalyo.lmy.doneorder;

import com.green.beadalyo.common.model.ResultDto;
import com.green.beadalyo.lmy.doneorder.model.DoneOrderGetRes;
import com.green.beadalyo.lmy.doneorder.model.DoneOrderMiniGetRes;
import com.green.beadalyo.lmy.order.model.OrderGetRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/done/")
@RequiredArgsConstructor
@Slf4j
public class DoneOrderController {
    private final DoneOrderService doneOrderService;

    @GetMapping("user/done")
    public ResultDto<List<DoneOrderMiniGetRes>> getDoneOrderByUserPk(@RequestParam Long userPk) {
        int code = 2;
        String msg = "유저 완료 주문기록 불러오기 완료";
        List<DoneOrderMiniGetRes> result = null;

        try {
            result = doneOrderService.getDoneOrderByUserPk(userPk);
        } catch (Exception e) {
            code = 4;
            msg = e.getMessage();
        }

        return ResultDto.<List<DoneOrderMiniGetRes>>builder()
                .statusCode(code)
                .resultMsg(msg)
                .resultData(result)
                .build();
    }

    @GetMapping("user/cancel")
    public ResultDto<List<DoneOrderMiniGetRes>> getCancelOrderByUserPk(@RequestParam Long userPk) {
        int code = 2;
        String msg = "유저 취소 주문기록 불러오기 완료";
        List<DoneOrderMiniGetRes> result = null;

        try {
            result = doneOrderService.getCancelOrderByUserPk(userPk);
        } catch (Exception e) {
            code = 4;
            msg = e.getMessage();
        }

        return ResultDto.<List<DoneOrderMiniGetRes>>builder()
                .statusCode(code)
                .resultMsg(msg)
                .resultData(result)
                .build();
    }

    @GetMapping("res/done")
    public ResultDto<List<DoneOrderMiniGetRes>> getDoneOrderByResPk(@RequestParam Long resPk) {
        int code = 2;
        String msg = "상점 완료 주문기록 불러오기 완료";
        List<DoneOrderMiniGetRes> result = null;

        try {
            result = doneOrderService.getDoneOrderByResPk(resPk);
        } catch (Exception e) {
            code = 4;
            msg = e.getMessage();
        }

        return ResultDto.<List<DoneOrderMiniGetRes>>builder()
                .statusCode(code)
                .resultMsg(msg)
                .resultData(result)
                .build();
    }

    @GetMapping("res/cancel")
    public ResultDto<List<DoneOrderMiniGetRes>> getCancelOrderByResPk(@RequestParam Long resPk) {
        int code = 2;
        String msg = "상점 완료 주문기록 불러오기 완료";
        List<DoneOrderMiniGetRes> result = null;

        try {
            result = doneOrderService.getCancelOrderByResPk(resPk);
        } catch (Exception e) {
            code = 4;
            msg = e.getMessage();
        }

        return ResultDto.<List<DoneOrderMiniGetRes>>builder()
                .statusCode(code)
                .resultMsg(msg)
                .resultData(result)
                .build();
    }

    @GetMapping
    public ResultDto<DoneOrderGetRes> getDoneOrderInfo(@RequestParam Long doneOrderPk) {
        int code = 2;
        String msg = "주문 상세보기 완료";
        DoneOrderGetRes result = null;

        try {
            result = doneOrderService.getDoneOrderInfo(doneOrderPk);
        } catch (Exception e) {
            code = 4;
            msg = e.getMessage();
        }

        return ResultDto.<DoneOrderGetRes>builder()
                .statusCode(code)
                .resultMsg(msg)
                .resultData(result)
                .build();
    }



}
