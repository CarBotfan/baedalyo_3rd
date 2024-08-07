package com.green.beadalyo.gyb.restaurant;

import com.green.beadalyo.gyb.category.CategoryService;
import com.green.beadalyo.gyb.common.*;
import com.green.beadalyo.gyb.model.Category;
import com.green.beadalyo.gyb.model.RestaurantDetailView;
import com.green.beadalyo.gyb.model.RestaurantListView;
import com.green.beadalyo.gyb.response.CategoryRes;
import com.green.beadalyo.gyb.response.RestaurantDetailRes;
import com.green.beadalyo.gyb.response.RestaurantListRes;
import com.green.beadalyo.jhw.security.AuthenticationFacade;

import com.green.beadalyo.jhw.useraddr.UserAddrServiceImpl;
import com.green.beadalyo.jhw.useraddr.model.UserAddrGetRes;
import com.green.beadalyo.kdh.menu.MenuService;
import com.green.beadalyo.kdh.menu.model.GetAllMenuReq;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("api/restaurant")
@Tag(name = "음식점 컨트롤러")
public class RestaurantApiController
{

    private final RestaurantService service;
    private final CategoryService categoryService ;
    private final MenuService menuService ;
    private final UserAddrServiceImpl userAddrService ;
    private final AuthenticationFacade authenticationFacade;

    @GetMapping
    @Operation(summary = "주소기반 카테고리 별 리스트 불러오기")
    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = ResultDto.class)) ,
            description =
                    "<p> 1 : 정상 </p>"+
                            "<p> -1 : 실패 </p>"+
                            "<p> -2 : 비 로그인시 addrX, addrY값 필수 </p>" +
                            "<p> -3 : 주소 정보 획득 실패 </p>"
    )
    public Result getRestaurantList(@Nullable @RequestParam("category_id") Long categoryId,
                                    @Nullable @RequestParam Integer page,
                                    @Nullable @RequestParam("order_type") Integer orderType,
                                    @Nullable @RequestParam String addrX,
                                    @Nullable @RequestParam String addrY)
    {
        //유효성 검증
        if (categoryId == null || categoryId < 0)
            categoryId = 0L;
        if (page == null || page < 1)
            page = 1;
        if (orderType == null || orderType < 1)
            orderType = 1;
        if (authenticationFacade.getLoginUserPk() == 0 && (addrX == null || addrY == null))
            return ResultError.builder().statusCode(-2).resultMsg("위경도의 정보를 획득하지 못하였습니다. 로그인 하거나 addrX, addrY의 값을 입력해 주세요.").build();

        try {
            BigDecimal x = null ;
            BigDecimal y = null ;

            if (addrX != null && addrY != null  ) {
                x = BigDecimal.valueOf(Double.parseDouble(Objects.requireNonNull(addrX))); ;
                y = BigDecimal.valueOf(Double.parseDouble(Objects.requireNonNull(addrY))); ;
            } else {
                UserAddrGetRes addr = userAddrService.getMainUserAddr() ;
                if (addr == null)
                    return ResultError.builder().resultMsg("메인 주소의 정보 획득을 실패 했습니다.").statusCode(-3).build();
                x = addr.getAddrCoorX() ;
                y = addr.getAddrCoorY() ;
            }

            Page<RestaurantListView> pageList = service.getRestaurantByCategory(categoryId,x,y,orderType,page) ;
            ResultPage<RestaurantListRes> data = RestaurantListRes.toResultPage(pageList) ;
            return ResultDto.builder().resultData(data).build();

        } catch (Exception e) {
            log.error("An error occurred: ", e);
            return ResultError.builder().build();
        }

    }

//    @GetMapping
//    @Operation(summary = "주소기반 팔로우한 상점 리스트 불러오기")
//    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = ResultDto.class)) ,
//            description =
//                    "<p> 1 : 정상 </p>"+
//                            "<p> -1 : 실패 </p>"+
//                            "<p> -2 : 비 로그인시 addrX, addrY값 필수 </p>" +
//                            "<p> -3 : 주소 정보 획득 실패 </p>"
//    )
//    public Result getFollowRestaurantList(@Nullable @RequestParam("category_id") Long categoryId,
//                                    @Nullable @RequestParam Integer page,
//                                    @Nullable @RequestParam String addrX,
//                                    @Nullable @RequestParam String addrY)
//    {
//
//
//        //유효성 검증
//        if (categoryId == null || categoryId < 0)
//            categoryId = 0L;
//        if (page == null || page < 1)
//            page = 1;
//        if (authenticationFacade.getLoginUserPk() == 0 && (addrX == null || addrY == null))
//            return ResultError.builder().statusCode(-2).resultMsg("위경도의 정보를 획득하지 못하였습니다. 로그인 하거나 addrX, addrY의 값을 입력해 주세요.").build();
//
//        try {
//            BigDecimal x = null ;
//            BigDecimal y = null ;
//
//            if (addrX != null && addrY != null  ) {
//                x = BigDecimal.valueOf(Double.parseDouble(Objects.requireNonNull(addrX))); ;
//                y = BigDecimal.valueOf(Double.parseDouble(Objects.requireNonNull(addrY))); ;
//            } else {
//                UserAddrGetRes addr = userAddrService.getMainUserAddr() ;
//                if (addr == null)
//                    return ResultError.builder().resultMsg("메인 주소의 정보 획득을 실패 했습니다.").statusCode(-3).build();
//                x = addr.getAddrCoorX() ;
//                y = addr.getAddrCoorY() ;
//            }
//
//            Page<RestaurantListView> pageList = service.getFollowRestaurantList(categoryId,x,y,orderType,page) ;
//            ResultPage<RestaurantListRes> data = RestaurantListRes.toResultPage(pageList) ;
//            return ResultDto.builder().resultData(data).build();
//
//        } catch (Exception e) {
//            log.error("An error occurred: ", e);
//            return ResultError.builder().build();
//        }
//
//    }


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
            RestaurantDetailView data = service.getRestaurantDataViewBySeq(seq);

            RestaurantDetailRes res = new RestaurantDetailRes(data) ;
            GetAllMenuReq temp = new GetAllMenuReq(data.getRestaurantPk()) ;

//            res.setMenuList(menuService.getAllMenu(temp));
            return ResultDto.<RestaurantDetailRes>builder().resultData(res).build();
        } catch (NullPointerException e) {
            return ResultError.builder().statusCode(-2).resultMsg("음식점 정보가 존재하지 않습니다.").build();
        } catch (Exception e) {
            log.error("An error occurred: ", e);
            return ResultError.builder().build();
        }
    }

    @GetMapping("category")
    @Operation(summary = "카테고리 리스트 조회")
    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = ResultDto.class)),
            description =
                    "<p> 1 : 정상 </p>" +
                    "<p> -1 : 실패 </p>"
    )
    public Result getCategoryList()
    {
        try {
            List<Category> list = categoryService.getCategoryAll() ;
            List<CategoryRes> reslist = CategoryRes.toCategoryRes(list) ;
            return ResultDto.builder().resultData(reslist).build() ;
        } catch (Exception e) {
            log.error("An error occurred: ", e);
            return ResultError.builder().build();
        }
    }

    @GetMapping
    @Operation(summary = "주소기반 카테고리 별 리스트 불러오기")
    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = ResultDto.class)) ,
            description =
                    "<p> 1 : 정상 </p>"+
                            "<p> -1 : 실패 </p>"+
                            "<p> -2 : 비 로그인시 addrX, addrY값 필수 </p>" +
                            "<p> -3 : 주소 정보 획득 실패 </p>"
    )
    public Result getFollowList(@Nullable @RequestParam Integer page,
                                    @Nullable @RequestParam("order_type") Integer orderType,
                                    @Nullable @RequestParam String addrX,
                                    @Nullable @RequestParam String addrY)
    {
        //유효성 검증
        if (page == null || page < 1)
            page = 1;
        if (orderType == null || orderType < 1)
            orderType = 1;
        if (authenticationFacade.getLoginUserPk() == 0 && (addrX == null || addrY == null))
            return ResultError.builder().statusCode(-2).resultMsg("위경도의 정보를 획득하지 못하였습니다. 로그인 하거나 addrX, addrY의 값을 입력해 주세요.").build();

        try {
            BigDecimal x = null ;
            BigDecimal y = null ;

            if (addrX != null && addrY != null  ) {
                x = BigDecimal.valueOf(Double.parseDouble(Objects.requireNonNull(addrX))); ;
                y = BigDecimal.valueOf(Double.parseDouble(Objects.requireNonNull(addrY))); ;
            } else {
                UserAddrGetRes addr = userAddrService.getMainUserAddr() ;
                if (addr == null)
                    return ResultError.builder().resultMsg("메인 주소의 정보 획득을 실패 했습니다.").statusCode(-3).build();
                x = addr.getAddrCoorX() ;
                y = addr.getAddrCoorY() ;
            }

            Page<RestaurantListView> pageList = service.getRestaurantByCategory(0L,x,y,orderType,page) ;
            ResultPage<RestaurantListRes> data = RestaurantListRes.toResultPage(pageList) ;
            return ResultDto.builder().resultData(data).build();

        } catch (Exception e) {
            log.error("An error occurred: ", e);
            return ResultError.builder().build();
        }

    }

}
