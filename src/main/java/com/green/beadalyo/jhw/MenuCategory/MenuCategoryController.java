package com.green.beadalyo.jhw.MenuCategory;

import com.green.beadalyo.gyb.common.Result;
import com.green.beadalyo.gyb.common.ResultDto;
import com.green.beadalyo.gyb.common.exception.DataNotFoundException;
import com.green.beadalyo.gyb.model.Restaurant;
import com.green.beadalyo.gyb.restaurant.RestaurantService;
import com.green.beadalyo.jhw.MenuCategory.model.MenuCategory;
import com.green.beadalyo.jhw.MenuCategory.model.MenuCategoryInsertDto;
import com.green.beadalyo.jhw.MenuCategory.model.PostMenuCategoryReq;
import com.green.beadalyo.jhw.security.AuthenticationFacade;
import com.green.beadalyo.jhw.user.UserServiceImpl;
import com.green.beadalyo.jhw.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResultDto<Integer> postMenuCategory(@RequestBody PostMenuCategoryReq p) {
        User user = userService.getUser(authenticationFacade.getLoginUserPk());
        int result = 0;
        int statusCode = 1;
        String msg = "카테고리 등록 완료";
        try {
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
}
