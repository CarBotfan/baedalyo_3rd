package com.green.beadalyo.jhw.user.exception;

public class DuplicatedInfoException extends RuntimeException {
    public DuplicatedInfoException(String key) {
        super(String.format("중복된 %s입니다.", key));
    }
}
