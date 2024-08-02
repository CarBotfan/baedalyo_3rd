package com.green.beadalyo.jhw.MenuCategory;

import com.green.beadalyo.gyb.common.Result;
import com.green.beadalyo.gyb.common.ResultDto;
import com.green.beadalyo.gyb.common.exception.DataNotFoundException;
import com.green.beadalyo.gyb.model.Restaurant;
import com.green.beadalyo.gyb.restaurant.RestaurantService;
import com.green.beadalyo.jhw.MenuCategory.model.GetMenuCategoryRes;
import com.green.beadalyo.jhw.MenuCategory.model.MenuCategory;
import com.green.beadalyo.jhw.MenuCategory.model.MenuCategoryInsertDto;
import com.green.beadalyo.jhw.MenuCategory.model.PostMenuCategoryReq;
import com.green.beadalyo.jhw.security.AuthenticationFacade;
import com.green.beadalyo.jhw.user.UserServiceImpl;
import com.green.beadalyo.jhw.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("api/menu_category")
public class MenuCategoryController {
    private final MenuCategoryService service;
    private final RestaurantService restaurantService;
    private final UserServiceImpl userService;
    private final AuthenticationFacade authenticationFacade;

    @PostMapping
    public ResultDto<Integer> postMenuCat(@RequestBody PostMenuCategoryReq p) {
        int result = 0;
        int statusCode = 1;
        String msg = "카테고리 등록 완료";
        try {
            User user = userService.getUser(authenticationFacade.getLoginUserPk());
            Restaurant restaurant = restaurantService.getRestaurantData(user);
            MenuCategoryInsertDto dto = new MenuCategoryInsertDto();
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
    @GetMapping("/list")
    public ResultDto<List<GetMenuCategoryRes>> getMenuCatResList() {
        List<GetMenuCategoryRes> result = new ArrayList<>();
        int statusCode = 1;
        String msg = "카테고리 목록 조회 성공";
        try {
            User user = userService.getUser(authenticationFacade.getLoginUserPk());
            Restaurant restaurant = restaurantService.getRestaurantData(user);
            List<MenuCategory> list = service.getMenuCatList(restaurant);
            for(MenuCategory menuCat : list) {
                result.add(new GetMenuCategoryRes(menuCat));
            }
        } catch(DataNotFoundException e) {
            msg = "식당 정보를 찾을 수 없음";
            statusCode = -2;
        } catch (Exception e) {
            e.printStackTrace();
            msg = e.getMessage();
            statusCode = -1;
        }
        return ResultDto.<List<GetMenuCategoryRes>>builder()
                .statusCode(statusCode)
                .resultMsg(msg)
                .resultData(result).build();
    }
    @GetMapping("/{position}")
    public ResultDto<GetMenuCategoryRes> getMenuCat(@PathVariable(name = "position") Long position) {
        int statusCode = 1;
        GetMenuCategoryRes result = null;
        String msg = "카테고리 조회 완료";
        try {
            User user = userService.getUser(authenticationFacade.getLoginUserPk());
            Restaurant restaurant = restaurantService.getRestaurantData(user);
            result = new GetMenuCategoryRes(service.getMenuCat(restaurant, position));

        } catch(DataNotFoundException e) {
            msg = "식당 정보를 찾을 수 없음";
            statusCode = -2;
        } catch (Exception e) {
            e.printStackTrace();
            msg = e.getMessage();
            statusCode = -1;
        }
        return ResultDto.<GetMenuCategoryRes>builder()
                .statusCode(statusCode)
                .resultMsg(msg)
                .resultData(result).build();
    }
    @DeleteMapping("/{position}")
    public ResultDto<Integer> deleteMenuCat(@PathVariable(name = "position") Long position) {
        int statusCode = 1;
        int result = 0;
        String msg = "카테고리 삭제 완료";
        try {
            User user = userService.getUser(authenticationFacade.getLoginUserPk());
            Restaurant restaurant = restaurantService.getRestaurantData(user);
            result = service.deleteMenuCat(restaurant, position);

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
