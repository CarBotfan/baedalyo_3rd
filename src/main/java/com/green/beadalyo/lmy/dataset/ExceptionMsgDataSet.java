package com.green.beadalyo.lmy.dataset;

import lombok.Getter;

@Getter
public enum ExceptionMsgDataSet {
    PAYMENT_METHOD_ERROR(-1,"결제가 완료되지 않았습니다"),
    STRING_LENGTH_ERROR(-2, "글 양식을 맞춰주세요 (글자 수)"),
    MENU_NOT_FOUND_ERROR(-3, "메뉴를 찾을 수 없습니다"),
    DATALIST_FAIL_ERROR(-4, "데이터 리스트 생성 실패"),

    ORDER_CANCEL_FAIL(-5, "주문 취소 실패"),

    GET_ORDER_LIST_FAIL(-6, "주문 정보 불러오기 실패"),
    GET_ORDER_LIST_NON(-7, "불러올 주문 정보가 없음"),

    NO_AUTHENTICATION(-8, "상점 주인의 접근이 아닙니다"),

    NO_NON_CONFIRM_CANCEL_AUTHENTICATION(-9, "접수전의 주문은 주문자, 상점주인만 취소 가능합니다"),
    NO_CONFIRM_CANCEL_AUTHENTICATION(-10, "접수중인 주문은 상점 주인만 취소 가능합니다");


    private Integer code;
    private String message;

    private ExceptionMsgDataSet(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
