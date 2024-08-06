package com.green.beadalyo.kdh.admin.report.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.green.beadalyo.jhw.user.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PatchAccountSuspensionReq {
    private Integer userState;

    private String userBlockDate;

    private Long userPk;

    @JsonIgnore
    private User user;
}
