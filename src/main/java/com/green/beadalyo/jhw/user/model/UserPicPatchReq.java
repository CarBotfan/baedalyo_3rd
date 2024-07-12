package com.green.beadalyo.jhw.user.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserPicPatchReq {
    @JsonIgnore
    private long signedUserPk;
    @Schema(defaultValue = "원래 사진 파일명")
    private String orignalPicName;
    @JsonIgnore
    private String picName;
}
