package com.green.beadalyo.lhn.coupon;

import com.green.beadalyo.gyb.model.Restaurant;
import com.green.beadalyo.gyb.restaurant.repository.RestaurantRepository;
import com.green.beadalyo.jhw.security.AuthenticationFacade;
import com.green.beadalyo.jhw.user.entity.User;
import com.green.beadalyo.jhw.user.repository.UserRepository;
import com.green.beadalyo.lhn.coupon.dto.CouponResponseDto;
import com.green.beadalyo.lhn.coupon.dto.CouponUserResponseDto;
import com.green.beadalyo.lhn.coupon.entity.Coupon;
import com.green.beadalyo.lhn.coupon.entity.CouponUser;
import com.green.beadalyo.lhn.coupon.model.CouponPostReq;
import com.green.beadalyo.lhn.coupon.repository.CouponRepository;
import com.green.beadalyo.lhn.coupon.repository.CouponUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final AuthenticationFacade authenticationFacade;
    private final CouponRepository couponRepository;
    private final CouponUserRepository couponUserRepository;
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;

    // 쿠폰 생성
    @Transactional
    public Coupon createCoupon(CouponPostReq p) {
        User user = userRepository.findByUserPk(authenticationFacade.getLoginUserPk());
        Restaurant restaurant = restaurantRepository.findRestaurantByUser(user);
        if (restaurant == null) {
            throw new RuntimeException("쿠폰을 생성할 권한이 없는 사용자입니다");
        }

        p.setRestaurant(restaurant);
        Coupon coupon = new Coupon();
        coupon.setRestaurant(p.getRestaurant());
        coupon.setMinOrderAmount(p.getMinOrderAmount());
        coupon.setContent(p.getContent());
        coupon.setPrice(p.getPrice());
        coupon.setName(p.getName());
        return couponRepository.save(coupon);
    }

    // 가게별 쿠폰 조회
    public List<Coupon> getCouponsByRestaurant(Long restaurantId , List<Integer> state) {
        return couponRepository.findByRestaurant_SeqAndStateIn(restaurantId , state);
    }

    // 유저 쿠폰 조회
    @Transactional
    public List<CouponUserResponseDto> getCouponByUser() {
        Long loginUserPk = authenticationFacade.getLoginUserPk();
        User user = userRepository.findByUserPk(loginUserPk);

        int state = 1;
        List<CouponUser> list = couponUserRepository.findByUserAndState(user, state);
        List<CouponUserResponseDto> result = new ArrayList<>();
        for(CouponUser cu : list) {
            CouponUserResponseDto dto = new CouponUserResponseDto();
            dto.setCouponId(cu.getCoupon().getId());
            dto.setId(cu.getId());
            dto.setContent(cu.getCoupon().getContent());
            dto.setPrice(cu.getCoupon().getPrice());
            dto.setName(cu.getCoupon().getName());
            dto.setMinOrderAmount(cu.getCoupon().getMinOrderAmount());
            result.add(dto);
        }
        return  result;
    }


    // 사장님 쿠폰 조회
    @Transactional(readOnly = true)
    public List<CouponResponseDto> getCouponsByOwner() {
        Long loginUserPk = authenticationFacade.getLoginUserPk();
        User user = userRepository.findByUserPk(loginUserPk);
        Restaurant restaurant = restaurantRepository.findRestaurantByUser(user);

        if (restaurant == null) {
            throw new IllegalArgumentException("가게의 주인이 아닙니다.");
        }
        List<Integer> state = new ArrayList<>();
        state.add(1);
        List<Coupon> list = couponRepository.findByRestaurant_SeqAndStateIn(restaurant.getSeq(),state);
        List<CouponResponseDto> result = new ArrayList<>();
        for (int a = 0; a < list.size(); a++) {
            CouponResponseDto data = new CouponResponseDto(
                    list.get(a).getId(),
                    list.get(a).getName(),
                    list.get(a).getContent(),
                    list.get(a).getPrice(),
                    list.get(a).getMinOrderAmount(),
                    list.get(a).getCreatedAt()

            );
            result.add(data);
        }
        return result;
    }


    // 쿠폰 발급
    @Transactional
    public Long issueCoupon(Long couponId){
        Long loginUserPk = authenticationFacade.getLoginUserPk();

        CouponUser usedCouponUser = couponUserRepository.findByUserIdAndStateAndCouponId(loginUserPk ,2 , couponId);
        if (usedCouponUser != null) {
            throw new IllegalArgumentException("이미 발급받아 사용한 쿠폰입니다.");
        }


        CouponUser result = couponUserRepository.findByCouponIdAndUserId(couponId, loginUserPk);
        if(result != null) {
            throw new RuntimeException("이미 발급된 쿠폰입니다.");
        }

        Coupon coupon = couponRepository.getReferenceById(couponId);
        User user = userRepository.findByUserPk(loginUserPk);

        CouponUser couponUser = new CouponUser();
        couponUser.setCoupon(coupon);
        couponUser.setUser(user);
        couponUser.setCreatedAt(LocalDateTime.now());
        couponUser.setState(1); // 쿠폰 상태 활성화
         couponUserRepository.save(couponUser);
        return couponUser.getId();
    }

    // 쿠폰 상태 변경
    @Transactional
    public CouponUser updateCouponStatus(Long couponUserId) {
        CouponUser couponUser = couponUserRepository.findById(couponUserId)
                .orElseThrow(() -> new IllegalArgumentException("해당 쿠폰 사용자가 존재하지 않습니다."));
        couponUser.setState(2);
        return couponUserRepository.save(couponUser);
    }

    // 쿠폰 삭제
    @Transactional

    public void deleteCoupon(Long couponId , int state) {
        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new IllegalArgumentException("해당 쿠폰이 존재하지 않습니다."));
        coupon.setState(state);
        couponRepository.save(coupon);

        // 발급된 쿠폰의 상태를 비활성화로 설정
        List<CouponUser> issuedCoupons = couponUserRepository.findByCouponId(couponId);
        for (CouponUser issuedCoupon : issuedCoupons) {
            issuedCoupon.setState(2); // 쿠폰 상태 비활성화
            couponUserRepository.save(issuedCoupon);
        }
    }

    //쿠폰 데이터 조회
    public Coupon getCouponByPk(Long couponPk) {
        return couponRepository.findById(couponPk).orElseThrow(NullPointerException::new);
    }

    //소지 쿠폰 데이터 조회
    public CouponUser getCouponUserByPk(Long couponUserPk) {
        return couponUserRepository.findById(couponUserPk).orElseThrow(NullPointerException::new);
    }

    //상태 저장
    public CouponUser save(CouponUser couponUser) {
        return couponUserRepository.save(couponUser);
    }

}