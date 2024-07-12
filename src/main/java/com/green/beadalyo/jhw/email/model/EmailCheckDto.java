package com.green.beadalyo.jhw.email.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailCheckDto {
    @NotEmpty(message = "이메일을 입력해주세요.")
    @Email
    private String email;
    private String authNum;
}
