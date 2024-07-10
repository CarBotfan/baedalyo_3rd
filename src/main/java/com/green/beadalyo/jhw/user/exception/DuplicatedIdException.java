package com.green.beadalyo.jhw.user.exception;

public class DuplicatedIdException extends RuntimeException{
    public DuplicatedIdException() {
        super("중복된 아이디입니다");
    }
}
