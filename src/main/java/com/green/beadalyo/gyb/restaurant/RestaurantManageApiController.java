package com.green.beadalyo.gyb.restaurant;

import com.green.beadalyo.gyb.category.CategoryService;
import com.green.beadalyo.gyb.common.Result;
import com.green.beadalyo.gyb.common.exception.DataNotFoundException;
import com.green.beadalyo.gyb.common.ResultDto;
import com.green.beadalyo.gyb.common.ResultError;
import com.green.beadalyo.gyb.common.exception.DataWrongException;
import com.green.beadalyo.gyb.model.Category;
import com.green.beadalyo.gyb.model.Restaurant;
import com.green.beadalyo.gyb.model.User;
import com.green.beadalyo.gyb.request.RestaurantManagePatchReq;
import com.green.beadalyo.gyb.response.RestaurantManageRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("api/restaurant/manage")
@Tag(name = "음식점 관련 컨트롤러")
public class RestaurantManageApiController
{
    private final RestaurantService service;
    private final CategoryService categoryService;


    @GetMapping
    @Operation(summary = "로그인한 유저의 음식점 데이터 불러오기")
    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = ResultDto.class)) ,
    description =
        "<p> 1 : 정상 </p>"+
        "<p> -1 : 실패 </p>"+
        "<p> -2 : 로그인 정보 획득 실패 </p>" +
        "<p> -3 : 음식점 정보 획득 실패 </p>"
    )
    public Result getManageData()
    {

        //유저 부분 처리전까지 임시 처리
        User user = getLoginUser() ;
        //유효성 검사
        if (user == null)
            return ResultError.builder().statusCode(-2).resultMsg("유저 정보가 일치하지 않습니다.").build();
        //


        try {
            Restaurant data = service.getRestaurantData(user);

            RestaurantManageRes res = new RestaurantManageRes(data);
            return ResultDto.<RestaurantManageRes>builder().resultData(res).build();
        } catch (DataNotFoundException e) {
            return ResultError.builder().statusCode(-2).resultMsg("유저 정보가 존재하지 않습니다.").build();
        } catch (NullPointerException e) {
            return ResultError.builder().statusCode(-3).resultMsg("음식점 정보가 존재하지 않습니다.").build();
        } catch (Exception e) {
            log.error("An error occurred: ", e);
            return ResultError.builder().build();
        }

    }

    @GetMapping("state")
    @Operation(summary = "음식점 상태 변경하기()")
    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = ResultDto.class)) ,
            description =
                    "<p> 1 : 정상(영업 중 으로 변경) </p>" +
                    "<p> 2 : 정상(휴점 으로 변경) </p>" +
                    "<p> -1 : 실패 </p>"+
                    "<p> -2 : 로그인 정보 획득 실패 </p>" +
                    "<p> -3 : 음식점 정보 획득 실패 </p>" +
                    "<p> -4 : 이미 폐업 처리 됨 </p>"
    )
    public Result ToggleState()
    {
        //유저 부분 처리전까지 임시 처리
        User user = getLoginUser() ;
        //유효성 검사
        if (user == null)
            return ResultError.builder().statusCode(-2).resultMsg("유저 정보가 일치하지 않습니다.").build();

        try {
            Integer data = service.toggleState(user);

            return ResultDto.builder().statusCode(data).build();
        } catch (DataWrongException e) {
            return ResultError.builder().statusCode(-4).resultMsg("이미 폐업한 음식점 입니다.").build();
        } catch (NullPointerException e) {
            return ResultError.builder().statusCode(-3).resultMsg("음식점 정보가 존재하지 않습니다.").build();
        } catch (Exception e) {
            log.error("An error occurred: ", e);
            return ResultError.builder().build();
        }
    }

    @PutMapping
    @Operation(summary = "음식점 정보 수정 하기")
    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = ResultDto.class)) ,
            description =
                    "<p> 1 : 정상 </p>"+
                    "<p> -1 : 실패 </p>"+
                    "<p> -2 : 로그인 정보 획득 실패 </p>" +
                    "<p> -3 : 음식점 정보 획득 실패 </p>" +
                    "<p> -4 : 사업자 등록 번호 유효성 검사 실패 </p>" +
                    "<p> -5 : 상호 명 유효성 검사 실패 </p>"
    )
    public Result patchManageData(RestaurantManagePatchReq request)
    {
        //유저 부분 처리전까지 임시 처리
        User user = getLoginUser() ;
        //유효성 검사
        if (user == null)
            return ResultError.builder().statusCode(-2).resultMsg("유저 정보가 일치하지 않습니다.").build();
        //사업자 번호 유효성 검사
        String regex = "^\\d{3}-\\d{2}-\\d{5}$" ;
        if (request.getRegiNum().matches(regex))
            return ResultError.builder().statusCode(-4).resultMsg("사업자 등록 번호를 재 확인 해 주세요.").build();
        //상호명 유효성 검사
        if (request.getRestaurantName().length() < 20)
            return ResultError.builder().statusCode(-5).resultMsg("상호 명 은 20자리 까지만 허용 됩니다.").build();

        try {
            Restaurant restaurant = service.getRestaurantData(user);
            restaurant.update(request);
            service.save(restaurant);

            RestaurantManageRes res = new RestaurantManageRes(restaurant);
            return ResultDto.<RestaurantManageRes>builder().resultData(res).build();

        } catch (DataNotFoundException e) {
            return ResultError.builder().statusCode(-2).resultMsg("유저 정보가 존재하지 않습니다.").build();
        } catch (NullPointerException e) {
            return ResultError.builder().statusCode(-3).resultMsg("음식점 정보가 존재하지 않습니다.").build();
        } catch (Exception e) {
            log.error("An error occurred: ", e);
            return ResultError.builder().build();
        }

    }

    @PostMapping("pic")
    @Operation(summary = "음식점 사진 수정하기")
    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = ResultDto.class)) ,
            description =
                    "<p> 1 : 정상 </p>"+
                    "<p> -1 : 실패 </p>"+
                    "<p> -2 : 로그인 정보 획득 실패 </p>" +
                    "<p> -3 : 음식점 정보 조회 실패 </p>"

    )
    public Result updatePic(MultipartFile file)
    {
        User user = getLoginUser() ;
        //유효성 검사
        if (user == null)
            return ResultError.builder().statusCode(-2).resultMsg("유저 정보가 일치하지 않습니다.").build();

        try {
            service.updateRestaurantPic(user,file);

            return ResultDto.builder().build();
        } catch (NullPointerException e) {
            return ResultError.builder().statusCode(-3).resultMsg("음식점 정보 조회 실패").build();
        } catch (Exception e) {
            log.error("An error occurred: ", e);
            return ResultError.builder().build();
        }

    }

    @PutMapping("category")
    @Operation(summary = "음식점 카테고리 추가")
    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = ResultDto.class)) ,
            description =
                    "<p> 1 : 정상 </p>" +
                    "<p> -1 : 실패 </p>" +
                    "<p> -2 : 로그인 정보 획득 실패 </p>" +
                    "<p> -3 : 카테고리 정보 조회 실패 </p>" +
                    "<p> -4 : 음식점 정보 조회 실패 </p>"
    )
    public Result putRestaurantCategory(@RequestParam Long seq)
    {
        User user = getLoginUser() ;
        if (user == null)
            return ResultError.builder().statusCode(-2).resultMsg("유저 정보가 일치하지 않습니다.").build();

        Category cate = null ;
        Restaurant restaurant = null ;

        try {
            cate = categoryService.getCategory(seq) ;
        } catch (NullPointerException e) {
            return ResultError.builder().statusCode(-3).resultMsg("카테고리 정보 조회 실패").build();
        } catch (Exception e) {
            log.error("An error occurred: ", e);
            return ResultError.builder().build();
        }

        try {
            restaurant = service.getRestaurantData(user) ;
        } catch (NullPointerException e) {
            return ResultError.builder().statusCode(-4).resultMsg("음식점 정보 조회 실패").build();
        } catch (Exception e) {
            log.error("An error occurred: ", e);
            return ResultError.builder().build();
        }

        try {
            categoryService.InsertRestaurantCategory(restaurant,cate);
        } catch (Exception e) {
            log.error("An error occurred: ", e);
            return ResultError.builder().build();
        }

        return ResultDto.builder().build();

    }

    @DeleteMapping("category")
    @Operation(summary = "음식점 카테고리 추가")
    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = ResultDto.class)) ,
            description =
                    "<p> 1 : 정상 </p>" +
                    "<p> -1 : 실패 </p>" +
                    "<p> -2 : 로그인 정보 획득 실패 </p>" +
                    "<p> -3 : 카테고리 정보 조회 실패 </p>" +
                    "<p> -4 : 음식점 정보 조회 실패 </p>"
    )
    public Result deleteRestaurantCategory(@RequestParam Long seq)
    {
        User user = getLoginUser() ;
        if (user == null)
            return ResultError.builder().statusCode(-2).resultMsg("유저 정보가 일치하지 않습니다.").build();

        Category cate = null ;
        Restaurant restaurant = null ;

        try {
            cate = categoryService.getCategory(seq) ;
        } catch (NullPointerException e) {
            return ResultError.builder().statusCode(-3).resultMsg("카테고리 정보 조회 실패").build();
        } catch (Exception e) {
            log.error("An error occurred: ", e);
            return ResultError.builder().build();
        }

        try {
            restaurant = service.getRestaurantData(user) ;
        } catch (NullPointerException e) {
            return ResultError.builder().statusCode(-4).resultMsg("음식점 정보 조회 실패").build();
        } catch (Exception e) {
            log.error("An error occurred: ", e);
            return ResultError.builder().build();
        }

        try {
            categoryService.deleteRestaurantCategory(restaurant,cate);
        } catch (Exception e) {
            log.error("An error occurred: ", e);
            return ResultError.builder().build();
        }

        return ResultDto.builder().build();

    }



    private User getLoginUser()
    {
        //유저 부분 처리전까지 임시 처리
        return new User();
    }

}
