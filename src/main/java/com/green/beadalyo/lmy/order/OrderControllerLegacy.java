package com.green.beadalyo.lmy.order;

public class OrderControllerLegacy
{

    //        List<Map<String, Object>> menuList = orderService.getMenuDetails(p.getMenuPk());
//        p.setOrderUserPk(authenticationFacade.getLoginUserPk());
//        int totalPrice = orderService.calculateTotalPrice(p.getMenuPk());
//        p.setOrderPrice(totalPrice);
//
//        Order order = orderService.saveOrder(p);
//
//        List<Map<String, Object>> orderMenuList = orderService.createOrderMenuList(p, menuList);
//
//        orderService.saveOrderMenuBatch(orderMenuList, order);
//
//        return ResultDto.<Long>builder()
//                .statusCode(SUCCESS_CODE)
//                .resultMsg(POST_ORDER_SUCCESS)
//                .resultData(order.getOrderPk())
//                .build();


//    @PutMapping("cancel/list/{order_pk}")
//    @Operation(summary = "주문취소 하기")
//    @PreAuthorize("hasAnyRole('USER', 'ADMIN','OWNER')")
//    @ApiResponse(
//            description =
//                    "<p> 1 : 주문 취소 성공 </p>"+
//                            "<p> -8 : 주문 취소 할 데이터가 없음 </p>" +
//                            "<p> -9 : 접수전의 주문은 주문자, 상점주인만 취소 가능합니다 </p>" +
//                            "<p> -10 : 접수중인 주문은 상점 주인만 취소 가능합니다 </p>" +
//                            "<p> -5 : 주문 취소 실패 </p>"
//    )
//    @Transactional
//    public Result cancelOrder(@PathVariable("order_pk") Long orderPk) {
//
//        long userPk = authenticationFacade.getLoginUserPk();
//
//        try {
//            if (orderService.getOrderByOrderPk(orderPk).getOrderState() == 1) {
//                if (userPk != orderService.getOrderByOrderPk(orderPk).getOrderRes().getUser().getUserPk()
//                        && userPk != orderService.getOrderByOrderPk(orderPk).getOrderUserPk().getUserPk()) {
//                    return ResultDto.<Integer>builder()
//                            .statusCode(ExceptionMsgDataSet.NO_NON_CONFIRM_CANCEL_AUTHENTICATION.getCode())
//                            .resultMsg(ExceptionMsgDataSet.NO_NON_CONFIRM_CANCEL_AUTHENTICATION.getMessage())
//                            .build();
//                }
//            } else if (orderService.getOrderByOrderPk(orderPk).getOrderState() == 2) {
//                if (userPk != orderService.getOrderByOrderPk(orderPk).getOrderRes().getUser().getUserPk()) {
//                    return ResultDto.<Integer>builder()
//                            .statusCode(ExceptionMsgDataSet.NO_CONFIRM_CANCEL_AUTHENTICATION.getCode())
//                            .resultMsg(ExceptionMsgDataSet.NO_CONFIRM_CANCEL_AUTHENTICATION.getMessage()).build();
//                }
//            }
//        } catch (EntityNotFoundException e) {
//            return  ResultError.builder().resultMsg("취소할 데이터가 존재하지 않습니다.").statusCode(-8).build();
//        } catch (Exception e) {
//            log.error("An error occurred: ", e);
//            return ResultError.builder().build();
//        }
//
//
//        Order order = orderService.getOrderEntity(orderPk);
//        orderService.saveDoneOrder(order, userPk,2);
//
////        List<OrderMenu> orderMenuList = orderService.getOrderMenuEntities(orderPk);
////        orderService.saveDoneOrderMenuBatch(orderMenuList, doneOrder);
//
//        orderService.deleteOrder(orderPk);
//
//        return ResultDto.<Integer>builder()
//                .statusCode(SUCCESS_CODE)
//                .resultMsg(CANCEL_ORDER_SUCCESS)
//                .resultData(1)
//                .build();
//    }

//    @PutMapping("owner/done/{order_pk}")
//    @Operation(summary = "주문완료 하기")
//    @PreAuthorize("hasAnyRole('ADMIN','OWNER')")
//    @ApiResponse(
//            description =
//                    "<p> 1 : 주문 완료 성공 </p>"+
//                            "<p> -8 : 상점 주인의 접근이 아닙니다 </p>"
//    )
//    public ResultDto<Integer> completeOrder(@PathVariable("order_pk") Long orderPk) {
//        int result = -1;
//
//        long userPk = authenticationFacade.getLoginUserPk();
//        if (userPk != orderService.getOrderByOrderPk(orderPk).getOrderRes().getUser().getUserPk()) {
//            return ResultDto.<Integer>builder()
//                    .statusCode(ExceptionMsgDataSet.NO_AUTHENTICATION.getCode())
//                    .resultMsg(ExceptionMsgDataSet.NO_AUTHENTICATION.getMessage()).build();
//        }
//
//        Order order = orderService.getOrderEntity(orderPk);
//        orderService.saveDoneOrder(order, userPk,1);
//
////        List<OrderMenu> orderMenuList = orderService.getOrderMenuEntities(orderPk);
////        orderService.saveDoneOrderMenuBatch(orderMenuList, doneOrder);
//
//        orderService.deleteOrder(orderPk);
//
//
//        return ResultDto.<Integer>builder()
//                .statusCode(SUCCESS_CODE)
//                .resultMsg(COMPLETE_ORDER_SUCCESS)
//                .resultData(result)
//                .build();
//    }

    //    @GetMapping("user/list")
//    @Operation(summary = "유저의 진행중인 주문 불러오기")
//    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
//    @ApiResponse(
//            description =
//                    "<p> 1 : 유저의 진행중인 주문 불러오기 완료 </p>"+
//                            "<p> -6 : 주문 정보 불러오기 실패 </p>"+
//                            "<p> -7 : 불러올 주문 정보가 없음 </p>"
//    )
//    public ResultDto<List<OrderMiniGetRes>> getUserOrderList() {
//        List<OrderMiniGetRes> result = null;
//
//        try {
//            result = orderService.getUserOrderList();
//        } catch (RuntimeException e) {
//            return ResultDto.<List<OrderMiniGetRes>>builder()
//                    .statusCode(ExceptionMsgDataSet.GET_ORDER_LIST_FAIL.getCode())
//                    .resultMsg(ExceptionMsgDataSet.GET_ORDER_LIST_FAIL.getMessage()).build();
//        }
//
//        if (result == null || result.isEmpty()) {
//            return ResultDto.<List<OrderMiniGetRes>>builder()
//                    .statusCode(ExceptionMsgDataSet.GET_ORDER_LIST_NON.getCode())
//                    .resultMsg(ExceptionMsgDataSet.GET_ORDER_LIST_NON.getMessage()).build();
//        }
//
//        return ResultDto.<List<OrderMiniGetRes>>builder()
//                .statusCode(SUCCESS_CODE)
//                .resultMsg(USER_ORDER_LIST_SUCCESS)
//                .resultData(result)
//                .build();
//    }

    //    @GetMapping("owner/noconfirm/list")
//    @Operation(summary = "상점의 접수 전 주문정보 불러오기")
//    @PreAuthorize("hasAnyRole('ADMIN','OWNER')")
//    @ApiResponse(
//            description =
//                    "<p> 1 : 상점의 접수 전 주문정보 불러오기 완료 </p>"+
//                            "<p> -8 : 상점 주인의 접근이 아닙니다 </p>"+
//                            "<p> -7 : 불러올 주문 정보가 없음 </p>"
//    )
//    public ResultDto<List<OrderMiniGetRes>> getResNonConfirmOrderList() {
//        List<OrderMiniGetRes> result = null;
//
//        long userPk = authenticationFacade.getLoginUserPk();
//
//        Restaurant resPk = null;
//        try {
//            resPk = restaurantService.getRestaurantData(userRepository.getReferenceById(userPk));
//        } catch (Exception e) {
//            return ResultDto.<List<OrderMiniGetRes>>builder()
//                    .statusCode(ExceptionMsgDataSet.NO_AUTHENTICATION.getCode())
//                    .resultMsg(ExceptionMsgDataSet.NO_AUTHENTICATION.getMessage())
//                    .build();
//        }
//
//        if (userPk != resPk.getUser().getUserPk()){
//            return ResultDto.<List<OrderMiniGetRes>>builder()
//                    .statusCode(ExceptionMsgDataSet.NO_AUTHENTICATION.getCode())
//                    .resultMsg(ExceptionMsgDataSet.NO_AUTHENTICATION.getMessage())
//                    .build();
//        }
//
//        result = orderService.getResNonConfirmOrderList(resPk.getSeq());
//
//
//        if (result == null || result.isEmpty()) {
//            return ResultDto.<List<OrderMiniGetRes>>builder()
//                    .statusCode(ExceptionMsgDataSet.GET_ORDER_LIST_NON.getCode())
//                    .resultMsg(ExceptionMsgDataSet.GET_ORDER_LIST_NON.getMessage())
//                    .build();
//        }
//
//        return ResultDto.<List<OrderMiniGetRes>>builder()
//                .statusCode(SUCCESS_CODE)
//                .resultMsg(RES_ORDER_NO_CONFIRM_LIST_SUCCESS)
//                .resultData(result)
//                .build();
//    }

    //    @GetMapping("owner/confirm/list")
//    @Operation(summary = "상점의 접수 후 주문정보 불러오기")
//    @PreAuthorize("hasAnyRole('ADMIN','OWNER')")
//    @ApiResponse(
//            description =
//                    "<p> 1 : 상점의 접수 후 주문정보 불러오기 완료 </p>"+
//                            "<p> -8 : 상점 주인의 접근이 아닙니다 </p>"+
//                            "<p> -7 : 불러올 주문 정보가 없음 </p>"
//    )
//    public ResultDto<List<OrderMiniGetRes>> getResConfirmOrderList() {
//        List<OrderMiniGetRes> result = null;
//
//        long userPk = authenticationFacade.getLoginUserPk();
//
//        Restaurant resPk = null;
//        try {
//            resPk = restaurantService.getRestaurantData(userRepository.getReferenceById(userPk));
//        } catch (Exception e) {
//            return ResultDto.<List<OrderMiniGetRes>>builder()
//                    .statusCode(ExceptionMsgDataSet.NO_AUTHENTICATION.getCode())
//                    .resultMsg(ExceptionMsgDataSet.NO_AUTHENTICATION.getMessage())
//                    .build();
//        }
//
//        if (userPk != resPk.getUser().getUserPk()){
//            return ResultDto.<List<OrderMiniGetRes>>builder()
//                    .statusCode(ExceptionMsgDataSet.NO_AUTHENTICATION.getCode())
//                    .resultMsg(ExceptionMsgDataSet.NO_AUTHENTICATION.getMessage())
//                    .build();
//        }
//
//            result = orderService.getResConfirmOrderList(resPk.getSeq());
//
//        if (result == null || result.isEmpty()) {
//            return ResultDto.<List<OrderMiniGetRes>>builder()
//                    .statusCode(ExceptionMsgDataSet.GET_ORDER_LIST_NON.getCode())
//                    .resultMsg(ExceptionMsgDataSet.GET_ORDER_LIST_NON.getMessage())
//                    .build();
//        }
//
//        return ResultDto.<List<OrderMiniGetRes>>builder()
//                .statusCode(SUCCESS_CODE)
//                .resultMsg(RES_ORDER_CONFIRM_LIST_SUCCESS)
//                .resultData(result)
//                .build();
//    }
//    @GetMapping("{order_pk}")
//    @Operation(summary = "주문정보 상세보기")
//    @ApiResponse(
//            description =
//                    "<p> 1 : 주문정보 상세보기 완료 </p>"+
//                            "<p> -6 : 주문 정보 불러오기 실패 </p>"+
//                            "<p> -7 : 불러올 주문 정보가 없음 </p>"
//    )
//    public Result getOrderInfo(@PathVariable("order_pk") Long orderPk) {
//
//        long userPk = authenticationFacade.getLoginUserPk();
//        if (userPk != orderService.getOrderByOrderPk(orderPk).getOrderRes().getUser().getUserPk()
//                && userPk != orderService.getOrderByOrderPk(orderPk).getOrderUserPk().getUserPk()) {
//            return ResultDto.<OrderGetRes>builder()
//                    .statusCode(ExceptionMsgDataSet.NO_AUTHENTICATION.getCode())
//                    .resultMsg(ExceptionMsgDataSet.NO_AUTHENTICATION.getMessage())
//                    .build();
//        }
//
//        try {
//            Order order = orderService.getOrderInfo(orderPk);
//            OrderGetRes o = new OrderGetRes(order) ;
//            return ResultDto.builder().resultData(o).build();
//
//
//        } catch (NullPointerException e) {
//            return ResultError.builder().statusCode(-7).resultMsg("불러올 주문의 정보가 없습니다.").build();
//        } catch (Exception e) {
//            log.error("An error occurred: ", e);
//            return ResultError.builder().build();
//        }
//
//    }
//
//    @PatchMapping("owner/confirm/{order_pk}")
//    @Operation(summary = "주문 접수하기")
//    @PreAuthorize("hasAnyRole('ADMIN', 'OWNER')")
//    @ApiResponse(
//            description =
//                    "<p> 1 : 주문 접수 완료 </p>"+
//                            "<p> -8 : 상점 주인의 접근이 아닙니다 </p>"
//    )
//    public ResultDto<Integer> confirmOrder(@PathVariable("order_pk") Long orderPk){
//        Integer result = -1;
//
//        long resUserPk = authenticationFacade.getLoginUserPk();
//        if (resUserPk != orderService.getOrderByOrderPk(orderPk).getOrderRes().getUser().getUserPk()) {
//            return ResultDto.<Integer>builder()
//                    .statusCode(ExceptionMsgDataSet.NO_AUTHENTICATION.getCode())
//                    .resultMsg(ExceptionMsgDataSet.NO_AUTHENTICATION.getMessage())
//                    .build();
//        }
//
//        result = orderService.confirmOrder(orderPk);
//
//        return ResultDto.<Integer>builder()
//                .statusCode(SUCCESS_CODE)
//                .resultMsg(CONFIRM_ORDER_SUCCESS)
//                .resultData(result)
//                .build();
//    }
}
