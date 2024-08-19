package com.green.beadalyo.lmy.order;

import com.green.beadalyo.gyb.common.Result;
import com.green.beadalyo.gyb.common.ResultDto;
import com.green.beadalyo.gyb.common.ResultError;
import com.green.beadalyo.gyb.common.exception.DataNotFoundException;
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
import com.green.beadalyo.lmy.doneorder.DoneOrderService;
import com.green.beadalyo.lmy.doneorder.entity.DoneOrder;
import com.green.beadalyo.lmy.doneorder.model.PostOrderRes;
import com.green.beadalyo.lmy.order.entity.Order;
import com.green.beadalyo.lmy.order.entity.OrderMenu;
import com.green.beadalyo.lmy.order.entity.OrderMenuOption;
import com.green.beadalyo.lmy.order.model.OrderMenuReq;
import com.green.beadalyo.lmy.order.model.OrderMiniGetRes;
import com.green.beadalyo.lmy.order.model.OrderPostReq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;


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
    private final DoneOrderService doneOrderService;


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
        if (p.getPaymentMethod() == 1 || p.getPaymentMethod() == 2)
            if (p.getCoupon() != null) {
                return ResultError.builder().statusCode(-9).resultMsg("후불결제에는 쿠폰을 사용할 수 없습니다.").build();
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
//            p.getMenu().forEach(orderMenuReq -> {
//
//            });
//            List<MenuEntity> menu = menuService.getInMenuData()
            //메뉴 Entity 를 OrderMenu 로 전환
            List<OrderMenu> orderMenus = OrderMenu.toOrderMenuList(menu, order, p.getMenu());
            //order 에 세팅
            for (OrderMenu i : orderMenus)
            {
                totalPrice.updateAndGet(v -> v + (i.getMenuPrice() * i.getMenuCount()));
                i.getOrderMenuOption().forEach(o -> {
                    totalPrice.updateAndGet(v -> v+ (o.getOptionPrice() * i.getMenuCount()) ) ;
                }) ;
            }

//            //메뉴 옵션 설정
//            p.getMenu().forEach(i ->
//                orderMenus.stream()
//                    .filter(j -> i.getMenuPk().equals(j.getOrderMenuPk()))
//                    .findFirst()
//                    .ifPresent(q -> {
//                        List<MenuOption> optionList = menuOptionService.getListIn(i.getMenuOptionPk());
//                        List<OrderMenuOption> orderMenu = OrderMenuOption.toOrderMenuOptionList(optionList, q);
//                        for (OrderMenuOption o : orderMenu)
//                        {
//                            totalPrice.updateAndGet(v -> v + (o.getOptionPrice() * q.getMenuCount()));
//                            System.out.println("작동 체크 123");
//                        }
//                        q.setOrderMenuOption(orderMenu);
//
//
//                    })
//            );
            order.setMenus(orderMenus);

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
                    if (lastPrice < 0) lastPrice = 0 ;
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

            //주문금액이 0일시 결제 완료 처리 후 알림 전송
            if (lastPrice < 0)
            {
                order.setOrderState(2);
                SSEApiController.sendEmitters("test",res.getUser());
            }
            //주문 타입이 후불 결제일시 결제 완료 처리 후 알림 전송
            if (order.getPaymentMethod() == 1 || p.getPaymentMethod() == 2)
            {
                order.setOrderState(2);
                SSEApiController.sendEmitters("test",res.getUser());
            }
            //데이터 저장
            userService.save(user);
            orderService.saveOrder(order) ;

            if (order.getOrderState() == 2) SSEApiController.sendEmitters("OrderRequest", order.getOrderRes().getUser());
            return ResultDto.builder().resultData(new PostOrderRes(order.getOrderPrice(), order.getTotalPrice(), order.getOrderPk())).build();


        } catch (Exception e) {
            log.error("An error occurred: ", e);
            return ResultError.builder().build();
        }





    }


    @PutMapping("cancel/list/{order_pk}")
    @Operation(summary = "주문취소 하기")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN','OWNER')")
    @ApiResponse(
            description =
                    "<p> 1 : 주문 취소 성공 </p>"+
                    "<p> -1 : 실패 </p>" +
                    "<p> -2 : 로그인 정보 확인 실패 </p>" +
                    "<p> -3 : 주문 확인 실패 </p>" +
                    "<p> -4 : 주문의 소유주가 아님 </p>" +
                    "<p> -10 : 접수중인 주문은 상점 주인만 취소 가능합니다 </p>"
    )
    public Result cancelOrder(@PathVariable("order_pk") Long orderPk) {
        long userPk = authenticationFacade.getLoginUserPk();
        try {
            //유저 획득
            User user = userService.getUser(userPk);
            Order order = orderService.getOrderInfo(orderPk) ;
            User owner = order.getOrderRes().getUser() ;

            //소유권 체크
            if (order.getOrderUser() != user && user != owner && !user.getUserRole().equals("USER_ADMIN"))
                return ResultError.builder().statusCode(-4).resultMsg("주문의 소유주가 아닙니다.").build();

            //권한 별 기능 구현
            if (order.getOrderState() == 3 && user != owner)
                return ResultError.builder().statusCode(-9).resultMsg("접수 중인 주문은 상점 주인만 취소 가능 합니다.").build();

            DoneOrder doneOrder = new DoneOrder(order) ;
            doneOrder.setDoneOrderState(2);
            doneOrderService.save(doneOrder);
            orderService.deleteOrder(order);

            return ResultDto.builder().resultData(doneOrder.getDoneOrderPk()).build();
        } catch (UserNotFoundException e) {
            return ResultError.builder().statusCode(-2).resultMsg("로그인 정보를 찾지 못했습니다.").build();
        } catch (NullPointerException e) {
            return ResultError.builder().statusCode(-3).resultMsg("주문이 존재하지 않습니다.").build();
        } catch (Exception e) {
            log.error("An error occurred: ", e);
            return ResultError.builder().build();
        }


    }


    @PutMapping("owner/done/{order_pk}")
    @Operation(summary = "주문완료 하기")
    @PreAuthorize("hasAnyRole('ADMIN','OWNER')")
    @ApiResponse(
            description =
                    "<p> 1 : 주문 완료 성공 </p>" +
                    "<p> -1 : 실패 </p>" +
                    "<p> -2 : 로그인 정보 확인 실패 </p>" +
                    "<p> -3 : 주문 확인 실패 </p>" +
                    "<p> -8 : 상점 주인의 접근이 아닙니다 </p>"
    )
    public Result completeOrder(@PathVariable("order_pk") Long orderPk)
    {
        long userPk = authenticationFacade.getLoginUserPk();

        try {
            // 유저 획득
            User user = userService.getUser(userPk);
            Order order = orderService.getOrderInfo(orderPk) ;

            //권한 체크
            if (user != order.getOrderRes().getUser())
                return ResultError.builder().statusCode(-8).resultMsg("상점 주인의 접근이 아닙니다.").build();

            DoneOrder doneOrder = new DoneOrder(order) ;
            doneOrder.setDoneOrderState(1);
            doneOrderService.save(doneOrder);
            orderService.deleteOrder(order);

            return ResultDto.builder().build();
        } catch (UserNotFoundException e) {
            return ResultError.builder().statusCode(-2).resultMsg("로그인 정보를 찾지 못했습니다.").build();
        } catch (NullPointerException e) {
            return ResultError.builder().statusCode(-3).resultMsg("주문이 존재하지 않습니다.").build();
        } catch (Exception e) {
            log.error("An error occurred: ", e);
            return ResultError.builder().build();
        }

    }


    @GetMapping("user/list")
    @Operation(summary = "유저의 진행중인 주문 불러오기")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @ApiResponse(
            description =
                    "<p> 1 : 유저의 진행중인 주문 불러오기 완료 </p>"+
                    "<p> -1 : 실패 </p>" +
                    "<p> -2 : 로그인 정보 획득 실패 </p>"
    )
    public Result getUserOrderList()
    {
        long userPk = authenticationFacade.getLoginUserPk();

        try {
            // 유저 획득
            User user = userService.getUser(userPk);
            List<Order> data = orderService.getOrderListByUser(user);
            List<OrderMiniGetRes> list = new ArrayList<>();
            data.forEach(order -> {
                OrderMiniGetRes res = new OrderMiniGetRes(order) ;
                list.add(res) ;
            });

            return ResultDto.builder().resultData(list).build();

        } catch (UserNotFoundException e) {
            return ResultError.builder().statusCode(-2).resultMsg("로그인 정보를 찾지 못했습니다.").build();
        } catch (Exception e) {
            log.error("An error occurred: ", e);
            return ResultError.builder().build();
        }
    }




    @GetMapping("owner/noconfirm/list")
    @Operation(summary = "상점의 접수 전 주문정보 불러오기")
    @PreAuthorize("hasAnyRole('ADMIN','OWNER')")
    @ApiResponse(
            description =
                    "<p> 1 : 상점의 접수 전 주문정보 불러오기 완료 </p>"+
                    "<p> -1 : 실패</p>"+
                    "<p> -2 : 로그인 정보 획득 실패 </p>"+
                    "<p> -3 : 레스토랑 정보 획득 실패 </p>"
    )
    public Result getResNonConfirmOrderList() {
        long userPk = authenticationFacade.getLoginUserPk();

        try {
            // 유저 획득
            User user = userService.getUser(userPk);
            Restaurant rest = restaurantService.getRestaurantData(user) ;
            List<Order> data = orderService.getOrderListByRestaurantAndState(rest,2);

            List<OrderMiniGetRes> list = new ArrayList<>();
            data.forEach(order -> {
                OrderMiniGetRes res = new OrderMiniGetRes(order) ;
                list.add(res) ;
            });

            return ResultDto.builder().resultData(list).build();

        } catch (UserNotFoundException e) {
            return ResultError.builder().statusCode(-2).resultMsg("로그인 정보를 찾지 못했습니다.").build();
        } catch (DataNotFoundException e) {
            return ResultError.builder().statusCode(-3).resultMsg("레스토랑 정보를 찾지 못했습니다.").build();
        } catch (Exception e) {
            log.error("An error occurred: ", e);
            return ResultError.builder().build();
        }
    }



    @GetMapping("owner/confirm/list")
    @Operation(summary = "상점의 접수 후 주문정보 불러오기")
    @PreAuthorize("hasAnyRole('ADMIN','OWNER')")
    @ApiResponse(
            description =
                    "<p> 1 : 상점의 접수 전 주문정보 불러오기 완료 </p>"+
                    "<p> -1 : 실패</p>"+
                    "<p> -2 : 로그인 정보 획득 실패 </p>"+
                    "<p> -3 : 레스토랑 정보 획득 실패 </p>"
    )
    public Result getResConfirmOrderList() {
        long userPk = authenticationFacade.getLoginUserPk();

        try {
            // 유저 획득
            User user = userService.getUser(userPk);
            Restaurant rest = restaurantService.getRestaurantData(user) ;
            List<Order> data = orderService.getOrderListByRestaurantAndState(rest,3);

            List<OrderMiniGetRes> list = new ArrayList<>();
            data.forEach(order -> {
                OrderMiniGetRes res = new OrderMiniGetRes(order) ;
                list.add(res) ;
            });

            return ResultDto.builder().resultData(list).build();

        } catch (UserNotFoundException e) {
            return ResultError.builder().statusCode(-2).resultMsg("로그인 정보를 찾지 못했습니다.").build();
        } catch (DataNotFoundException e) {
            return ResultError.builder().statusCode(-3).resultMsg("레스토랑 정보를 찾지 못했습니다.").build();
        } catch (Exception e) {
            log.error("An error occurred: ", e);
            return ResultError.builder().build();
        }
    }



    @GetMapping("{order_pk}")
    @Operation(summary = "주문정보 상세보기")
    @ApiResponse(
            description =
                    "<p> 1 : 주문정보 상세보기 완료 </p>"+
                    "<p> -1 : 실패</p>"+
                    "<p> -2 : 로그인 정보 획득 실패 </p>"+
                    "<p> -3 : 레스토랑 정보 획득 실패 </p>" +
                    "<p> -4 : 유저 소유권 확인 실패 </p>"

    )
    public Result getOrderInfo(@PathVariable("order_pk") Long orderPk) {
        long userPk = authenticationFacade.getLoginUserPk();

        try {
            // 유저 획득
            User user = userService.getUser(userPk);
            Order data = orderService.getOrderBySeq(orderPk);
            //소유권 검증
            if (user != data.getOrderUser() && user != data.getOrderRes().getUser())
                return ResultError.builder().statusCode(-4).resultMsg("해당 주문의 소유자가 아닙니다.").build();

            OrderMiniGetRes res = new OrderMiniGetRes(data) ;
            return ResultDto.builder().resultData(res).build();

        } catch (UserNotFoundException e) {
            return ResultError.builder().statusCode(-2).resultMsg("로그인 정보를 찾지 못했습니다.").build();
        } catch (DataNotFoundException e) {
            return ResultError.builder().statusCode(-3).resultMsg("레스토랑 정보를 찾지 못했습니다.").build();
        } catch (Exception e) {
            log.error("An error occurred: ", e);
            return ResultError.builder().build();
        }
    }


    @PatchMapping("owner/confirm/{order_pk}")
    @Operation(summary = "주문 접수하기")
    @PreAuthorize("hasAnyRole('ADMIN', 'OWNER')")
    @ApiResponse(
            description =
                    "<p> 1 : 주문 접수 완료 </p>"+
                    "<p> -1 : 실패 </p>"+
                    "<p> -2 : 로그인 정보 획득 실패 </p>"+
                    "<p> -3 : 주문 정보 획득 실패 </p>" +
                    "<p> -8 : 상점 주인의 접근이 아닙니다. </p>"
    )
    public Result confirmOrder(@PathVariable("order_pk") Long orderPk)
    {
        long userPk = authenticationFacade.getLoginUserPk();

        try {
            // 유저 획득
            User user = userService.getUser(userPk);
            Order data = orderService.getOrderBySeq(orderPk);
            Restaurant rest = data.getOrderRes() ;
            //오너 검증
            if (user != rest.getUser())
                return ResultError.builder().statusCode(-8).resultMsg("상점 주인의 접근이 아닙니다.").build();

            data.setOrderState(3);
            orderService.saveOrder(data);

            return ResultDto.builder().build();

        } catch (UserNotFoundException e) {
            return ResultError.builder().statusCode(-2).resultMsg("로그인 정보를 찾지 못했습니다.").build();
        } catch (DataNotFoundException e) {
            return ResultError.builder().statusCode(-3).resultMsg("주문 정보를 찾지 못했습니다.").build();
        } catch (Exception e) {
            log.error("An error occurred: ", e);
            return ResultError.builder().build();
        }
    }


}

