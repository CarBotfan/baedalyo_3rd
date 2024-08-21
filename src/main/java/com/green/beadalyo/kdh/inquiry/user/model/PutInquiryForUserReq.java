package com.green.beadalyo.kdh.inquiry.user.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.green.beadalyo.jhw.user.entity.User;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PutInquiryForUserReq {

    private Long inquiryPk;

    @NotBlank(message = "제목을 비우면 안됩니다.")
    private String inquiryTitle;

    private String inquiryContent;

    @JsonIgnore
    private User user;

}
