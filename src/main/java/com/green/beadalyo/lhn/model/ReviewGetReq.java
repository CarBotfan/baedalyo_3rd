package com.green.beadalyo.lhn.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ReviewGetReq {
    @Schema(example = "2", description = "식당의 고유 Pk")
    private long resPk;


}

