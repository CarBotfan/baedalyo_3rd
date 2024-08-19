package com.green.beadalyo.kdh.menu;

import com.green.beadalyo.common.CustomFileUtils;
import com.green.beadalyo.common.model.ResultDto;
import com.green.beadalyo.gyb.model.Restaurant;
import com.green.beadalyo.gyb.restaurant.RestaurantService;
import com.green.beadalyo.jhw.menucategory.MenuCategoryService;
import com.green.beadalyo.jhw.menucategory.exception.MenuCatNotFoundException;
import com.green.beadalyo.jhw.security.AuthenticationFacade;
import com.green.beadalyo.jhw.user.UserServiceImpl;
import com.green.beadalyo.jhw.user.entity.User;
import com.green.beadalyo.kdh.menu.entity.MenuEntity;
import com.green.beadalyo.kdh.menu.model.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.util.List;

@RestController
@RequestMapping("api/owner/menu")
@RequiredArgsConstructor
@Tag(name = "메뉴 관련 컨트롤러입니다.(사장님 전용)")
@Slf4j
public class MenuController {
    private final MenuService service;
    private final CustomFileUtils customFileUtils;
    private final long maxSize = 3145728;
    private final UserServiceImpl userServiceImpl;
    private final AuthenticationFacade authenticationFacade;
    private final RestaurantService restaurantService;
    private final MenuCategoryService menuCategoryService;

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE
            , MediaType.MULTIPART_FORM_DATA_VALUE})
    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
    @Operation(summary = "메뉴 등록" , description = "menuPk는 등록된 메뉴의 고유 번호(PK)입니다.\n" +
                                                    "menuResPk는 메뉴가 등록된 식당의 고유 번호(PK)입니다.\n" +
                                                    "<p>menuState는 ex)1이면 판매 중 2면 품절과 같은 판매상태입니다.</p>" +
                                                    "<p> 1 : 메뉴 등록 완료 </p>"+
                                                    "<p> -1 : 메뉴 등록 실패 </p>"+
                                                    "<p> -2 : 등록 양식이 맞지 않음 </p>" +
                                                    "<p> -3 : 가게 사장님만 메뉴 등록 가능 </p>"+
                                                    "<p> -4 : 메뉴 이름이 중복되는게 있습니다.</p>"+
                                                    "<p> -5 : 파일 사이즈는 3mb이하만 됩니다.</p>")
    public ResultDto<Long> postMenu(@RequestPart PostMenuReq p,
                                          @RequestPart(required = false) MultipartFile pic){

        Long result = null;

        if (( !p.getMenuName().isEmpty() && p.getMenuName().length()>=20)
            && ( !p.getMenuContent().isEmpty()) && p.getMenuContent().length() >= 100 ) {
            return ResultDto.<Long>builder()
                    .statusCode(-2)
                    .resultMsg("메뉴 양식이 안맞습니다.")
                    .build();
        }

        try {
            MenuEntity menuEntity = service.makeMenuEntityForPost(p);
            menuEntity.setMenuCategory(menuCategoryService.getMenuCat(p.getMenuCatPk()));

            String filename = "" ;

            if (pic != null && !pic.isEmpty()) {
                if (pic.getSize() > maxSize) {
                    return ResultDto.<Long>builder()
                            .statusCode(-5)

                            .resultMsg("파일 사이즈는 3mb이하만 됩니다.")
                            .resultData(result)
                            .build();
                }
                filename = customFileUtils.makeRandomFileName(pic);
            }

                result = service.postMenu(menuEntity,filename);

                if (pic != null && !pic.isEmpty()) {

                    service.postPic(menuEntity, pic);
                }


            return ResultDto.<Long>builder()
                    .statusCode(1)
                    .resultMsg("메뉴 등록 완료")
                    .resultData(result)
                    .build();

        }  catch (IllegalArgumentException e){
            return ResultDto.<Long>builder()
                    .statusCode(-4)
                    .resultMsg("메뉴 이름 중복")
                    .resultData(result)
                    .build();
        } catch(MenuCatNotFoundException e) {
            return ResultDto.<Long>builder()
                    .statusCode(-4)
                    .resultMsg(e.getMessage())
                    .resultData(result)
                    .build();
        }catch (RuntimeException e){
            e.printStackTrace();
            return ResultDto.<Long>builder()
                    .statusCode(-3)
                    .resultMsg("가게 사장님만 메뉴를 등록할 수 있습니다.")
                    .resultData(result)
                    .build();
        }  catch (Exception e){
            e.printStackTrace();
            return ResultDto.<Long>builder()
                    .statusCode(-1)
                    .resultMsg(e.getMessage())
                    .resultData(result)
                    .build();
        }


    }
    //메뉴 상태 ( menu_state) 1번과 2번만 불러옴
    @GetMapping
    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
    @Operation(summary = "가게에 있는 메뉴를 불러옵니다." , description = "menuPk는 등록된 메뉴의 고유 번호(PK)입니다.\n" +
            "                                       menuResPk는 메뉴가 등록된 식당의 고유 번호(PK)입니다.\n" +
            "                                       <p> menuState는 ex)1이면 판매 중 2면 품절과 같은 판매상태입니다.</p>"+
                                                    "<p> 1 : 메뉴 리스트 불러오기 완료 </p>"+
                                                    "<p> -1 : 메뉴 리스트 불러오기 실패 </p>" +
                                                    "<p> -2 : 소유한 가게가 없음 </p>"
                                                    )
    public ResultDto<List<MenuListGetRes>> getAllMenu(){
        List<MenuListGetRes> result = null;
        try {
            User user = userServiceImpl.getUser(authenticationFacade.getLoginUserPk());
            Restaurant res = restaurantService.getRestaurantData(user);
            result = service.getMenuList(res);
        } catch (RuntimeException e){
            e.printStackTrace();
            return ResultDto.<List<MenuListGetRes>>builder()
                    .statusCode(-2)
                    .resultMsg("소유한 가게가 없음")
                    .resultData(result)
                    .build();
        }
        catch (Exception e){
            e.printStackTrace();
            return ResultDto.<List<MenuListGetRes>>builder()
                    .statusCode(-1)
                    .resultMsg("메뉴 리스트 불러오기 실패")
                    .resultData(result)
                    .build();
        }

        return ResultDto.<List<MenuListGetRes>>builder()
                .statusCode(1)
                .resultMsg("메뉴 리스트 불러오기 완료")
                .resultData(result)
                .build();
    }



    @PutMapping(consumes = {MediaType.APPLICATION_JSON_VALUE
            , MediaType.MULTIPART_FORM_DATA_VALUE})
    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
    @Operation(summary = "메뉴를 수정합니다." , description = "menuPk는 등록된 메뉴의 고유 번호(PK)입니다.\n" +
            "                                       menuResPk는 메뉴가 등록된 식당의 고유 번호(PK)입니다.\n" +
            "                                       <p>menuState는 ex)1이면 판매 중 2면 품절과 같은 판매상태입니다.</p>"+
                                                    "<p> 1 : 메뉴 수정 완료 </p>"+
                                                    "<p> -1 : 메뉴 수정 실패 </p>"+
                                                    "<p> -2 : 양식이 맞지 않음 </p>"+
                                                    "<p> -3 : 사장님만 메뉴를 수정할 수 있습니다. </p>"+
                                                    "<p> -4 : 메뉴 이름이 중복되는게 있습니다.</p>"   )
    public ResultDto<Long> putMenu(@RequestPart PutMenuReq p,
                                         @RequestPart(required = false) MultipartFile pic) {
        Long result = null;

        if (p.getMenuName() != null) {
            if (p.getMenuName().length() >= 20) {
                return ResultDto.<Long>builder()
                        .statusCode(-2)
                        .resultMsg("메뉴 양식이 안맞습니다.")
                        .build();
            }
        }
        if (p.getMenuContent() != null) {
            if (p.getMenuContent().length() >= 100) {
                return ResultDto.<Long>builder()
                        .statusCode(-2)
                        .resultMsg("메뉴 양식이 안맞습니다.")
                        .build();
            }
        }

        try {
            MenuEntity menuEntity = service.makeMenuEntityForPut(p);
            String filename = "";

            if (pic != null && !pic.isEmpty()) {
                if (pic.getSize() > maxSize) {
                    return ResultDto.<Long>builder()
                            .statusCode(-5)
                            .resultMsg("파일 사이즈는 3mb이하만 됩니다.")
                            .resultData(result)
                            .build();
                }
                filename = customFileUtils.makeRandomFileName(pic);
            }
            result = service.putMenu(menuEntity, filename);
            if (pic != null && !pic.isEmpty()) {
                service.putPic(menuEntity, pic);
            }

            return ResultDto.<Long>builder()
                    .statusCode(1)
                    .resultMsg("메뉴 수정 완료")
                    .resultData(result)
                    .build();

        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResultDto.<Long>builder()
                    .statusCode(-3)
                    .resultMsg("가게 사장님만 메뉴를 등록할 수 있습니다.")
                    .resultData(result)
                    .build();
        } catch (Exception e) {
            return ResultDto.<Long>builder()
                    .statusCode(-1)
                    .resultMsg(e.getMessage())
                    .resultData(result)
                    .build();
        }
    }
    @DeleteMapping("{menu_pk}")
    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
    @Operation(summary = "메뉴를 삭제합니다." , description = "menuPk는 등록된 메뉴의 고유 번호(PK)입니다.\n" +
                                                "<p> 1 : 메뉴 삭제 완료 </p>"+
                                                "<p> -1 : 메뉴 삭제 실패 </p>"+
                                                "<p> -2 : 사장님만 메뉴를 삭제 할 수있습니다.</p>"

    )

    //실제 메뉴를 삭제하는게 아님! menu_state를 3으로 변경
    public ResultDto<Integer> delMenu(@PathVariable(name = "menu_pk") long menuPk){

        int result= 0;
        try {
           result =  service.delMenu(menuPk);
        }  catch (RuntimeException e){
            return ResultDto.<Integer>builder()
                    .statusCode(-2)
                    .resultMsg("사장님만 삭제 할 수있습니다.")
                    .resultData(null)
                    .build();
        }
         catch (Exception e){
            return ResultDto.<Integer>builder()
                    .statusCode(-1)
                    .resultMsg("메뉴 삭제 실패")
                    .resultData(null)
                    .build();
        }
        return ResultDto.<Integer>builder()
                .statusCode(1)
                .resultMsg("메뉴 삭제 완료")
                .resultData(result)
                .build();
    }

    @Operation(summary = "메뉴의 카테고리 변경")
    @PatchMapping("/patch-category")
    public ResultDto<Integer> patchCategory(@RequestBody MenuPatchCategoryReq p) {
        int statusCode = 1;
        int result= 0;
        String msg = "카테고리 추가 완료";
        try {
            User user = userServiceImpl.getUser(authenticationFacade.getLoginUserPk());
            Restaurant restaurant = restaurantService.getRestaurantData(user);
            MenuPatchCategoryDto dto = new MenuPatchCategoryDto(p);
            dto.setRestaurant(restaurant);
            result = service.patchCategory(dto);
        } catch(RuntimeException e) {
            statusCode = -2;
            msg = e.getMessage();
        } catch (Exception e) {
            msg = e.getMessage();
            statusCode = -1;
        }
        return ResultDto.<Integer>builder()
                .statusCode(statusCode)
                .resultMsg(msg)
                .resultData(result)
                .build();
    }

}

