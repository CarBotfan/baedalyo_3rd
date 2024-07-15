package com.green.beadalyo.jhw.user.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserPicPatchReq {
    @JsonIgnore
    private long signedUserPk;
    @JsonIgnore
    @Pattern(regexp = "^[a-zA-Z0-9_.-]+(\\.[a-zA-Z0-9_.-]+)*\\.(?i)(jpg|jpeg|png|gif|bmp|tiff|svg|webp)$")
    private String picName;
}
