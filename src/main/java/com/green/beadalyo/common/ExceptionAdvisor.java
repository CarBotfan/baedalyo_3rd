package com.green.beadalyo.common;

import com.green.beadalyo.gyb.common.ResultDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ExceptionAdvisor {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResultDto<String> processValidationError (MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        String ErrorMessage = "";
        StringBuilder sb = new StringBuilder();
        for(FieldError fieldError : bindingResult.getFieldErrors()) {
            sb.append(fieldError.getField());
            sb.append(" error : ");
            ErrorMessage = fieldError.getDefaultMessage();
            sb.append(ErrorMessage);
            sb.append("\n");
        }
        return ResultDto.<String>builder()
                .statusCode(-6)
                .resultMsg(ErrorMessage)
                .resultData(sb.toString()).build();
    }
}
