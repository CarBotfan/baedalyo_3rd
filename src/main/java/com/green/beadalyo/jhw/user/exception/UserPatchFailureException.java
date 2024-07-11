package com.green.beadalyo.jhw.user.exception;

public class UserPatchFailureException extends RuntimeException{
    public UserPatchFailureException() {
        super("유저 정보 수정 실패");
    }
}
