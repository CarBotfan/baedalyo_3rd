package com.green.beadalyo.lhn.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.green.beadalyo.jhw.user.entity.User;
import com.green.beadalyo.lhn.entity.Review;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportPostReq {
    private long reviewPk;
    private String reportContent;
    private String created_at;

    @JsonIgnore
    private User user;
    @JsonIgnore
    private Review review;
}
