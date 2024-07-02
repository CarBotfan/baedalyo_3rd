package com.green.beadalyo.gyb.common;

import lombok.Builder;

@Builder
public class ResultEnum implements Result
{
    @Builder.Default
    private Integer statusCode = 1 ;
    @Builder.Default
    private String resultMsg = "정상처리 되었습니다." ;
}
