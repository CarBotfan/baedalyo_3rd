package com.green.beadalyo.kdh.report.user;

import com.green.beadalyo.common.model.ResultDto;
import com.green.beadalyo.jhw.security.AuthenticationFacade;
import com.green.beadalyo.jhw.user.UserServiceImpl;
import com.green.beadalyo.jhw.user.entity.User;
import com.green.beadalyo.kdh.report.entity.ReportEntity;
import com.green.beadalyo.kdh.report.user.model.GetReportListResForUser;
import com.green.beadalyo.kdh.report.user.model.GetReportOneResForUser;
import com.green.beadalyo.kdh.report.user.model.ReportPutReq;
import com.green.beadalyo.lhn.Review.entity.Review;
import com.green.beadalyo.kdh.report.user.model.ReportPostReq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/user/report")
@RequiredArgsConstructor
@Tag(name = "유저 신고 관리 컨트롤러")
public class ReportControllerForUser {
    private final ReportServiceForUser service;
    private final UserServiceImpl userService;
    private final AuthenticationFacade authenticationFacade;


    @PostMapping()
    @Operation(summary = "리뷰 신고기능", description = "리뷰를 신고합니다.")
    @PreAuthorize("hasAnyRole('USER','OWNER')")
    public ResultDto<Integer> postReport(@RequestBody ReportPostReq p){

        Review review = service.getReviewByPk(p.getReviewPk());
        User user = userService.getUser(authenticationFacade.getLoginUserPk());
        p.setReview(review);
        p.setUser(user);

        try {

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
    public ResultDto<Integer> putReport(@RequestBody ReportPutReq p){


        User user = userService.getUser(authenticationFacade.getLoginUserPk());
        p.setUser(user);

        try {

            ReportEntity reportEntity = service.makeReportForPut(p);
            service.saveReport(reportEntity);

        } catch (RuntimeException e) {
            return ResultDto.<Integer>builder()
                    .statusCode(-2)
                    .resultMsg("신고한 유저와 다른 유저입니다.")
                    .build();
        }catch (Exception e){
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

    @GetMapping("list")
    @Operation(summary = "신고 리스트 불러오기", description = "신고목록을 불러옵니다.")
    @PreAuthorize("hasAnyRole('USER','OWNER')")
    public ResultDto<List<GetReportListResForUser>> getReportListForUser(){

        Long userPk = authenticationFacade.getLoginUserPk();
        List<GetReportListResForUser> result = null;

        try {

          result = service.getReportListForUsers(userPk);

        } catch (Exception e){
            return ResultDto.<List<GetReportListResForUser>>builder()
                    .statusCode(-1)
                    .resultMsg(e.getMessage())
                    .build();
        }
        return ResultDto.<List<GetReportListResForUser>>builder()
                .statusCode(1)
                .resultMsg("리스트 불러오기 완료")
                .resultData(result)
                .build();
    }

    @GetMapping("{report_pk}")
    @Operation(summary = "신고 자세히 보기", description = "신고사항을 확인합니다.")
    @PreAuthorize("hasAnyRole('USER','OWNER')")
    public ResultDto<GetReportOneResForUser> getReportOneForUser(@PathVariable ("report_pk") Long reportPk){

        Long userPk = authenticationFacade.getLoginUserPk();
        GetReportOneResForUser result = null;

        try {

            result = service.getReportOneForUser(reportPk);

        } catch (Exception e){
            return ResultDto.<GetReportOneResForUser>builder()
                    .statusCode(-1)
                    .resultMsg(e.getMessage())
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
