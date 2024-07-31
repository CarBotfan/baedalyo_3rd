package com.green.beadalyo.kdh.menu;

import com.green.beadalyo.common.CustomFileUtils;
import com.green.beadalyo.common.model.ResultDto;
import com.green.beadalyo.gyb.model.Restaurant;
import com.green.beadalyo.gyb.restaurant.repository.RestaurantRepository;
import com.green.beadalyo.jhw.security.AuthenticationFacade;
import com.green.beadalyo.jhw.user.repository.UserRepository2;
import com.green.beadalyo.kdh.menu.entity.MenuEntity;
import com.green.beadalyo.kdh.menu.model.*;
import com.green.beadalyo.kdh.menu.repository.MenuRepository;
import com.green.beadalyo.kdh.menuOption.model.GetMenuWithOptionReq;
import com.green.beadalyo.kdh.menuOption.model.GetMenuWithOptionRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("api/owner/menu")
@RequiredArgsConstructor
@Tag(name = "메뉴 관련 컨트롤러입니다.(사장님 전용)")
@Slf4j
public class MenuController {
    private final MenuService service;
    private final AuthenticationFacade authenticationFacade;
    private final RestaurantRepository restaurantRepository;
    private final UserRepository2 userRepository;
    private final MenuRepository menuRepository;
    private final CustomFileUtils customFileUtils;
    private final long maxSize = 3145728;
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

        p.setResUserPk(authenticationFacade.getLoginUserPk());
        Restaurant restaurant = restaurantRepository.findRestaurantByUser(userRepository.getReferenceById(p.getResUserPk()));

        if (restaurant == null) {
            return ResultDto.<Long>builder()
                    .statusCode(-3)
                    .resultMsg("가게 사장님만 메뉴를 등록할 수 있습니다.")
                    .resultData(result)
                    .build();
        }

        List<GetAllMenuNames> menuName = menuRepository.findMenuNameByMenuResPk(restaurant.getSeq());
        for (GetAllMenuNames menu : menuName){
            if (menu.equals(p.getMenuName())){
                return ResultDto.<Long>builder()
                        .statusCode(-4)
                        .resultMsg("메뉴 이름 중복")
                        .resultData(result)
                        .build();
            }
        }

        if (( !p.getMenuName().isEmpty() && p.getMenuName().length()>=20)
            && ( !p.getMenuContent().isEmpty()) && p.getMenuContent().length() >= 100 ) {
            return ResultDto.<Long>builder()
                    .statusCode(-2)
                    .resultMsg("메뉴 양식이 안맞습니다.")
                    .build();
        }



        try {
            MenuEntity menuEntity = new MenuEntity();
            menuEntity.setMenuResPk(restaurant);
            menuEntity.setMenuContent(p.getMenuContent());
            menuEntity.setMenuName(p.getMenuName());
            menuEntity.setMenuPrice(p.getMenuPrice());
            menuEntity.setMenuState(p.getMenuState());
            String filename = "" ;

            if (pic != null){
                if(pic.getSize() > maxSize){
                    return ResultDto.<Long>builder()
                            .statusCode(-5)
                            .resultMsg("파일 사이즈는 3mb이하만 됩니다.")
                            .resultData(result)
                            .build();
                }
                filename = customFileUtils.makeRandomFileName(pic);
                service.postPic(menuEntity, pic);
            }
              result = service.postMenu(menuEntity,filename);


            return ResultDto.<Long>builder()
                    .statusCode(1)
                    .resultMsg("메뉴 등록 완료")
                    .resultData(result)
                    .build();

        }  catch (Exception e){

            return ResultDto.<Long>builder()
                    .statusCode(-1)
                    .resultMsg(e.getMessage())
                    .resultData(result)
                    .build();
        }


    }

    @GetMapping
    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
    @Operation(summary = "가게에 있는 메뉴를 불러옵니다." , description = "menuPk는 등록된 메뉴의 고유 번호(PK)입니다.\n" +
            "                                       menuResPk는 메뉴가 등록된 식당의 고유 번호(PK)입니다.\n" +
            "                                       <p> menuState는 ex)1이면 판매 중 2면 품절과 같은 판매상태입니다.</p>"+
                                                    "<p> 1 : 메뉴 리스트 불러오기 완료 </p>"+
                                                    "<p> -1 : 메뉴 리스트 불러오기 실패 </p>" +
                                                    "<p> -2 : 소유한 가게가 없음 </p>"
                                                    )
    public ResultDto<List<GetAllMenuResInterface>> getAllMenu(){
        List<GetAllMenuResInterface> result = null;
        long userPk = authenticationFacade.getLoginUserPk();
        Restaurant restaurant = restaurantRepository.findRestaurantByUser(userRepository.getReferenceById(userPk));

        if (restaurant == null || restaurant.getSeq() == 0 ){
            return ResultDto.<List<GetAllMenuResInterface>>builder()
                    .statusCode(-2)
                    .resultMsg("소유한 가게가 없음")
                    .resultData(result)
                    .build();
        }
        try {
            result = service.getAllMenuByUserPk(restaurant.getSeq());
        } catch (Exception e){

            return ResultDto.<List<GetAllMenuResInterface>>builder()
                    .statusCode(-1)
                    .resultMsg("메뉴 리스트 불러오기 실패")
                    .resultData(result)
                    .build();
        }

        return ResultDto.<List<GetAllMenuResInterface>>builder()
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

        p.setResUserPk(authenticationFacade.getLoginUserPk());
        Restaurant restaurant = restaurantRepository.findRestaurantByUser(userRepository.getReferenceById(p.getResUserPk()));

        if (restaurant == null) {
            return ResultDto.<Long>builder()
                    .statusCode(-3)
                    .resultMsg("가게 사장님만 메뉴를 수정할 수 있습니다.")
                    .resultData(result)
                    .build();
        }

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
        List<GetAllMenuNames> menuName = menuRepository.findMenuNameByMenuResPkAndMenuPk(restaurant.getSeq(), p.getMenuPk());
        for (GetAllMenuNames menu : menuName) {
            if (menu.equals(p.getMenuName())) {
                return ResultDto.<Long>builder()
                        .statusCode(-4)
                        .resultMsg("메뉴 이름 중복")
                        .resultData(result)
                        .build();
            }
        }
        try {
            MenuEntity menuEntity = new MenuEntity();
            menuEntity.setMenuResPk(restaurant);
            menuEntity.setMenuContent(p.getMenuContent());
            menuEntity.setMenuName(p.getMenuName());
            menuEntity.setMenuPrice(p.getMenuPrice());
            menuEntity.setMenuState(p.getMenuState());
            String filename = "";

            if (pic != null) {
                if (pic.getSize() > maxSize) {
                    return ResultDto.<Long>builder()
                            .statusCode(-5)
                            .resultMsg("파일 사이즈는 3mb이하만 됩니다.")
                            .resultData(result)
                            .build();
                }
                filename = customFileUtils.makeRandomFileName(pic);
                service.postPic(menuEntity, pic);
            }
            result = service.postMenu(menuEntity, filename);


            return ResultDto.<Long>builder()
                    .statusCode(1)
                    .resultMsg("메뉴 수정 완료")
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

    public ResultDto<Integer> delMenu(@PathVariable(name = "menu_pk") long menuPk){
        Long resUserPk = authenticationFacade.getLoginUserPk();
        Restaurant restaurant = restaurantRepository.findRestaurantByUser(userRepository.getReferenceById(resUserPk));
        MenuEntity menuEntity = menuRepository.getReferenceById(menuPk);


        if (restaurant.getSeq() != menuEntity.getMenuResPk().getSeq()){
            return ResultDto.<Integer>builder()
                    .statusCode(-2)
                    .resultMsg("사장님만 삭제 할 수있습니다.")
                    .resultData(null)
                    .build();
        }

        int result= 0;
        try {
           result =  service.delMenu(menuEntity);
        }  catch (RuntimeException e){

        } catch (Exception e){
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
}

