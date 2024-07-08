package com.green.beadalyo.gyb.restaurant;

import com.green.beadalyo.gyb.common.*;
import com.green.beadalyo.gyb.model.Restaurant;
import com.green.beadalyo.gyb.response.RestaurantDetailRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("api/restaurant")
public class RestaurantApiController
{

    private final RestaurantService service;

    @GetMapping
    @Operation(summary = "주소기반 카테고리 별 리스트 불러오기")
    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = ResultDto.class)) ,
            description =
                    "<p> 1 : 정상 </p>"+
                            "<p> -1 : 실패 </p>"+
                            "<p> -2 : 로그인 정보 획득 실패 </p>" +
                            "<p> -3 : 음식점 정보 획득 실패 </p>" +
                            "<p> -4 : 사업자 등록 번호 유효성 검사 실패 </p>" +
                            "<p> -5 : 상호 명 유효성 검사 실패 </p>"
    )
    public Result getRestaurantList(@RequestParam("category_id") Long categoryId,
                                    @RequestParam("addr_id") Long addrId,
                                    @RequestParam Integer page,
                                    @RequestParam("order_type") Integer orderType)
    {

        return null ;
    }

    @GetMapping("{seq}")
    @Operation(summary = "음식점 상세 조회")
    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = ResultDto.class)),
            description =
                    "<p> 1 : 정상 </p>" +
                            "<p> -1 : 실패 </p>" +
                            "<p> -2 : 음식점 정보 획득 실패 </p>"
    )
    public Result getRestaurant(@PathVariable Long seq)
    {

        try {
            Restaurant data = service.getRestaurantDataBySeq(seq);
            RestaurantDetailRes res = new RestaurantDetailRes(data) ;
            System.out.println(res);
            return ResultDto.<RestaurantDetailRes>builder().resultData(res).build();
        } catch (NullPointerException e) {
            return ResultError.builder().statusCode(-2).resultMsg("음식점 정보가 존재하지 않습니다.").build();
        } catch (Exception e) {
            log.error("An error occurred: ", e);
            return ResultError.builder().build();
        }
    }
}
