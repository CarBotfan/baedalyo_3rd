package com.green.beadalyo.lhn.coupon.repository;


import com.green.beadalyo.jhw.user.entity.User;
import com.green.beadalyo.lhn.coupon.entity.Coupon;
import com.green.beadalyo.lhn.coupon.entity.CouponUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CouponUserRepository extends JpaRepository<CouponUser, Long> {

    @Query("select cu from CouponUser cu where cu.coupon.id = :couponId and cu.user.userPk =:userPk")
    CouponUser findByCouponIdAndUserId(@Param("couponId") Long couponId, @Param("userPk") Long userPk);

    List<CouponUser> findByCouponId(Long couponId);

    Boolean existsCouponUserByCouponAndUser(Coupon coupon, User user);

    List<CouponUser> findByUserAndState(User user , int state);
}