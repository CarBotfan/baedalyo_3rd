package com.green.beadalyo.gyb.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResultDto<T> implements Result
{
    @Builder.Default
    @Schema(description = "코드 번호")
    private Integer statusCode = 1;
    @Builder.Default
    @Schema(description = "메시지")
    private String resultMsg = "정상처리 되었습니다.";
    @Schema(description = "데이터")
    private T resultData;
}
