package com.green.beadalyo.jhw.menucategory.exception;

public class MenuCatNotFoundException extends RuntimeException{
    public MenuCatNotFoundException() {
        super("메뉴 카테고리 정보 조회에 실패했습니다.");
    }
}
