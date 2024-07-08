package com.green.beadalyo.common.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResultDto<T> {
    @Schema(example = "2")
    private int statusCode;
    @Schema(example = "완료")
    private String resultMsg;
    private T resultData;
}
