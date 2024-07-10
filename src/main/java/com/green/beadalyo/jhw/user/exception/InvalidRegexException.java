package com.green.beadalyo.jhw.user.exception;

public class InvalidRegexException extends RuntimeException {
    public InvalidRegexException() {
        super("올바르지 않은 형식의 데이터입니다.");
    }
}
