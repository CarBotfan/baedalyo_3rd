package com.green.beadalyo.jhw.user.exception;

public class NullCookieException extends RuntimeException{
    public NullCookieException() {
        super("유효하지 않은 쿠키입니다.");
    }
}
