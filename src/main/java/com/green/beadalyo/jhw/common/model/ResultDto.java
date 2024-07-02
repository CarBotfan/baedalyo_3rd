package com.green.beadalyo.jhw.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResultDto<T> {
    private int statusCode;
    private String resultMsg;
    private T result;
}
