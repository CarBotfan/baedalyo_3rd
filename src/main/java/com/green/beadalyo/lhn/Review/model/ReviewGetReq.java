package com.green.beadalyo.lhn.Review.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ReviewGetReq {
    @Schema(example = "2", description = "식당의 고유 Pk")
    private long resPk;

    @Schema(example = "1", description = "페이지")
    private Integer page;


}

