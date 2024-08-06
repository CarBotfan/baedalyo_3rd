package com.green.beadalyo.lhn.Review.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ReviewGetReq {
    @Schema(defaultValue = "1")
    private Long resPk;
    @Schema(defaultValue = "1")
    private Integer page;
}
