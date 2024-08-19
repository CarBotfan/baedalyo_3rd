package com.green.beadalyo.kdh.inquiry.admin;

import com.green.beadalyo.gyb.common.ResultDto;
import com.green.beadalyo.jhw.security.AuthenticationFacade;
import com.green.beadalyo.kdh.inquiry.admin.model.GetInquiryListForAdmin;
import com.green.beadalyo.kdh.inquiry.admin.model.GetInquiryOneForAdmin;
import com.green.beadalyo.kdh.inquiry.admin.model.InquiryListDto;
import com.green.beadalyo.kdh.inquiry.entity.InquiryEntity;
import com.green.beadalyo.kdh.inquiry.admin.model.PostInquiryResponseForAdminReq;
import com.green.beadalyo.kdh.inquiry.user.model.GetInquiryListForUser;
import com.green.beadalyo.kdh.inquiry.user.model.GetInquiryOneForUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/admin/inquiry")
@RequiredArgsConstructor
@Tag(name = "문의 컨트롤러(어드민)")
public class InquiryControllerForAdmin {
    private final InquiryServiceForAdmin service;
    private final AuthenticationFacade authenticationFacade;



    @PostMapping("response")
    @PreAuthorize("hasAnyRole('ADMIN')")
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
    @PreAuthorize("hasAnyRole('ADMIN')")
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


    @GetMapping("inquiry_list/{page}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "문의리스트 불러오기(어드민용)")
    public ResultDto<InquiryListDto> getInquiryListForAdmin(@PathVariable Integer page){
        if (page == null || page < 0 ){
            page = 1;
        }

        InquiryListDto result = new InquiryListDto();

        try {
            Page<InquiryEntity> pageEntity = service.getInquiryListForAdmins(page);

            List<GetInquiryListForAdmin> resultList = service.makeGetInquiryListForAdmin(pageEntity);

            result = service.makeInquiryListDto(result, pageEntity, resultList);

        }  catch (Exception e){
            return ResultDto.<InquiryListDto>builder()

                    .statusCode(-1)
                    .resultMsg("리스트 불러오기 실패")
                    .build();
        }  return ResultDto.<InquiryListDto>builder()
                .statusCode(1)
                .resultMsg("리스트 불러오기 완료")
                .resultData(result).build();
    }

    @GetMapping("inquiry_list_finished/{page}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "문의리스트 불러오기(어드민용)", description = "완료된 항목을 불러옵니다.")
    public ResultDto<InquiryListDto> getInquiryListFinishedForAdmin(@PathVariable Integer page){
        if (page == null || page < 0 ){
            page = 1;
        }

        InquiryListDto result = new InquiryListDto();

        try {
            Page<InquiryEntity> pageEntity = service.getInquiryListFinishedForAdmins(page);

            List<GetInquiryListForAdmin> resultList = service.makeGetInquiryListForAdmin(pageEntity);

            result = service.makeInquiryListDto(result, pageEntity, resultList);

        }  catch (Exception e){
            return ResultDto.<InquiryListDto>builder()

                    .statusCode(-1)
                    .resultMsg("리스트 불러오기 실패")
                    .build();
        }  return ResultDto.<InquiryListDto>builder()
                .statusCode(1)
                .resultMsg("리스트 불러오기 완료")
                .resultData(result).build();
    }


    @GetMapping("inquiry_list_unfinished/{page}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "문의리스트 불러오기(어드민용)")
    public ResultDto<InquiryListDto> getInquiryListUnFinishedForAdmin(@PathVariable Integer page){
        if (page == null || page < 0 ){
            page = 1;
        }

        InquiryListDto result = new InquiryListDto();

        try {
            Page<InquiryEntity> pageEntity = service.getInquiryListUnfinishedForAdmins(page);

            List<GetInquiryListForAdmin> resultList = service.makeGetInquiryListForAdmin(pageEntity);

            result = service.makeInquiryListDto(result, pageEntity, resultList);

        }  catch (Exception e){
            return ResultDto.<InquiryListDto>builder()

                    .statusCode(-1)
                    .resultMsg("리스트 불러오기 실패")
                    .build();
        }  return ResultDto.<InquiryListDto>builder()
                .statusCode(1)
                .resultMsg("리스트 불러오기 완료")
                .resultData(result).build();
    }

    @GetMapping("{inquiry_pk}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "문의 불러오기(어드민용)")
    public ResultDto<GetInquiryOneForAdmin> getInquiryOneForAdmin(@PathVariable("inquiry_pk") Long inquiryPk){



        GetInquiryOneForAdmin result = null;

        try {
            result = service.getInquiryOneForAdmin(inquiryPk);
        }  catch (Exception e){
            return ResultDto.<GetInquiryOneForAdmin>builder()
                    .statusCode(-1)
                    .resultMsg("리스트 불러오기 실패")
                    .build();
        }


        return ResultDto.<GetInquiryOneForAdmin>builder()
                .statusCode(1)
                .resultMsg("리스트 불러오기 완료")
                .resultData(result).build();
    }

}
