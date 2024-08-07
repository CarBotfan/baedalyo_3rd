package com.green.beadalyo.kdh.report.admin;

import com.green.beadalyo.common.model.ResultDto;
import com.green.beadalyo.jhw.user.UserServiceImpl;
import com.green.beadalyo.kdh.report.admin.model.GetReportListResForAdmin;
import com.green.beadalyo.kdh.report.admin.model.GetReportOneResForAdmin;
import com.green.beadalyo.kdh.report.admin.model.PatchAccountSuspensionReq;
import com.green.beadalyo.kdh.report.admin.model.PutReportForAdminReq;
import com.green.beadalyo.kdh.report.entity.ReportEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/admin/report")
@RequiredArgsConstructor
@Tag(name = "신고 관리 컨트롤러(어드민)")
public class ReportControllerForAdmin {
    private final ReportServiceForAdmin service;
    private final UserServiceImpl userService;


    @GetMapping("report_list")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "신고된 리뷰들 목록 불러오기",description = "처리 + 비처리 다 불러옵니다.")
    public ResultDto<List<GetReportListResForAdmin>> getReportList(){
        List<GetReportListResForAdmin> List = service.getReportList();

        return ResultDto.<List<GetReportListResForAdmin>>builder()
                .statusCode(1)
                .resultMsg("리스트 불러오기 성공")
                .resultData(List)
                .build();
    }

    @GetMapping("report_list_finished")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "신고된 리뷰들 목록 불러오기",description = "처리된 신고를 다 불러옵니다.")
    public ResultDto<List<GetReportListResForAdmin>> getReportFinishedList(){
        List<GetReportListResForAdmin> List = service.getReportFinishedList();

        return ResultDto.<List<GetReportListResForAdmin>>builder()
                .statusCode(1)
                .resultMsg("리스트 불러오기 성공")
                .resultData(List)
                .build();
    }

    @GetMapping("report_list_unfinished")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "신고된 리뷰들 목록 불러오기",description = "비처리된 신고를 다 불러옵니다.")
    public ResultDto<List<GetReportListResForAdmin>> getReportUnFinishedList(){
        List<GetReportListResForAdmin> List = service.getReportUnFinishedList();

        return ResultDto.<List<GetReportListResForAdmin>>builder()
                .statusCode(1)
                .resultMsg("리스트 불러오기 성공")
                .resultData(List)
                .build();
    }

    @GetMapping("{report_pk}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "신고된 항목 자세히 보기",description = "신고 중 하나를 선택해 봅니다")
    public ResultDto<GetReportOneResForAdmin> getReportOne(@PathVariable("report_pk") Long reportPk){
        GetReportOneResForAdmin result = service.getReportOne(reportPk);

        return ResultDto.<GetReportOneResForAdmin>builder()
                .statusCode(1)
                .resultMsg("신고 불러오기 성공")
                .resultData(result)
                .build();
    }

    @PatchMapping("account_suspension")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "유저 계정 정지하기",description = "신고된 리뷰의 작성자를 정지합니다.")
    public ResultDto<Integer> patchAccountSuspension(@RequestBody PatchAccountSuspensionReq p){

        try {
            service.patchUserStateAndBlockDate(p);
            service.patchReportStateAndResult(p);
        } catch (Exception e){
            return ResultDto.<Integer>builder()
                    .statusCode(-1)
                    .resultMsg("계정 정지 실패")
                    .build();
        }


        return ResultDto.<Integer>builder()
                .statusCode(1)
                .resultMsg("계정 정지 성공")
                .resultData(1)
                .build();
    }

    @DeleteMapping("{report_pk}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "신고된 항목 삭제",description = "신고 중 하나를 삭제합니다")
    public ResultDto<Integer> delReport(@PathVariable("report_pk") Long reportPk){

        try {
             service.delReport(reportPk);
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

    @PutMapping()
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "답변수정하기",description = "답변을 수정합니다.")
    public ResultDto<Integer> patchAccountSuspension(@RequestBody PutReportForAdminReq p){

        try {
            ReportEntity reportEntity = service.makePutReportEntity(p);
            service.saveReport(reportEntity);
        } catch (Exception e){
            return ResultDto.<Integer>builder()
                    .statusCode(-1)
                    .resultMsg("수정 실패")
                    .build();
        }


        return ResultDto.<Integer>builder()
                .statusCode(1)
                .resultMsg("수정 성공")
                .resultData(1)
                .build();
    }

}
