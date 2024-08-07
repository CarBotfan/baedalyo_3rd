package com.green.beadalyo.kdh.menuoption;

import com.green.beadalyo.common.model.ResultDto;
import com.green.beadalyo.kdh.menu.MenuService;
import com.green.beadalyo.kdh.menu.entity.MenuEntity;
import com.green.beadalyo.kdh.menuoption.entity.MenuOption;
import com.green.beadalyo.kdh.menuoption.model.*;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/menu/option")
@Tag(name = "메뉴 옵션")
@RequiredArgsConstructor
public class MenuOptionController {
    private final MenuOptionService service;
    private final MenuService menuService;

//ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ post ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
    @PostMapping("owner")
    @PreAuthorize("hasAnyRole('OWNER')")
    @ApiResponse(
            description =
                    "<p> 1 : 메뉴 옵션 등록 완료 </p>"+
                            "<p> -8 : 상점 주인의 접근이 아닙니다 </p>"
    )
    public ResultDto<PostMenuOptionRes> postMenuOption(@RequestBody PostMenuOptionReq req){
        PostMenuOptionRes result = null;

        if (service.validateMenuOwner(req.getOptionMenuPk())) {
            return ResultDto.<PostMenuOptionRes>builder()
                    .statusCode(-8)
                    .resultMsg("상점 주인의 접근이 아닙니다")
                    .build();
        }

        MenuOption option = service.makeOptionEntity(req);
        option = service.saveOption(option);
        result  = service.makePostMenuOptionRes(option);

        return ResultDto.<PostMenuOptionRes>builder()
                .statusCode(1)
                .resultMsg("메뉴 옵션 등록 완료")
                .resultData(result)
                .build();
    }

//ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ get ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

    @GetMapping("{menu_pk}")
    public ResultDto<List<GetMenuOptionRes>> getMenuOptions(@PathVariable("menu_pk") Long menuPk){
        MenuEntity menu = menuService.getMenuByMenuPk(menuPk);
        List<GetMenuOptionRes> result = service.getMenuOptions(menuPk);

        return ResultDto.<List<GetMenuOptionRes>>builder()
                .statusCode(1)
                .resultMsg("메뉴 옵션 불러오기 완료")
                .resultData(result)
                .build();
    }

//ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ put ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

    @PutMapping("owner")
    @PreAuthorize("hasAnyRole('OWNER')")
    @ApiResponse(
            description =
                    "<p> 1 : 메뉴 옵션 수정 완료 </p>"+
                            "<p> -8 : 상점 주인의 접근이 아닙니다 </p>"
    )
    public ResultDto<PutMenuOptionRes> putMenuOption(@RequestBody PutMenuOptionReq p){
        PutMenuOptionRes result = null;

        if (service.validateMenuOwner(menuService.getMenuByOptionPk(p.getOptionPk()).getMenuPk())) {
            return ResultDto.<PutMenuOptionRes>builder()
                    .statusCode(-8)
                    .resultMsg("상점 주인의 접근이 아닙니다")
                    .build();
        }

        MenuOption menuOption = service.updateOptionEntity(p);
        MenuOption option = service.saveOption(menuOption);
        result = service.makePutMenuOptionRes(option);

        return ResultDto.<PutMenuOptionRes>builder()
                .statusCode(1)
                .resultMsg("메뉴 옵션 수정 완료")
                .resultData(result)
                .build();
    }

//ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ patch ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

    @PatchMapping("owner/{option_pk}")
    @PreAuthorize("hasAnyRole('OWNER')")
    @ApiResponse(
            description =
                    "<p> 1 : 메뉴 옵션 상태 변경 완료 </p>"+
                            "<p> -8 : 상점 주인의 접근이 아닙니다 </p>"
    )
    public ResultDto<Integer> toggleMenuOption(@PathVariable("option_pk") Long optionPk){
        Integer result = 1;

        if (service.validateMenuOwner(menuService.getMenuByOptionPk(optionPk).getMenuPk())) {
            return ResultDto.<Integer>builder()
                    .statusCode(-8)
                    .resultMsg("상점 주인의 접근이 아닙니다")
                    .build();
        }

        MenuOption menuOption = service.changeOptionState(optionPk);
        service.saveOption(menuOption);

        return ResultDto.<Integer>builder()
                .statusCode(1)
                .resultMsg("메뉴 옵션 상태 변경 완료")
                .resultData(result)
                .build();
    }

//ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ delete ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

    @DeleteMapping("owner/{option_pk}")
    @PreAuthorize("hasAnyRole('OWNER')")
    @ApiResponse(
            description =
                    "<p> 1 : 메뉴 옵션 삭제 완료 </p>"+
                            "<p> -8 : 상점 주인의 접근이 아닙니다 </p>"
    )
    public ResultDto<Integer> delMenuOption(@PathVariable("option_pk") Long optionPk){
        int result = 1;

        if (service.validateMenuOwner(menuService.getMenuByOptionPk(optionPk).getMenuPk())) {
            return ResultDto.<Integer>builder()
                    .statusCode(-8)
                    .resultMsg("상점 주인의 접근이 아닙니다")
                    .build();
        }

        MenuOption entity = service.deleteOption(optionPk);
        service.saveOption(entity);

        return ResultDto.<Integer>builder()
                .statusCode(1)
                .resultMsg("메뉴 옵션 삭제 완료")
                .resultData(result)
                .build();
    }

}


