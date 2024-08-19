package com.green.beadalyo.lhn.coupon.repository;


import com.green.beadalyo.jhw.user.entity.User;
import com.green.beadalyo.lhn.coupon.entity.Coupon;
import com.green.beadalyo.lhn.coupon.entity.CouponUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CouponUserRepository extends JpaRepository<CouponUser, Long> {

    @Query("select cu from CouponUser cu where cu.coupon.id = :couponId and cu.user.userPk =:userPk")
    CouponUser findByCouponIdAndUserId(@Param("couponId") Long couponId, @Param("userPk") Long userPk);

    List<CouponUser> findByCouponId(Long couponId);

    List<CouponUser> findByUserAndState(User user , int state);

//    Optional<CouponUser> findByCouponIdAndUserIdAndState(Long couponId, Long userId, int state);

    @Query("select cu from CouponUser cu where cu.coupon.id = :couponId and cu.user.userPk = :userPk and cu.state = :state")
    CouponUser findByUserIdAndStateAndCouponId(@Param("userPk") Long userPk, @Param("state") int state, @Param("couponId") Long couponId);


}





