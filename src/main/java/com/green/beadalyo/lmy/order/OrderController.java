package com.green.beadalyo.lmy.order;

import com.green.beadalyo.gyb.common.Result;
import com.green.beadalyo.gyb.common.ResultDto;
import com.green.beadalyo.gyb.common.ResultError;
import com.green.beadalyo.gyb.model.Restaurant;
import com.green.beadalyo.gyb.restaurant.RestaurantService;
import com.green.beadalyo.gyb.sse.SSEApiController;
import com.green.beadalyo.jhw.security.AuthenticationFacade;
import com.green.beadalyo.jhw.user.UserServiceImpl;
import com.green.beadalyo.jhw.user.entity.User;
import com.green.beadalyo.jhw.user.exception.UserNotFoundException;
import com.green.beadalyo.jhw.user.repository.UserRepository;
import com.green.beadalyo.kdh.menu.MenuService;
import com.green.beadalyo.kdh.menu.entity.MenuEntity;
import com.green.beadalyo.kdh.menuoption.MenuOptionService;
import com.green.beadalyo.kdh.menuoption.entity.MenuOption;
import com.green.beadalyo.lhn.coupon.CouponService;
import com.green.beadalyo.lhn.coupon.entity.CouponUser;
import com.green.beadalyo.lmy.dataset.ExceptionMsgDataSet;
import com.green.beadalyo.lmy.doneorder.model.PostOrderRes;
import com.green.beadalyo.lmy.order.entity.Order;
import com.green.beadalyo.lmy.order.entity.OrderMenu;
import com.green.beadalyo.lmy.order.entity.OrderMenuOption;
import com.green.beadalyo.lmy.order.model.OrderGetRes;
import com.green.beadalyo.lmy.order.model.OrderMenuReq;
import com.green.beadalyo.lmy.order.model.OrderMiniGetRes;
import com.green.beadalyo.lmy.order.model.OrderPostReq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;


import static com.green.beadalyo.lmy.dataset.ResponseDataSet.*;



@RestController
@RequestMapping("api/order/")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "주문 관련 컨트롤러")
public class OrderController {
    private final OrderService orderService;
    private final RestaurantService restaurantService;
    private final AuthenticationFacade authenticationFacade;
    private final UserServiceImpl userService;
    private final MenuOptionService menuOptionService ;
    private final MenuService menuService;
    private final UserRepository userRepository;
    private final SSEApiController SSEApiController;
    private final CouponService couponService ;


    @PostMapping
    @Operation(summary = "주문하기")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @ApiResponse(
            description =
                    "<p> 1 : 주문하기 성공 </p>"+
                            "<p> -1 : 결제가 완료되지 않았습니다 </p>"+
                            "<p> -2 : 글 양식을 맞춰주세요 (글자 수) </p>" +
                            "<p> -3 : 메뉴를 찾을 수 없습니다 </p>" +
                            "<p> -4 : 데이터 리스트 생성 실패 </p>" +
                            "<p> -5 : 레스토랑 데이터 조회 실패 </p>" +
                            "<p> -6 : 메뉴 검증 실패 </p>" +
                            "<p> -7 : 유저 검증 실패 </p>" +
                            "<p> -8 : 사용 마일리지가 1000이하 </p>" +
                            "<p> -9 : 후불결제에는 마일리지를 사용할 수 없음 </p>" +
                            "<p> -10 : 유저가 소지한 마일리지가 충분하지 않음. </p>" +
                            "<p> -11 : 쿠폰이 존재하지 않음 </p>" +
                            "<p> -12 : 해당 음식점에서 발급된 쿠폰이 아님 </p>" +
                            "<p> -13 : 쿠폰을 소유한 대상이 아님 </p>" +
                            "<p> -14 : 쿠폰 최소 금액이 충족되지않음. </p>"
    )
    @Transactional
    public Result postOrder(@RequestBody OrderPostReq p)
    {

        //유저 검증
        Long userPk = authenticationFacade.getLoginUserPk();

        User user = null;
        try {
            user = userService.getUser(userPk);

        } catch (UserNotFoundException e) {
            return ResultError.builder().resultMsg("유저 정보를 찾을수 없습니다.").statusCode(-7).build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultError.builder().build();
        }

        //결제수단 검증
        if (p.getPaymentMethod() == null) {
            return ResultError.builder()
                    .statusCode(ExceptionMsgDataSet.PAYMENT_METHOD_ERROR.getCode())
                    .resultMsg(ExceptionMsgDataSet.PAYMENT_METHOD_ERROR.getMessage())
                    .build();
        }
        //주문요구사항 길이 검증
        if (500 < p.getOrderRequest().length()) {
            return ResultError.builder()
                    .statusCode(ExceptionMsgDataSet.STRING_LENGTH_ERROR.getCode())
                    .resultMsg(ExceptionMsgDataSet.STRING_LENGTH_ERROR.getMessage())
                    .build();
        }
        //후불결제에 마일리지가 들어왔는지 검증
        if (p.getPaymentMethod() == 1 || p.getPaymentMethod() == 2)
            if (p.getUseMileage() > 0) {
                return ResultError.builder().statusCode(-9).resultMsg("후불결제에는 마일리지를 사용할 수 없습니다.").build();
            }
        //마일리지 사용시 1000마일리지 미만인지 검증
        if (p.getUseMileage() != 0 && p.getUseMileage() < 1000)
            return ResultError.builder().statusCode(-8).resultMsg("사용 마일리지가 정상적이지 않습니다.").build();
        //유저가 소유한 마일리지값 검증
        if (user.getMileage() > p.getUseMileage())
            return ResultError.builder().statusCode(-10).resultMsg("소지한 마일리지가 충분하지 않습니다.").build();

        //레스토랑 검증
        final Restaurant res  ;
        try {
             res = restaurantService.getRestaurantDataBySeq(p.getOrderResPk());

        } catch (NullPointerException e) {
            return ResultError.builder().resultMsg("음식점이 존재하지 않습니다.").statusCode(-5).build() ;
        } catch (Exception e) {
            log.error("An error occurred: ", e);
            return ResultError.builder().build();
        }


//        //메뉴 검증
//        try {
//            boolean checksum = p.getMenu().stream()
//                    //req 의 orderMenuPk 를 추출
//                    .map(OrderMenuReq::getMenuPk)
//                    //추출한 값을 menuOptionPk 에 세팅후 검증실행
//                    .allMatch(menuOptionPk ->
//                            //res 의 메뉴 리스트를 스트림하여
//                            res.getMenuList().forEach(
//                                    menuCategory ->
//                                            menuCategory.getMenuList().stream().map(MenuEntity::getMenuPk)
//                                            .anyMatch(menuPk -> menuPk.equals(menuOptionPk)));
//                                    )
//                            // 추출한 두 값을 비교하여 매치
//
//
//            if (!checksum) return ResultError.builder().statusCode(-6).resultMsg("메뉴 검증에 실패하였습니다.").build();

//        } catch (Exception e) {
//            log.error("An error occurred: ", e);
//            return ResultError.builder().build();
//        }

        try {
            //오더 객체 생성
            Order order = new Order(p, user, res);
            order.setUseMileage(p.getUseMileage());
            AtomicReference<Integer> totalPrice = new AtomicReference<>(order.getOrderPrice());
            //받은 pk 리스트를 기반으로 메뉴 pk 조회
            List<MenuEntity> menu = menuService.getInMenuData(
                    p.getMenu().stream()
                    .map(OrderMenuReq::getMenuPk)
                    .toList());
            //메뉴 Entity 를 OrderMenu 로 전환
            List<OrderMenu> orderMenus = OrderMenu.toOrderMenuList(menu, order, p.getMenu());
            //order 에 세팅
            order.setMenus(orderMenus);
            for (OrderMenu i : orderMenus)
            {
                totalPrice.updateAndGet(v -> v + (i.getMenuPrice() * i.getMenuCount()));
            }
            //메뉴 옵션 설정
            p.getMenu().forEach(i ->
                orderMenus.stream()
                    .filter(j -> i.getMenuPk().equals(j.getOrderMenuPk()))
                    .findFirst()
                    .ifPresent(j -> {
                        List<MenuOption> optionList = menuOptionService.getListIn(i.getMenuOptionPk());
                        List<OrderMenuOption> orderMenu = OrderMenuOption.toOrderMenuOptionList(optionList, j);
                        for (OrderMenuOption o : orderMenu)
                        {
                            totalPrice.updateAndGet(v -> v + (o.getOptionPrice() * j.getMenuCount()));
                        }
                        j.setOrderMenuOption(orderMenu);
                    })
            );

            Integer lastPrice = totalPrice.get();

            //쿠폰 검증
            if (p.getCoupon() != null)
            {
                try {
                    //존재하는지 검증
                    CouponUser cp = couponService.getCouponUserByPk(p.getCoupon());
                    //발급된 음식점이 맞는지 검증
                    if (cp.getCoupon().getRestaurant() != res) return ResultError.builder().resultMsg("쿠폰이 발급된 음식점이 아닙니다.").statusCode(-12).build() ;
                    //쿠폰의 소유자인지 검증
                    if (cp.getUser() != user) return ResultError.builder().resultMsg("쿠폰의 소유주가 아닙니다.").statusCode(-13).build() ;
                    //주문금액이 쿠폰의 최소 금액을 초과했는지 검증
                    if (cp.getCoupon().getMinOrderAmount() > lastPrice) return ResultError.builder().resultMsg("쿠폰의 최소 금액을 충족하지 못했습니다.").statusCode(-14).build() ;
                    // 쿠폰 적용
                    lastPrice -= cp.getCoupon().getPrice() ;
                    cp.setState(2);
                    //사용한 쿠폰 값 저장
                    couponService.save(cp) ;
                } catch (NullPointerException e) {
                    return ResultError.builder().resultMsg("쿠폰이 존재하지 않습니다.").statusCode(-11).build();
                } catch (Exception e){
                    return ResultError.builder().build();
                }
            }

            //마일리지 실 사용 처리
            lastPrice -= p.getUseMileage() ;
            user.setMileage(user.getMileage()-p.getUseMileage());
            order.setOrderPrice(totalPrice.get());
            order.setTotalPrice(lastPrice);

            //데이터 저장
            userService.save(user);
            orderService.saveOrder(order) ;

            if (order.getOrderState() == 2) SSEApiController.sendEmitters("OrderRequest", order.getOrderRes().getUser());
            return ResultDto.builder().resultData(new PostOrderRes(order.getOrderPrice(), order.getTotalPrice(), order.getOrderPk())).build();


        } catch (Exception e) {
            log.error("An error occurred: ", e);
            return ResultError.builder().build();
        }




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
    }

    @PutMapping("cancel/list/{order_pk}")
    @Operation(summary = "주문취소 하기")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN','OWNER')")
    @ApiResponse(
            description =
                    "<p> 1 : 주문 취소 성공 </p>"+
                            "<p> -8 : 주문 취소 할 데이터가 없음 </p>" +
                            "<p> -9 : 접수전의 주문은 주문자, 상점주인만 취소 가능합니다 </p>" +
                            "<p> -10 : 접수중인 주문은 상점 주인만 취소 가능합니다 </p>" +
                            "<p> -5 : 주문 취소 실패 </p>"
    )
    @Transactional
    public Result cancelOrder(@PathVariable("order_pk") Long orderPk) {

        long userPk = authenticationFacade.getLoginUserPk();

        try {
            if (orderService.getOrderByOrderPk(orderPk).getOrderState() == 1) {
                if (userPk != orderService.getOrderByOrderPk(orderPk).getOrderRes().getUser().getUserPk()
                        && userPk != orderService.getOrderByOrderPk(orderPk).getOrderUserPk().getUserPk()) {
                    return ResultDto.<Integer>builder()
                            .statusCode(ExceptionMsgDataSet.NO_NON_CONFIRM_CANCEL_AUTHENTICATION.getCode())
                            .resultMsg(ExceptionMsgDataSet.NO_NON_CONFIRM_CANCEL_AUTHENTICATION.getMessage())
                            .build();
                }
            } else if (orderService.getOrderByOrderPk(orderPk).getOrderState() == 2) {
                if (userPk != orderService.getOrderByOrderPk(orderPk).getOrderRes().getUser().getUserPk()) {
                    return ResultDto.<Integer>builder()
                            .statusCode(ExceptionMsgDataSet.NO_CONFIRM_CANCEL_AUTHENTICATION.getCode())
                            .resultMsg(ExceptionMsgDataSet.NO_CONFIRM_CANCEL_AUTHENTICATION.getMessage()).build();
                }
            }
        } catch (EntityNotFoundException e) {
            return  ResultError.builder().resultMsg("취소할 데이터가 존재하지 않습니다.").statusCode(-8).build();
        } catch (Exception e) {
            log.error("An error occurred: ", e);
            return ResultError.builder().build();
        }


        Order order = orderService.getOrderEntity(orderPk);
        orderService.saveDoneOrder(order, userPk,2);

//        List<OrderMenu> orderMenuList = orderService.getOrderMenuEntities(orderPk);
//        orderService.saveDoneOrderMenuBatch(orderMenuList, doneOrder);

        orderService.deleteOrder(orderPk);

        return ResultDto.<Integer>builder()
                .statusCode(SUCCESS_CODE)
                .resultMsg(CANCEL_ORDER_SUCCESS)
                .resultData(1)
                .build();
    }

    @PutMapping("owner/done/{order_pk}")
    @Operation(summary = "주문완료 하기")
    @PreAuthorize("hasAnyRole('ADMIN','OWNER')")
    @ApiResponse(
            description =
                    "<p> 1 : 주문 완료 성공 </p>"+
                            "<p> -8 : 상점 주인의 접근이 아닙니다 </p>"
    )
    public ResultDto<Integer> completeOrder(@PathVariable("order_pk") Long orderPk) {
        int result = -1;

        long userPk = authenticationFacade.getLoginUserPk();
        if (userPk != orderService.getOrderByOrderPk(orderPk).getOrderRes().getUser().getUserPk()) {
            return ResultDto.<Integer>builder()
                    .statusCode(ExceptionMsgDataSet.NO_AUTHENTICATION.getCode())
                    .resultMsg(ExceptionMsgDataSet.NO_AUTHENTICATION.getMessage()).build();
        }

        Order order = orderService.getOrderEntity(orderPk);
        orderService.saveDoneOrder(order, userPk,1);

//        List<OrderMenu> orderMenuList = orderService.getOrderMenuEntities(orderPk);
//        orderService.saveDoneOrderMenuBatch(orderMenuList, doneOrder);

        orderService.deleteOrder(orderPk);


        return ResultDto.<Integer>builder()
                .statusCode(SUCCESS_CODE)
                .resultMsg(COMPLETE_ORDER_SUCCESS)
                .resultData(result)
                .build();
    }

    @GetMapping("user/list")
    @Operation(summary = "유저의 진행중인 주문 불러오기")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @ApiResponse(
            description =
                    "<p> 1 : 유저의 진행중인 주문 불러오기 완료 </p>"+
                            "<p> -6 : 주문 정보 불러오기 실패 </p>"+
                            "<p> -7 : 불러올 주문 정보가 없음 </p>"
    )
    public ResultDto<List<OrderMiniGetRes>> getUserOrderList() {
        List<OrderMiniGetRes> result = null;

        try {
            result = orderService.getUserOrderList();
        } catch (RuntimeException e) {
            return ResultDto.<List<OrderMiniGetRes>>builder()
                    .statusCode(ExceptionMsgDataSet.GET_ORDER_LIST_FAIL.getCode())
                    .resultMsg(ExceptionMsgDataSet.GET_ORDER_LIST_FAIL.getMessage()).build();
        }

        if (result == null || result.isEmpty()) {
            return ResultDto.<List<OrderMiniGetRes>>builder()
                    .statusCode(ExceptionMsgDataSet.GET_ORDER_LIST_NON.getCode())
                    .resultMsg(ExceptionMsgDataSet.GET_ORDER_LIST_NON.getMessage()).build();
        }

        return ResultDto.<List<OrderMiniGetRes>>builder()
                .statusCode(SUCCESS_CODE)
                .resultMsg(USER_ORDER_LIST_SUCCESS)
                .resultData(result)
                .build();
    }

    @GetMapping("owner/noconfirm/list")
    @Operation(summary = "상점의 접수 전 주문정보 불러오기")
    @PreAuthorize("hasAnyRole('ADMIN','OWNER')")
    @ApiResponse(
            description =
                    "<p> 1 : 상점의 접수 전 주문정보 불러오기 완료 </p>"+
                            "<p> -8 : 상점 주인의 접근이 아닙니다 </p>"+
                            "<p> -7 : 불러올 주문 정보가 없음 </p>"
    )
    public ResultDto<List<OrderMiniGetRes>> getResNonConfirmOrderList() {
        List<OrderMiniGetRes> result = null;

        long userPk = authenticationFacade.getLoginUserPk();

        Restaurant resPk = null;
        try {
            resPk = restaurantService.getRestaurantData(userRepository.getReferenceById(userPk));
        } catch (Exception e) {
            return ResultDto.<List<OrderMiniGetRes>>builder()
                    .statusCode(ExceptionMsgDataSet.NO_AUTHENTICATION.getCode())
                    .resultMsg(ExceptionMsgDataSet.NO_AUTHENTICATION.getMessage())
                    .build();
        }

        if (userPk != resPk.getUser().getUserPk()){
            return ResultDto.<List<OrderMiniGetRes>>builder()
                    .statusCode(ExceptionMsgDataSet.NO_AUTHENTICATION.getCode())
                    .resultMsg(ExceptionMsgDataSet.NO_AUTHENTICATION.getMessage())
                    .build();
        }

        result = orderService.getResNonConfirmOrderList(resPk.getSeq());


        if (result == null || result.isEmpty()) {
            return ResultDto.<List<OrderMiniGetRes>>builder()
                    .statusCode(ExceptionMsgDataSet.GET_ORDER_LIST_NON.getCode())
                    .resultMsg(ExceptionMsgDataSet.GET_ORDER_LIST_NON.getMessage())
                    .build();
        }

        return ResultDto.<List<OrderMiniGetRes>>builder()
                .statusCode(SUCCESS_CODE)
                .resultMsg(RES_ORDER_NO_CONFIRM_LIST_SUCCESS)
                .resultData(result)
                .build();
    }

    @GetMapping("owner/confirm/list")
    @Operation(summary = "상점의 접수 후 주문정보 불러오기")
    @PreAuthorize("hasAnyRole('ADMIN','OWNER')")
    @ApiResponse(
            description =
                    "<p> 1 : 상점의 접수 후 주문정보 불러오기 완료 </p>"+
                            "<p> -8 : 상점 주인의 접근이 아닙니다 </p>"+
                            "<p> -7 : 불러올 주문 정보가 없음 </p>"
    )
    public ResultDto<List<OrderMiniGetRes>> getResConfirmOrderList() {
        List<OrderMiniGetRes> result = null;

        long userPk = authenticationFacade.getLoginUserPk();

        Restaurant resPk = null;
        try {
            resPk = restaurantService.getRestaurantData(userRepository.getReferenceById(userPk));
        } catch (Exception e) {
            return ResultDto.<List<OrderMiniGetRes>>builder()
                    .statusCode(ExceptionMsgDataSet.NO_AUTHENTICATION.getCode())
                    .resultMsg(ExceptionMsgDataSet.NO_AUTHENTICATION.getMessage())
                    .build();
        }

        if (userPk != resPk.getUser().getUserPk()){
            return ResultDto.<List<OrderMiniGetRes>>builder()
                    .statusCode(ExceptionMsgDataSet.NO_AUTHENTICATION.getCode())
                    .resultMsg(ExceptionMsgDataSet.NO_AUTHENTICATION.getMessage())
                    .build();
        }

            result = orderService.getResConfirmOrderList(resPk.getSeq());

        if (result == null || result.isEmpty()) {
            return ResultDto.<List<OrderMiniGetRes>>builder()
                    .statusCode(ExceptionMsgDataSet.GET_ORDER_LIST_NON.getCode())
                    .resultMsg(ExceptionMsgDataSet.GET_ORDER_LIST_NON.getMessage())
                    .build();
        }

        return ResultDto.<List<OrderMiniGetRes>>builder()
                .statusCode(SUCCESS_CODE)
                .resultMsg(RES_ORDER_CONFIRM_LIST_SUCCESS)
                .resultData(result)
                .build();
    }

    @GetMapping("{order_pk}")
    @Operation(summary = "주문정보 상세보기")
    @ApiResponse(
            description =
                    "<p> 1 : 주문정보 상세보기 완료 </p>"+
                            "<p> -6 : 주문 정보 불러오기 실패 </p>"+
                            "<p> -7 : 불러올 주문 정보가 없음 </p>"
    )
    public ResultDto<OrderGetRes> getOrderInfo(@PathVariable("order_pk") Long orderPk) {
        OrderGetRes result = null;

        long userPk = authenticationFacade.getLoginUserPk();
        if (userPk != orderService.getOrderByOrderPk(orderPk).getOrderRes().getUser().getUserPk()
                && userPk != orderService.getOrderByOrderPk(orderPk).getOrderUserPk().getUserPk()) {
            return ResultDto.<OrderGetRes>builder()
                    .statusCode(ExceptionMsgDataSet.NO_AUTHENTICATION.getCode())
                    .resultMsg(ExceptionMsgDataSet.NO_AUTHENTICATION.getMessage())
                    .build();
        }

        result = orderService.getOrderInfo(orderPk);

        if (result == null) {
            return ResultDto.<OrderGetRes>builder()
                    .statusCode(ExceptionMsgDataSet.GET_ORDER_LIST_NON.getCode())
                    .resultMsg(ExceptionMsgDataSet.GET_ORDER_LIST_NON.getMessage())
                    .build();
        }

        return ResultDto.<OrderGetRes>builder()
                .statusCode(SUCCESS_CODE)
                .resultMsg(ORDER_INFO_SUCCESS)
                .resultData(result)
                .build();
    }

    @PatchMapping("owner/confirm/{order_pk}")
    @Operation(summary = "주문 접수하기")
    @PreAuthorize("hasAnyRole('ADMIN', 'OWNER')")
    @ApiResponse(
            description =
                    "<p> 1 : 주문 접수 완료 </p>"+
                            "<p> -8 : 상점 주인의 접근이 아닙니다 </p>"
    )
    public ResultDto<Integer> confirmOrder(@PathVariable("order_pk") Long orderPk){
        Integer result = -1;

        long resUserPk = authenticationFacade.getLoginUserPk();
        if (resUserPk != orderService.getOrderByOrderPk(orderPk).getOrderRes().getUser().getUserPk()) {
            return ResultDto.<Integer>builder()
                    .statusCode(ExceptionMsgDataSet.NO_AUTHENTICATION.getCode())
                    .resultMsg(ExceptionMsgDataSet.NO_AUTHENTICATION.getMessage())
                    .build();
        }

        result = orderService.confirmOrder(orderPk);

        return ResultDto.<Integer>builder()
                .statusCode(SUCCESS_CODE)
                .resultMsg(CONFIRM_ORDER_SUCCESS)
                .resultData(result)
                .build();
    }
}

