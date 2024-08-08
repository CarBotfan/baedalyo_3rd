package com.green.beadalyo.kdh.inquiry.admin;

import com.green.beadalyo.gyb.common.ResultDto;
import com.green.beadalyo.jhw.security.AuthenticationFacade;
import com.green.beadalyo.kdh.inquiry.entity.InquiryEntity;
import com.green.beadalyo.kdh.inquiry.admin.model.PostInquiryResponseForAdminReq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/admin/inquiry")
@RequiredArgsConstructor
@Tag(name = "문의 컨트롤러(어드민)")
public class InquiryControllerForAdmin {
    private final InquiryServiceForAdmin service;
    private final AuthenticationFacade authenticationFacade;



    @PostMapping("response")
    @PreAuthorize("hasRole(ADMIN)")
    @Operation(summary = "고객센터 문의답변하기(어드민용)")
    public ResultDto<Integer> postInquiryResponse(@RequestBody PostInquiryResponseForAdminReq p){


        Integer result = null;

        try {
            InquiryEntity entity = service.makeInquiryResponse(p);
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

    @PutMapping ("response")
    @PreAuthorize("hasRole(ADMIN)")
    @Operation(summary = "고객센터 문의수정하기(어드민용)")
    public ResultDto<Integer> putInquiryResponse(@RequestBody PostInquiryResponseForAdminReq p){


        Integer result = null;

        try {
            InquiryEntity entity = service.makeInquiryResponse(p);
            result = service.saveInquiry(entity);
        } catch (Exception e){
            return ResultDto.<Integer>builder()
                    .statusCode(-1)
                    .resultMsg("고객문의 수정 실패")
                    .build();
        }


        return ResultDto.<Integer>builder()
                .statusCode(1)
                .resultMsg("고객문의 수정 완료")
                .resultData(result).build();
    }


}
