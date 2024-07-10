package com.green.beadalyo.kdh.menu;

import com.green.beadalyo.common.model.ResultDto;
import com.green.beadalyo.kdh.menu.model.*;
import com.green.beadalyo.kdh.menuOption.model.GetMenuWithOptionReq;
import com.green.beadalyo.kdh.menuOption.model.GetMenuWithOptionRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("api/menu")
@RequiredArgsConstructor
@Tag(name = "메뉴 관련 컨트롤러입니다.")
public class MenuController {
    private final MenuService service;

    @PostMapping
    @Operation(summary = "메뉴 등록" , description = "menuPk는 등록된 메뉴의 고유 번호(PK)입니다.\n" +
            "                                       menuResPk는 메뉴가 등록된 식당의 고유 번호(PK)입니다.\n" +
            "                                       <p>menuState는 ex)1이면 판매 중 2면 품절과 같은 판매상태입니다.</p>" +
                                                    "<p> 1 : 메뉴 등록 완료 </p>"+
                                                    "<p> -1 : 메뉴 등록 실패 </p>"+
                                                    "<p> -2 : 등록 양식이 맞지 않음 </p>" +
                                                    "<p> -3 : 가게 사장님만 메뉴 등록 가능 </p>")
    public ResultDto<PostMenuRes> postMenu(@RequestPart PostMenuReq p,
                                           @RequestPart(required = false) MultipartFile pic){

        PostMenuRes result = null;
        String msg = "메뉴 등록 완료";
        int code = 1;


        if ((p.getMenuName().length()<=20 && !p.getMenuName().isEmpty())
            && (p.getMenuContent().length() <= 100 && !p.getMenuContent().isEmpty())) {
            return ResultDto.<PostMenuRes>builder()
                    .statusCode(-2)
                    .resultMsg("메뉴 양식이 안맞습니다.")
                    .build();
        }

        try {
            result = service.postMenu(p,pic);
        } catch (RuntimeException e){

            return ResultDto.<PostMenuRes>builder()
                    .statusCode(-3)
                    .resultMsg("가게 사장님만 메뉴를 등록할 수 있습니다.")
                    .resultData(result)
                    .build();

        } catch (Exception e){

            return ResultDto.<PostMenuRes>builder()
                    .statusCode(-1)
                    .resultMsg(e.getMessage())
                    .resultData(result)
                    .build();
        }

        return ResultDto.<PostMenuRes>builder()
                .statusCode(code)
                .resultMsg(msg)
                .resultData(result)
                .build();
    }

    @GetMapping
    @Operation(summary = "가게에 있는 메뉴를 불러옵니다." , description = "menuPk는 등록된 메뉴의 고유 번호(PK)입니다.\n" +
            "                                       menuResPk는 메뉴가 등록된 식당의 고유 번호(PK)입니다.\n" +
            "                                       <p> menuState는 ex)1이면 판매 중 2면 품절과 같은 판매상태입니다.</p>"+
                                                    "<p> 1 : 메뉴 리스트 불러오기 완료 </p>"+
                                                    "<p> -1 : 메뉴 리스트 불러오기 실패 </p>"
                                                    )
    public ResultDto<List<GetAllMenuRes>> getAllMenu(@ParameterObject @ModelAttribute GetAllMenuReq p){
        List<GetAllMenuRes> result = null;

        String msg = "메뉴 리스트 불러오기 완료";
        int code = 1;
        try {
            result = service.getAllMenu(p);
        } catch (Exception e){
            msg = e.getMessage();
            code = -1;
        }

        return ResultDto.<List<GetAllMenuRes>>builder()
                .statusCode(code)
                .resultMsg(msg)
                .resultData(result)
                .build();
    }



    @PutMapping
    @Operation(summary = "메뉴를 수정합니다." , description = "menuPk는 등록된 메뉴의 고유 번호(PK)입니다.\n" +
            "                                       menuResPk는 메뉴가 등록된 식당의 고유 번호(PK)입니다.\n" +
            "                                       <p>menuState는 ex)1이면 판매 중 2면 품절과 같은 판매상태입니다.</p>"+
                                                    "<p> 1 : 메뉴 수정 완료 </p>"+
                                                    "<p> -1 : 메뉴 수정 실패 </p>"+
                                                    "<p> -2 : 양식이 맞지 않음 </p>"+
                                                    "<p> -3 : 양식이 맞지 않음 </p>")
    public ResultDto<PutMenuRes> putMenu(@RequestPart PutMenuReq p,
                                         @RequestPart(required = false) MultipartFile pic){
        PutMenuRes result = null;

        String msg = "메뉴 수정 완료";
        int code = 1;
        if ((p.getMenuName().length()<=20 && !p.getMenuName().isEmpty())
                && (p.getMenuContent().length() <= 100 && !p.getMenuContent().isEmpty())) {
            return ResultDto.<PutMenuRes>builder()
                    .statusCode(-2)
                    .resultMsg("메뉴 양식이 안맞습니다.")
                    .build();
        }
        try {
            result = service.putMenu(pic, p);
        } catch (RuntimeException e){
            msg = "사장님만 메뉴를 수정할 수 있습니다.";
            code = -3;
        } catch (Exception e){
            msg = "메뉴 수정 실패";
            code = -1;
        }

        return ResultDto.<PutMenuRes>builder()
                .statusCode(code)
                .resultMsg(msg)
                .resultData(result)
                .build();
    }

    @DeleteMapping
    @Operation(summary = "메뉴를 삭제합니다." , description = "menuPk는 등록된 메뉴의 고유 번호(PK)입니다.\n" +
                                                "<p> 1 : 메뉴 삭제 완료 </p>"+
                                                "<p> -1 : 메뉴 삭제 실패 </p>"
    )
    public ResultDto<Integer> delMenu(@RequestParam(name = "menu_pk") long menuPk){
        int result = 0;

        String msg = "메뉴 삭제 완료";
        int code = 1;
        try {
            result = service.delMenu(menuPk);
        } catch (Exception e){
            msg = e.getMessage();
            code = -1;
        }

        return ResultDto.<Integer>builder()
                .statusCode(code)
                .resultMsg(msg)
                .resultData(result)
                .build();
    }
}

