package com.green.beadalyo.kdh.report.user;

import com.green.beadalyo.common.model.ResultDto;
import com.green.beadalyo.jhw.security.AuthenticationFacade;
import com.green.beadalyo.jhw.user.UserServiceImpl;
import com.green.beadalyo.jhw.user.entity.User;
import com.green.beadalyo.jhw.user.exception.DuplicatedIdException;
import com.green.beadalyo.kdh.report.entity.ReportEntity;
import com.green.beadalyo.kdh.report.user.model.*;
import com.green.beadalyo.lhn.Review.entity.Review;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/user/report")
@RequiredArgsConstructor
@Tag(name = "신고 관리 컨트롤러(유저)")
public class ReportControllerForUser {
    private final ReportServiceForUser service;
    private final UserServiceImpl userService;
    private final ReportServiceForUser reportServiceForUser;
    private final AuthenticationFacade authenticationFacade;


    @PostMapping()
    @Operation(summary = "리뷰 신고기능", description = "리뷰를 신고합니다.")
    @PreAuthorize("hasAnyRole('USER','OWNER')")
    public ResultDto<Integer> postReport(@Valid @RequestBody PostReportForUserReq p){

        Review review = service.getReviewByPk(p.getReviewPk());

        try {
            User user = userService.getUser(authenticationFacade.getLoginUserPk());
            if (reportServiceForUser.getReportCountByReviewPkAndUser(
                    review, user) != 0) {
                return ResultDto.<Integer>builder()
                        .statusCode(-2)
                        .resultMsg("같은 리뷰를 여러번 신고할 수 없습니다.")
                        .build();
            }
            p.setReview(review);
            p.setUser(user);
            ReportEntity reportEntity = service.makeReportForPost(p);
            service.saveReport(reportEntity);

        } catch (Exception e){
            return ResultDto.<Integer>builder()
                    .statusCode(-1)
                    .resultMsg("리뷰 신고 실패")
                    .build();
        }
        return ResultDto.<Integer>builder()
                .statusCode(1)
                .resultMsg("리뷰 신고 완료")
                .resultData(1)
                .build();
    }

    @PutMapping()
    @Operation(summary = "리뷰 신고수정 기능", description = "신고사항을 수정합니다.")
    @PreAuthorize("hasAnyRole('USER','OWNER')")
    public ResultDto<Integer> putReport(@Valid @RequestBody PutReportForUserReq p){


        boolean checkUser = service.checkUser(p.getReportPk());
        if (!checkUser){
            return ResultDto.<Integer>builder()
                    .statusCode(-2)
                    .resultMsg("작성자가 아닙니다.")
                    .build();
        }
        try {

            ReportEntity reportEntity = service.makeReportForPut(p);
            service.saveReport(reportEntity);

        } catch (DuplicatedIdException e){
            return ResultDto.<Integer>builder()
                    .statusCode(-3)
                    .resultMsg("이미 처리된 내역입니다.")
                    .build();
        } catch (Exception e){
            return ResultDto.<Integer>builder()
                    .statusCode(-1)
                    .resultMsg("수정 실패")
                    .build();
        }

        return ResultDto.<Integer>builder()
                .statusCode(1)
                .resultMsg("수정 완료")
                .resultData(1)
                .build();
    }

    @GetMapping("report_list/{page}")
    @Operation(summary = "신고 리스트 불러오기", description = "신고목록을 불러옵니다.")
    @PreAuthorize("hasAnyRole('USER','OWNER')")
    public ResultDto<ReportListDto> getReportListForUser(@PathVariable Integer page){

        if (page == null || page < 0 ){
            page = 1;
        }

        Long userPk = authenticationFacade.getLoginUserPk();
        ReportListDto result = new ReportListDto();

        try {
            Page<ReportEntity> pageEntity = service.getReportListForUsers(userPk, page);

            List<GetReportListResForUser> resultList = service.makeGetReportListForUser(pageEntity);

            result = service.makeReportListDto(result, pageEntity, resultList);

        } catch (Exception e){
            return ResultDto.<ReportListDto>builder()
                    .statusCode(-1)
                    .resultMsg(e.getMessage())
                    .build();
        }
        return ResultDto.<ReportListDto>builder()
                .statusCode(1)
                .resultMsg("리스트 불러오기 완료")
                .resultData(result)
                .build();
    }

    @GetMapping("{report_pk}")
    @Operation(summary = "신고 자세히 보기", description = "신고사항을 확인합니다.")
    @PreAuthorize("hasAnyRole('USER','OWNER')")
    public ResultDto<GetReportOneResForUser> getReportOneForUser(@PathVariable ("report_pk") Long reportPk){

        GetReportOneResForUser result = null;
            boolean checkUser = service.checkUser(reportPk);
            if (!checkUser){
                return ResultDto.<GetReportOneResForUser>builder()
                        .statusCode(-2)
                        .resultMsg("작성자가 아닙니다.")
                        .build();
            }
        try {

            result = service.getReportOneForUser(reportPk);

        } catch (Exception e){
            return ResultDto.<GetReportOneResForUser>builder()
                    .statusCode(-1)
                    .resultMsg("신고 불러오기 실패")
                    .build();
        }
        return ResultDto.<GetReportOneResForUser>builder()
                .statusCode(1)
                .resultMsg("불러오기 완료")
                .resultData(result)
                .build();
    }

    @DeleteMapping("{report_pk}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "신고된 항목 삭제",description = "신고 중 하나를 삭제합니다")
    public ResultDto<Integer> delReport(@PathVariable("report_pk") Long reportPk){


        boolean checkUser = service.checkUser(reportPk);
        if (!checkUser){
            return ResultDto.<Integer>builder()
                    .statusCode(-2)
                    .resultMsg("작성자가 아닙니다.")
                    .build();
        }
        try {
            service.delReport(reportPk);
        } catch (RuntimeException e){
            return ResultDto.<Integer>builder()
                    .statusCode(-2)
                    .resultMsg("이미 처리 된 신고사항입니다")
                    .build();
        } catch (Exception e){
            return ResultDto.<Integer>builder()
                    .statusCode(-1)
                    .resultMsg("신고 삭제 실패")
                    .build();
        }

        return ResultDto.<Integer>builder()
                .statusCode(1)
                .resultMsg("신고 삭제 성공")
                .resultData(1)
                .build();
    }
}
