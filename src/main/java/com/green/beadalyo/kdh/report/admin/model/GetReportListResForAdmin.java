package com.green.beadalyo.kdh.report.admin.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GetReportListResForAdmin {
    private Long reportPk;
    private String reportTitle;
    private Integer reportState;
    private LocalDateTime updatedAt;
    private String reportUserNickName;
    private LocalDateTime createdAt;

    public GetReportListResForAdmin(Long reportPk, String reportTitle,
                                    Integer reportState, LocalDateTime updatedAt,
                                    String reportUserNickName, LocalDateTime createdAt) {
        this.reportPk = reportPk;
        this.reportTitle = reportTitle;
        this.reportState = reportState;
        this.updatedAt = updatedAt;
        this.reportUserNickName = reportUserNickName;
        this.createdAt = createdAt;
    }
}
