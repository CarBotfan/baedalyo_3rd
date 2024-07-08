package com.green.beadalyo.jhw.user.exception;

public class FileUploadFailedException extends RuntimeException {
    public FileUploadFailedException() {
        super("파일 업로드 실패");
    }
}
