package com.green.beadalyo.jhw.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResultDto<T> {
    private HttpStatus statusCode;
    private String ResultMsg;
    private T Result;
}
