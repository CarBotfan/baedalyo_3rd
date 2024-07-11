package com.green.beadalyo.gyb.restaurant;

import com.green.beadalyo.gyb.model.RestaurantDetailView;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RestaurantDetailViewRepository extends JpaRepository<RestaurantDetailView , Long>
{
    Optional<RestaurantDetailView> findTop1ByRestaurantPk(Long seq) ;
}
