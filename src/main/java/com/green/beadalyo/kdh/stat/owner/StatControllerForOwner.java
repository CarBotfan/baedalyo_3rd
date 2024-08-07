package com.green.beadalyo.kdh.stat.owner;

import com.green.beadalyo.common.model.ResultDto;
import com.green.beadalyo.gyb.restaurant.repository.RestaurantRepository;
import com.green.beadalyo.jhw.security.AuthenticationFacade;
import com.green.beadalyo.jhw.user.repository.UserRepository;
import com.green.beadalyo.kdh.stat.owner.model.GetDateReq;
import com.green.beadalyo.lmy.doneorder.model.DailyOrderCountDto;
import com.green.beadalyo.lmy.doneorder.model.DailySalesDto;
import com.green.beadalyo.lmy.doneorder.model.MonthOrderCountDto;
import com.green.beadalyo.lmy.doneorder.model.MonthSalesDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/owner/stat")
@RequiredArgsConstructor
@Tag(name = "가게 통계 컨트롤러입니다.")
public class StatControllerForOwner {
    private final StatServiceForOwner service;
    private final AuthenticationFacade authenticationFacade;
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;

    //리뷰 관련 기능은 안쓰길래 폐기처분
//    @GetMapping("review_count")
//    @Operation(summary = "가게의 리뷰 갯수를 불러옵니다.." , description = "<p>res_pk는 가게의 PK(고유번호)입니다.</p>"+
//                                                                            "<p> 1 : 리뷰 갯수 불러오기 완료 </p>"+
//                                                                            "<p> -1 : 리뷰 갯수 불러오기 실패 </p>")
//    public ResultDto<GetReviewCountRes> getReviewCount(@ParameterObject @ModelAttribute GetReviewStatReq p){
//        GetReviewCountRes result = null;
//
//        String msg = "가게 리뷰 갯수 불러오기 완료";
//        int code = 1;
//        try {
//            result = service.getReviewCount(p);
//        }  catch (Exception e){
//            msg = e.getMessage();
//            code = -1;
//        }
//
//        return ResultDto.<GetReviewCountRes>builder()
//                .statusCode(code)
//                .resultMsg(msg)
//                .resultData(result)
//                .build();
//    }
//
//    @GetMapping("review_avg")
//    @Operation(summary = "가게의 별점 평균을 불러옵니다." , description = "<p>res_pk는 가게의 PK(고유번호)입니다.</p>"+
//                                                                        "<p> 1 : 별점 평균 불러오기 완료 </p>"+
//                                                                        "<p> -1 : 별점 평균 불러오기 실패 </p>")
//    public ResultDto<GetReviewAvgRes> getReviewAvg(@ParameterObject @ModelAttribute GetReviewStatReq p){
//        GetReviewAvgRes result = null;
//
//        String msg = "가게 별점 평균 불러오기 완료";
//        int code = 1;
//        try {
//            result = service.getReviewAvg(p);
//        } catch (Exception e){
//            msg = e.getMessage();
//            code = -1;
//        }
//
//        return ResultDto.<GetReviewAvgRes>builder()
//                .statusCode(code)
//                .resultMsg(msg)
//                .resultData(result)
//                .build();
//    }

    @GetMapping("month_sales")
    @Operation(summary = "가게의 월 매출을 불러옵니다." , description = "date의 양식은 2024 입니다.\n" +
                                                                    "<p> 1 : 월 매출 불러오기 완료 </p>"+
                                                                    "<p> -1 : 월 매출 불러오기 실패 </p>"+
                                                                    "<p> -2 : 사장님이 아닙니다.</p>")
    public ResultDto<List<MonthSalesDto>> getMonthSales(@ParameterObject @ModelAttribute GetDateReq p){
        List<MonthSalesDto> result = null;


        int code = 1;
        try {
            result = service.getMonthSales(p);
        } catch (RuntimeException e){
            return ResultDto.<List<MonthSalesDto>>builder()
                    .statusCode(-2)
                    .resultMsg("사장님이 아닙니다")
                    .resultData(result)
                    .build();
        }
        catch (Exception e){

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
    @Operation(summary = "가게의 월 주문수를 불러옵니다." , description = "date의 양식은 2024 입니다.\n" +
                                                                        "<p> 1 : 월 주문 수 불러오기 완료 </p>"+
                                                                        "<p> -1 : 월 주문 수 불러오기 실패 </p>"+
                                                                        "<p> -2 : 사장님이 아닙니다.</p>")
    public ResultDto<List<MonthOrderCountDto>> getMonthOrderCount(@ParameterObject @ModelAttribute GetDateReq p){
        List<MonthOrderCountDto> result = null;

        try {
            result = service.getMonthOrderCount(p);
        } catch (RuntimeException e){
            return ResultDto.<List<MonthOrderCountDto>>builder()
                    .statusCode(-2)
                    .resultMsg("사장님이 아닙니다")
                    .resultData(result)
                    .build();
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
    @Operation(summary = "가게의 일일 매출을 불러옵니다." , description = "date의 양식은 2024-07 입니다.\n" +
                                                                        "<p> 1 : 일일 매출 불러오기 완료 </p>"+
                                                                        "<p> -1 : 일일 매출 불러오기 실패 </p>"+
                                                                        "<p> -2 : 사장님이 아닙니다.</p>")
    public ResultDto<List<DailySalesDto>> getDailySales(@ParameterObject @ModelAttribute GetDateReq p){
        List<DailySalesDto> result = null;


        try {
            result = service.getDailySales(p);
        } catch (RuntimeException e){
            return ResultDto.<List<DailySalesDto>>builder()
                    .statusCode(-2)
                    .resultMsg("사장님이 아닙니다.")
                    .resultData(result)
                    .build();
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
    @Operation(summary = "가게의 일일 주문 수를 불러옵니다." , description = "date의 양식은 2024-07 입니다.\n" +
                                                                        "<p> 1 : 일일 주문 수 불러오기 완료 </p>"+
                                                                        "<p> -1 : 일일 주문 수 불러오기 실패 </p>"+
                                                                        "<p> -2 : 사장님이 아닙니다.</p>")
    public ResultDto<List<DailyOrderCountDto>> getDailyOrderCount(@ParameterObject @ModelAttribute GetDateReq p){
        List<DailyOrderCountDto> result = service.getDailyOrderCount(p);


        try {
            result = service.getDailyOrderCount(p);
        } catch (RuntimeException e){
            return ResultDto.<List<DailyOrderCountDto>>builder()
                    .statusCode(-2)
                    .resultMsg("사장님이 아닙니다.")
                    .resultData(result)
                    .build();
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
}

