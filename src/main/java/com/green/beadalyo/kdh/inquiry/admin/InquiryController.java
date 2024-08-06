package com.green.beadalyo.kdh.inquiry.admin;

import com.green.beadalyo.gyb.common.ResultDto;
import com.green.beadalyo.jhw.security.AuthenticationFacade;
import com.green.beadalyo.jhw.user.entity.User;
import com.green.beadalyo.kdh.inquiry.entity.InquiryEntity;
import com.green.beadalyo.kdh.inquiry.admin.model.PostInquiryReq;
import com.green.beadalyo.kdh.inquiry.admin.model.PostInquiryResponseReq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/inquiry")
@RequiredArgsConstructor
@Tag(name = "문의 컨트롤러")
public class InquiryController {
    private final InquiryService service;
    private final AuthenticationFacade authenticationFacade;

    @PostMapping()
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "고객센터 문의하기(유저용)")
    public ResultDto<Long> postInquiry(@RequestBody PostInquiryReq p){

        User user = service.getUserByPk(authenticationFacade.getLoginUserPk());
        p.setUser(user);

        Long result = null;

       try {
            InquiryEntity entity = service.makeInquiry(p);
            result = service.postInquiry(entity);
       } catch (Exception e){
           return ResultDto.<Long>builder()
                   .statusCode(-1)
                   .resultMsg("고객문의 실패")
                   .build();
       }


        return ResultDto.<Long>builder()
                .statusCode(1)
                .resultMsg("고객문의 완료")
                .resultData(result).build();
    }

    @PostMapping("response")
    @PreAuthorize("hasRole(ADMIN)")
    @Operation(summary = "고객센터 문의답변하기(어드민용)")
    public ResultDto<Integer> postInquiryResponse(@RequestBody PostInquiryResponseReq p){


        Integer result = null;

        try {
            InquiryEntity entity = service.makeInquiryResponse(p);
            result = service.postInquiry(entity);
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
}
