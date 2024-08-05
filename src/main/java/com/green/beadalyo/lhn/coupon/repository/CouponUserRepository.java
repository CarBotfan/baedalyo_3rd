package com.green.beadalyo.lhn.coupon.repository;


import com.green.beadalyo.lhn.coupon.entity.CouponUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CouponUserRepository extends JpaRepository<CouponUser, Long> {

    @Query("SELECT COUNT(cu) > 0 FROM CouponUser cu WHERE cu.coupon.id = :couponId AND cu.user.userPk = :userId")
    boolean existsByCouponIdAndUserId(@Param("couponId") Long couponId, @Param("userId") Long userId);
}