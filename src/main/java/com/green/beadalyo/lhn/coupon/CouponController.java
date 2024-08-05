//package com.green.beadalyo.lhn.coupon;
//
//import com.green.beadalyo.common.model.ResultDto;
//import com.green.beadalyo.lhn.coupon.entity.Coupon;
//import com.green.beadalyo.lhn.coupon.dto.CouponResponseDto;
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@RestController
//@RequestMapping("/api/coupons")
//@RequiredArgsConstructor
//public class CouponController {
//
//    private final CouponService couponService;
//
//    // 쿠폰 생성
//    @PostMapping("/create")
//    public ResultDto<CouponResponseDto> createCoupon(@RequestBody Coupon coupon, @RequestParam Long restaurantId) {
//        Coupon createdCoupon = couponService.createCoupon(coupon, restaurantId);
//        return new ResultDto<>(convertToDto(createdCoupon));
//    }
//
//    // 가게별 쿠폰 조회
//    @GetMapping("/restaurant/{restaurantId}")
//    public ResultDto<List<CouponResponseDto>> getCouponsByRestaurant(@PathVariable Long restaurantId) {
//        List<Coupon> coupons = couponService.getCouponsByRestaurant(restaurantId);
//        List<CouponResponseDto> couponDtos = coupons.stream().map(this::convertToDto).collect(Collectors.toList());
//       return new ResultDto<>(couponDtos);
//    }
//
//    // 쿠폰 엔티티를 DTO로 변환
//    private CouponResponseDto convertToDto(Coupon coupon) {
//        return new CouponResponseDto(
//                coupon.getId(),
//                coupon.getName(),
//                coupon.getContent(),
//                coupon.getPrice(),
//                coupon.getMinOrderAmount(),
//                coupon.getCreatedAt()
//        );
//    }
//}