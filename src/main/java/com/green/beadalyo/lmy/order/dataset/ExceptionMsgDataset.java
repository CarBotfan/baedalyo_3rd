package com.green.beadalyo.lmy.order.dataset;

public interface ExceptionMsgDataset {
    String PAYMENT_METHOD_ERROR = "결제가 완료되지 않았습니다";
    String STRING_LENGTH_ERROR = "글 양식을 맞춰주세요 (글자 수)";
    String MENU_NOT_FOUND_ERROR = "메뉴를 찾을 수 없습니다";
    String DATALIST_FAIL_ERROR = "데이터 리스트 생성 실패";

    String ORDER_CANCEL_FAIL = "주문 취소 실패";

    String GET_ORDER_LIST_FAIL = "주문 정보 불러오기 실패";
    String GET_ORDER_LIST_NON = "불러올 주문 정보가 없음";

    String NO_AUTHENTICATION = "상점 주인의 접근이 아닙니다";

    String NO_NON_CONFIRM_CANCEL_AUTHENTICATION = "접수전의 주문은 주문자, 상점주인만 취소 가능합니다";
    String NO_CONFIRM_CANCEL_AUTHENTICATION = "접수중인 주문은 상점 주인만 취소 가능합니다";
}
