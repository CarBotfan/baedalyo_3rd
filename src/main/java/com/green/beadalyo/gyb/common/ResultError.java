package com.green.beadalyo.gyb.common;

import lombok.Builder;

@Builder
public class ResultError implements Result
{
    @Builder.Default
    private Integer statusCode = -1 ;
    @Builder.Default
    private String resultMsg = "처리에 실패하였습니다." ;
}
