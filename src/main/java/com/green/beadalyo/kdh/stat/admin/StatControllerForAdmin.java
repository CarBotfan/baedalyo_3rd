package com.green.beadalyo.kdh.stat.admin;

import com.green.beadalyo.common.model.ResultDto;
import com.green.beadalyo.kdh.stat.admin.model.*;
import com.green.beadalyo.lmy.doneorder.model.DailyOrderCountDto;
import com.green.beadalyo.lmy.doneorder.model.DailySalesDto;
import com.green.beadalyo.lmy.doneorder.model.MonthOrderCountDto;
import com.green.beadalyo.lmy.doneorder.model.MonthSalesDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/admin/stat")
@RequiredArgsConstructor
@Tag(name = "어드민 통계 컨트롤러입니다.")
public class StatControllerForAdmin {
    private final StatServiceForAdmin service;

    @GetMapping("month_sales")
    @PreAuthorize("hasRole(ADMIN)")
    @Operation(summary = "가게의 월 매출을 불러옵니다." , description = "date의 양식은 2024 입니다.\n" +
            "<p> 1 : 월 매출 불러오기 완료 </p>"+
            "<p> -1 : 월 매출 불러오기 실패 </p>"+
            "<p> -2 : 존재하지 않는 가게입니다.</p>")
    public ResultDto<List<MonthSalesDto>> getMonthSales(@ParameterObject @ModelAttribute GetRestaurantStatForAdminReq p){
        List<MonthSalesDto> result = null;


        boolean checkRestaurant = service.checkRestaurant(p.getResPk());
        if (!checkRestaurant){
            return ResultDto.<List<MonthSalesDto>>builder()
                    .statusCode(-2)
                    .resultMsg("존재하지 않는 가게입니다.")
                    .resultData(result)
                    .build();
        }
        int code = 1;
        try {
            result = service.getMonthSalesForAdmin(p);
        } catch (Exception e){

            return ResultDto.<List<MonthSalesDto>>builder()
                    .statusCode(-1)
                    .resultMsg("가게 월 매출 불러오기 실패")
                    .resultData(result)
                    .build();
        }
        return ResultDto.<List<MonthSalesDto>>builder()
                .statusCode(1)
                .resultMsg("가게 월 매출 불러오기 완료")
                .resultData(result)
                .build();

    }

    @GetMapping("month_order_count")
    @PreAuthorize("hasRole(ADMIN)")
    @Operation(summary = "가게의 월 주문수를 불러옵니다." , description = "date의 양식은 2024 입니다.\n" +
            "<p> 1 : 월 주문 수 불러오기 완료 </p>"+
            "<p> -1 : 월 주문 수 불러오기 실패 </p>"+
            "<p> -2 : 존재하지 않는 가게입니다.</p>")
    public ResultDto<List<MonthOrderCountDto>> getMonthOrderCount(@ParameterObject @ModelAttribute GetRestaurantStatForAdminReq p){
        List<MonthOrderCountDto> result = null;

        boolean checkRestaurant = service.checkRestaurant(p.getResPk());
        if (!checkRestaurant){
            return ResultDto.<List<MonthOrderCountDto>>builder()
                    .statusCode(-2)
                    .resultMsg("존재하지 않는 가게입니다.")
                    .resultData(result)
                    .build();
        }
        try {
            result = service.getMonthOrderCountForAdmin(p);
        }
        catch (Exception e){
            return ResultDto.<List<MonthOrderCountDto>>builder()
                    .statusCode(-1)
                    .resultMsg("가게 월 주문수 불러오기 실패")
                    .resultData(result)
                    .build();
        }

        return ResultDto.<List<MonthOrderCountDto>>builder()
                .statusCode(1)
                .resultMsg("가게 월 주문수 불러오기 완료")
                .resultData(result)
                .build();
    }

    @GetMapping("daily_sales")
    @PreAuthorize("hasRole(ADMIN)")
    @Operation(summary = "가게의 일일 매출을 불러옵니다." , description = "date의 양식은 2024-07 입니다.\n" +
            "<p> 1 : 일일 매출 불러오기 완료 </p>"+
            "<p> -1 : 일일 매출 불러오기 실패 </p>"+
            "<p> -2 : 존재하지 않는 가게입니다.</p>")
    public ResultDto<List<DailySalesDto>> getDailySales(@ParameterObject @ModelAttribute GetRestaurantStatForAdminReq p){
        List<DailySalesDto> result = null;

        boolean checkRestaurant = service.checkRestaurant(p.getResPk());
        if (!checkRestaurant){
            return ResultDto.<List<DailySalesDto>>builder()
                    .statusCode(-2)
                    .resultMsg("존재하지 않는 가게입니다.")
                    .resultData(result)
                    .build();
        }

        try {
            result = service.getDailySalesForAdmin(p);
        }
        catch (Exception e){

            return ResultDto.<List<DailySalesDto>>builder()
                    .statusCode(-1)
                    .resultMsg("가게 일일 매출 불러오기 실패")
                    .resultData(result)
                    .build();
        }

        return ResultDto.<List<DailySalesDto>>builder()
                .statusCode(1)
                .resultMsg("가게 일일 매출 불러오기 완료")
                .resultData(result)
                .build();
    }

    @GetMapping("daily_order_count")
    @PreAuthorize("hasRole(ADMIN)")
    @Operation(summary = "가게의 일일 주문 수를 불러옵니다." , description = "date의 양식은 2024-07 입니다.\n" +
            "<p> 1 : 일일 주문 수 불러오기 완료 </p>"+
            "<p> -1 : 일일 주문 수 불러오기 실패 </p>"+
            "<p> -2 : 존재하지 않는 가게입니다.</p>")
    public ResultDto<List<DailyOrderCountDto>> getDailyOrderCount(@ParameterObject @ModelAttribute GetRestaurantStatForAdminReq p){
        List<DailyOrderCountDto> result = null;

        boolean checkRestaurant = service.checkRestaurant(p.getResPk());
        if (!checkRestaurant){
            return ResultDto.<List<DailyOrderCountDto>>builder()
                    .statusCode(-2)
                    .resultMsg("존재하지 않는 가게입니다.")
                    .resultData(result)
                    .build();
        }

        try {
            result = service.getDailyOrderCountForAdmin(p);
        }
        catch (Exception e){

            return ResultDto.<List<DailyOrderCountDto>>builder()
                    .statusCode(-1)
                    .resultMsg("가게 일일 주문수 불러오기 실패")
                    .resultData(result)
                    .build();
        }

        return ResultDto.<List<DailyOrderCountDto>>builder()
                .statusCode(1)
                .resultMsg("가게 일일 주문수 불러오기 성공")
                .resultData(result)
                .build();
    }

    @GetMapping("daily_sign_up_count")
    @PreAuthorize("hasRole(ADMIN)")
    @Operation(summary = "일일 회원가입수를 불러옵니다." , description = "date의 양식은 2024-07 입니다.\n" +
            "<p> 1 : 일일 회원가입수 불러오기 완료 </p>"+
            "<p> -1 : 일일 회원가입수 불러오기 실패 </p>")
    public ResultDto<List<GetDailySignUpCount>> getDailySignUpCount(@ParameterObject @ModelAttribute GetSignStatForAdminReq p){
        List<GetDailySignUpCount> result = null;

        try {
            result = service.getDailySignUpCount(p.getDate());
        }
        catch (Exception e){

            return ResultDto.<List<GetDailySignUpCount>>builder()
                    .statusCode(-1)
                    .resultMsg("일일 회원가입수 불러오기 실패")
                    .resultData(result)
                    .build();
        }

        return ResultDto.<List<GetDailySignUpCount>>builder()
                .statusCode(1)
                .resultMsg("일일 회원가입수 불러오기 성공")
                .resultData(result)
                .build();
    }
    @GetMapping("month_sign_up_count")
    @PreAuthorize("hasRole(ADMIN)")
    @Operation(summary = "달별 회원가입수를 불러옵니다." , description = "date의 양식은 2024 입니다.\n" +
            "<p> 1 : 달별 회원가입수 불러오기 완료 </p>"+
            "<p> -1 : 달별 회원가입수 불러오기 실패 </p>")
    public ResultDto<List<GetMonthSignUpCount>> getMonthSignUpCount(@ParameterObject @ModelAttribute GetSignStatForAdminReq p){
        List<GetMonthSignUpCount> result = null;

        try {
            result = service.getMonthSignUpCount(p.getDate());
        }
        catch (Exception e){

            return ResultDto.<List<GetMonthSignUpCount>>builder()
                    .statusCode(-1)
                    .resultMsg("달별 회원가입수 불러오기 실패")
                    .resultData(result)
                    .build();
        }

        return ResultDto.<List<GetMonthSignUpCount>>builder()
                .statusCode(1)
                .resultMsg("달별 회원가입수 불러오기 성공")
                .resultData(result)
                .build();
    }

    @GetMapping("daily_sign_out_count")
    @PreAuthorize("hasRole(ADMIN)")
    @Operation(summary = "일일 회원탈퇴수를 불러옵니다." , description = "date의 양식은 2024-07 입니다.\n" +
            "<p> 1 : 일일 회원탈퇴수 불러오기 완료 </p>"+

            "<p> -1 : 일일 회원탈퇴수 불러오기 실패 </p>")
    public ResultDto<List<GetDailySignOutCount>> getDailySignOutCount(@ParameterObject @ModelAttribute GetSignStatForAdminReq p){
        List<GetDailySignOutCount> result = null;

        try {
            result = service.getDailySignOutCount(p.getDate());
        }
        catch (Exception e){

            return ResultDto.<List<GetDailySignOutCount>>builder()
                    .statusCode(-1)
                    .resultMsg("일일 회원탈퇴수 불러오기 실패")
                    .resultData(result)
                    .build();
        }

        return ResultDto.<List<GetDailySignOutCount>>builder()
                .statusCode(1)
                .resultMsg("일일 회원탈퇴수 불러오기 성공")
                .resultData(result)
                .build();
    }

    @GetMapping("month_sign_out_count")
    @PreAuthorize("hasRole(ADMIN)")
    @Operation(summary = "달별 회원탈퇴수를 불러옵니다." , description = "date의 양식은 2024 입니다.\n" +
            "<p> 1 : 달별 회원탈퇴수 불러오기 완료 </p>"+
            "<p> -1 : 달별 회원탈퇴수 불러오기 실패 </p>")
    public ResultDto<List<GetMonthSignOutCount>> getMonthSignOutCount(@ParameterObject @ModelAttribute GetSignStatForAdminReq p){
        List<GetMonthSignOutCount> result = null;

        try {
            result = service.getMonthSignOutCount(p.getDate());
        } catch (Exception e){

            return ResultDto.<List<GetMonthSignOutCount>>builder()
                    .statusCode(-1)
                    .resultMsg("일일 회원가입수 불러오기 실패")
                    .resultData(result)
                    .build();
        }

        return ResultDto.<List<GetMonthSignOutCount>>builder()
                .statusCode(1)
                .resultMsg("일일 회원가입수 불러오기 성공")
                .resultData(result)
                .build();
    }

}
