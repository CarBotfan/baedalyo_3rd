package com.green.beadalyo.kdh.report.admin.model;

import lombok.Data;

import java.util.List;

@Data
public class ReportListDto {
    private int totalElements;
    private int totalPage;
    private List<GetReportListResForAdmin> result;
}
