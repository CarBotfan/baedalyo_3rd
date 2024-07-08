package com.green.beadalyo.kdh.menu;

import com.green.beadalyo.common.model.ResultDto;
import com.green.beadalyo.kdh.stat.StatService;
import com.green.beadalyo.kdh.stat.model.*;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/stat")
@RequiredArgsConstructor
public class StatController {
    private final StatService service;

    @GetMapping("review_count")
    @Operation(summary = "가게의 리뷰 갯수를 불러옵니다.." , description = "resPk는 가게의 PK(고유번호)입니다.")
    public ResultDto<GetReviewCountRes> getReviewCount(@ParameterObject @ModelAttribute GetReviewStatReq p){
        GetReviewCountRes result = null;

        String msg = "가게 리뷰 갯수 불러오기 완료";
        int code = 2;
        try {
            result = service.getReviewCount(p);
        } catch (Exception e){
            msg = e.getMessage();
            code = 4;
        }

        return ResultDto.<GetReviewCountRes>builder()
                .statusCode(code)
                .resultMsg(msg)
                .resultData(result)
                .build();
    }

    @GetMapping("review_avg")
    @Operation(summary = "가게의 별점 평균을 불러옵니다." , description = "resPk는 가게의 PK(고유번호)입니다.")
    public ResultDto<GetReviewAvgRes> getReviewAvg(@ParameterObject @ModelAttribute GetReviewStatReq p){
        GetReviewAvgRes result = null;

        String msg = "가게 별점 평균 불러오기 완료";
        int code = 2;
        try {
            result = service.getReviewAvg(p);
        } catch (Exception e){
            msg = e.getMessage();
            code = 4;
        }

        return ResultDto.<GetReviewAvgRes>builder()
                .statusCode(code)
                .resultMsg(msg)
                .resultData(result)
                .build();
    }

    @GetMapping("month_sales")
    @Operation(summary = "가게의 월 매출을 불러옵니다." , description = "req의 양식은 2024-06 입니다.\n" +
                                                                     "resPk는 가게의 PK(고유번호)입니다.")
    public ResultDto<GetMonthSaleRes> getMonthSales(@ParameterObject @ModelAttribute GetDateReq p){
        GetMonthSaleRes result = null;

        String msg = "가게 월 매출 불러오기 완료";
        int code = 2;
        try {
            result = service.getMonthSales(p);
        } catch (Exception e){
            msg = e.getMessage();
            code = 4;
        }

        return ResultDto.<GetMonthSaleRes>builder()
                .statusCode(code)
                .resultMsg(msg)
                .resultData(result)
                .build();
    }

    @GetMapping("month_order_count")
    @Operation(summary = "가게의 월 주문수를 불러옵니다." , description = "req의 양식은 2024-06 입니다.\n" +
                                                                        "resPk는 가게의 PK(고유번호)입니다.")
    public ResultDto<GetMonthOrderCountRes> getMonthOrderCount(@ParameterObject @ModelAttribute GetDateReq p){
        GetMonthOrderCountRes result = null;

        String msg = "가게 월 주문수 불러오기 완료";
        int code = 2;
        try {
            result = service.getMonthOrderCount(p);
        } catch (Exception e){
            msg = e.getMessage();
            code = 4;
        }

        return ResultDto.<GetMonthOrderCountRes>builder()
                .statusCode(code)
                .resultMsg(msg)
                .resultData(result)
                .build();
    }

    @GetMapping("daily_sales")
    @Operation(summary = "가게의 일일 매출을 불러옵니다." , description = "req의 양식은 2024-07-07 입니다.\n" +
                                                                        "resPk는 가게의 PK(고유번호)입니다.")
    public ResultDto<GetDailySalesRes> getDailySales(@ParameterObject @ModelAttribute GetDateReq p){
        GetDailySalesRes result = null;

        String msg = "가게 일일 매출 불러오기 완료";
        int code = 2;
        try {
            result = service.getDailySales(p);
        } catch (Exception e){
            msg = e.getMessage();
            code = 4;
        }

        return ResultDto.<GetDailySalesRes>builder()
                .statusCode(code)
                .resultMsg(msg)
                .resultData(result)
                .build();
    }

    @GetMapping("daily_order_count")
    @Operation(summary = "가게의 일일 주문 수를 불러옵니다." , description = "req의 양식은 2024-07-07 입니다.\n" +
                                                                         "resPk는 가게의 PK(고유번호)입니다.")
    public ResultDto<GetDailyOrderCountRes> getdailyOrderCount(@ParameterObject @ModelAttribute GetDateReq p){
        GetDailyOrderCountRes result = null;

        String msg = "가게 일일 주문 수 불러오기 완료";
        int code = 2;
        try {
            result = service.getDailyOrderCount(p);
        } catch (Exception e){
            msg = e.getMessage();
            code = 4;
        }

        return ResultDto.<GetDailyOrderCountRes>builder()
                .statusCode(code)
                .resultMsg(msg)
                .resultData(result)
                .build();
    }
}

