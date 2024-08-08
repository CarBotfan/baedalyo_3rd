package com.green.beadalyo.jhw.menucategory;

import com.green.beadalyo.gyb.common.ResultDto;
import com.green.beadalyo.gyb.common.exception.DataNotFoundException;
import com.green.beadalyo.gyb.model.Restaurant;
import com.green.beadalyo.gyb.restaurant.RestaurantService;
import com.green.beadalyo.jhw.menucategory.exception.MenuCatNotFoundException;
import com.green.beadalyo.jhw.menucategory.model.*;
import com.green.beadalyo.jhw.security.AuthenticationFacade;
import com.green.beadalyo.jhw.user.UserServiceImpl;
import com.green.beadalyo.jhw.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Description;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("api/menu_category")
@Tag(name = "메뉴 카테고리 컨트롤러")
public class MenuCategoryController {
    private final MenuCategoryService service;
    private final RestaurantService restaurantService;
    private final UserServiceImpl userService;
    private final AuthenticationFacade authenticationFacade;

    @Operation(summary = "메뉴 카테고리 등록")
    @PostMapping
    public ResultDto<Integer> postMenuCat(@RequestBody MenuCatPostReq p) {
        int result = 0;
        int statusCode = 1;
        String msg = "카테고리 등록 완료";
        try {
            User user = userService.getUser(authenticationFacade.getLoginUserPk());
            Restaurant restaurant = restaurantService.getRestaurantData(user);
            MenuCatInsertDto dto = new MenuCatInsertDto();
            dto.setMenuCatName(p.getMenuCategoryName());
            dto.setRestaurant(restaurant);
            dto.setPosition(service.getMenuCatCount(restaurant) + 1);
            service.insertMenuCat(dto);
            result = 1;
        } catch(DataNotFoundException e) {
            msg = "식당 정보를 찾을 수 없음";
            statusCode = -2;
        } catch (Exception e) {
            e.printStackTrace();
            msg = e.getMessage();
            statusCode = -1;
        }
        return ResultDto.<Integer>builder()
                .statusCode(statusCode)
                .resultMsg(msg)
                .resultData(result).build();
    }

    @Operation(summary = "메뉴 카테고리 목록 조회")
    @GetMapping("/list")
    public ResultDto<List<MenuCatGetRes>> getMenuCatResList() {
        List<MenuCatGetRes> result = new ArrayList<>();
        int statusCode = 1;
        String msg = "카테고리 목록 조회 성공";
        try {
            User user = userService.getUser(authenticationFacade.getLoginUserPk());
            Restaurant restaurant = restaurantService.getRestaurantData(user);
            List<MenuCategory> list = service.getMenuCatList(restaurant);
            for(MenuCategory menuCat : list) {
                result.add(new MenuCatGetRes(menuCat));
            }
        } catch(DataNotFoundException e) {
            msg = "식당 정보를 찾을 수 없음";
            statusCode = -2;
        } catch(MenuCatNotFoundException e) {
            msg = e.getMessage();
            statusCode = -3;
        } catch (Exception e) {
            e.printStackTrace();
            msg = e.getMessage();
            statusCode = -1;
        }
        return ResultDto.<List<MenuCatGetRes>>builder()
                .statusCode(statusCode)
                .resultMsg(msg)
                .resultData(result).build();
    }

    @Operation(summary = "메뉴 카테고리 조회")
    @GetMapping("/{menu_cat_pk}")
    public ResultDto<MenuCatGetRes> getMenuCat(@PathVariable(name = "menu_cat_pk") Long MenuCatPk) {
        int statusCode = 1;
        MenuCatGetRes result = null;
        String msg = "카테고리 조회 완료";
        try {
            result = new MenuCatGetRes(service.getMenuCat(MenuCatPk));

        } catch(MenuCatNotFoundException e) {
            msg = e.getMessage();
            statusCode = -3;
        } catch (Exception e) {
            e.printStackTrace();
            msg = e.getMessage();
            statusCode = -1;
        }
        return ResultDto.<MenuCatGetRes>builder()
                .statusCode(statusCode)
                .resultMsg(msg)
                .resultData(result).build();
    }

    @Operation(summary = "메뉴 카테고리 삭제")
    @DeleteMapping("/{menu_cat_pk}")
    public ResultDto<Integer> deleteMenuCat(@PathVariable(name = "menu_cat_pk") Long menuCatPk) {
        int statusCode = 1;
        int result = 0;
        String msg = "카테고리 삭제 완료";
        try {
            User user = userService.getUser(authenticationFacade.getLoginUserPk());
            Restaurant restaurant = restaurantService.getRestaurantData(user);
            result = service.deleteMenuCat(menuCatPk, restaurant);

        } catch(MenuCatNotFoundException e) {
            msg = e.getMessage();
            statusCode = -3;
        } catch (Exception e) {
            e.printStackTrace();
            msg = e.getMessage();
            statusCode = -1;
        }
        return ResultDto.<Integer>builder()
                .statusCode(statusCode)
                .resultMsg(msg)
                .resultData(result).build();
    }

    @Operation(summary = "메뉴 카테고리 수정")
    @PatchMapping()
    public ResultDto<Integer> patchMenuCat(@RequestBody MenuCatPatchReq p) {
        int statusCode = 1;
        int result = 0;
        String msg = "카테고리 수정 완료";
        try {
            User user = userService.getUser(authenticationFacade.getLoginUserPk());
            Restaurant restaurant = restaurantService.getRestaurantData(user);

            MenuCatPatchDto dto = new MenuCatPatchDto(p);
            dto.setRestaurant(restaurant);
            result = service.patchMenuCat(dto);
        } catch(MenuCatNotFoundException e) {
            msg = e.getMessage();
            statusCode = -3;
        } catch(DataNotFoundException e) {
            msg = "식당 정보를 찾을 수 없음";
            statusCode = -2;
        } catch (Exception e) {
            e.printStackTrace();
            msg = e.getMessage();
            statusCode = -1;
        }
        return ResultDto.<Integer>builder()
                .statusCode(statusCode)
                .resultMsg(msg)
                .resultData(result).build();
    }

    @Operation(summary = "메뉴 카테고리 순서 변경", description = "position에 이동할 위치 입력")
    @PatchMapping("/position")
    public ResultDto<Integer> patchMenuPosition(@RequestBody MenuCatPositionPatchReq p) {
        int statusCode = 1;
        int result = 0;
        String msg = "카테고리 수정 완료";
        try {
            if(p.getPosition() <= 0) {
                throw new RuntimeException("올바르지 않은 위치입니다.");
            }
            User user = userService.getUser(authenticationFacade.getLoginUserPk());
            Restaurant restaurant = restaurantService.getRestaurantData(user);
            MenuCatPositionPatchDto dto = new MenuCatPositionPatchDto(p);
            dto.setRestaurant(restaurant);
            result = service.patchMenuCatPosition(dto);
        } catch(MenuCatNotFoundException e) {
            msg = e.getMessage();
            statusCode = -3;
        } catch(DataNotFoundException e) {
            msg = "식당 정보를 찾을 수 없음";
            statusCode = -2;
        } catch (Exception e) {
            e.printStackTrace();
            msg = e.getMessage();
            statusCode = -1;
        }
        return ResultDto.<Integer>builder()
                .statusCode(statusCode)
                .resultMsg(msg)
                .resultData(result).build();
    }
}
