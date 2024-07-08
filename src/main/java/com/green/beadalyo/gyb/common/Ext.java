package com.green.beadalyo.gyb.common;

public enum Ext
{
    JPG(".jpg"),
    JPEG(".jpeg"),
    PNG(".png"),
    GIF(".gif");

    private final String mimeType;

    // 생성자
    Ext(String mimeType) {
        this.mimeType = mimeType;
    }

    // MIME 타입 반환 메서드
    public String toString() {
        return mimeType;
    }
}
