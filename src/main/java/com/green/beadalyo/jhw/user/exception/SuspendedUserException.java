package com.green.beadalyo.jhw.user.exception;

import java.time.LocalDate;

public class SuspendedUserException extends RuntimeException{
    public SuspendedUserException(LocalDate date) {
        super(String.format("신고로 인해 %s 까지 정지된 계정입니다.", date));
    }
}
