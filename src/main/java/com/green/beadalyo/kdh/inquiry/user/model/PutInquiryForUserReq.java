package com.green.beadalyo.kdh.inquiry.user.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.green.beadalyo.jhw.user.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PutInquiryForUserReq {

    private Long inquiryPk;

    private String inquiryTitle;

    private String inquiryContent;

    @JsonIgnore
    private User user;

}
