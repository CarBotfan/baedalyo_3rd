package com.green.beadalyo.lhn.coupon;

import com.green.beadalyo.gyb.model.Restaurant;
import com.green.beadalyo.gyb.restaurant.repository.RestaurantRepository;
import com.green.beadalyo.jhw.user.entity.User;
import com.green.beadalyo.jhw.user.repository.UserRepository;
import com.green.beadalyo.lhn.coupon.dto.CouponResponseDto;
import com.green.beadalyo.lhn.coupon.entity.Coupon;
import com.green.beadalyo.lhn.coupon.entity.CouponUser;
import com.green.beadalyo.lhn.coupon.repository.CouponRepository;
import com.green.beadalyo.lhn.coupon.repository.CouponUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;
    private final CouponUserRepository couponUserRepository;
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;

    // 쿠폰 생성
    @Transactional
    public Coupon createCoupon(Coupon coupon, Long resPk) {
        Restaurant restaurant = restaurantRepository.findById(resPk)
                .orElseThrow(() -> new IllegalArgumentException("해당 가게가 존재하지 않습니다."));
        coupon.setRestaurant(restaurant);
        coupon.setCreatedAt(LocalDateTime.now());
        return couponRepository.save(coupon);
    }

    // 가게별 쿠폰 조회
    public List<CouponResponseDto> getCouponsByRestaurant(Long resPk) {
        List<Coupon> coupons = couponRepository.findByRestaurantId(resPk);
        return coupons.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // 쿠폰 발급
    @Transactional
    public CouponUser issueCoupon(Long couponId, Long userId) {
        if (couponUserRepository.existsByCouponIdAndUserId(couponId, userId)) {
            throw new IllegalArgumentException("이미 발급된 쿠폰입니다.");
        }

        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new IllegalArgumentException("해당 쿠폰이 존재하지 않습니다."));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));

        CouponUser couponUser = new CouponUser();
        couponUser.setCoupon(coupon);
        couponUser.setUser(user);
        couponUser.setCreatedAt(LocalDateTime.now());

        return couponUserRepository.save(couponUser);
    }

    // 쿠폰 엔티티를 DTO로 변환
    private CouponResponseDto convertToDto(Coupon coupon) {
        return new CouponResponseDto(
                coupon.getId(),
                coupon.getName(),
                coupon.getContent(),
                coupon.getPrice(),
                coupon.getCreatedAt()
        );
    }
}