package com.green.beadalyo.jhw.user.exception;

public class PwConfirmFailureException extends RuntimeException{
    public PwConfirmFailureException() {
        super("두 비밀번호가 일치하지 않습니다.");
    }
}
