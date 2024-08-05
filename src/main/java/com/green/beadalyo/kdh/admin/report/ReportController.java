package com.green.beadalyo.kdh.admin.report;

import com.green.beadalyo.common.model.ResultDto;
import com.green.beadalyo.kdh.admin.report.model.GetReportListResInterface;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/admin/report")
@RequiredArgsConstructor
@Tag(name = "어드민 신고 관리")
public class ReportController {
    private final ReportService service;


    @GetMapping("report_list")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "신고된 리뷰들 목록 불러오기",description = "처리 + 비처리 다 불러옵니다.")
    public ResultDto<List<GetReportListResInterface>> getReportList(){
        List<GetReportListResInterface> List = service.getReportList();

        return ResultDto.<List<GetReportListResInterface>>builder()
                .statusCode(1)
                .resultMsg("리스트 불러오기 성공")
                .resultData(List)
                .build();
    }

    @GetMapping("report_list_finished")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "신고된 리뷰들 목록 불러오기",description = "처리된 신고를 다 불러옵니다.")
    public ResultDto<List<GetReportListResInterface>> getReportFinishedList(){
        List<GetReportListResInterface> List = service.getReportFinishedList();

        return ResultDto.<List<GetReportListResInterface>>builder()
                .statusCode(1)
                .resultMsg("리스트 불러오기 성공")
                .resultData(List)
                .build();
    }

    @GetMapping("report_list_unfinished")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "신고된 리뷰들 목록 불러오기",description = "비처리된 신고를 다 불러옵니다.")
    public ResultDto<List<GetReportListResInterface>> getReportUnFinishedList(){
        List<GetReportListResInterface> List = service.getReportUnFinishedList();

        return ResultDto.<List<GetReportListResInterface>>builder()
                .statusCode(1)
                .resultMsg("리스트 불러오기 성공")
                .resultData(List)
                .build();
    }
}
