package com.green.beadalyo.gyb.category;

import com.green.beadalyo.gyb.common.*;
import com.green.beadalyo.gyb.model.Category;
import com.green.beadalyo.gyb.model.User;
import com.green.beadalyo.gyb.response.CategoryRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("api/category")
public class CategoryApiController
{

    private final CategoryService service;

    @PutMapping
    @Operation(summary = "카테고리 추가")
    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = ResultDto.class)),
            description =
                    "<p> 1 : 정상 </p>" +
                    "<p> -1 : 실패 </p>" +
                    "<p> -2 : 로그인 정보 획득 실패 </p>" +
                    "<p> -3 : 권한 부족 </p>"
    )
    public Result putCategory(@RequestPart String str, @RequestPart MultipartFile file)
    {
        User user = new User().Admin();
        //유효성 검증
        if (user == null)
            return ResultError.builder().statusCode(-2).resultMsg("유저 정보가 일치하지 않습니다.").build();
        //관리자 권한 검증
        if (!user.getUserRole().equals("ROLE_ADMIN"))
            return ResultError.builder().statusCode(-3).resultMsg("권한이 부족합니다.").build();

        String filename = null ;
        if (file != null && !file.isEmpty())
        {
            try {

                filename = FileUtils.fileInput("category",file) ;
            } catch (Exception e) {
                log.error("An error occurred: ", e);
                return ResultError.builder().build();
            }

        }

        try {
            service.InsertCategory(str,filename);
        } catch (Exception e) {
            log.error("An error occurred: ", e);
            return ResultError.builder().build();
        }

        return ResultDto.builder().build();

    }
    
    @DeleteMapping("{seq}")
    @Operation(summary = "카테고리 삭제")
    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = ResultDto.class)),
            description =
                    "<p> 1 : 정상 </p>" +
                            "<p> -1 : 실패 </p>" +
                            "<p> -2 : 로그인 정보 획득 실패 </p>" +
                            "<p> -3 : 권한 부족 </p>"
    )
    public Result deleteCategory(@PathVariable Long seq)
    {
        User user = new User().Admin();
        //유효성 검증
        if (user == null)
            return ResultError.builder().statusCode(-2).resultMsg("유저 정보가 일치하지 않습니다.").build();
        //관리자 권한 검증
        if (!user.getUserRole().equals("ROLE_ADMIN"))
            return ResultError.builder().statusCode(-3).resultMsg("권한이 부족합니다.").build();

        try {
            Category cate = service.getCategory(seq) ;
            FileUtils.fileDelete("category",cate.getCategoryPic()) ;
            service.deleteCategory(cate) ;
            
            return ResultDto.builder().build();
        } catch (Exception e) {
            log.error("An error occurred: ", e);
            return ResultError.builder().build();
        }
    }

    @GetMapping
    @Operation(summary = "카테고리 리스트 조회")
    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = ResultDto.class)),
            description =
                    "<p> 1 : 정상 </p>" +
                    "<p> -1 : 실패 </p>"
    )
    public Result getCategoryList()
    {
        try {
            List<Category> list = service.getCategoryAll() ;
            List<CategoryRes> reslist = CategoryRes.toCategoryRes(list) ;
            return ResultDto.builder().resultData(reslist).build() ;
        } catch (Exception e) {
            log.error("An error occurred: ", e);
            return ResultError.builder().build();
        }
    }


}
