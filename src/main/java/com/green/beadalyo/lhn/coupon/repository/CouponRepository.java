package com.green.beadalyo.lhn.coupon.repository;


import com.green.beadalyo.lhn.coupon.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CouponRepository extends JpaRepository<Coupon, Long> {

    @Query("SELECT c FROM Coupon c WHERE c.restaurant.seq = :restaurantId")
    List<Coupon> findByRestaurantId(@Param("restaurantId") Long restaurantId);
}

