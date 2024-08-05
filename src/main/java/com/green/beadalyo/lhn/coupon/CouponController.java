package com.green.beadalyo.lhn.coupon;

import com.green.beadalyo.lhn.coupon.dto.CouponResponseDto;
import com.green.beadalyo.lhn.coupon.entity.Coupon;
import com.green.beadalyo.lhn.coupon.entity.CouponUser;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/coupons")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

    // 쿠폰 생성
    @PostMapping("/create")
    public Coupon createCoupon(@RequestBody Coupon coupon, @RequestParam Long resPk) {
        return couponService.createCoupon(coupon, resPk);
    }

    // 가게별 쿠폰 조회
    @GetMapping("/restaurant/{resPk}")
    public List<CouponResponseDto> getCouponsByRestaurant(@PathVariable Long resPk) {
        return couponService.getCouponsByRestaurant(resPk);
    }

    // 쿠폰 발급
    @PostMapping("/issue")
    public CouponUser issueCoupon(@RequestParam Long couponId, @RequestParam Long userId) {
        return couponService.issueCoupon(couponId, userId);
    }
}