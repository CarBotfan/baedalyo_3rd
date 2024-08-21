package com.green.beadalyo.kdh.report.user.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.green.beadalyo.jhw.user.entity.User;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PutReportForUserReq {
    private long reportPk;
    @NotBlank(message = "제목을 비우면 안됩니다.")
    private String reportTitle;
    private String reportContent;

    @JsonIgnore
    private User user;
}
