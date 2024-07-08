package com.green.beadalyo.jhw.user.exception;

public class IncorrectPwException extends RuntimeException{
    public IncorrectPwException() {
        super("비밀번호가 일치하지 않습니다.");
    }
}
