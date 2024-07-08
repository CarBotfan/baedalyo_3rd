package com.green.beadalyo.kdh.menuOption;

import com.green.beadalyo.common.model.ResultDto;
import com.green.beadalyo.kdh.menuOption.model.*;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/menu/option")
@RequiredArgsConstructor
public class MenuOptionController {
    private final MenuOptionService service;

    @PostMapping
    @Operation(summary = "옵션을 등록합니다." , description = "optionPk는 등록된 '옵션'의 고유 번호(PK)입니다.\n" +
            "                                       optionMenuPk는 옵션이 달린 '메뉴'의 고유 번호(PK)입니다.\n" +
            "                                       optionState ex)1이면 판매 중 2면 품절과 같은 판매상태입니다.")
    public ResultDto<PostMenuOptionRes> postMenuOption(@RequestBody PostMenuOptionReq p){

        PostMenuOptionRes result = null;
        String msg = "메뉴 옵션 등록 완료";
        int code = 2;
        try {
            result = service.postMenuOption(p);
        } catch (Exception e){
            msg = e.getMessage();
            code = 4;
        }

        return ResultDto.<PostMenuOptionRes>builder()
                .statusCode(code)
                .resultMsg(msg)
                .resultData(result)
                .build();
    }
    @GetMapping("detail")
    @Operation(summary = "메뉴에 딸린 옵션을 불러옵니다." , description = "menu_pk는 등록된 '메뉴'의 고유 번호(PK)입니다.")
    public ResultDto<List<GetMenuWithOptionRes>> getMenuWithOption(@ParameterObject @ModelAttribute GetMenuWithOptionReq p){
        List<GetMenuWithOptionRes> result = null;

        String msg = "메뉴 옵션 불러오기 완료";
        int code = 2;
        try {
            result = service.getMenuWithOption(p);
        } catch (Exception e){
            msg = e.getMessage();
            code = 4;
        }

        return ResultDto.<List<GetMenuWithOptionRes>>builder()
                .statusCode(code)
                .resultMsg(msg)
                .resultData(result)
                .build();
    }

    @PutMapping
    @Operation(summary = "옵션을 수정합니다." , description = "optionPk는 등록된 '옵션'의 고유 번호(PK)입니다.\n" +
            "                                       optionMenuPk는 옵션이 달린 '메뉴'의 고유 번호(PK)입니다.\n" +
            "                                       optionState ex)1이면 판매 중 2면 품절과 같은 판매상태입니다.")
    public ResultDto<PutMenuOptionRes> putMenuOption(@RequestBody PutMenuOptionReq p){

        PutMenuOptionRes result = null;
        String msg = "메뉴 옵션 수정 완료";
        int code = 2;
        try {
            result = service.putMenuOption(p);
        } catch (Exception e){
            msg = e.getMessage();
            code = 4;
        }

        return ResultDto.<PutMenuOptionRes>builder()
                .statusCode(code)
                .resultMsg(msg)
                .resultData(result)
                .build();
    }

    @DeleteMapping
    @Operation(summary = "옵션을 삭제합니다." , description = "option_pk는 등록된 '옵션'의 고유 번호(PK)입니다.\n" +
            "                                        넘어온 데이터가 1이라면 삭제 완료입니다.\n" +
            "                                        (삭제한 옵션의 갯수라고 생각하시면 됩니다.)")
    public ResultDto<Integer> delMenuOption(@ParameterObject @ModelAttribute DelMenuOptionReq p){

        int result = 0;
        String msg = "메뉴 옵션 삭제 완료";
        int code = 2;
        try {
            result = service.delMenuOption(p);
        } catch (Exception e){
            msg = e.getMessage();
            code = 4;
        }

        return ResultDto.<Integer>builder()
                .statusCode(code)
                .resultMsg(msg)
                .resultData(result)
                .build();
    }
}


