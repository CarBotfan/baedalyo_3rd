package com.green.beadalyo.kdh.report.user.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.green.beadalyo.jhw.user.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PutReportForUserReq {
    private long reportPk;
    private String reportTitle;
    private String reportContent;

    @JsonIgnore
    private User user;
}
