package com.green.beadalyo.kdh.report.user.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GetReportListResForUser {
    private Long reportPk;
    private String reportTitle;
    private Integer reportState;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;

    public GetReportListResForUser(Long reportPk, String reportTitle, Integer reportState, LocalDateTime updatedAt,
                                   LocalDateTime createdAt) {
        this.reportPk = reportPk;
        this.reportTitle = reportTitle;
        this.reportState = reportState;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
    }
}

