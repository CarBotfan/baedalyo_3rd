package com.green.beadalyo.kdh.inquiry.user;

import com.green.beadalyo.gyb.common.ResultDto;
import com.green.beadalyo.jhw.security.AuthenticationFacade;
import com.green.beadalyo.jhw.user.entity.User;
import com.green.beadalyo.kdh.inquiry.entity.InquiryEntity;
import com.green.beadalyo.kdh.inquiry.user.model.GetInquiryListForUser;
import com.green.beadalyo.kdh.inquiry.user.model.GetInquiryOneForUser;
import com.green.beadalyo.kdh.inquiry.user.model.PostInquiryForUserReq;
import com.green.beadalyo.kdh.inquiry.user.model.PutInquiryForUserReq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/user/inquiry")
@Tag(name = "문의 컨트롤러(유저)")
public class InquiryControllerForUser {
    private final InquiryServiceForUser service;
    private final AuthenticationFacade authenticationFacade;

    @PostMapping()
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "고객센터 문의하기(유저용)")
    public ResultDto<Integer> postInquiry(@RequestBody PostInquiryForUserReq p){

        User user = service.getUserByPk(authenticationFacade.getLoginUserPk());
        p.setUser(user);

        Integer result = null;

        try {
            InquiryEntity entity = service.makeInquiryForPost(p);
            result = service.saveInquiry(entity);
        } catch (Exception e){
            return ResultDto.<Integer>builder()
                    .statusCode(-1)
                    .resultMsg("고객문의 실패")
                    .build();
        }


        return ResultDto.<Integer>builder()
                .statusCode(1)
                .resultMsg("고객문의 완료")
                .resultData(result).build();
    }

    @PutMapping()
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "문의하기 수정하기(유저용)")
    public ResultDto<Integer> PutInquiry(@RequestBody PutInquiryForUserReq p){

        User user = service.getUserByPk(authenticationFacade.getLoginUserPk());
        p.setUser(user);

        Integer result = null;

        try {
            InquiryEntity entity = service.makeInquiryForPut(p);
            result = service.saveInquiry(entity);
        } catch (RuntimeException e){
            return ResultDto.<Integer>builder()
                    .statusCode(-2)
                    .resultMsg("작성자가 아닙니다.")
                    .build();
        } catch (Exception e){
            return ResultDto.<Integer>builder()
                    .statusCode(-1)
                    .resultMsg("작성자가 아닙니다.")
                    .build();
        }


        return ResultDto.<Integer>builder()
                .statusCode(1)
                .resultMsg("고객문의 완료")
                .resultData(result).build();
    }

    @GetMapping("inquiry_list")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "문의리스트 불러오기(유저용)")
    public ResultDto<List<GetInquiryListForUser>> getInquiryListForUser(){

        Long userPk = authenticationFacade.getLoginUserPk();

        List<GetInquiryListForUser> result = null;

        try {
            result = service.getInquiryListForUser(userPk);
        }  catch (Exception e){
            return ResultDto.<List<GetInquiryListForUser>>builder()
                    .statusCode(-1)
                    .resultMsg("리스트 불러오기 실패")
                    .build();
        }


        return ResultDto.<List<GetInquiryListForUser>>builder()
                .statusCode(1)
                .resultMsg("리스트 불러오기 완료")
                .resultData(result).build();
    }

    @GetMapping("{inquiry_pk}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "문의 자세히보기(유저용)")
    public ResultDto<GetInquiryOneForUser> getInquiryOneForUser(@PathVariable("inquiry_pk") Long inquiryPk){


        boolean checkUser = service.checkUser(inquiryPk);
        if (!checkUser){
            return ResultDto.<GetInquiryOneForUser>builder()
                    .statusCode(-2)
                    .resultMsg("작성자가 아닙니다.")
                    .build();
        }
        GetInquiryOneForUser result = null;


        try {
            result = service.getInquiryOneForUser(inquiryPk);
        }  catch (Exception e){
            return ResultDto.<GetInquiryOneForUser>builder()
                    .statusCode(-1)
                    .resultMsg("문의 불러오기 실패")
                    .build();
        }


        return ResultDto.<GetInquiryOneForUser>builder()
                .statusCode(1)
                .resultMsg("문의 불러오기 완료")
                .resultData(result).build();
    }


    @DeleteMapping("{inquiry_pk}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "문의 삭제하기(유저용)")
    public ResultDto<Integer> delInquiryForUser(@PathVariable("inquiry_pk") Long inquiryPk){


        boolean checkUser = service.checkUser(inquiryPk);
        if (!checkUser){
            return ResultDto.<Integer>builder()
                    .statusCode(-2)
                    .resultMsg("작성자가 아닙니다.")
                    .build();
        }

        try {

            service.delInquiry(inquiryPk);

        }  catch (RuntimeException e){
            return ResultDto.<Integer>builder()
                    .statusCode(-1)
                    .resultMsg("이미 처리된 내역입니다.")
                    .build();

        } catch (Exception e) {
            return ResultDto.<Integer>builder()
                    .statusCode(-1)
                    .resultMsg("삭제 실패")
                    .build();
        }

        return ResultDto.<Integer>builder()
                .statusCode(1)
                .resultMsg("삭제 완료")
                .resultData(1).build();
    }
}
