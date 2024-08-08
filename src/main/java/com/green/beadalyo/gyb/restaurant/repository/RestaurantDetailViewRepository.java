package com.green.beadalyo.gyb.restaurant.repository;

import com.green.beadalyo.gyb.model.RestaurantDetailView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RestaurantDetailViewRepository extends JpaRepository<RestaurantDetailView , Long>
{
    Optional<RestaurantDetailView> findTop1ByRestaurantPk(Long seq) ;


    @Query("SELECT new com.green.beadalyo.gyb.model.RestaurantDetailView(" +
            "r.restaurantPk, r.restaurantName, r.reviewScore, r.reviewTotalElements, r.reviewDesc, r.restaurantDesc, r.regiNum, r.totalMenu, " +
            "r.closeTime, r.openTime, r.restaurantAddr, r.restaurantPic, r.restaurantState, r.restaurantCoorX, r.restaurantCoorY, r.createdAt, " +
            "CASE WHEN rf.resFollowPk IS NOT NULL THEN 1 ELSE 0 END) " +
            "FROM RestaurantDetailView r " +
            "LEFT JOIN ResFollow rf ON r.restaurantPk = rf.resPk.seq " +
            "WHERE r.restaurantPk = :seq")
    Optional<RestaurantDetailView> findByRestaurantPk(Long seq);
}
