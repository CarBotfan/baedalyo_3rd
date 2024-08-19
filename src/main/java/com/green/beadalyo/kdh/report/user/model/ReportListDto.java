package com.green.beadalyo.kdh.report.user.model;

import com.green.beadalyo.kdh.report.admin.model.GetReportListResForAdmin;
import lombok.Data;

import java.util.List;

@Data
public class ReportListDto {
    private int totalElements;
    private int totalPage;
    private List<GetReportListResForUser> result;
}
