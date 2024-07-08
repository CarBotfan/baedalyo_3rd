package com.green.beadalyo.gyb.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultError implements Result
{
    @Builder.Default
    private Integer statusCode = -1 ;
    @Builder.Default
    private String resultMsg = "처리에 실패하였습니다." ;
}
